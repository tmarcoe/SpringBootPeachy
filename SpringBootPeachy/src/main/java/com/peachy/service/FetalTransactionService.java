package com.peachy.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.antlr.v4.runtime.RecognitionException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftl.helper.FetalTransaction;
import com.ftl.helper.SalesItem;
import com.ftl.helper.VariableType;
import com.peachy.component.FetalConfigurator;
import com.peachy.component.FilePath;
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
import com.peachy.reports.CreatePayStub;


@Service
public class FetalTransactionService extends FetalTransaction{
	
	@Autowired
	private FetalTransactionDao transDao;
	
	private Session session;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private InvoiceItemService invoiceItemService;
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	TimeSheetService timeSheetService;
	
	@Autowired
	PurchaseOrderService purchaseOrderService;
	
	@Autowired
	PaymentRegisterService paymentRegisterService;
	
	@Autowired
	private FetalConfigurator fc;
	
	@Autowired
	FilePath fp;
	
	public void processPettyCash(PettyCashRegister pettyCash) throws IOException {
		initTransaction(fp.getConfig() + "fetal.properties");
		
		addVariable("transactionAmount", VariableType.DOUBLE , pettyCash.getAmount());
		setDescription("Petty Cash: " + pettyCash.getReason());
		loadRule("pettycash.trans");
	}

	public void processAdjustment(double adjAmount) throws IOException {
		initTransaction(fp.getConfig() + "fetal.properties");
		addVariable("adjustment", VariableType.DOUBLE, adjAmount);
		setDescription("Petty Cash Adjustment");
		loadRule("adjustment.trans");
	}


	public void purchaseInventory(Order order) throws RecognitionException, IOException, RuntimeException {
		initTransaction(fc.getProperiesFile());

		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setPrice(order.getPrice());
		purchaseOrder.setTax(order.getTax());
		purchaseOrder.setQty(order.getAmount());
		String skuNum = order.getInventory().getSku_num();
		purchaseOrder.setSkuNum(skuNum);
		purchaseOrder.setPurchaseDate(new Date());
		purchaseOrderService.create(purchaseOrder);
		

		setAmount(order.getPrice());
		setTax(order.getTax());
		setDescription("Purchase of inventory (SKU #" + order.getInventory().getSku_num() + ")");
		loadRule("inventory.trans");

		Inventory inventory = order.getInventory();
		inventory.setAmt_in_stock(inventory.getAmt_in_stock() + order.getAmount());
		inventoryService.update(inventory);

	}


