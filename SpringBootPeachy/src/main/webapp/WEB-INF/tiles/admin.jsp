<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="printorders">
	<c:choose>
		<c:when test="${objectList.pageList.size() > 0}">
			<form:form id="pgform" method="post" modelAttribute="objectList"
				action="${pageContext.request.contextPath}/vendor/processorders">
				<h2>Orders Ready For Processing</h2>
				<table class="tableview tableshadow" id="viewheader">
					<thead>
						<tr>
							<th>Invoice #</th>
							<th>Date</th>
							<th>User #</th>
							<th>Total</th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					<c:forEach var="item" items="${objectList.pageList}">
						<fmt:formatNumber type="number" pattern="00000000"
							value="${item.invoice_num}" var="invNum" />
						<fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
							value="${(item.total + item.total_tax + item.shipping_cost + item.added_charges) * rate}"
							var="total" />
						<fmt:formatDate value="${item.modified}" var="stdate" />
						<tr>
							<td>${invNum}</td>
							<td>${stdate}</td>
							<td>${item.user_id}</td>
							<td>${total}</td>
							<td><button type="button" onclick="window.location.href = '/user/viewcart?invoiceNum=${item.invoice_num}'">View</button></td>
						</tr>

					</c:forEach>
					<tfoot class="tablefooter">
						<tr>
							<td colspan="5"><input type="submit" value="Process Orders" /></td>
						</tr>
					</tfoot>
				</table>
			</form:form>
		</c:when>
		<c:otherwise>
			<h1>No orders to Ship</h1>
		</c:otherwise>
	</c:choose>
</div>
