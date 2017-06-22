package com.peachy.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peachy.component.FetalConfigurator;
import com.peachy.component.FilePath;
import com.peachy.entity.FetalScripts;
import com.peachy.service.FetalScriptsService;



@Controller
public class FetalController {

	@Autowired
	private FilePath fp;
	
	@Autowired
	private FetalConfigurator fc;
	
	@Autowired
	private FetalScriptsService fetalScriptsService;
	
	PagedListHolder<FetalScripts> fetalScripts;
	private final String pageLink = "/vendor/fetalpaging";

	private SimpleDateFormat dateFormat;
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}
	@RequestMapping("/fetaladmin")
	public String fetalAdmin() {
		
		return "fetaladmin";
	}
	@RequestMapping("/vendor/fetallist") 
	public String fetalList(Model model) {
		
		fetalScripts = fetalScriptsService.retrieveList();
		
		model.addAttribute("objectList", fetalScripts);
		model.addAttribute("pagelink", pageLink);
		

		
		return "fetalList";
	}
	
	@RequestMapping("/admin/postToProd")
	public String postToProduction(@ModelAttribute("id") int id, Model model) throws IOException {
		String destination = "";
		URL url = new URL(fc.getProperiesFile());
		InputStream in = url.openStream();
		Reader reader = new InputStreamReader(in);
		Properties props = new Properties();
		props.load(reader);
		FetalScripts fetal = fetalScriptsService.retrieve(id);
		File moveFile = new File(props.getProperty("quarantinePath") + fetal.getFile_name());
		
		switch(fetal.getType()) {
		case "RULE":
			destination = props.getProperty("transPath");
			break;
		case "COUPON":
			destination = props.getProperty("couponPath");
			break;
		case "BLOCK":
			destination = props.getProperty("blockPath");
			break;
		default:
			destination = props.getProperty("transPath");
			break;
		}
		moveFile.renameTo(new File(destination + fetal.getFile_name()));
		
		fetal.setStatus("PRODUCTION");
		fetalScriptsService.update(fetal);
		
		
		return "redirect:/vendor/fetallist";
	}
	
	@RequestMapping("/vendor/edittransaction")
	public String editTransaction(@ModelAttribute("id")int id, Model model) throws IOException {
		URL url = new URL(fc.getProperiesFile());
		InputStream in = url.openStream();
		Reader reader = new InputStreamReader(in);
		Properties props = new Properties();
		props.load(reader);
		FetalScripts fetalScripts = fetalScriptsService.retrieve(id);

		String fetalUrl = "";
		switch(fetalScripts.getType()) {
		case "RULE":
			fetalUrl = props.getProperty("transactionUrl");
			break;
		case "COUPON":
			fetalUrl = props.getProperty("couponUrl");
			break;
		case "BLOCK":
			fetalUrl = props.getProperty("blockUrl");
			break;
		default:
			fetalUrl = props.getProperty("transactionUrl");
			break;
		}
		
		model.addAttribute("quarantine", fp.getQuarantinePath());
		model.addAttribute("fetalUrl", fetalUrl);
		model.addAttribute("transFile", fetalScripts.getFile_name());
		
		return "edittransaction";
	}
	
	@RequestMapping(value = "/vendor/fetalpaging", method = RequestMethod.GET)
	public String fetalPager(@ModelAttribute("page") String page, Model model) throws Exception {
		
		int pgNum;

		pgNum = isInteger(page);

		if ("next".equals(page)) {
			fetalScripts.nextPage();
		} else if ("prev".equals(page)) {
			fetalScripts.previousPage();
		} else if (pgNum != -1) {
			fetalScripts.setPage(pgNum);
		}

	
		
		model.addAttribute("objectList", fetalScripts);
		model.addAttribute("pagelink", pageLink);
		
		return "products";
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
