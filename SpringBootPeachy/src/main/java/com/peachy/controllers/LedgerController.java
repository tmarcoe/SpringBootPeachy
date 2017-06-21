package com.peachy.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

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

import com.peachy.component.FilePath;
import com.peachy.entity.GeneralLedger;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.helper.DatePicker;
import com.peachy.reports.GeneralLedgerPDF;
import com.peachy.service.GeneralLedgerService;

@Controller
public class LedgerController implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String pageLink = "/admin/ledgerpaging";

	@Autowired
	private GeneralLedgerService generalLedgerService;
	
	@Autowired
	FilePath fileLocations;
	
	private DatePicker datePicker;

	private PagedListHolder<GeneralLedger> ledgerList;

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@RequestMapping("/admin/datepicker")
	public String pickDate(Model model) {
		DatePicker datePicker = new DatePicker();

		model.addAttribute("datePicker", datePicker);

		return "datepicker";
	}

	@RequestMapping(value = "/admin/generalledger", method = RequestMethod.POST)
	public String viewGeneralLedger(
			@Valid @ModelAttribute(value = "datePicker") DatePicker picker,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "datepicker";
		}
		datePicker = picker;
		picker.setSf(dateFormat);
		if (ledgerList != null) {
			ledgerList.getSource().clear();
			ledgerList = null;
			System.gc();
		}
		ledgerList = generalLedgerService.getPagedList(picker);
		ledgerList.setPage(0);
		ledgerList.setPageSize(20);

		model.addAttribute("datePicker", picker);
		model.addAttribute("objectList", ledgerList);
		model.addAttribute("pagelink", pageLink);

		return "generalledger";
	}
	@RequestMapping("/admin/exportledger")
	public String exportLedger(@ModelAttribute("startDt") Date startDt, 
								@ModelAttribute("endDt") Date endDt, Model model) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		DatePicker picker = new DatePicker();
		picker.setStart(startDt);
		picker.setEnd(endDt);
		PagedListHolder<GeneralLedger> pagedList = generalLedgerService.getPagedList(picker);
		ledgerList.setPage(0);
		ledgerList.setPageSize(20);
		GeneralLedgerPDF gl = new GeneralLedgerPDF();
		String fileName = df.format(startDt) + "-" + df.format(endDt) + ".pdf";
		String filePath = fileLocations.getReportPath() + fileName;
		gl.pdfLedgerReport(filePath, pagedList, startDt, endDt);
		
		model.addAttribute("datePicker", picker);
		
		return "exportledger";
	}

	@RequestMapping(value = "/admin/ledgerpaging", method = RequestMethod.GET)
	public String handleLedgerRequest(@ModelAttribute("page") String page,
			Model model) throws Exception {
		int pgNum;

			if (ledgerList == null) {
	            throw new SessionTimedOutException("Your session has timed out. Please login again.");
			}
			pgNum = isInteger(page);

			if ("next".equals(page)) {
				ledgerList.nextPage();
			} else if ("prev".equals(page)) {
				ledgerList.previousPage();
			} else if (pgNum != -1) {
				ledgerList.setPage(pgNum);
			}
			model.addAttribute("objectList", ledgerList);
			model.addAttribute("datePicker", datePicker);
			model.addAttribute("pagelink", pageLink);

			return "generalledger";
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
