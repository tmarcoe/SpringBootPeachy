package com.peachy.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.peachy.entity.Schemes;
import com.peachy.service.SchemesService;


@Controller
@RequestMapping("/admin")
public class SchemesController {
	private final String pageLink = "/schemepaging";
	
	@Autowired
	SchemesService schemesService;
	
	@RequestMapping("/schemes")
	public String showSchemes (Model model){
		
		List<String> schemesList = schemesService.retrieveAllSchemes();
		
		if (schemesList.size() > 0 ) {
		
			model.addAttribute("objectList", schemesList);
			model.addAttribute("pagelink", pageLink);

			return "schemes";
		}else{
			model.addAttribute("schemes", new Schemes());
			
			return "newscheme";
		}
	}
	
	@RequestMapping("/deletescheme")
	public String deleteSchem(@ModelAttribute("scheme") String scheme, Model model) {
		schemesService.delete(scheme);
		
		List<String> schemesList = schemesService.retrieveAllSchemes();
		
		if (schemesList.size() > 0 ) {

			return "redirect:/admin/schemes";
		}else{
			model.addAttribute("schemes", new Schemes());
			
			return "newscheme";
		}
	}
	
	@RequestMapping("/deletesinglescheme")
	public String deleteSingScheme(@ModelAttribute("entryId") int entryId, Model model) {
		
		schemesService.delete(entryId);
		
		List<String> schemesList = schemesService.retrieveAllSchemes();
		
		if (schemesList.size() > 0 ) {

			return "return:/admin/schemes";
		}else{
			model.addAttribute("schemes", new Schemes());
			
			return "newscheme";
		}
	}
	
	@RequestMapping("/singlescheme")
	public String singleScheme(@ModelAttribute("scheme") String scheme, Model model) {
		List<Schemes> schemes = schemesService.retrieveScheme(scheme);
		
		model.addAttribute("objectList", schemes);
		
		return "singlescheme";
	}
	@RequestMapping("/editscheme")
	public String editScheme(@ModelAttribute("schemeId") int schemeId, Model model) {
		Schemes schemes = schemesService.retrieve(schemeId);
		
		model.addAttribute("schemes", schemes);
		
		return "editscheme";
	}
	@RequestMapping("/updatescheme")
	public String updateScheme(@ModelAttribute("schemes") Schemes scheme, Model model) {
		
		schemesService.update(scheme);
		
		List<Schemes> schemes = schemesService.retrieveScheme(scheme.getScheme());
		model.addAttribute("objectList", schemes);	
		
		return "singlescheme";
	}
	
	@RequestMapping("/newscheme")
	public String newScheme(@ModelAttribute("schemeStr") String schemeStr, Model model) {
		Schemes schemes = new Schemes();
	
		if ("".compareTo(schemeStr) != 0 ) {
			schemes.setScheme(schemeStr);
		}
		model.addAttribute("schemes", schemes);
		
		return "newscheme";
	}
	
	@RequestMapping("/savescheme")
	public String saveScheme(@Valid @ModelAttribute("schemes") Schemes schemes, BindingResult results, Model model) {
		if (results.hasErrors() == true) {
			return "newscheme";
		}
		if (schemesService.idExists(schemes.getScheme(), schemes.getId()) == true) {
			results.rejectValue("id", "Unique.schemes.id");
			return "newscheme";
		}
		
		schemesService.create(schemes);

		return "redirect:/admin/schemes";

	}
	

}
