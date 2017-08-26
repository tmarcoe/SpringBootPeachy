package com.peachy.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.RecognitionException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftl.helper.FetalTransaction;
import com.ftl.helper.VariableType;
import com.peachy.component.PayStubReportSettings;
import com.peachy.dao.FetalTransactionDao;
import com.peachy.entity.Coupons;
import com.peachy.entity.Employee;
import com.peachy.entity.Inventory;
import com.peachy.entity.Invoice;
import com.peachy.entity.InvoiceItem;
import com.peachy.entity.PaymentRegister;
import com.peachy.entity.PettyCashRegister;
import com.peachy.entity.PurchaseOrder;
import com.peachy.entity.UserProfile;
import com.peachy.helper.Order;
import com.peachy.helper.Receipt;
import com.peachy.reports.CreatePayStub;

@Service
public class FetalTransactionService extends FetalTransaction {
	
	@Autowired
	PayStubReportSettings pStub;

	@Autowired
	private FetalTransactionDao transDao;

	private Session session;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private InvoiceItemService invoiceItemService;

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private TimeSheetService timeSheetService;

	@Autowired
	private PurchaseOrderService purchaseOrderService;

	@Autowired
	private PaymentRegisterService paymentRegisterService;


	@Value("${fetal.properiesFile}")
	private String filePath;
	
	@Value("${filePath.payStubPath}")
	private String payStubPath;
	
	public void processPettyCash(PettyCashRegister pettyCash) throws IOException {
		initTransaction(filePath);

		publish("transactionAmount", VariableType.DECIMAL, pettyCash.getAmount());
		setDescription("Petty Cash: " + pettyCash.getReason());
		loadRule("pettycash.trans");
		closeFetal();
	}

	public void processAdjustment(double adjAmount) throws IOException {
		initTransaction(filePath);
		publish("adjustment", VariableType.DECIMAL, adjAmount);
		setDescription("Petty Cash Adjustment");
		loadRule("adjustment.trans");
		closeFetal();
	}

	public void purchaseInventory(Order order) throws RecognitionException, IOException, RuntimeException {
		initTransaction(filePath);

		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setPrice(order.getPrice());
		purchaseOrder.setTax(order.getTax());
		purchaseOrder.setQty(order.getAmount());
		String skuNum = order.getInventory().getSku_num();
		purchaseOrder.setSkuNum(skuNum);
		purchaseOrder.setPurchaseDate(new Date());
		purchaseOrderService.create(purchaseOrder);

		setDescription("Purchase of inventory (SKU #" + order.getInventory().getSku_num() + ")");
		Inventory inventory = order.getInventory();
		publish("order", VariableType.OBJECT, order);
		loadRule("inventory.trans");

		inventory.setAmt_in_stock(inventory.getAmt_in_stock() + order.getAmount());
		inventoryService.update(inventory);
		closeFetal();
	}

	public void processPayroll(List<UserProfile> employees, Date startPeriod)
			throws RecognitionException, IOException, RuntimeException {
		initTransaction(filePath);

		double ttGrossWage = 0.0;
		double ttlFederalTax = 0.0;
		double ttlStateTax = 0.0;
		double ttlFederalUnemployment = 0.0;
		double ttlStateUmemployment = 0.0;
		double ttlMedical = 0.0;
		double ttlSsInsurance = 0.0;
		double ttlRetirement = 0.0;
		double ttlGarnishments = 0.0;
		double ttlOther = 0.0;
		for (UserProfile user : employees) {
			Employee employee = employeeService.retrieve(user.getUser_id());
			if (employee != null) {
				double grossWage = 0.0;
				double hoursWorked = timeSheetService.totalHours(user.getUser_id(), startPeriod);
				if (hoursWorked == 0)
					continue;
				publish("hoursWorked", VariableType.DECIMAL, hoursWorked);
				double hourlyRate = employee.getHourlyRate();
				if (employee.isSalary() == true) {
					grossWage = hourlyRate * 40;
				} else {
					grossWage = hourlyRate * hoursWorked;
				}
				String employeeName = user.getFirstname() + " " + user.getLastname();
				publish("employeeName", VariableType.STRING, employeeName);
				publish("grossWage", VariableType.DECIMAL, grossWage);

				publish("employee", VariableType.DAO, employee);
				loadRule("payment.trans");

				PaymentRegister paymentRegister = new PaymentRegister();

				paymentRegister.setUserId(employee.getUser_id());
				paymentRegister.setStartPeriod(startPeriod);
				paymentRegister.setGrossWage(grossWage);
				paymentRegister.setFederalTx((double) getValue("federalTax"));
				paymentRegister.setStateTx((double) getValue("stateTax"));
				paymentRegister.setFederalUnEm((double) getValue("federalUnem"));
				paymentRegister.setStateUnEm((double) getValue("stateUnem"));
				paymentRegister.setMedical((double) getValue("medicare"));
				paymentRegister.setSsiTx((double) getValue("ssi"));
				paymentRegister.setRetirement((double) getValue("retirement"));
				paymentRegister.setGarnishment(employee.getGarnishment());
				paymentRegister.setOther(employee.getOther());
				paymentRegister.setOtherExpl(employee.getOtherExpl());
				paymentRegister.setNetWage((double) getValue("netWage"));
				paymentRegister.setPaymentMethod(employee.getPayMethod());
				paymentRegister.setAccountNum(employee.getAccountNum());
				paymentRegister.setRoutingNum(employee.getRoutingNum());

				ttGrossWage += paymentRegister.getGrossWage();
				ttlFederalTax += paymentRegister.getFederalTx();
				ttlStateTax += paymentRegister.getStateTx();
				ttlFederalUnemployment += paymentRegister.getFederalUnEm();
				ttlStateUmemployment += paymentRegister.getStateUnEm();
				ttlMedical += paymentRegister.getMedical();
				ttlSsInsurance += paymentRegister.getSsiTx();
				ttlRetirement += paymentRegister.getRetirement();
				ttlGarnishments += paymentRegister.getGarnishment();
				ttlOther += paymentRegister.getOther();
				CreatePayStub payStub = new CreatePayStub(pStub);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				String fileName = String.format("%s%s-%08d.pdf", payStubPath, df.format(startPeriod), user.getUser_id());
				paymentRegisterService.create(paymentRegister);

				employeeService.update(employee);

				payStub.pdfPayStub(fileName,
						timeSheetService.timeSheetDao.getApprovedTimeSheets(employee.getUser_id(), startPeriod),
						employee, paymentRegister);

				timeSheetService.closeTimeSheet(employee.getUser_id(), paymentRegister.getStartPeriod());
			}
			closeFetal();
		}

		clearVariables();
		publish("startingPeriod", VariableType.DATE, startPeriod);
		publish("ttGrossWage", VariableType.DECIMAL, ttGrossWage);
		publish("ttlFederalTax", VariableType.DECIMAL, ttlFederalTax);
		publish("ttlStateTax", VariableType.DECIMAL, ttlStateTax);
		publish("ttlFederalUnemployment", VariableType.DECIMAL, ttlFederalUnemployment);
		publish("ttlStateUmemployment", VariableType.DECIMAL, ttlStateUmemployment);
		publish("ttlMedical", VariableType.DECIMAL, ttlMedical);
		publish("ttlSsInsurance", VariableType.DECIMAL, ttlSsInsurance);
		publish("ttlRetirement", VariableType.DECIMAL, ttlRetirement);
		publish("ttlGarnishments", VariableType.DECIMAL, ttlGarnishments);
		publish("ttlOther", VariableType.DECIMAL, ttlOther);

		loadRule("payroll.trans");
		closeFetal();
	}

