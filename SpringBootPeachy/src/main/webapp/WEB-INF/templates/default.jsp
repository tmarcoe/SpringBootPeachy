<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld"%>
<sql:setDataSource var="ds" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/donzalma_peachys" user="donzalma_admin" password="In_heaven3" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="description" content="Enjoy the taste of the Philippines. Enjoy Peach's Coffee.">
<meta name="keywords" content="coffee">
<title><tiles:insertAttribute name="title"></tiles:insertAttribute></title>
<link rel="icon" href="<c:url value='/images/favicon.png' />">
<link href="/css/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/script/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="/script/Chart.js"></script>
<sec:getPrincipal />
<tiles:insertAttribute name="includes"></tiles:insertAttribute>

</head>

<body>
	<c:choose>
		<c:when test="${principal  != 'anonymousUser'}">
			<sql:query var="rs" dataSource="${ds}">
				SELECT firstname, lastname, currency, user_id FROM user_profile WHERE username='${principal}'
			</sql:query>
			<c:forEach var="row" items="${rs.rows}">
				<c:set var="firstName" value="${row.firstname}" />
				<c:set var="lastName" value="${row.lastname}" />
				<c:set var="currency" value="${row.currency}" />
				<c:set var="user_id" value="${row.user_id}" />
			</c:forEach>
			<sql:query var="hd" dataSource="${ds}">
				SELECT invoice_num FROM invoice WHERE user_id = ${user_id} AND processed is null
			</sql:query>
			<c:forEach var="row" items="${hd.rows}">
				<c:set var="invoiceNum" value="${row.invoice_num}" />
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:set var="firstName" value="Anonymous" />
			<c:set var="lastName" value="User" />
		</c:otherwise>
	</c:choose>

	<input id="currencyHolder" type="hidden" value="${currency}" onchange="" />
	<input id="nameHolder" type="hidden" value="${firstName}${lastName}">
	<table class="rightside">
		<tr>
			<td>Currency:</td>
			<td>
				<div class="userinfo">
					<select id="getCurrency" onchange="getCookieCurrency()">
						<option value="PHP">Philippines Peso</option>
						<option value="USD">United States Dollar</option>
						<option value="AUD">Australia Dollar</option>
						<option value="BGN">Bulgaria Lev</option>
						<option value="BRL">Brazil Real</option>
						<option value="CAD">Canada Dollar</option>
						<option value="CHF">Switzerland Franc</option>
						<option value="CNY">China Yuan Renminbi</option>
						<option value="CZK">Czech Republic Koruna</option>
						<option value="DKK">Denmark Krone</option>
						<option value="EUR">European Union Euro</option>
						<option value="GBP">United Kingdom Pound</option>
						<option value="HKD">Hong Kong Dollar</option>
						<option value="HRK">Croatia Kuna</option>
						<option value="HUF">Hungary Forint</option>
						<option value="IDR">Indonesia Rupiah</option>
						<option value="ILS">Israel Shekel</option>
						<option value="INR">India Rupee</option>
						<option value="JPY">Japan Yen</option>
						<option value="KRW">South Korea Won</option>
						<option value="MXN">Mexico Peso</option>
						<option value="MYR">Malaysia Ringgit</option>
						<option value="NOK">Norway Krone</option>
						<option value="NZD">New Zealand Dollar</option>
						<option value="PLN">Poland Zloty</option>
						<option value="RON">Romania New Leu</option>
						<option value="RUB">Russia Ruble</option>
						<option value="SEK">Sweden Krona</option>
						<option value="SGD">Singapore Dollar</option>
						<option value="THB">Thailand Baht</option>
						<option value="TRY">Turkey Lira</option>
						<option value="ZAR">South Africa Rand</option>
					</select>
				</div>
			</td>
		</tr>
	</table>
	<table class="leftside">
		<tr>
			<c:if test="${principal  != 'anonymousUser'}">
				<c:set var="total" value="${0}" />
				<td>Welcome back</td>
				<c:if test="${invoiceNum != null}">
					<sql:query var="inv" dataSource="${ds}">
					SELECT amount FROM invoice_item WHERE invoice_num = ${invoiceNum} AND sku_num NOT LIKE 'CPN%'
				</sql:query>
					<c:forEach var="row" items="${inv.rows}">
						<c:set var="total" value="${total + row.amount}" />
					</c:forEach>
				</c:if>
			</c:if>
			<td><h6 class="label">${firstName}</h6></td>
			<td><h6 class="label">${lastName}</h6></td>
			<c:if test="${principal  != 'anonymousUser'}">
				<td><img alt="Image not available" src="<c:url value='/images/scicon.png'/>" width="18" /> (${total})</td>
			</c:if>
			<td>&nbsp;</td>
			<td>Search: <input type="text" id="mySearch" onchange="submitSearch()" /></td>
		</tr>
	</table>
	<script type="text/javascript">
		$(document).ready(
				function() {
					var user = document.getElementById("nameHolder").value;
					if (user != "AnonymousUser") {
						document.getElementById("getCurrency").disabled = true;
					} else {
						currency = getCookie('currency');
						if (currency == "") {
							document.cookie = "currency=PHP; path=/";
							$("#currencyHolder").val("PHP");
						} else {
							$("#currencyHolder").val(currency);
						}
					}
					$("#getCurrency").val($("#currencyHolder").val());
					var pg = location.pathname.substring(location.pathname
							.lastIndexOf("/") + 1);
					if (pg == "vieworder" || pg == "pcinfo") {
						$("#mySearch").hide();
					} else {
						$("#mySearch").show();
					}
				});

		function getCookieCurrency() {
			var ctx = document.getElementById("getCurrency");
			var myCurrency = ctx.options[ctx.selectedIndex].value;
			$("#getCurrency").val(myCurrency);
			document.cookie = "currency=" + myCurrency + "; path=/";
			location.reload();
		}

		function submitSearch() {
			var srch = $("#mySearch").val();

			window.location.href = "${pageContext.request.contextPath}/public/shopbysearch?mySearch=" + srch;
		}

		function getCookie(cname) {
			var name = cname + "=";
			var decodedCookie = decodeURIComponent(document.cookie);
			var ca = decodedCookie.split(';');
			for (var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) == ' ') {
					c = c.substring(1);
				}
				if (c.indexOf(name) == 0) {
					return c.substring(name.length, c.length);
				}
			}
			return "";
		}
	</script>
	<div class="heading">
		<div class="container">
			<tiles:insertAttribute name="heading_title"></tiles:insertAttribute>
		</div>
	</div>
	<div class="toolbar">
		<tiles:insertAttribute name="toolbar"></tiles:insertAttribute>
	</div>
	<div class="content">
		<tiles:insertAttribute name="content"></tiles:insertAttribute>
	</div>

	<tiles:insertAttribute name="footer" />
<script>
	function shutdown() {
		if (confirm("This will shutdown the entire system. Do you wish to proceed?") == true) {
			window.location.href = "/admin/shutdown";
		}
	}
</script>
</body>
</html>