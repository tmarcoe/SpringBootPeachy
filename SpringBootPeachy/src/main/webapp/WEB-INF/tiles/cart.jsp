<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<sf:form method="post" action="${pageContext.request.contextPath}/user/vieworder">
	<c:set var="total" scope="session" value="0" />
	<c:set var="pr" scope="session" value="0" />
	<c:set var="ttax" scope="session" value="0" />
	<h3>
		Invoice #
		<fmt:formatNumber type="number" value="${invoice.invoice_num}" pattern="00000000" />
	</h3>
	<table class="tableview" id="listinvoice">
		<thead class="invoicehead">
			<tr>
				<td>Item</td>
				<td>Quantity</td>
				<td>Product Name</td>
				<td>Options</td>
				<td>Price</td>
				<td>Tax</td>
				<td>&nbsp;</td>
				<c:if test="${invoice.processed == null}">
					<td>&nbsp;</td>
				</c:if>
			</tr>
		</thead>
		<tbody>

			<c:forEach var="item" items="${invoiceList}" varStatus="i" begin="0">
				<c:set var="pr" value="${item.price * item.amount}" />
				<c:set var="tx" value="${item.tax * item.amount}" />
				<c:set var="total" value="${total + pr}" />
				<c:set var="ttax" value="${ttax + tx}" />
				<tr>
					<td><fmt:formatNumber type="number" minIntegerDigits="2" groupingUsed="false" value="${i.index + 1}" /></td>
					<td>${item.amount}</td>
					<td>${item.product_name}</td>
					<td>${item.options}</td>
					<td><fmt:formatNumber type='currency' currencySymbol='${currencySymbol}' value='${pr * rate}' /></td>
					<td><fmt:formatNumber type='currency' currencySymbol='${currencySymbol}' value='${tx * rate}' /></td>
					<c:if test="${invoice.processed == null}">
						<td><button type="button" onclick="rowRemoved(${item.invoice_num}, '${item.sku_num}');">Delete</button></td>
						<td>&nbsp;</td>
					</c:if>
					<c:if test="${invoice.processed != null}">
						<td>&nbsp;</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot class="tablefooter">
			<tr>
				<td class="currency" colspan="5">Subtotal =======></td>
				<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${total * rate}" /></td>
				<td>&nbsp;</td>
				<c:if test="${invoice.processed == null}">
					<td colspan="3">&nbsp;</td>
				</c:if>
			</tr>
			<tr>
				<td class="currency" colspan="5">Total Tax =======></td>
				<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${ttax * rate}" /></td>
				<td>&nbsp;</td>
				<c:if test="${invoice.processed == null}">
					<td colspan="3">&nbsp;</td>
				</c:if>
			</tr>
			<tr>
				<td class="currency" colspan="5">Added Charge ======></td>
				<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${invoice.added_charges * rate}" /></td>
				<td>&nbsp;</td>
				<c:if test="${invoice.processed == null}">
					<td colspan="3">&nbsp;</td>
				</c:if>
			</tr>
			<tr>
				<td class="currency" colspan="5">Shipping Charge ======></td>
				<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${invoice.shipping_cost * rate}" /></td>
				<c:if test="${invoice.processed == null}">
					<td colspan="3">&nbsp;</td>
				</c:if>
			</tr>
			<tr>
				<td class="currency" colspan="5">Total =======></td>
				<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
						value="${(total + ttax + invoice.added_charges + invoice.shipping_cost) * rate}" /></td>
				<td>&nbsp;</td>
				<c:if test="${invoice.processed == null}">
					<td colspan="3">&nbsp;</td>
				</c:if>
			</tr>
			<c:if test="${invoice.processed == null}">
				<tr>
					<td colspan="11">&nbsp;</td>
				</tr>
				<tr>
					<td><input type="submit" value="Check Out" /></td>
					<td><input type="button" Value="Cancel Order" onclick="cancel()" /></td>
					<td><input type="button" value="Continue Shopping" onclick="followLink('/public/pickcategory')" /></td>
					<td>Coupon: <input type="text" name="couponName" /></td>
					<td><div class="error">${errorMsg}</div></td>
					<td colspan="6">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="4">Enter the coupon during checkout.</td>
					<td colspan="7">&nbsp;</td>
				</tr>
			</c:if>
			<c:if test="${invoice.processed != null}">
				<tr>
					<td><button type="button" onclick="goBack()">Back</button></td>
				</tr>
			</c:if>
		</tfoot>
	</table>
</sf:form>
<script type="text/javascript">
	function rowRemoved(invoiceNum, skuNum) {
		if (confirm("Are you sure you want to remove SKU number '"
				+ skuNum + "' from the shopping cart?") == true) {
			window.location.href = "${pageContext.request.contextPath}/user/deleteinvoiceitem?invoiceNum="
					+ invoiceNum + "&skuNum=" + skuNum;
		}
	}

	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
	function cancel() {
		if (confirm("Are you sure you want to cancel this order?") == true) {
			window.location.href = "${pageContext.request.contextPath}/user/cancelsale";
		}
	}

	function goBack() {
		window.history.back();
	}

</script>