	public void processSales(Invoice invoice) throws IOException, RuntimeException {
		initTransaction(filePath);
		invoice.setProcessed(new Date());
		Receipt receipt = new Receipt();
		publish("receipt", VariableType.OBJECT, receipt);
		publish("invoice", VariableType.DAO, invoice);
		setDescription("Internet Sales");

		loadRule("purchase.trans");
		invoiceService.merge(invoice);
		commitReceipt(invoice.getItems());
		closeFetal();
	}

	public void processShipping (Invoice invoice) throws IOException {
		initTransaction(filePath);

		loadRule("aftershipping.trans");
		depleteReceipt(invoice.getItems());
		closeFetal();
	}
	
	public void useCoupon(Invoice invoice, Coupons coupon) throws IOException {
		final String couponValue = "couponValue";
		initTransaction(filePath);
		
		publish(couponValue, VariableType.DECIMAL, 0);
		Receipt receipt = new Receipt();
		receipt.setInvList(invoice.getItems());
		publish("receipt", VariableType.OBJECT, receipt);
		loadCoupon(coupon.getRuleName());

		if ((double) getValue(couponValue) < 0.0) {
			InvoiceItem item = new InvoiceItem();
			item.setAmount(1);
			item.setInvoice_num(invoice.getInvoice_num());
			item.setPrice((double) getValue(couponValue));
			item.setProduct_name(coupon.getName());
			item.setSku_num(coupon.getCoupon_id());
			invoiceItemService.addItem(item);
		}
		closeFetal();
	}

	public double calculateShippingCharges() {
		try {
			initTransaction(filePath);
			loadRule("shipping.trans");
		} catch (IOException | RuntimeException e) {
			return 0;
		}
		Double shipCharges = (double) getValue("shipCharges");
		
		closeFetal();
		return shipCharges;
	}
	
	public void depleteReceipt(Set<InvoiceItem> invoiceItems) {
		for (InvoiceItem item : invoiceItems) {
			transDao.depleteStock(item.getSku_num(), item.getAmount(), session);
		}
	}
	
	public void commitReceipt(Set<InvoiceItem> invoiceItems) {		
		for (InvoiceItem item : invoiceItems) {
			transDao.commitStock(item.getSku_num(), item.getAmount(), session);
		}
	}
	
	public void addStock(String sku, Long amout) {
		transDao.addStock(sku, amout, session);
	}
	
	/******************************************************
	 * Overridden methods
	 ******************************************************/

	@Override
	public void beginTrans() {
		session = transDao.beginTrans();
	}

	@Override
	public void commitTrans() {
		transDao.commitTrans(session);
	}

	@Override
	public void credit(Double amount, String account) {
		account = getMap(account);
		transDao.credit(amount, account, session);
	}

	@Override
	public void debit(Double amount, String account) {
		account = getMap(account);
		transDao.debit(amount, account, session);
	}

	@Override
	public void ledger(char type, Double amount, String account, String description) {
		account = getMap(account);
		transDao.ledger(type, amount, account, description, session);
	}

	@Override
	public double getBalance(String account) {
		account = getMap(account);
		return transDao.getBalance(account, session);
	}


	@Override
	public void rollback() {
		transDao.rollback(session);
		
	}

	@Override
	public Object lookup(String sql,  Object...args) {
		String sqlWithArgs = String.format(sql, args);
		
		return transDao.lookup(sqlWithArgs);
	}

	@Override
	public List<Object> list(String sql, Object...args) {
		return null;
	}

	@Override
	public void update(String sql, Object... args) {
		String sqlWithArgs = String.format(sql, args);
		transDao.update(sqlWithArgs);
	}

}