	public void processPayroll(List<UserProfile> employees, Date startPeriod)
			throws RecognitionException, IOException, RuntimeException {
		initTransaction(fp.getConfig() + "fetal.properties");

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
				addVariable("hoursWorked", VariableType.DOUBLE, hoursWorked);
				double hourlyRate = employee.getHourlyRate();
				if (employee.isSalary() == true) {
					grossWage = hourlyRate * 40;
				} else {
					grossWage = hourlyRate * hoursWorked;
				}
				String employeeName = user.getFirstname() + " " + user.getLastname();
				addVariable("employeeName", VariableType.STRING, employeeName);
				addVariable("grossWage", VariableType.DOUBLE, grossWage);

				addVariable("fTaxPrcnt", VariableType.DOUBLE, (employee.getfTaxPrcnt() / 100));
				addVariable("sTaxPrcnt", VariableType.DOUBLE, (employee.getsTaxPrcnt() / 100));
				addVariable("fUnPrcnt", VariableType.DOUBLE, (employee.getfUnPrcnt() / 100));
				addVariable("sUnPrcnt", VariableType.DOUBLE, (employee.getsUnPrcnt() / 100));
				addVariable("medPrcnt", VariableType.DOUBLE, (employee.getMedPrcnt() / 100));
				addVariable("ssiPrcnt", VariableType.DOUBLE, (employee.getSsiPrcnt() / 100));
				addVariable("retirePrcnt", VariableType.DOUBLE, (employee.getRetirePrcnt() / 100));
				addVariable("garnishment", VariableType.DOUBLE, employee.getGarnishment());
				addVariable("other", VariableType.DOUBLE, employee.getOther());

				addVariable("fTaxYtd", VariableType.DOUBLE, employee.getfTaxYtd());
				addVariable("sTaxYtd", VariableType.DOUBLE, employee.getsTaxYtd());
				addVariable("fUnYtd", VariableType.DOUBLE, employee.getfUnYtd());
				addVariable("sUnYtd", VariableType.DOUBLE, employee.getsUnYtd());
				addVariable("medYtd", VariableType.DOUBLE, employee.getMedYtd());
				addVariable("ssiYtd", VariableType.DOUBLE, employee.getSsiYtd());
				addVariable("retireYtd", VariableType.DOUBLE, employee.getRetireYtd());
				addVariable("garnishmentYtd", VariableType.DOUBLE, employee.getGarnishmentYtd());
				addVariable("otherYtd", VariableType.DOUBLE, employee.getOtherYtd());
				addVariable("wagesYtd", VariableType.DOUBLE, employee.getWagesYtd());

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
				CreatePayStub payStub = new CreatePayStub();
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				String filePath = fp.getPayStubPath();
				String fileName = String.format("%s%s-%08d.pdf", filePath, df.format(startPeriod), user.getUser_id());
				paymentRegisterService.create(paymentRegister);

				employee.setfTaxYtd((double) getValue("fTaxYtd"));
				employee.setsTaxYtd((double) getValue("sTaxYtd"));
				employee.setfUnYtd((double) getValue("fUnYtd"));
				employee.setsUnYtd((double) getValue("sUnYtd"));
				employee.setMedYtd((double) getValue("medYtd"));
				employee.setSsiYtd((double) getValue("ssiYtd"));
				employee.setRetireYtd((double) getValue("retireYtd"));
				employee.setGarnishmentYtd((double) getValue("garnishmentYtd"));
				employee.setOtherYtd((double) getValue("otherYtd"));
				employee.setWagesYtd((double) getValue("wagesYtd"));

				employeeService.update(employee);

				payStub.pdfPayStub(fileName,
						timeSheetService.timeSheetDao.getApprovedTimeSheets(employee.getUser_id(), startPeriod),
						employee, paymentRegister);

				timeSheetService.closeTimeSheet(employee.getUser_id(), paymentRegister.getStartPeriod());
			}
		}

		clearVariables();
		addVariable("startingPeriod", VariableType.DATE, startPeriod);
		addVariable("ttGrossWage", VariableType.DOUBLE, ttGrossWage);
		addVariable("ttlFederalTax", VariableType.DOUBLE, ttlFederalTax);
		addVariable("ttlStateTax", VariableType.DOUBLE, ttlStateTax);
		addVariable("ttlFederalUnemployment", VariableType.DOUBLE, ttlFederalUnemployment);
		addVariable("ttlStateUmemployment", VariableType.DOUBLE, ttlStateUmemployment);
		addVariable("ttlMedical", VariableType.DOUBLE, ttlMedical);
		addVariable("ttlSsInsurance", VariableType.DOUBLE, ttlSsInsurance);
		addVariable("ttlRetirement", VariableType.DOUBLE, ttlRetirement);
		addVariable("ttlGarnishments", VariableType.DOUBLE, ttlGarnishments);
		addVariable("ttlOther", VariableType.DOUBLE, ttlOther);

		loadRule("payroll.trans");
	}

	public void processSales(Invoice header) throws IOException, RuntimeException {
		initTransaction(fc.getProperiesFile());
		header.setProcessed(new Date());

		setDescription("Internet Sales");
		loadSalesReceipt(header.getInvoice_num());
		loadRule("purchase.trans");
		invoiceService.merge(header);
	}
	
	public void useCoupon(Invoice invoice, Coupons coupon) throws IOException {
		final String couponValue = "couponValue";
		initTransaction(fc.getProperiesFile());
		loadSalesReceipt(invoice.getInvoice_num());
		addVariable(couponValue, VariableType.DOUBLE, 0);
		loadCoupon(coupon.getRuleName());
	
		if((double)getValue(couponValue) < 0) {
			InvoiceItem item = new InvoiceItem();
			item.setAmount(1);
			item.setInvoice_num(invoice.getInvoice_num());
			item.setPrice((double)getValue(couponValue));
			item.setProduct_name(coupon.getName());
			invoiceItemService.addItem(item);
		}

	}
	
	public void loadSalesReceipt(int key) {
		Invoice invoice = invoiceService.retrieve(key);

		clearSalesItems();
		for (InvoiceItem invItem : invoice.getItems()) {
			SalesItem item = new SalesItem();
			item.setPrice(invItem.getPrice());
			item.setTax(invItem.getTax());
			item.setQty(invItem.getAmount());
			addSalesItem(invItem.getSku_num(), item);
		}
		setAddedCharges(invoice.getAdded_charges());
		setShipCharges(invoice.getShipping_cost());
		invoice.setTotal(getAmount());
		invoice.setTotal_tax(getTax());

	}
	
	public double calculateShippingCharges(){
		try {
			initTransaction(fc.getProperiesFile());
			loadRule("shipping.trans");
		} catch (IOException | RuntimeException e) {
			return 0;
		}
		
		return (double) getValue("shipCharges");
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
	public void addStock(String sku, Long qty) {
		transDao.addStock(sku, qty, session);
	}

	@Override
	public void depleteStock(String sku, Long qty) {
		if (sku.startsWith("CPN") == false) {
			transDao.depleteStock(sku, qty, session);
		}
	}

	@Override
	public void commitStock(String sku, Long qty) {
		if (sku.startsWith("CPN") == false) {
			transDao.commitStock(sku, qty, session);
		}
	}

	@Override
	public double getRate(String Target) {
		return transDao.getRate(Target);
	}

	@Override
	public String getBaseCurrency() {
		return transDao.getBaseCurrency();
	}

	@Override
	public Date lastRefreshDate() {
		return null;
	}


}
