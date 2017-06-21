package com.peachy.taglibs;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class HasRole extends SimpleTagSupport {
	private String role;
	private boolean hasRole = false;

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public void doTag() throws JspException, IOException {
		Collection<? extends GrantedAuthority> roles;
		roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		String[] roleArray = role.split(",");
		for (int i = 0; i < roleArray.length; i++) {
			for (GrantedAuthority item : roles) {
				if (item.getAuthority().compareTo(roleArray[i].trim()) == 0) {
					hasRole = true;
					break;
				}
			}
		}
		if (hasRole == true) {
			getJspBody().invoke(null);
		}
	}

}
