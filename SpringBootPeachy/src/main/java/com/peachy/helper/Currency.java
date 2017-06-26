package com.peachy.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import com.peachy.component.CurrencyConfigurator;


public class Currency {
	

	CurrencyConfigurator cc;
	
	public Currency(CurrencyConfigurator cc) {
		this.cc = cc;
	}
	
	public String getRecord(String base) throws IOException, JSONException {
		URL currency = new URL(cc.getCurrencyUrl() + cc.getBaseCurrency());
		BufferedReader in = null;
		BufferedWriter out = null;
		boolean fileIO = false;
		
		try {
	        in = new BufferedReader(new InputStreamReader(currency.openStream()));
		} catch (IOException e) {
			in = new BufferedReader(new FileReader(cc.getCurrencyFile()));
			fileIO = true;
		}

        String inputLine;
        String buff = "";
        while ((inputLine = in.readLine()) != null) {
        	buff = buff + inputLine;
        }
        
        in.close();
        if (fileIO == false) {
        	out = new BufferedWriter(new FileWriter(cc.getCurrencyFile()));
            out.write(buff, 0, buff.length());
            out.close();
       }
        
        return buff;
	}
	
	public Date getLastUpdate() throws JSONException, IOException, ParseException {
		String currencyRecord;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		currencyRecord = getRecord(cc.getBaseCurrency());
		JSONObject lastUpdate = new JSONObject(currencyRecord);

		return df.parse((String) lastUpdate.get("date"));
	}
	
	public double getRate(String target) throws IOException, URISyntaxException {
		double rate;
		
		if (cc.getBaseCurrency().compareTo(target) == 0) {
			return 1.0;
		}
		
		String currencyRecord = getRecord(cc.getBaseCurrency());
		JSONObject record = new JSONObject(currencyRecord);
       
        JSONObject rates = record.getJSONObject("rates");
        rate = rates.getDouble(target);
        
		return rate;
	}
		
	public String getSymbol(String currency) {
		String symbol = "";
		
		switch (currency) {
		case "AUD":
			symbol = "$";
			break;
		case "BGN":
			symbol = "BGN";
			break;
		case "BRL":
			symbol = "R$";
			break;
		case "CAD":
			symbol = "$";
			break;
		case "CHF":
			symbol = "CHF";
			break;
		case "CNY":
			symbol = "Y";
			break;
		case "CZK":
			symbol = "KC";
			break;
		case "DKK":
			symbol = "KR";
			break;
		case "EUR":
			symbol ="&euro;";
			break;
		case "GBP":
			symbol = "£";
			break;
		case "HKD":
			symbol = "$";
			break;
		case "HRK":
			symbol = "KN";
			break;
		case "HUF":
			symbol = "Ft";
			break;
		case "IDR":
			symbol = "Rp";
			break;
		case "ILS":
			symbol = "IL";
			break;
		case "INR":
			symbol = "&#8377;";
			break;
		case "JPY":
			symbol = "&#165;";
			break;
		case "KRW":
			symbol = "&#8361;";
			break;
		case "MXN":
			symbol = "$";
			break;
		case "MYR":
			symbol = "RM";
			break;
		case "NOK":
			symbol = "KR";
			break;
		case "NZD":
			symbol = "$";
			break;
		case "PHP":
			symbol = "&#8369;";
			break;
		case "PLN":
			symbol = "Zt";
			break;
		case "RON":
			symbol = "lei";
			break;
		case "RUB":
			symbol = "&#8381;";
			break;
		case "SEK":
			symbol = "kr";
			break;
		case "SGD":
			symbol = "$";
			break;
		case "THB":
			symbol = "B";
			break;
		case "TRY":
			symbol = "&#8378;";
			break;
		case "USD":
			symbol = "$";
			break;
		case "ZAR":
			symbol = "Z$";
			break;
		default:
			symbol = "$";
			break;				
		}
		
		return symbol;
	}
	
	public String getAsciiSymbol(String currency) {
		String symbol = "";
		
		switch (currency) {
		case "AUD":
			symbol = "$";
			break;
		case "BGN":
			symbol = "BGN";
			break;
		case "BRL":
			symbol = "R$";
			break;
		case "CAD":
			symbol = "$";
			break;
		case "CHF":
			symbol = "CHF";
			break;
		case "CNY":
			symbol = "Y";
			break;
		case "CZK":
			symbol = "KC";
			break;
		case "DKK":
			symbol = "KR";
			break;
		case "EUR":
			symbol = "€";
			break;
		case "GBP":
			symbol = "£";
			break;
		case "HKD":
			symbol = "$";
			break;
		case "HRK":
			symbol = "KN";
			break;
		case "HUF":
			symbol = "Ft";
			break;
		case "IDR":
			symbol = "Rp";
			break;
		case "ILS":
			symbol = "IL";
			break;
		case "INR":
			symbol = "INR";
			break;
		case "JPY":
			symbol = "Y";
			break;
		case "KRW":
			symbol = "KR";
			break;
		case "MXN":
			symbol = "$";
			break;
		case "MYR":
			symbol = "RM";
			break;
		case "NOK":
			symbol = "KR";
			break;
		case "NZD":
			symbol = "$";
			break;
		case "PHP":
			symbol = "P";
			break;
		case "PLN":
			symbol = "Zt";
			break;
		case "RON":
			symbol = "lei";
			break;
		case "RUB":
			symbol = "R";
			break;
		case "SEK":
			symbol = "kr";
			break;
		case "SGD":
			symbol = "$";
			break;
		case "THB":
			symbol = "B";
			break;
		case "TRY":
			symbol = "Try";
			break;
		case "USD":
			symbol = "$";
			break;
		case "ZAR":
			symbol = "Z$";
			break;
		default:
			symbol = "$";
			break;				
		}
		
		return symbol;
	}
	
	public double convert(double amount, String target) throws IOException, URISyntaxException {
		return amount * getRate(target);
	}
}
