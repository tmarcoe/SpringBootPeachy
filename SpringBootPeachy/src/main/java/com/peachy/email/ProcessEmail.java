package com.peachy.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.support.PagedListHolder;

import com.peachy.component.FilePath;
import com.peachy.entity.Inventory;
import com.peachy.entity.Returns;
import com.peachy.entity.UserProfile;
import com.peachy.helper.DataEncryption;
import com.peachy.helper.FileUpload;


public class ProcessEmail {
	final private String configFile = "email.properties";
	final private String from = "customer_service@peachyscoffee.com";
	final private String password = "In_heaven3!";

	private FilePath fp;
	
		
	public ProcessEmail(FilePath fp) {
		this.fp = fp;
	}
	public void sendMail(final Email email) throws Exception {
		Properties properties = new Properties();
		
		URL url = new URL(fp.getConfig() + configFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
        try {
            properties.load(in);
        } catch (SecurityException | IOException | IllegalArgumentException e) {
            throw e;
        } finally {
            try { in.close(); }
            catch (IOException ie) { throw ie; }
        }

		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email.getFrom(),
						email.getPassword());
			}
		};

		Session session = Session.getInstance(properties, auth);
		MimeMessage msg = new MimeMessage(session);
			
		msg.setSubject(email.getSubject());
		msg.setContent(email.getMessage(), "text/html");

		msg.setFrom(new InternetAddress(email.getFrom(), email.getName()));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));

		Transport.send(msg);

	}
	public void sendRma(UserProfile user, Returns returns) throws Exception {
		Email email = new Email();
		String greeting = "";
		email.setName(user.getFirstname() + " " + user.getLastname());
		email.setFrom(from);
		email.setPassword(password);
		if (user.getMaleFemale().compareToIgnoreCase("M") == 0) {
			greeting = "<h3>Dear Mr. " + user.getLastname() + "</h3>";
		}else{
			greeting = "<h3>Dear Ms. " + user.getLastname() + "</h3>";
		}
		email.setSubject(String.format("RMA# - %08d", returns.getRmaId()));
		String msg = greeting + String.format("<h3>RMA #%08d has been submitted.</h3>", returns.getRmaId());
		email.setTo(user.getUsername());
		email.setMessage(msg);
		sendMail(email);
	}
	
	public void sendLoginLink(UserProfile user, String baseUrl) throws Exception {
		Email email = new Email();
		String encodedId = DataEncryption.encode(String.valueOf(user.getUser_id()));
				
		email.setName("Peachy's Coffee");
		email.setFrom(from);
		email.setPassword(password);
		email.setSubject("Welcome to Peachy's Coffee");
		String msg = 	 "<h1>Welcome " + user.getFirstname() + " " + user.getLastname() + "</h1>" +
		             	 "<h3>Please click the link to activate your account</h3>" +
		             	 "<a href='" + baseUrl + encodedId + "&prKey=" + user.getPassword() + "'>" +
		             	 "Activate your accout</a>";

		email.setTo(user.getUsername());
		email.setMessage(msg);
		
		sendMail(email);
		
	}

	public void sendPasswordRecovery(UserProfile user, String baseUrl, String token) throws Exception {
		
		Email email = new Email();
		String encodedId = DataEncryption.encode(String.valueOf(user.getUser_id()));
				
		email.setName("Peachy's Coffee");
		email.setFrom(from);
		email.setPassword(password);
		email.setSubject("Password Recovery");
		String msg = 	 "<h1>Click the link to reset your password</h1>" +
		             	 "<a href='" + baseUrl + encodedId + "&prKey=" + token + "'>" +
		             	 "Reset Password</a>";

		email.setTo(user.getUsername());
		email.setMessage(msg);
		
		sendMail(email);
		
	}
	
	public String getDailySpecials(List <Inventory> inventoryList) throws IOException {
		final String head = "<div " +
						    "style=\"color: #fff; background: " +
						    "#f0c575; padding: .5em; font-size: " + 
						    "31px; font-style: italic; text-align: " + 
						    "center;\">Peachy's Coffee</div>\n" + 
						    "<center>\n" +
						    "<fieldset style=\"background: #d5e0ff; " +
						    "display: block; width: 50%; border: 4px ridge #808080;\">\n" +
						    "<legend style=\"font-size: 30;\">Daily Specials</legend>\n" +
						    "<ul style=\"list-style-type: none;\" >";
		final String foot = "</ul> \n" +
							"</fieldset> \n" +
							"</center> \n";
		String saleItems = "";
		for (Inventory item : inventoryList) {
			saleItems += "<li><a href=\"http://www.peachyscoffee.com:8080/productdetails?skuNum=" +
						 item.getSku_num() + "\">" + item.getProduct_name() + "</a>\n";
		}
		
		
		
		return head + saleItems + foot;
	}
		
	public void monthlyNewsLetter(List<UserProfile> users, FileUpload fileUpload ) throws Exception {
		BufferedReader is = new BufferedReader(new InputStreamReader(fileUpload.getFile().getInputStream()));
		String inBuff = "";
		StringBuilder message = new StringBuilder();
		Email email = new Email();

		while ((inBuff = is.readLine()) != null) {
			message.append(inBuff);
		}
		email.setFrom(from);
		email.setPassword(password);
		email.setSubject("Monthly News Letter");

		for (UserProfile user : users) {
			email.setName(user.getFirstname() + " " + user.getLastname());
			email.setTo(user.getUsername());
			email.setMessage(message.toString());
			sendMail(email);
		}
		
		
	}
		
	public PagedListHolder<MsgDisplay> receiveEmail(Email email) throws MessagingException, IOException, URISyntaxException {
			List<MsgDisplay> msgList = new ArrayList<MsgDisplay>();
			
			URL url = new URL(fp.getConfig() + configFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			Properties properties = new Properties();
			
	        try {
	            properties.load(in);
	        } catch (SecurityException | IOException | IllegalArgumentException e) {
	            throw e;
	        } finally {
	            try { in.close(); }
	            catch (IOException ie) { throw ie; }
	        }

			Session emailSession = Session.getDefaultInstance(properties);

			Store store = emailSession.getStore();
			String mailHost = properties.getProperty("mail.imap.host");
			store.connect(mailHost, email.getFrom(), email.getPassword());

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();

			for (int i = 0, n = messages.length; i < n; i++) {

				MsgDisplay msg = new MsgDisplay();
				msg.setFrom(messages[i].getFrom()[0].toString());
				msg.setSubject(messages[i].getSubject());
				msg.setText(messages[i].getContent().toString());
				msg.setRead(messages[i].isSet(Flag.SEEN));
				if(msg.isRead() == false) {
					msgList.add(msg);
				}
			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();
			
		return new PagedListHolder<MsgDisplay>(msgList);
	}
	
	public void sendDailySpecials(List<UserProfile> users, String message) throws Exception {
		
		Email email = new Email();

		email.setFrom(from);
		email.setPassword(password);
		email.setSubject("Daily Specials");
		for (UserProfile user : users) {
			email.setName("Peachy's Coffee");
			email.setTo(user.getUsername());
			email.setMessage(message);
			sendMail(email);
		}
	}

}
