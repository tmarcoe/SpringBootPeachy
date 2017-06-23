<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<table class="tableview tableshadow" id="pCash">
	<thead>
		<tr>
			<th>Id</th>
			<th>Request By</th>
			<th>Paid With</th>
			<th>Paid To</th>
			<th>Amount</th>
			<th>Date</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${objectList.pageList}">
			<tr>
				<td>${item.registerId}</td>
				<td>${item.userId}</td>
				<td>${item.paymentMethod}</td>
				<td>${item.payableTo}</td>
				<td>${item.amount}</td>
				<td><fmt:formatDate value="${item.transactionDate}" pattern="yyy-MM-dd" /></td>
				<td><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/pettycashedit?id=${item.registerId}'">Edit</button></td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot class="tablefooter">
		<tr>
			<td><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/manageaccount'">Back</button></td>
			<td><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/pettycashenter'">New</button></td>
			<td colspan="5">&nbsp;</td>
		</tr>
	</tfoot>
</table>