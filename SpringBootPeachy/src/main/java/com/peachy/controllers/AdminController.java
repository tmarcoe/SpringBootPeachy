package com.peachy.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.peachy.component.CurrencyConfigurator;
import com.peachy.component.FilePath;
import com.peachy.component.SalesReceiptSettings;
import com.peachy.email.ProcessEmail;
import com.peachy.entity.Inventory;
import com.peachy.entity.Invoice;
import com.peachy.entity.InvoiceItem;
import com.peachy.entity.UserProfile;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.helper.AddressLabel;
import com.peachy.helper.Currency;
import com.peachy.helper.DatePicker;
import com.peachy.payment.CompressPaymentFiles;
import com.peachy.reports.CreatePDFSalesReceipt;
import com.peachy.service.FetalTransactionService;
import com.peachy.service.InventoryService;
import com.peachy.service.InvoiceService;
import com.peachy.service.UserProfileService;


@Controller
@Scope(value = "session")
public class AdminController implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String pageLink = "/headerpaging";
	
	@Autowired
	private CurrencyConfigurator cc;
	
	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private FilePath fp;

	@Autowired
	private SalesReceiptSettings sr;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	FetalTransactionService transactionService;

	private PagedListHolder<Invoice> headerList;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AdminController.class
			.getName());

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	/******************************************************************************
	 * Admin Home Page
	 * @throws IOException 
	 * @throws URISyntaxException 
	 ******************************************************************************/
	@RequestMapping("/vendor/admin")
	public String showAdmin(Model model, Principal principal) throws IOException, URISyntaxException {
		UserProfile user = userProfileService.retrieve(principal.getName());
		if (headerList != null) {
			headerList.getSource().clear();
			headerList = null;
			System.gc();
		}
		PagedListHolder<Invoice> headerList = invoiceService.getProcessedInvoices();
		headerList.setPage(0);
		headerList.setPageSize(10);
		Currency currency = new Currency(cc);
		
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("objectList", headerList);
		model.addAttribute("pagelink", pageLink);
		
		return "admin";
	}

	@RequestMapping("/vendor/processorders")
	public String processOrders() throws Exception {
		List<String> fileNames = new ArrayList<String>();
		AddressLabel lbl = new AddressLabel();
		Currency currency = new Currency(cc);
		CreatePDFSalesReceipt pdf = new CreatePDFSalesReceipt(sr);
		double rate = 1;
		String symbol = "P";

		String[] label = { "firstname", "lastname", "address1", "address2",
				"city", "region", "postalCode", "country", "invoiceNum" };
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");

		String fileName = fp.getOutPath() + sdf.format(new Date())
				+ ".csv";
		fileNames.add(sdf.format(new Date()) + ".csv");
		Writer hdr = new FileWriter(fileName);
		CsvBeanWriter csvWriter = new CsvBeanWriter(hdr,
				CsvPreference.STANDARD_PREFERENCE);

		List<Invoice> headers = invoiceService
				.getProcessedInvoicesRawList();
		csvWriter.writeHeader(label);
		
		for (Invoice header : headers) {
			UserProfile user = userProfileService.retrieve(header.getUser_id());
			if (user == null) {
				csvWriter.close();
				throw new Exception("User not found when referenced by 'header'.");
			}
			lbl.setFirstname(user.getFirstname());
			lbl.setLastname(user.getLastname());
			lbl.setAddress1(user.getAddress1());
			lbl.setAddress2(user.getAddress2());
			lbl.setCity(user.getCity());
			lbl.setRegion(user.getRegion());
			lbl.setPostalCode(user.getPostalCode());
			lbl.setCountry(user.getCountry());
			lbl.setInvoiceNum(String.format("%06d", header.getInvoice_num()));
			csvWriter.write(lbl, label);
			
			String fn = fp.getOutPath() + String.format("%08d", header.getInvoice_num()) + ".pdf";
			fileNames.add(String.format("%08d", header.getInvoice_num()) + ".pdf");
			rate = currency.getRate(user.getCurrency());
			symbol = currency.getAsciiSymbol(user.getCurrency());
			List<InvoiceItem> invoices = new ArrayList<InvoiceItem>(header.getItems());
			pdf.createReceipt(fn, rate, symbol, header, invoices, lbl);
			transactionService.processShipping(header);
			header.setShipped(new Date());
			invoiceService.merge(header);
		}
		csvWriter.close();
		CompressPaymentFiles.compressFiles(fp.getOutPath(), fileNames);
		CompressPaymentFiles.deleteFileList(fp.getOutPath(), fileNames);
		
		
		return "admin";
	}

	@RequestMapping("/vendor/senddailyspecials")
	public String sendDailySpecial(Model model, Principal principal) throws Exception {
		ProcessEmail pe = new ProcessEmail(fp);
		List <UserProfile> userList = userProfileService.getDailySpecialUsers();
		List <Inventory> inventoryList = inventoryService.listSaleItems();
		
		
		pe.sendDailySpecials(userList, pe.getDailySpecials(inventoryList));
		model.addAttribute("mailCount", userList.size());
		
		return "senddailyspecials";
	}
	@RequestMapping("/auditdatepicker")
	public String auditDatePicker(Model model) {
		DatePicker datePicker = new DatePicker();
		
		model.addAttribute("datePicker", datePicker);
		
		return "auditdatepicker";
	}
	
	@RequestMapping("/vendor/vendoradmin") 
	public String showVendorAdmin(Model model, Principal principal) throws IOException, URISyntaxException{
		UserProfile user = userProfileService.retrieve(principal.getName());
		if (headerList != null) {
			headerList.getSource().clear();
			headerList = null;
			System.gc();
		}
		PagedListHolder<Invoice> headerList = invoiceService.getProcessedInvoices();
		headerList.setPage(0);
		headerList.setPageSize(10);
		Currency currency = new Currency(cc);
		
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("objectList", headerList);
		model.addAttribute("pagelink", pageLink);
		
		return "vendoradmin";
	}

	/*********************************************************************************************************************
	 * Pageination Handlers
	 * 
	 ********************************************************************************************************************/

	@RequestMapping(value = "/vendor/headerpaging", method = RequestMethod.GET)
	public String handleHeaderRequest(@ModelAttribute("page") String page,
			Model model) throws Exception {
		int pgNum;

			if (headerList == null) {
	            throw new SessionTimedOutException("Your session has timed out. Please login again.");
			}
			pgNum = isInteger(page);

			if ("next".equals(page)) {
				headerList.nextPage();
			} else if ("prev".equals(page)) {
				headerList.previousPage();
			} else if (pgNum != -1) {
				headerList.setPage(pgNum);
			}
	        model.addAttribute("objectList", headerList);
	        model.addAttribute("pagelink", pageLink);
	        
	        return "admin";
	}

	/**************************************************************************************************************************************
	 * Used for both detecting a number, and converting to a number. If this
	 * routine returns a -1, the input parameter was not a number.
	 * 
	 **************************************************************************************************************************************/

	private int isInteger(String s) {
		int retInt;
		try {
			retInt = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return -1;
		} catch (NullPointerException e) {
			return -1;
		}
		// only got here if we didn't return false
		return retInt;
	}
}
