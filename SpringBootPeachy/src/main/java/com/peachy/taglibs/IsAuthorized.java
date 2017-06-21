package com.peachy.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.security.core.context.SecurityContextHolder;

public class IsAuthorized extends SimpleTagSupport {
	

	 public void doTag() throws JspException, IOException
	 {
		 final String usr = "anonymousUser";
		 if(SecurityContextHolder.getContext().getAuthentication().getName().compareTo(usr) !=0 ) {
			 getJspBody().invoke(null);
		 }
	 }

}
