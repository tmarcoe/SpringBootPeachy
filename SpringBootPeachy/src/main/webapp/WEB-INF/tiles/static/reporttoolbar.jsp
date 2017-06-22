<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--<img class="logo" alt="FlowerLogo.png"
	src="<c:url value='/static/web/images/FlowerLogo.jpg' />" />
  -->

<div class="nav-bar">
	<div class="container">
		<ul class="nav">
			<li><a href="/public/home">Home</a></li>
			<li><a href="/vendor/calendaryear?reportType=sales">Sales Report</a></li>
			<li><a href="/vendor/calendaryear?reportType=profit">COGS Report</a></li>
			<li><a href="/vendor/gender">Demographic Report (Gender)</a></li>
			<li><a href="/vendor/viewsurveyreport">Satisfaction Surveys</a></li>
			<li><a href="/vendor/calendaryear?reportType=count">Customer Counts by Month</a></li>
		</ul>
	</div>
</div>