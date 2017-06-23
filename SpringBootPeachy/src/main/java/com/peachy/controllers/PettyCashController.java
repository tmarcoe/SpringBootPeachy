package com.peachy.controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
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

import com.peachy.entity.PettyCashRegister;
import com.peachy.entity.UserProfile;
import com.peachy.service.FetalTransactionService;
import com.peachy.service.PettyCashRegisterService;
import com.peachy.service.UserProfileService;

@Controller
@RequestMapping("/vendor")
public class PettyCashController {
	private final String pageLink = "/pcpaging";

	@Autowired
	private PettyCashRegisterService pettyCash;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	FetalTransactionService transactionService;

	private SimpleDateFormat dateFormat;

	private PagedListHolder<PettyCashRegister> pcList;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@RequestMapping("/pettycashenter")
	public String pettyCashEnter(Model model) {
		model.addAttribute("pettyCashReg", new PettyCashRegister());

		return "pettycashenter";
	}

	@RequestMapping("pettycashsave")
	public String pettyCashSave(@ModelAttribute("pettyCash") PettyCashRegister pettyCashReg, BindingResult result,
			Principal principal, Model model) throws IOException, ParseException {
		UserProfile user = userProfileService.retrieve(principal.getName());
		pettyCashReg.setUserId(user.getUser_id());
		pettyCashReg.setTransactionDate(new Date());
		pettyCash.create(pettyCashReg);
		transactionService.processPettyCash(pettyCashReg);
		pcList = pettyCash.retrieveList();

		model.addAttribute("objectList", pcList);

		return "pettycashlist";
	}

	@RequestMapping("/pettycashlist")
	public String pettyCashList(Model model) throws ParseException {
		pcList = pettyCash.retrieveList();

		model.addAttribute("objectList", pcList);

		return "pettycashlist";
	}

	@RequestMapping("/pettycashedit")
	public String pettyCashEdit(@ModelAttribute("id") int id, Model model) {
		double diffAmount;
		PettyCashRegister pcr = pettyCash.retrieve(id);
		diffAmount = pcr.getAmount();

		model.addAttribute("pettyCashReg", pcr);
		model.addAttribute("diffAmount", diffAmount);

		return "pettycashedit";
	}

	@RequestMapping("/pettycashupdate")
	public String pettyCashUpdate(@ModelAttribute("diffAmount") double diffAmount,
			@ModelAttribute("pettyCashReg") PettyCashRegister pettyCashReg, Model model)
			throws ParseException, IOException {

		pettyCash.update(pettyCashReg);
		if (pettyCashReg.getAmount() != diffAmount) {
			transactionService.processAdjustment(pettyCashReg.getAmount() - diffAmount);
		}
		pcList = pettyCash.retrieveList();

		model.addAttribute("objectList", pcList);

		return "pettycashlist";
	}

	@RequestMapping(value = "/pcpaging", method = RequestMethod.GET)
	public String handleUserRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;

		pgNum = isInteger(page);

		if ("next".equals(page)) {
			pcList.nextPage();
		} else if ("prev".equals(page)) {
			pcList.previousPage();
		} else if (pgNum != -1) {
			pcList.setPage(pgNum);
		}
		model.addAttribute("objectList", pcList);
		model.addAttribute("pagelink", pageLink);

		return "users";
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
