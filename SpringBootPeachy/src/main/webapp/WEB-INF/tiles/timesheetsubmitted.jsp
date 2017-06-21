<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>Your Timesheet has been submitted for starting period: <fmt:formatDate type="date" pattern="yyy-MM-dd" value="${startPeriod}"/></h1>