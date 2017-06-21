<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:choose>
	<c:when test="${objectList.pageList.size() > 0}">
		<table class="tableview" id="shoppingtable">
			<tr>
				<th>Invoice #</th>
				<th>Grand Total</th>
				<th>Purchased On</th>
				<th>Processed On</th>
			</tr>
			<c:forEach var="item" items="${objectList.pageList}">
				<c:set var="price"
					value="${item.total + item.total_tax + item.shipping_cost + item.added_charges}" />
				<c:if test="${price > 0}">
				<fmt:formatDate value="${item.processed}" var="purchased" />
				<fmt:formatDate value="${item.shipped}" var="shipped" />

				<tr>
					<td><fmt:formatNumber type="number" pattern="00000000"
							value="${item.invoice_num}" /></td>
					<td><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
							value="${price * rate}" /></td>
					<td>${purchased}</td>
					<td>${shipped}</td>
				
				</tr>
			</c:if>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<h1>No Purchase History</h1>
	</c:otherwise>
</c:choose>