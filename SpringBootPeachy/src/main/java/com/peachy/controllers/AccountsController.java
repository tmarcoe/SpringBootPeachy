package com.peachy.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.peachy.component.ChartOfAccountsReportSettings;
import com.peachy.component.FilePath;
import com.peachy.entity.ChartOfAccounts;
import com.peachy.reports.ChartOfAccountsReport;
import com.peachy.service.ChartOfAccountsService;


@Controller
public class AccountsController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AccountsController.class.getName());
	private final String pageLink = "/vendor/accountpaging";
	private final int pageSize = 10;
	private final String ftlConfigFile = "fetal.properties";
	private final String xmlMap = "accounts.xml";
	
	@Autowired
	private ChartOfAccountsService chartOfAccountsService;
	
	@Autowired
	FilePath fp;
	
	@Autowired
	private ChartOfAccountsReportSettings ts;
	
	PagedListHolder<ChartOfAccounts> accounts;
	

	@RequestMapping("/vendor/manageaccount")
	public String showMangeAccounts(Model model) {
		if (accounts != null) {
			accounts.getSource().clear();
			accounts = null;
			System.gc();
		}
		accounts = chartOfAccountsService.retrieveList();
		accounts.setPage(0);
		accounts.setPageSize(pageSize);
		
		if (accounts.getSource().size() == 0) {
			ChartOfAccounts chartOfAccounts = new ChartOfAccounts();
			model.addAttribute("chartOfAccounts", chartOfAccounts);

			return "addaccount";
		}
		String deleteKey = new String("");

		model.addAttribute("objectList", accounts);
		model.addAttribute("deleteKey", deleteKey);
		model.addAttribute("pagelink", pageLink);
		
		return "manageaccount";
	}

	@RequestMapping("/vendor/addaccount")
	public String addAccount(Model model) {
		ChartOfAccounts chartOfAccounts = new ChartOfAccounts();
		model.addAttribute("chartOfAccounts", chartOfAccounts);

		return "addaccount";
	}

	@RequestMapping("/vendor/submitaddaccount")
	public String submitAddAccount(@Valid ChartOfAccounts chartOfAccounts, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "addaccount";
		}
		if (chartOfAccountsService.exists(chartOfAccounts.getAccountNum()) == true){
			result.rejectValue("accountNum", "DuplicateKey.chartOfAccounts.accountNum");
			return "addaccount";
		}
		chartOfAccountsService.create(chartOfAccounts);
		logger.info("'" + chartOfAccounts.getAccountNum() + "' has been created." );
		if (accounts != null) {
			accounts.getSource().clear();
			accounts = null;
			System.gc();
		}

		return "redirect:/vendor/manageaccount";
	}


	@RequestMapping("/vendor/saveaccount")
	public String saveAccount(@Valid ChartOfAccounts chartOfAccounts, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "accountdetail";
		}
		chartOfAccountsService.update(chartOfAccounts);
		logger.info("Account # '" + chartOfAccounts.getAccountNum() + "' has been updated.");

		if (accounts != null) {
			accounts.getSource().clear();
			accounts = null;
			System.gc();
		}

		return "redirect:/vendor/manageaccount";
	}

	@RequestMapping("/vendor/deleteaccount")
	public String deleteAccount(@ModelAttribute("deleteKey") String deleteKey,
			Model model) {

		chartOfAccountsService.delete(deleteKey);
		
		logger.info("Account # '" + deleteKey + "' has been deleted.");

		if (accounts != null) {
			accounts.getSource().clear();
			accounts = null;
			System.gc();
		}

		if (chartOfAccountsService.exists() ==  false) {
			ChartOfAccounts chartOfAccounts = new ChartOfAccounts();
			model.addAttribute("chartOfAccounts", chartOfAccounts);

			return "addaccount";
		}


		return "redirect:/vendor/manageaccount";
	}

	@RequestMapping("/vendor/accountdetail")
	public String showAccountDetail(
			@ModelAttribute("detailKey") String detailKey, Model model) {

		ChartOfAccounts account = chartOfAccountsService.retrieve(detailKey);
		model.addAttribute("chartOfAccounts", account);

		return "accountdetail";
	}
	
	@RequestMapping("/vendor/printaccounts")
	public String printAccounts(Model model) throws IOException {
		ChartOfAccountsReport report = new ChartOfAccountsReport(ts);
		String filePath = fp.getReportPath();
		String fileName = filePath + "ChartOfAccounts.pdf";
		accounts = chartOfAccountsService.retrieveList();
		report.pdfChartOfAccuntsReport(fileName, accounts);
		
		return "printaccounts";
	}
	@RequestMapping("/vendor/createmap")
	public String createMappingFile() throws MalformedURLException, IOException, ParserConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		Properties prop = new Properties();
		InputStream ftlConfig = new URL(fp.getConfig() + ftlConfigFile).openStream();
		prop.load(ftlConfig);
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("alias");

		doc.appendChild(rootElement);
		
		List<ChartOfAccounts> accounts = chartOfAccountsService.getRawList();
		for (ChartOfAccounts item : accounts) {
			Element map = doc.createElement("map");
			map.setAttribute("name", item.getAccountName());
			map.setAttribute("value", item.getAccountNum());
			rootElement.appendChild(map);
		}
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		OutputStream out = new FileOutputStream(new File(prop.getProperty("mapPathOut") + xmlMap));
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);

		
		return "redirect:/vendor/manageaccount";
	}
	@RequestMapping(value="/vendor/accountpaging", method=RequestMethod.GET)
	public String handleAccountsRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;
	        
	        pgNum = isInteger(page);
	        
	        if ("next".equals(page)) {
	        	accounts.nextPage();
	        }
	        else if ("prev".equals(page)) {
	        	accounts.previousPage();
	        }else if (pgNum != -1) {
	        	accounts.setPage(pgNum);
	        }
	       model.addAttribute("objectList", accounts);
	       model.addAttribute("pagelink", pageLink);
	        
	        return "manageaccount";
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
