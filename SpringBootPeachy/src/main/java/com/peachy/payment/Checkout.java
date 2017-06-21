package com.peachy.payment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CreditCard;
import com.braintreegateway.Customer;
import com.braintreegateway.Transaction;
import com.braintreegateway.Transaction.Status;
import com.peachy.component.FilePath;
import com.peachy.entity.Invoice;
import com.peachy.entity.UserProfile;

public class Checkout {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(Checkout.class
			.getName());
	private FilePath fp;
	
	private Status[] TRANSACTION_SUCCESS_STATUSES = new Status[] {
	        Transaction.Status.AUTHORIZED,
	        Transaction.Status.AUTHORIZING,
	        Transaction.Status.SETTLED,
	        Transaction.Status.SETTLEMENT_CONFIRMED,
	        Transaction.Status.SETTLEMENT_PENDING,
	        Transaction.Status.SETTLING,
	        Transaction.Status.SUBMITTED_FOR_SETTLEMENT
	     };

	public Checkout(FilePath fp) {
		this.fp = fp;
	}

	public boolean makePayment(Invoice header, Payment payment) throws URISyntaxException, IOException {
		switch (payment.getPaymentType()) {
		case "7-connect":
			sevenConnect(header, payment);
			break;
		default:
			return false;
		}
		return false;
	}

	public boolean sevenConnect(Invoice header, Payment payment ) throws URISyntaxException, IOException {
		PaymentObject po = new PaymentObject();
		Properties prop = getProperties("sevenConnect.properties");
		po.merchantID(prop.getProperty("merchantID"));
		po.merchantRef(prop.getProperty("merchantRef"));
		po.amount(header.getTotal() + header.getTotal_tax() + header.getShipping_cost() + header.getAdded_charges());
		po.successURL(prop.getProperty("successURL"));
		po.failURL(prop.getProperty("failURL"));
		po.token("628e936f45884030ac1f34bcde9c28efa6ae9c839623b45b8942bd4490e1f05d");
		po.transactionDescription("");
		po.receiptRemarks(buildAddress(payment));
		po.email(payment.getUsername());
		po.returnPaymentDetails(false);
		/*
		HttpClient httpClient = HttpClientBuilder.create().build();
		JSONObject pmntObject = po.getPmntObject();
		StringEntity params =new StringEntity(pmntObject.toString());
		HttpPost request = new HttpPost(prop.getProperty("transactURL"));
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.setEntity(params);
		@SuppressWarnings("unused")
		HttpResponse response = httpClient.execute(new HttpPost(prop.getProperty("inquireURL")));
		*/
		return false;
	}
	private String buildAddress(Payment payment) {
		String result = payment.getFirstName() + " " + 
						payment.getLastName() + "\n" +
						payment.getAddress1() + "\n" +
						payment.getAddress2() + "\n" +
						payment.getCity() + ", " + payment.getRegion() + " " + payment.getPostal();
		
		return result;
	}
	
	private Properties getProperties(String propFile) throws URISyntaxException, IOException {

		Reader fr = new FileReader(new File(new URI(fp.getConfig() + propFile)));
		Properties prop = new Properties();
		prop.load(fr);
		fr.close();
		return prop;
	}
	
    public boolean paymentCard(Payment payment, BraintreeGateway gateway, BigDecimal amount, String nonce) {
    	/*
    	amount = amount.setScale(2, RoundingMode.CEILING);
        TransactionRequest request = new TransactionRequest()
            	.amount(amount)
            	.paymentMethodNonce(nonce)
            	.billingAddress()
            		.firstName(payment.getFirstName())
            		.lastName(payment.getLastName())
            		.streetAddress(payment.getAddress1())
            		.extendedAddress(payment.getAddress2())
            		.locality(payment.getCity())
            		.region(payment.getRegion())
            		.countryCodeAlpha3(payment.getCountry())
            		.done()
            	.options()
            		.storeInVault(payment.isSaveInfo())
            		.addBillingAddressToPaymentMethod(payment.isSaveInfo())
            		.submitForSettlement(true)
            		.done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {    
            return true;
        } else if (result.getTransaction() != null) {
        	logger.error(result.getMessage());
            return false;
        } else {
            String errorString = "";
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
               errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
            }
            logger.error(errorString);
        }
        //TODO: Change to false after testing
         */
        return true;
    }

    public String getTransaction(BraintreeGateway gateway, String transactionId, Model model) throws Exception {
        Transaction transaction;
        CreditCard creditCard;
        Customer customer;

        transaction = gateway.transaction().find(transactionId);
        creditCard = transaction.getCreditCard();
        customer = transaction.getCustomer();

        model.addAttribute("isSuccess", Arrays.asList(TRANSACTION_SUCCESS_STATUSES).contains(transaction.getStatus()));
        model.addAttribute("transaction", transaction);
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("customer", customer);

        return "checkouts/show";
    }
    
	public void populatePayment(Payment payment, UserProfile user) {
		payment.setFirstName(user.getFirstname());
		payment.setLastName(user.getLastname());
		payment.setAddress1(user.getAddress1());
		payment.setAddress2(user.getAddress2());
		payment.setCity(user.getCity());
		payment.setRegion(user.getRegion());
		payment.setPostal(user.getPostalCode());
		payment.setCountry(user.getCountry());
	}

}
