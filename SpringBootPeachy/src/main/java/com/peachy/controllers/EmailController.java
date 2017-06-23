package com.peachy.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.peachy.component.FilePath;
import com.peachy.email.Email;
import com.peachy.email.MsgDisplay;
import com.peachy.email.ProcessEmail;
import com.peachy.entity.UserProfile;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.helper.FileUpload;
import com.peachy.service.UserProfileService;


@Controller
public class EmailController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	FilePath fp;
	
	private final String pageLink = "/vendor/emailpaging";
	
	private PagedListHolder<MsgDisplay> msgs;
	
	@RequestMapping("/vendor/checkemail")
	public String checkEmail(Model model) throws MessagingException, IOException, URISyntaxException {
		ProcessEmail email = new ProcessEmail(fp);
		Email myEmail = new Email();
		myEmail.setFrom("customer_service@peachyscoffee.com");
		myEmail.setPassword("In_heaven3!");
		if (msgs != null) {
			msgs.getSource().clear();
			msgs = null;
			System.gc();
		}
		msgs = email.receiveEmail(myEmail);
		msgs.setPageSize(15);
		msgs.setPage(0);
		model.addAttribute("objectList", msgs);
		model.addAttribute("pagelink", pageLink);
				
		return "checkemail";
	}
	
	@RequestMapping("/vendor/getemailfile")
	public String getEmailFile(Model model) {
		FileUpload fu = new FileUpload();
		
		model.addAttribute("fileUpload",fu);
		
		return "getemailfile";
	}
	
	@RequestMapping("/vendor/mailsent")
	public String sendMonthlyNewsLetter(@ModelAttribute("fileUpload") FileUpload fu, BindingResult result, Model model) throws Exception {
		List<UserProfile> users = userProfileService.getMonthlyNewsLetterUsers();
		ProcessEmail pe = new ProcessEmail(fp);
		pe.monthlyNewsLetter(users, fu);
		
		model.addAttribute("mailCount", users.size());
		
		return "mailsent";
	}
	@RequestMapping("/vendor/monthlynewsletter")
	public String getMonthlyNewsLetterList(Model model) throws IOException {

		
		String[] label = {"userID", "firstname","lastname", "maleFemale", "address1", "address2", "city", 
						  "region", "postalCode", "country", "currency", "homePhone", "cellPhone", "username", 
						  "password", "shippingInfo", "monthlyMailing", "authority", "enabled", "dailySpecials", "dateAdded"};
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		List<UserProfile> users = userProfileService.getDailySpecialUsers();
		String fileName = fp.getOutPath() + "MailMerge" + sdf.format(new Date()) + ".csv";
		Writer hdr = new FileWriter(fileName);
		CsvBeanWriter csvWriter = new CsvBeanWriter(hdr,
				CsvPreference.STANDARD_PREFERENCE);
		csvWriter.writeHeader(label);
		
		for(UserProfile user: users) {
			csvWriter.write(user, label);
		}
		
		csvWriter.close();
		
		model.addAttribute("mailCount", users.size());
		
		return "monthlynewsletter";
	}
	
	
/************************************************************************************************************
 * Paging Handlers
 ************************************************************************************************************/
	
	
	@RequestMapping(value="/vendor/emailpaging", method=RequestMethod.GET)
	public String handleEmailRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;
	        
	        if (msgs == null) {
	            throw new SessionTimedOutException("Your session has timed out. Please login again.");
	        }
	        pgNum = isInteger(page);
	        
	        if ("next".equals(page)) {
	        	msgs.nextPage();
	        }
	        else if ("prev".equals(page)) {
	        	msgs.previousPage();
	        }else if (pgNum != -1) {
	        	msgs.setPage(pgNum);
	        }
	        model.addAttribute("objectList", msgs);
	        model.addAttribute("pagelink", pageLink);
	        
	        return "checkemail";        
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
