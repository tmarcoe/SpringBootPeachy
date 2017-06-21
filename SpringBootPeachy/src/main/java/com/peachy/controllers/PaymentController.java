package com.peachy.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Principal;

import javax.xml.soap.SOAPException;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.braintreegateway.BraintreeGateway;
import com.peachy.component.CurrencyConfigurator;
import com.peachy.component.FilePath;
import com.peachy.entity.Invoice;
import com.peachy.entity.UserProfile;
import com.peachy.helper.Currency;
import com.peachy.payment.BraintreeGatewayFactory;
import com.peachy.payment.Checkout;
import com.peachy.payment.Payment;
import com.peachy.service.FetalTransactionService;
import com.peachy.service.InvoiceService;
import com.peachy.service.UserProfileService;

@Controller
public class PaymentController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	FilePath fp;
	
	@Autowired
	CurrencyConfigurator cc;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private FetalTransactionService transactionService;

	private BraintreeGateway gateway;

	private static Logger logger = Logger.getLogger(PaymentController.class
			.getName());

	@RequestMapping("/user/pcinfo")
	public String processPayment(
			@ModelAttribute("invoice") Invoice invoice,
			Principal principal, Model model) throws SecurityException,
			IllegalArgumentException, IOException, URISyntaxException,
			UnknownHostException {

		Payment payment = new Payment();
		UserProfile user = userProfileService.retrieve(principal.getName());
		Checkout checkout = new Checkout(fp);
		payment.setPaymentType("braintree");
		
		invoice = invoiceService.getOpenOrder(user.getUser_id());
		switch (payment.getPaymentType()) {
		case "7-connect":
			checkout.sevenConnect(invoice, payment);
			break;
		case "braintree":
			
			gateway = BraintreeGatewayFactory.fromConfigFile(new URL(
					fp.getConfig() + "braintree.properties"));
			checkout.populatePayment(payment, user);
			model.addAttribute("clientToken", gateway.clientToken().generate());
			model.addAttribute("payment", payment);
			return "pcinfo";
		default:
			return "error";
		}
		Currency currency = new Currency(cc);
		
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));

		return "thankyou";
	}


	@RequestMapping("/user/pcdenied")
	public String showPcDenied() {
		return "pcdenied";
	}

	@RequestMapping("/user/processcart")
	public String processShoppingCart(
			@ModelAttribute("payment") Payment payment,
			@ModelAttribute("payment_method_nonce") String nonce,
			Principal principal, Model model) throws RecognitionException,
			URISyntaxException, IOException, SOAPException {

		UserProfile user = userProfileService.retrieve(principal.getName());
		payment.setUsername(user.getUsername());
		Invoice header = invoiceService.getOpenOrder(user
				.getUser_id());
		Checkout checkout = new Checkout(fp);
		header = invoiceService.totalInvoice(header);
		header.setUser_id(user.getUser_id());

		if (checkout.paymentCard(
				payment,
				gateway,
				BigDecimal.valueOf(header.getTotal() + header.getTotal_tax()
						+ header.getShipping_cost() + header.getAdded_charges()),
				nonce) == true) {
			logger.info("Processing shopping cart.");
			try {
				transactionService.processSales(header);
			} catch (IOException | RuntimeException e) {
				logger.error(e.getMessage());
				return "error";
			}
		} else {
			return "redirect:/user/pcdenied";
		}
		model.addAttribute("invoiceHeader", header);
		Currency currency = new Currency(cc);
		
		model.addAttribute("rate", currency.getRate(user.getCurrency()));
		model.addAttribute("currencySymbol", currency.getSymbol(user.getCurrency()));

		return "thankyou";
	}
}
