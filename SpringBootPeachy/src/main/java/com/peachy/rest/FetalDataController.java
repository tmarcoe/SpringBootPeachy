package com.peachy.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.peachy.component.FetalConfigurator;
import com.peachy.entity.FetalScripts;
import com.peachy.service.FetalScriptsService;


@RestController
@RequestMapping(value = "/vendor/fetal-data", method = RequestMethod.GET)
public class FetalDataController {

	@Autowired
	private FetalConfigurator fc;
	
	@Autowired 
	FetalScriptsService fetalScriptsService;

	@RequestMapping("/trans-file")
	public String getTrasactionFile(@ModelAttribute("transFile") String transFile,
			@ModelAttribute("fetalUrl") String fetalUrl) throws IOException {
		URL url = new URL(fetalUrl + transFile);
		BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
		String sCurrentLine = "";
		String respString = "";

		while ((sCurrentLine = read.readLine()) != null) {
			respString = respString + sCurrentLine + "\n";
		}
		return respString;
	}

	@RequestMapping(value="/save-file", method=RequestMethod.PUT)
	public void putFile(@ModelAttribute("transFile") String transFile, @ModelAttribute("transData") String transData ) throws IOException {
		URL url = new URL(fc.getProperiesFile());
		InputStream in = url.openStream();
		Reader reader = new InputStreamReader(in);
		Properties props = new Properties();
		props.load(reader);
		FetalScripts fetal = fetalScriptsService.retrieve(transFile);
		
		File file = new File(props.getProperty("quarantinePath") + transFile);
		
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(transData.getBytes(Charset.forName("UTF-8")));
		fos.close();
		
		fetal.setStatus("QUARANTINE");
		fetalScriptsService.update(fetal);

	}

}
