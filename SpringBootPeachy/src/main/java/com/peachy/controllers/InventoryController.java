package com.peachy.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peachy.component.FilePath;
import com.peachy.entity.Inventory;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.helper.FileUpload;
import com.peachy.helper.Order;
import com.peachy.service.FetalTransactionService;
import com.peachy.service.InventoryService;
import com.peachy.service.SchemesService;

@Controller
public class InventoryController implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String pageLink = "/vendor/productpaging";

	private static Logger logger = Logger.getLogger(InventoryController.class.getName());

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private SchemesService schemesService;

	private PagedListHolder<Inventory> adminInventoryList;
	private PagedListHolder<Inventory> orderList;

	@Autowired
	private FilePath fileLocations;

	@Autowired
	private FetalTransactionService transactionService;

	@RequestMapping("/vendor/productadded")
	public String productAdded(@Valid @ModelAttribute("inventory") Inventory inventory, BindingResult result,
			Model model) throws IOException, URISyntaxException {
		if (result.hasErrors()) {
			return "addinventory";
		}

		if (inventoryService.retrieve(inventory.getSku_num()) != null) {
			result.rejectValue("skuNum", "Duplicate.inventory.skuNum");
			return "addinventory";
		}

		if (inventory.getImage().length() == 0) {
			inventory.setImage("notavailable.jpg");
		}

		inventoryService.create(inventory);
		logger.info("New inventory item added.");
		if (adminInventoryList != null) {
			adminInventoryList.getSource().clear();
			adminInventoryList = null;
			System.gc();
		}
		adminInventoryList = inventoryService.listAllProducts();
		adminInventoryList.setPage(0);
		adminInventoryList.setPageSize(15);
		model.addAttribute("objectList", adminInventoryList);
		model.addAttribute("pagelink", pageLink);

		return "manageinventory";
	}

	@RequestMapping("/vendor/adminsearch")
	public String adminSearch(@ModelAttribute("mySearch") String mySearch, BindingResult result, Model model) {
		if (adminInventoryList != null) {
			adminInventoryList.getSource().clear();
			adminInventoryList = null;
		}
		adminInventoryList = inventoryService.searchInventory(mySearch);

		model.addAttribute("objectList", adminInventoryList);
		model.addAttribute("pagelink", pageLink);

		return "manageinventory";
	}

	@RequestMapping("/vendor/managesales")
	public String manageSaleItems(Model model) {

		if (adminInventoryList != null) {
			adminInventoryList.getSource().clear();
			adminInventoryList = null;
		}
		adminInventoryList = new PagedListHolder<Inventory>(inventoryService.listSaleItems());
		if (adminInventoryList.getSource().size() == 0) {
			return "nosaleitems";
		}

		return "redirect:/vendor/manageinventory";
	}

	@RequestMapping("/vendor/manageinventory")
	public String showManageInventory(Model model) throws IOException, URISyntaxException {

		if (adminInventoryList != null) {
			adminInventoryList.getSource().clear();
			adminInventoryList = null;
		}
		adminInventoryList = inventoryService.listAllProducts();
		adminInventoryList.setPage(0);
		adminInventoryList.setPageSize(15);

		model.addAttribute("objectList", adminInventoryList);
		model.addAttribute("pagelink", pageLink);

		return "manageinventory";
	}

	@RequestMapping("/vendor/inventorysaved")
	public String inventorySaved(@ModelAttribute("inventory") Inventory inventory, Model model)
			throws IOException, URISyntaxException {

		inventoryService.update(inventory);
		logger.info("'" + inventory.getSku_num() + "' has been updated.");

		if (adminInventoryList != null) {
			adminInventoryList.getSource().clear();
			adminInventoryList = null;
		}

		return "redirect:/vendor/manageinventory";
	}

	@RequestMapping("/vendor/deleteinventory")
	public String deleteInventory(@ModelAttribute("deleteKey") String deleteKey, Model model)
			throws URISyntaxException, IOException {
		Inventory inventory = inventoryService.retrieve(deleteKey);

		File file = new File(fileLocations.getImgUploadLoc() + inventory.getImage());
		file.delete();
		file = null;
		logger.info("File: " + fileLocations.getImageLoc() + inventory.getImage() + " is deleted.");

		inventoryService.delete(inventory);
		logger.info("Item '" + deleteKey + "' is deleted.");
		if (adminInventoryList != null) {
			adminInventoryList.getSource().clear();
			adminInventoryList = null;
			System.gc();
		}
		return "redirect:/vendor/manageinventory";
	}

	@RequestMapping("/vendor/uploadfile")
	public String showUploadFile(Model model) {

		FileUpload fileUpload = new FileUpload();
		model.addAttribute("fileUpload", fileUpload);

		return "uploadfile";
	}


	@RequestMapping("/vendor/singleitem")
	public String restockSingleItem(Model model) {
		String inventoryKey = "";

		model.addAttribute("sku", inventoryKey);

		return "singleitem";
	}

	@RequestMapping("/vendor/addinventory")
	public String addInventory(@ModelAttribute("fileUpload") FileUpload fileUpload, BindingResult result, Model model)
			throws URISyntaxException {
		File file = null;

		String contentType = fileUpload.getFile().getContentType();

		if (!contentType.equals("image/png")) {
			result.rejectValue("file", "Unsupported.fileUpload.file");
			return "uploadfile";
		}

		try {
			InputStream is = fileUpload.getFile().getInputStream();
			File f1 = new File(fileLocations.getImgUploadLoc());

			file = File.createTempFile("img", ".png", f1);

			FileOutputStream fos = new FileOutputStream(file);

			int data = 0;
			while ((data = is.read()) != -1) {
				fos.write(data);
			}

			fos.close();
			is.close();
		} catch (IOException i) {
			result.rejectValue("file", "IOException.fileUpload.file", i.getMessage());
			i.printStackTrace();
		} catch (IllegalStateException s) {
			result.rejectValue("file", "IllegalStateException.fileUpload.file", s.getMessage());
		}
		logger.info("'" + file.getPath() + file.getName() + "' has been created.");
		Inventory inventory = new Inventory();
		inventory.setImage(file.getName());
		List<String> schemeList = schemesService.retrieveAllSchemes();

		model.addAttribute("schemeList", schemeList);

		model.addAttribute("inventory", inventory);

		return "addinventory";
	}

	@RequestMapping("/vendor/inventorydetails")
	public String showInventoryDetails(@ModelAttribute("InventoryKey") String inventoryKey, Model model)
			throws IOException, URISyntaxException {
		List<String> schemeList = schemesService.retrieveAllSchemes();
		Inventory inventory = inventoryService.retrieve(inventoryKey);

		model.addAttribute("schemeList", schemeList);
		model.addAttribute("inventory", inventory);

		return "inventorydetails";
	}

	@RequestMapping("/vendor/orderinventory")
	public String orderInventory(Model model) {
		if (orderList != null) {
			orderList.getSource().clear();
			orderList = null;
			System.gc();
		}
		orderList = inventoryService.getReplenishList();
		orderList.setPageSize(20);
		model.addAttribute("objectList", orderList);
		model.addAttribute("pagelink", "/orderpaging");

		return "orderinventory";
	}

	@RequestMapping("/vendor/stockshelves")
	public String stockShelves(@ModelAttribute("order") Order order, Model model)
			throws IOException, URISyntaxException {

		order.setInventory(inventoryService.retrieve(order.getInventory().getSku_num()));

		try {
			transactionService.purchaseInventory(order);
		} catch (IOException | RuntimeException e) {
			return "error";
		}

		logger.info("'" + order.getInventory().getSku_num() + "' has been purchased.");
		orderList = inventoryService.getReplenishList();
		orderList.setPageSize(20);
		model.addAttribute("objectList", orderList);
		model.addAttribute("pagelink", "/orderpaging");

		return "orderinventory";
	}

	@RequestMapping("/vendor/replenish")
	public String replenishStock(@ModelAttribute("sku") String skuNum, Model model)
			throws IOException, URISyntaxException {
		Inventory inventory = inventoryService.retrieve(skuNum);

		if (inventory == null) {
			String inventoryKey = "";
			String error = "Invalid product code.";

			model.addAttribute("sku", inventoryKey);
			model.addAttribute("error", error);

			return "singleitem";
		}
		Order order = new Order(inventory);
		model.addAttribute("order", order);

		return "replenish";
	}

	@RequestMapping(value = "/vendor/orderpaging", method = RequestMethod.GET)
	public String handleOrderRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;

		if (orderList == null) {
			throw new SessionTimedOutException("Your session has timed out. Please login again.");
		}
		pgNum = isInteger(page);

		if ("next".equals(page)) {
			orderList.nextPage();
		} else if ("prev".equals(page)) {
			orderList.previousPage();
		} else if (pgNum != -1) {
			orderList.setPage(pgNum);
		}
		model.addAttribute("objectList", orderList);
		model.addAttribute("pagelink", "/orderpaging");

		return "orderinventory";
	}

	@RequestMapping(value = "/vendor/productpaging", method = RequestMethod.GET)
	public String handleProductRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;

		if (adminInventoryList == null) {
			throw new SessionTimedOutException("Your session has timed out. Please login again.");
		}
		pgNum = isInteger(page);

		if ("next".equals(page)) {
			adminInventoryList.nextPage();
		} else if ("prev".equals(page)) {
			adminInventoryList.previousPage();
		} else if (pgNum != -1) {
			adminInventoryList.setPage(pgNum);
		}
		model.addAttribute("objectList", adminInventoryList);
		model.addAttribute("pagelink", pageLink);

		return "manageinventory";
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
