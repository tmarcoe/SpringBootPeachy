package com.peachy.rest;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peachy.entity.Schemes;
import com.peachy.service.SchemesService;

@RestController
@RequestMapping("/public/dynamic-screen")
public class DynamicScreenController {
	
	@Autowired
	SchemesService schemesService;
	
	@RequestMapping("/product-info")
	public String buildJsonScreen(@ModelAttribute("schemeStr") String schemeStr) throws IOException {

		JSONArray json = new JSONArray();
		List<Schemes> scheme = schemesService.retrieveScheme(schemeStr);
		for (Schemes item: scheme) {
			String element = String.format("{'label':'%s', 'name':'%s', 'id':'%s', 'widget':'%s', 'options':'%s', " + 
										   "'value':'%s', 'comments':'%s'}", 
										   item.getLabel(), item.getName(), item.getId(), item.getWidget(),
										   item.getOptions(), item.getValue(), item.getComments());
			json.put(new JSONObject(new JSONTokener(element)));
		}
		
		return json.toString();
	}

}
