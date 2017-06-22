package com.peachy.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.xml.soap.SOAPException;

import org.antlr.v4.runtime.RecognitionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.NestedServletException;

import com.peachy.component.CurrencyConfigurator;
import com.peachy.entity.Coupons;
import com.peachy.entity.Invoice;
import com.peachy.entity.InvoiceItem;
import com.peachy.entity.UserProfile;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.helper.Currency;
import com.peachy.helper.FileUpload;
import com.peachy.service.CouponsService;
import com.peachy.service.FetalTransactionService;
import com.peachy.service.InvoiceItemService;
import com.peachy.service.InvoiceService;
import com.peachy.service.UserProfileService;

@Controller
@Scope(value = "session")
public class ShoppingCartController implements Serializable {
	private static final long serialVersionUID = 4725326820861092920L;
	private static Logger logger = Logger.getLogger(ShoppingCartController.class.getName());
	private final String pageLink = "/historypaging";

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private InvoiceItemService invoiceItemService;

	@Autowired
	private CurrencyConfigurator cc;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private CouponsService couponsService;

	@Autowired
	private FetalTransactionService transactionService;

	private PagedListHolder<Invoice> historyList;

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@RequestMapping("/user/saveitem")
	public String saveInvoiceItem(@Valid @ModelAttribute("item") InvoiceItem item, BindingResult result, Model model)
			throws IOException, URISyntaxException, SOAPException {

		if (result.hasErrors()) {
			return "redirect:/user/editcart";
		}
		int invoiceNum = item.getInvoice_num();
		invoiceItemService.update(item);
		Invoice header = invoiceService.retrieve(invoiceNum);

		if (header.getProcessed() == null) {
			invoiceService.purgeCoupons(invoiceNum);
		}
		header.setShipping_cost(transactionService.calculateShippingCharges());
		header = invoiceService.totalInvoice(header);
		invoiceService.merge(header);

		return "redirect:/user/cart";
	}

	@RequestMapping("/user/cancelsale")
	public String cancelSale(Principal principal, Model model) throws IOException, URISyntaxException {

		UserProfile user = userProfileService.retrieve(principal.getName());
		Invoice header = invoiceService.getOpenOrder(user.getUser_id());

		if (header == null) {
			return "redirect:/user/nocart";
		}

		invoiceService.deleteInvoice(header.getInvoice_num());

		return "redirect:/public/home";
	}

	@RequestMapping("/user/deleteinvoiceitem")
	public String deleteInvoiceItem(@ModelAttribute("invoiceNum") int invoiceNum,
			@ModelAttribute("skuNum") String skuNum, Principal principal, Model model)
			throws IOException, URISyntaxException, SOAPException {

		invoiceService.removeItem(invoiceNum, skuNum);
		logger.info(String.format("'%s' has been removed from invoice # '%08d'.", skuNum, invoiceNum));
		Invoice header = invoiceService.retrieve(invoiceNum);
		if (header == null) {
			return "redirect:/user/nocart";
		}

		return "redirect:/user/cart";
	}

	@RequestMapping("/user/cart")
	public String showCart(Principal principal, Model model) throws IOException, URISyntaxException, SOAPException {

		UserProfile user = userProfileService.retrieve(principal.getName());
		Invoice header = invoiceService.getOpenOrder(user.getUser_id());
		if (header == null) {
			return "nocart";
		}

		if (header.getProcessed() == null) {
			invoiceService.purgeCoupons(header.getInvoice_num());
		}

		String errorMsg = "";
		String couponNum = "";
		Currency currency = new Currency(cc);

		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("errorMsg", errorMsg);
		model.addAttribute("couponNum", couponNum);
		model.addAttribute("invoice", header);
		model.addAttribute("invoiceList", listItems(header));

		return "cart";
	}

