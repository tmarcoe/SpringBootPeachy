package com.peachy.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peachy.component.CurrencyConfigurator;
import com.peachy.component.FilePath;
import com.peachy.entity.Inventory;
import com.peachy.entity.Invoice;
import com.peachy.entity.InvoiceItem;
import com.peachy.entity.UserProfile;
import com.peachy.helper.Categories;
import com.peachy.helper.Currency;
import com.peachy.service.InventoryService;
import com.peachy.service.InvoiceItemService;
import com.peachy.service.InvoiceService;
import com.peachy.service.UserProfileService;

import org.springframework.validation.BindingResult;

@Controller
@Scope(value = "session")
public class ShopController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ShopController.class.getName());
	private final String pageLink = "/public/paging";

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private InvoiceItemService invoiceItemService;

	@Autowired
	private InventoryService inventoryService;

	private PagedListHolder<Inventory> inventoryList = null;
	
	@Autowired
	CurrencyConfigurator cc;

	@Autowired
	private FilePath fp;

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private CurrencyConfigurator cp;

	private Categories categories = null;

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@RequestMapping("/public/searchproducts") 
	public String searchProducts(Model model){
		String mySearch = new String();
		
		model.addAttribute("mySearch", mySearch);
		
		return "searchproducts";
	}
	
	@RequestMapping(value = "/public/shopbysearch")
	public String shopBySearch(@ModelAttribute("mySearch") String mySearch,
			@CookieValue(value = "currency", defaultValue = "") String myCurrency, Model model,
			Principal principal) throws IOException, URISyntaxException {
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}
		
		String fileLoc = fp.getImageLoc();
		UserProfile user;
		if (principal != null) {
			user = userProfileService.retrieve(principal.getName());
		} else {
			user = new UserProfile();
			user.setCurrency(myCurrency);
		}
		if (categories == null) {
			categories = new Categories();
		}
		if (inventoryList != null) {
			inventoryList.getSource().clear();
			inventoryList = null;
			System.gc();
		}
		inventoryList = inventoryService.searchInventory(mySearch);
		inventoryList.setPageSize(4);
		inventoryList.setPage(0);
		Currency currency = new Currency(cc);

		model.addAttribute("objectList", inventoryList);
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("filter", "");
		model.addAttribute("fileLoc", fileLoc);
		model.addAttribute("pagelink", pageLink);
		
		return "products";
	}
	
	@RequestMapping(value = "/public/products")
	public String products(@CookieValue(value = "currency", defaultValue = "") String myCurrency, Model model,
			Principal principal) throws IOException, URISyntaxException {
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}
		String fileLoc = fp.getImageLoc();
		UserProfile user;
		if (principal != null) {
			user = userProfileService.retrieve(principal.getName());
		} else {
			user = new UserProfile();
			user.setCurrency(myCurrency);
		}
		if (categories == null) {
			categories = new Categories();
		}
		if (inventoryList != null) {
			inventoryList.getSource().clear();
			inventoryList = null;
			System.gc();
		}
		inventoryList = inventoryService.getList(categories);
		inventoryList.setPageSize(4);
		inventoryList.setPage(0);
		Currency currency = new Currency(cc);

		model.addAttribute("objectList", inventoryList);
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("filter", buildFilter(categories));
		model.addAttribute("fileLoc", fileLoc);
		model.addAttribute("pagelink", pageLink);

		return "products";
	}

	@RequestMapping(value = "/public/productdetails", method = RequestMethod.GET)
	public String showProductDetails(@CookieValue(value = "currency", defaultValue = "") String myCurrency,
			@ModelAttribute("skuNum") String skuNum, Principal principal, Model model)
			throws IOException, URISyntaxException {
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}
		UserProfile user;
		if (principal != null) {
			user = userProfileService.retrieve(principal.getName());
		} else {
			user = new UserProfile();
			user.setCurrency(myCurrency);
		}
		String fileLoc = fp.getImageLoc();
		Inventory inventory = inventoryService.retrieve(skuNum);
		InvoiceItem item = new InvoiceItem();
		item.setSku_num(inventory.getSku_num());
		item.setProduct_name(inventory.getProduct_name());
		if (inventory.isOn_sale() ==  true) {
			item.setPrice(inventory.getDiscount_price());
		}else{
			item.setPrice(inventory.getSale_price());
		}
		item.setAmt_in_stock(inventory.getAmt_in_stock());
		item.setTax(inventory.getTax_amt());
		Currency currency = new Currency(cc);
		model.addAttribute("schemeStr", inventory.getExtra_info_scheme());
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("invoiceItem", item);
		model.addAttribute("inventory", inventory);
		model.addAttribute("fileLoc", fileLoc);

		return "productdetails";
	}

	@RequestMapping("/user/orderproduct")
	public String orderProduct(HttpServletResponse response, 
			@CookieValue(value = "order", defaultValue = "success") String myOrder,
			@Valid @ModelAttribute("invoiceItem") InvoiceItem item, BindingResult result, Model model,
			Principal principal) throws IOException, URISyntaxException, SOAPException {

		UserProfile user = userProfileService.retrieve(principal.getName());

		if (result.hasErrors()) {

			return "redirect:/user/productdetails";
		}
		Invoice header = invoiceService.getOpenOrder(user.getUser_id());
		if (header == null) {
			header = new Invoice();
			header.setModified(new Date());
			header.setUser_id(user.getUser_id());
			invoiceService.create(header);
			header = invoiceService.getOpenOrder(user.getUser_id());
		}
		item.setInvoice_num(header.getInvoice_num());
		
		invoiceItemService.addItem(item);
		logger.info("'" + item.getSku_num() + "' was just added to the shopping cart.");
		header = invoiceService.retrieve(header.getInvoice_num());
		header = invoiceService.totalInvoice(header);
		invoiceService.merge(header);
		

		return "redirect:/public/home";
	}

	@RequestMapping("/public/pickcategory")
	public String pickCategory(Model model) {
		String fileLoc = fp.getImageLoc();
		List<Inventory> catList = inventoryService.getCategory();

		model.addAttribute("fileLoc", fileLoc);
		model.addAttribute("catList", (List<Inventory>) catList);

		return "pickcategory";
	}

	@RequestMapping("/public/setcategory")
	public String setCategory(@CookieValue(value = "currency", defaultValue = "") String myCurrency,
			@ModelAttribute("cat") String cat, Model model, Principal principal)
			throws IOException, URISyntaxException {
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}

		UserProfile user;
		if (principal != null) {
			user = userProfileService.retrieve(principal.getName());
		} else {
			user = new UserProfile();
			user.setCurrency(myCurrency);
		}

		String fileLoc = fp.getImageLoc();

		if (categories == null) {
			categories = new Categories();
		}

		if (cat.length() == 0) {
			categories.setCategory("");
			categories.setSubCategory("");
			if (inventoryList != null) {
				inventoryList.getSource().clear();
				inventoryList = null;
				System.gc();
			}

			inventoryList = inventoryService.getList(categories);
			inventoryList.setPageSize(4);
			inventoryList.setPage(0);
			Currency currency = new Currency(cc);

			model.addAttribute("rate", currency.getRate(user.getCurrency()));
			model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
			model.addAttribute("objectList", inventoryList);
			model.addAttribute("fileLoc", fileLoc);
			model.addAttribute("pagelink", pageLink);

			return "products";
		}
		categories.setCategory(cat);
		categories.setSubCategory("");
		List<Inventory> catList = inventoryService.getSubCategory(categories.getCategory());
		model.addAttribute("catList", catList);
		model.addAttribute("fileLoc", fileLoc);

		return "picksubcategory";
	}

	@RequestMapping("/public/setsubcategory")
	public String setSubCategory(@CookieValue(value = "currency", defaultValue = "") String myCurrency,
			@ModelAttribute("cat") String cat, Model model, Principal principal)
			throws IOException, URISyntaxException {
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}
		UserProfile user;
		if (principal != null) {
			user = userProfileService.retrieve(principal.getName());
		} else {
			user = new UserProfile();
			user.setCurrency(myCurrency);
		}
		if (categories == null) {
			return "home";
		}
		String fileLoc = fp.getImageLoc();
		if (inventoryList != null) {
			inventoryList.getSource().clear();
			inventoryList = null;
			System.gc();
		}
		categories.setSubCategory(cat);
		inventoryList = inventoryService.getList(categories);

		inventoryList.setPageSize(4);
		inventoryList.setPage(0);
		Currency currency = new Currency(cc);

		model.addAttribute("objectList", inventoryList);
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("filter", buildFilter(categories));
		model.addAttribute("fileLoc", fileLoc);
		model.addAttribute("pagelink", pageLink);

		return "products";
	}

	/***************************************************************************************************************
	 * Pageination handlers
	 **************************************************************************************************************/

	@RequestMapping(value = "/public/paging", method = RequestMethod.GET)
	public String handleRequest(@CookieValue(value = "currency", defaultValue = "") String myCurrency,
									  @ModelAttribute("page") String page, Model model, Principal principal) throws Exception {
		
		if (inventoryList == null) {
			return "redirect:/public/pickcategory";
		}
		
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}
		UserProfile user;
		if (principal != null) {
			user = userProfileService.retrieve(principal.getName());
		} else {
			user = new UserProfile();
			user.setCurrency(myCurrency);
		}
		String fileLoc = fp.getImageLoc();
		int pgNum;

		pgNum = isInteger(page);

		if ("next".equals(page)) {
			inventoryList.nextPage();
		} else if ("prev".equals(page)) {
			inventoryList.previousPage();
		} else if (pgNum != -1) {
			inventoryList.setPage(pgNum);
		}

		model.addAttribute("filter", buildFilter(categories));
		
		Currency currency = new Currency(cc);
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));
		model.addAttribute("objectList", inventoryList);
		model.addAttribute("pagelink", pageLink);
		model.addAttribute("fileLoc", fileLoc);

		return "products";
	}

	private String buildFilter(Categories categories) {
		String filter = "";

		if (categories.getCategory().length() > 0) {
			filter = "Category: " + categories.getCategory();
		}
		if (categories.getSubCategory().length() > 0) {
			filter = filter + " >> Sub Category: " + categories.getSubCategory();
		}

		return filter;
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
	

}
