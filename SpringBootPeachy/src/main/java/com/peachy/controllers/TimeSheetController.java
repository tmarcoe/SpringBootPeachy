package com.peachy.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.antlr.v4.runtime.RecognitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.peachy.entity.TimeSheet;
import com.peachy.entity.TimeSheetAccounts;
import com.peachy.entity.UserProfile;
import com.peachy.service.EmployeeService;
import com.peachy.service.FetalTransactionService;
import com.peachy.service.TimeSheetAccountsService;
import com.peachy.service.TimeSheetService;
import com.peachy.service.UserProfileService;


@Controller
public class TimeSheetController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	TimeSheetService timeSheetService;

	@Autowired
	TimeSheetAccountsService timeSheetAccounts;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	FetalTransactionService transactionService;

	private SimpleDateFormat dateFormat;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@RequestMapping("/employee/entertime")
	public String showEnterTime(Principal principal, Model model) {
		UserProfile user = userProfileService.retrieve(principal.getName());
		TimeSheet timeSheet = new TimeSheet();

		timeSheet.setUserId(user.getUser_id());
		timeSheet.setEntered(new Date());
		timeSheet.setStartPeriod(getStartPeriod());

		List<TimeSheetAccounts> accounts = timeSheetAccounts.retrieveRawList();

		model.addAttribute("accounts", accounts);
		model.addAttribute("timeSheet", timeSheet);

		return "entertime";
	}

	@RequestMapping("/employee/savetime")
	public String saveTimeSheetEntry(@Valid @ModelAttribute("timeSheet") TimeSheet timeSheet, BindingResult result,
			Principal principal, Model model) {
		if (result.hasErrors()) {

			return "redirect:/employee/entertime";
		}

		TimeSheetAccounts tmAccount = timeSheetAccounts.retrieve(timeSheet.getAccountNum());

		UserProfile user = userProfileService.retrieve(principal.getName());
		timeSheet.setUserId(user.getUser_id());
		Date startPeriod = getStartPeriod();

		timeSheet.setEntered(new Date());
		timeSheet.setStartPeriod(startPeriod);
		timeSheet.setName(tmAccount.getName());

		if (timeSheetService.create(timeSheet) == false) {
			return "accountclosed";
		}

		return "redirect:/employee/timesheet";
	}

	@RequestMapping("/employee/timesheet")
	public String showTimeSheet(Principal principal, Model model) {
		UserProfile user = userProfileService.retrieve(principal.getName());
		boolean periodClosed = false;
		Date startPeriod = getStartPeriod();

		List<TimeSheet> timeSheets = timeSheetService.retrieveList(user.getUser_id(), startPeriod);
		if (timeSheets.size() == 0) {
			timeSheets = timeSheetService.retrieveClosed(user.getUser_id(), startPeriod);
			if (timeSheets.size() == 0) {
				TimeSheet timeSheet = new TimeSheet();

				timeSheet.setUserId(user.getUser_id());
				timeSheet.setEntered(new Date());
				timeSheet.setStartPeriod(getStartPeriod());

				List<TimeSheetAccounts> accounts = timeSheetAccounts.retrieveRawList();

				model.addAttribute("accounts", accounts);
				model.addAttribute("timeSheet", timeSheet);

				return "entertime";
			} else {
				periodClosed = true;
			}
		}
		model.addAttribute("periodClosed", periodClosed);
		model.addAttribute("startPeriod", startPeriod);
		model.addAttribute("timeSheets", timeSheets);

		return "timesheet";
	}

	@RequestMapping("/employee/previousperiod")
	public String showPerviosPeriod(@ModelAttribute("userID") int userID,
			@ModelAttribute("startPeriod") Date startPeriod, Model model) {

		List<TimeSheet> timeSheets = timeSheetService.retrieveApproveList(userID, startPeriod);

		model.addAttribute("startPeriod", startPeriod);

		model.addAttribute("timeSheets", timeSheets);

		return "approvetime";
	}

	@RequestMapping("/employee/edittime")
	public String editEntry(@ModelAttribute("entryId") int entryId, BindingResult result, Model model) {
		TimeSheet timeSheet = timeSheetService.retrieve(entryId);

		model.addAttribute("timeSheet", timeSheet);

		return "edittime";
	}

	@RequestMapping("/employee/updatetime")
	public String updateTime(@ModelAttribute("timeSheet") TimeSheet timeSheet, BindingResult result, Model model,
			Principal principal) {
		boolean periodClosed = false;
		timeSheetService.update(timeSheet);
		Date startPeriod = getStartPeriod();
		UserProfile user = userProfileService.retrieve(principal.getName());
		List<TimeSheet> timeSheets = timeSheetService.retrieveList(user.getUser_id(), startPeriod);

		if (timeSheets.size() == 0) {
			timeSheet = new TimeSheet();

			timeSheet.setUserId(user.getUser_id());
			timeSheet.setEntered(new Date());
			timeSheet.setStartPeriod(getStartPeriod());

			List<TimeSheetAccounts> accounts = timeSheetAccounts.retrieveRawList();

			model.addAttribute("accounts", accounts);
			model.addAttribute("timeSheet", timeSheet);

			return "entertime";
		}

		model.addAttribute("periodClosed", periodClosed);
		model.addAttribute("startPeriod", startPeriod);
		model.addAttribute("timeSheets", timeSheets);

		return "timesheet";
	}

	@RequestMapping("/timereporting")
	public String timeReporting() {

		return "timereporting";
	}

	@RequestMapping("/employee/submittime")
	public String sumbitTimeSheet(@ModelAttribute("startPeriod") Date startPeriod, BindingResult result, Model model,
			Principal principal) {

		UserProfile user = userProfileService.retrieve(principal.getName());
		timeSheetService.submit(user.getUser_id(), startPeriod);
		model.addAttribute("startPeriod", startPeriod);

		return "timesheetsubmitted";
	}

	@RequestMapping("/admin/employeelist")
	public String employeeList(Model model) {
		List<UserProfile> employees = employeeService.employeeList();

		model.addAttribute("employees", employees);

		return "employeelist";
	}

	@RequestMapping("/admin/approvetime")
	public String approveTimeSheet(@ModelAttribute("userID") int userID, BindingResult result, Model model) {

		Date startPeriod = getStartPeriod();
		List<TimeSheet> timeSheets = timeSheetService.retrieveApproveList(userID, startPeriod);

		model.addAttribute("startPeriod", startPeriod);

		model.addAttribute("timeSheets", timeSheets);

		return "approvetime";
	}

	@RequestMapping("/admin/submitapproval")
	public String submitApproval(@ModelAttribute("userId") int userId, @ModelAttribute("startPeriod") Date startPeriod,
			Model model) {

		timeSheetService.approveTimeSheet(userId, startPeriod);

		model.addAttribute(startPeriod);

		return "timesheetapproved";
	}

	@RequestMapping("/employee/payrollperiod")
	public String getPayrollPeriod(Model model) {

		List<String> periods = timeSheetService.getPayrollPeriods();
		model.addAttribute("periods", periods);

		return "payrollperiod";
	}

	@RequestMapping("/admin/payroll")
	public String parollProcessing(@ModelAttribute("period") String period, Model model)
			throws RecognitionException, IOException, RuntimeException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startPeriod = df.parse(period);
		List<UserProfile> employees = employeeService.employeeList();

		transactionService.processPayroll(employees, startPeriod);

		return "payrolldone";
	}

	private Date getStartPeriod() {
		Calendar c = GregorianCalendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return c.getTime();
	}
}
