package com.peachy.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.peachy.component.FilePath;
import com.peachy.email.ProcessEmail;
import com.peachy.entity.Role;
import com.peachy.entity.UserProfile;
import com.peachy.exceptions.SessionTimedOutException;
import com.peachy.service.RoleService;
import com.peachy.service.UserProfileService;

@Controller
public class UserProfileController implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String pageLink = "/admin/userpaging";

	private static Logger logger = Logger.getLogger(UserProfileController.class
			.getName());

	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private RoleService roleService;
	
	private PagedListHolder<UserProfile> userList;

	private SimpleDateFormat dateFormat;

	@Autowired
	FilePath fp;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@RequestMapping("/admin/users")
	public String showUsers(@ModelAttribute("page") String page, Model model) {
		if (userList != null) {
			userList.getSource().clear();
			userList = null;
			System.gc();
		}
		userList = userProfileService.retrieveList();
		userList.setPageSize(15);
		userList.setPage(0);

		model.addAttribute("objectList", userList);
		model.addAttribute("pagelink", pageLink);

		return "users";

	}

	@RequestMapping("/admin/deleteuser")
	public String deleteUser(@ModelAttribute("deleteKey") String deleteKey,
			Model model) {

		userProfileService.delete(deleteKey);
		logger.info("User: '" + deleteKey + "' Deleted");
		if (userList != null) {
			userList.getSource().clear();
			userList = null;
			System.gc();
		}

		return "redirect:/admin/users";
	}

	@RequestMapping("/admin/userdetails")
	public String showUserDetails(
			@ModelAttribute("detailKey") int detailKey, Model model) {
		String roleIndex = "";

		UserProfile userProfile = userProfileService.retrieve(detailKey);
		for (Role r: userProfile.getRoles()) {
			if (roleIndex.length() > 0) {
				roleIndex += ("," + String.valueOf(r.getId()));
			}else{
				roleIndex += String.valueOf(r.getId());
			}
		}
		
		model.addAttribute("roleIndex", roleIndex);
		model.addAttribute("roles", roleService.retrieveList());
		model.addAttribute("userProfile", userProfile);
		
		return "userdetails";
	}

	@RequestMapping("/public/signup")
	public String signup(Model model) {
		model.addAttribute("userProfile", new UserProfile());

		return "signup";
	}

	@RequestMapping("/user/myprofile")
	public String showMyDonzalMart(Model model, Principal principal) {

		UserProfile user = userProfileService.retrieve(principal.getName());
		model.addAttribute("userProfile", user);

		return "myprofile";
	}

	@RequestMapping("/user/saveuser")
	public String partialUpdate(
			@Valid @ModelAttribute("userProfile") UserProfile userProfile, BindingResult result, Model model)
			throws IOException, URISyntaxException {
		if (result.hasFieldErrors("firstname") || 
			result.hasFieldErrors("lastname") || 
			result.hasFieldErrors("maleFemale") || 
			result.hasFieldErrors("address1") || 
			result.hasFieldErrors("city") || 
			result.hasFieldErrors("country") || 
			result.hasFieldErrors("currency") || 
			result.hasFieldErrors("username")) {
			
			return "myprofile";
		}
		userProfileService.partialUpdate(userProfile);
		
		return "redirect:/public/home";
	}

	@RequestMapping("/admin/adminsaveuser")
	public String adminPartialUpdate(
			@Valid @ModelAttribute("userProfile") UserProfile user, BindingResult result, Model model)
			throws IOException, URISyntaxException {
		String roleIndex = "";
		
		if (result.hasFieldErrors("firstname") || 
			result.hasFieldErrors("lastname") || 
			result.hasFieldErrors("maleFemale") || 
			result.hasFieldErrors("address1") || 
			result.hasFieldErrors("city") || 
			result.hasFieldErrors("country") || 
			result.hasFieldErrors("currency") || 
			result.hasFieldErrors("username")) {

			if (user.getRoles() == null) {
				user.setRoles(new HashSet<Role>());
			}
			String[] roleNames = user.getRoleString().split(";");
			for (int i = 0 ; i < roleNames.length; i++) {
				user.getRoles().add(roleService.retrieve(roleNames[i]));
			}
			
			for (Role r: user.getRoles()) {
				if (roleIndex.length() > 0) {
					roleIndex += ("," + String.valueOf(r.getId()));
				}else{
					roleIndex += String.valueOf(r.getId());
				}
			}
			
			model.addAttribute("roleIndex", roleIndex);
			model.addAttribute("roles", roleService.retrieveList());
			model.addAttribute("userProfile", user);
			
			return "userdetails";
		}
		if (user.getEmployee() != null) {
			user.getEmployee().setUser_id(user.getUser_id());
		}
		if (user.getRoles() == null) {
			user.setRoles(new HashSet<Role>());
		}
		String[] roleNames = user.getRoleString().split(";");
		for (int i = 0 ; i < roleNames.length; i++) {
			user.getRoles().add(roleService.retrieve(roleNames[i]));
		}
		
		userProfileService.merge(user);

		if (userList != null) {
			userList.getSource().clear();
			userList = null;
			System.gc();
		}

		return "redirect:/admin/users";
	}
	
	@RequestMapping("/public/createprofile")
	public String createProfile(HttpServletRequest request, 
			@Valid @ModelAttribute("userProfile") UserProfile user, BindingResult result, Model model)
			throws Exception {
		
		if (result.hasErrors()) {
			return "signup";
		}
		
		if (userProfileService.exists(user.getUsername())) {
			result.rejectValue("username", "DuplicateKey.userProfile.username");
			return "signup";
		}
		try {
			if (user.getEmployee() != null) {
				user.getEmployee().setUser_id(user.getUser_id());
			}

			userProfileService.create(user);
	
		} catch (DuplicateKeyException e) {
			result.reject("duplicate.userProfile.email",
					"A user already exists with that email address.");
			return "signup";
		}
		logger.info("'" + user.getUsername() + "' has just signed up.");
		
		
		String baseUrl = String.format("%s://%s:%d/public/verify?puKey=",request.getScheme(), request.getServerName(), request.getServerPort());
		ProcessEmail pe = new ProcessEmail(fp);
		pe.sendLoginLink(user, baseUrl);

		String name = user.getFirstname() + " " + user.getLastname();

		model.addAttribute("name", name);
		model.addAttribute("email", user.getUsername());

		return "createprofile";
	}

	@RequestMapping("/user/changepassword")
	public String showChangePassword(Model model, Principal principal) {
		UserProfile user = userProfileService.retrieve(principal.getName());

		user.setPassword("");

		model.addAttribute("userProfile", user);

		return "changepassword";
	}

	@RequestMapping("/user/passwordchanged")
	public String passwordChanged(@Valid 
			@ModelAttribute("userProfile") UserProfile user, BindingResult result,
			Principal principal, Model model) {
		
		if (result.hasFieldErrors("password")) {
			return "changepassword";
		}

		userProfileService.updatePassword(user);

		user = userProfileService.retrieve(principal.getName());

		logger.info("," + user.getUsername()
				+ "' has just changed the password.");

		model.addAttribute("userProfile", user);
		return "redirect:/public/home";
	}

	@RequestMapping("/user/updateuser")
	public String updateProfile(@ModelAttribute("userProfile") UserProfile user) {

		userProfileService.update(user);
		logger.info("'" + user.getUsername()
				+ "' has just changed the user info.");

		return ("myprofile");
	}

	@RequestMapping(value = "/admin/userpaging", method = RequestMethod.GET)
	public String handleUserRequest(@ModelAttribute("page") String page, Model model) throws Exception {
		int pgNum;

			if (userList == null) {
				throw new SessionTimedOutException("Your session has timed out. Please login again.");
			}
			pgNum = isInteger(page);

			if ("next".equals(page)) {
				userList.nextPage();
			} else if ("prev".equals(page)) {
				userList.previousPage();
			} else if (pgNum != -1) {
				userList.setPage(pgNum);
			}
			model.addAttribute("objectList", userList);
			model.addAttribute("pagelink", pageLink);

			return "users";
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
