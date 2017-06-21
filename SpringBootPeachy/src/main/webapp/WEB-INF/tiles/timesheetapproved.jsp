<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>Timesheet approved for period ending: <fmt:formatDate type="date" pattern="yyy-MM-dd" value="${startPeriod}"/></h1>