	@RequestMapping("/user/vieworder")
	public String viewOrder(@ModelAttribute("errorMsg") String errorMsg, @ModelAttribute("couponName") String couponName,
			Principal principal, Model model)
			throws RecognitionException, NestedServletException, IOException, URISyntaxException, SOAPException {
		Currency currency = new Currency(cc);
		UserProfile user = userProfileService.retrieve(principal.getName());
		Invoice header = invoiceService.getOpenOrder(user.getUser_id());
		if (header == null) {
			return "redirect:/user/nocart";
		}

		if (header.getProcessed() == null) {
			invoiceService.purgeCoupons(header.getInvoice_num());
		}
		header = invoiceService.totalInvoice(header);

		if (couponName.length() > 3) {

			Coupons coupon = couponsService.retrieveByName(couponName);
			if (coupon == null) {
				errorMsg = "That coupon does not exist";

				return "redirect:/user/cart";
			}
			if (new Date().after(coupon.getExpires())) {
				errorMsg = "That coupon has expired";

				return "redirect:/user/cart";

			}

			if (invoiceItemService.countCoupons(user.getUser_id(), couponName) <= coupon.getUseage()) {
				errorMsg = "That coupon is used up.";

				return "redirect:/user/cart";
			}

			if (invoiceItemService.hasCoupons(header.getInvoice_num())) {
				errorMsg = "Only one coupon per order";

				return "redirect:/user/cart";
			}

			transactionService.useCoupon(header, coupon);
		}
		model.addAttribute("invoice", header);
		model.addAttribute("invoiceList", new ArrayList<InvoiceItem>(header.getItems()));
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));

		return "vieworder";
	}

	@RequestMapping("/user/viewcart")
	public String viewCart(@ModelAttribute("invoiceNum") int invoiceNum, Model model)
			throws IOException, URISyntaxException, SOAPException {

		Invoice header = invoiceService.retrieve(invoiceNum);
		UserProfile user = userProfileService.retrieve(header.getUser_id());

		if (header.getProcessed() == null) {
			invoiceService.purgeCoupons(invoiceNum);
		}
		header = invoiceService.totalInvoice(header);
		String errorMsg = "";
		String couponNum = "";
		Currency currency = new Currency(cc);

		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("errorMsg", errorMsg);
		model.addAttribute("couponNum", couponNum);
		model.addAttribute("invoice", header);
		model.addAttribute("invoiceList", listItems(header));

		return "cart";
	}

	@RequestMapping("/user/editcart")
	public String editCart(@ModelAttribute("invoiceNum") int invoiceNum, @ModelAttribute("skuNum") String skuNum,
			Model model, Principal principal) throws IOException, URISyntaxException {

		UserProfile user = userProfileService.retrieve(principal.getName());
		InvoiceItem item = invoiceItemService.retrieve(invoiceNum, skuNum);
		Currency currency = new Currency(cc);

		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("item", item);

		return "editcart";
	}

	@RequestMapping("/user/filepicker")
	public String filePicker(Model model) {
		FileUpload filePrint = new FileUpload();

		model.addAttribute("filePrint", filePrint);

		return "filepicker";
	}

	@RequestMapping("/user/shoppinghistory")
	public String showShoppingHistory(Principal principal, Model model) throws IOException, URISyntaxException {
		UserProfile user = userProfileService.retrieve(principal.getName());
		if (historyList != null) {
			historyList.getSource().clear();
			historyList = null;
			System.gc();
		}
		historyList = invoiceService.getHistory(user.getUser_id());
		historyList.setPageSize(15);
		historyList.setPage(0);
		Currency currency = new Currency(cc);

		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("objectList", historyList);
		model.addAttribute("pagelink", pageLink);

		return "shoppinghistory";
	}

	/********************************************************************************************************
	 * Pagination Handlers
	 ********************************************************************************************************/
	@RequestMapping(value = "/historypaging", method = RequestMethod.GET)
	public String handleHostoryRequest(@ModelAttribute("page") String page, Model model, Principal principal)
			throws Exception {
		UserProfile user = userProfileService.retrieve(principal.getName());
		Currency currency = new Currency(cc);

		int pgNum;

		if (historyList == null) {
			throw new SessionTimedOutException("Your session has timed out. Please login again.");
		}

		pgNum = isInteger(page);

		if ("next".equals(page)) {
			historyList.nextPage();
		} else if ("prev".equals(page)) {
			historyList.previousPage();
		} else if (pgNum != -1) {
			historyList.setPage(pgNum);
		}

		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("objectList", historyList);
		model.addAttribute("pagelink", pageLink);

		return "shoppinghistory";
	}

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

	private List<InvoiceItem> listItems(Invoice invoice) {
		return new ArrayList<InvoiceItem>(invoice.getItems());
	}
}
