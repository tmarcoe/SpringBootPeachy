package com.peachy.controllers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peachy.entity.Coupons;
import com.peachy.service.CouponsService;
import com.peachy.service.FetalTransactionService;

@Controller

public class CouponController implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String pageLink = "/vendor/couponpaging";
	
	@Autowired
	private CouponsService couponsService;
	
	@Autowired
	FetalTransactionService transactionService;
	
	
	PagedListHolder<Coupons> couponList;
	
	private SimpleDateFormat dateFormat;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@RequestMapping("/vendor/createcoupon")
	public String createCoupon(Model model) {
		model.addAttribute("coupon", new Coupons());
		
		return "createcoupon";
	}

	@RequestMapping("/vendor/savecoupon")
	public String saveCoupon(@ModelAttribute("coupon") Coupons coupon, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "createcoupon";
		}
		coupon.setCoupon_id("CPN" + coupon.getCoupon_id());
		couponsService.create(coupon);

		
		return "redirect:/vendor/listcoupons";
	}

	@RequestMapping("/vendor/listcoupons")
	public String listCoupons(Model model) {
		if (couponList != null) {
			couponList.getSource().clear();
			couponList = null;
			System.gc();
		}
		couponList = couponsService.retrieveList();
		if (couponList.getPageList().size() == 0) {
			model.addAttribute("coupon", new Coupons());
			return "createcoupon";
		}
		model.addAttribute("objectList", couponList);
		return "listcoupons";
	}
	
	@RequestMapping("/vendor/editcoupon")
	public String editCoupon(@ModelAttribute("key") String key, Model model) {
		Coupons coupon = couponsService.retrieve(key);
		model.addAttribute("coupon", coupon);
		
		return "editcoupon";
	}
	
	@RequestMapping("/vendor/updatecoupon")
	public String updateCoupon(@ModelAttribute("coupon") Coupons coupon, Model model) {
		couponsService.update(coupon);

		
		return "redirect:/vendor/listcoupons";
	}
	
	@RequestMapping("/vendor/deletecoupon")
	public String deleteCoupon(@ModelAttribute("key") String key, Model model) {
		Coupons coupon = couponsService.retrieve(key);
		
		couponsService.delete(coupon);
		
		return "redirect:/vendor/listcoupons";
	}
	
	/******************************************************************************************************
	 * Pagination handler
	 ******************************************************************************************************/
	@RequestMapping(value="/vendor/couponpaging", method=RequestMethod.GET)
	public String handleCouponRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;


	        pgNum = isInteger(page);
	        
	        if ("next".equals(page)) {
	        	couponList.nextPage();
	        }
	        else if ("prev".equals(page)) {
	        	couponList.previousPage();
	        }else if (pgNum != -1) {
	        	couponList.setPage(pgNum);
	        }
	        model.addAttribute("objectList", couponList);
	        model.addAttribute("pagelink", pageLink);
	        
	        return "listcoupons";
	}

	
	/**************************************************************************************************************************************
	 * Used for both detecting a number, and converting to a number. If this routine returns a -1, the input parameter was not a number.
	 * 
	 **************************************************************************************************************************************/
		
		private int isInteger(String s) {
			int retInt;
		    try { 
		    	retInt = Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return -1; 
		    } catch(NullPointerException e) {
		        return -1;
		    }
		    // only got here if we didn't return false
		    return retInt;
		}
	
}
