package com.peachy.controllers;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.peachy.component.CurrencyConfigurator;
import com.peachy.component.FilePath;
import com.peachy.entity.Inventory;
import com.peachy.entity.UserProfile;
import com.peachy.helper.Currency;
import com.peachy.service.InventoryService;
import com.peachy.service.UserProfileService;

@Controller
public class HomeController implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private FilePath fp;

	@Autowired
	private CurrencyConfigurator cp;
	
	@Autowired
	CurrencyConfigurator cc;
	
	@RequestMapping(value="/public/home", method = RequestMethod.GET)
	public String showRoot(@CookieValue(value = "currency", defaultValue = "") String myCurrency, 
								Model model, HttpServletResponse response, Principal principal) throws Exception {
		
		if ("".compareTo(myCurrency) == 0) {
			myCurrency = cp.getBaseCurrency();
		}
		double rate = 1;
		String currencySymbol = "P";
		Currency currency = new Currency(cc);
		if (principal != null) {
			UserProfile user = userProfileService.retrieve(principal.getName());
			if (user != null) {
				rate = currency.getRate(user.getCurrency());
				currencySymbol = currency.getSymbol(user.getCurrency());
			}
		}else{
			rate = currency.getRate(myCurrency);
			currencySymbol = currency.getSymbol(myCurrency);
		}
		String fileLoc = fp.getImageLoc();
		List<Inventory> inventory = inventoryService.listSaleItems();
		
		model.addAttribute("currencySymbol", currencySymbol);
		model.addAttribute("rate", rate);
		model.addAttribute("inventory",inventory);
		model.addAttribute("fileLoc", fileLoc);

		return "home";
	}
	
	@RequestMapping("/")
	public String showHome() {
		
		return "redirect:/public/home";
	}
	
	@RequestMapping("/public/contactus")
	public String contactUs() {

		return "contactus";
	}
	
	@RequestMapping("/public/aboutus")
	public String aboutUs() {

		return "aboutus";
	}
	
}
