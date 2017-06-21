<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="total" scope="session" value="0" />
<c:set var="pr" scope="session" value="0" />
<c:set var="ttax" scope="session" value="0" />

<h3>
	Invoice #
	<fmt:formatNumber type="number"
		value="${invoice.invoice_num}" minIntegerDigits="8"
		groupingUsed="false" />
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
		</tr>
	</thead>
	<tbody>

		<c:forEach var="item" items="${invoiceList}" varStatus="i"
			begin="0">
			<c:set var="pr" value="${item.price * item.amount}" />
			<c:set var="tx" value="${item.tax * item.amount}" />
			<c:set var="total" value="${total + pr}" />
			<c:set var="ttax" value="${ttax + tx}" />
			<tr>
				<td><fmt:formatNumber type="number" minIntegerDigits="2"
						groupingUsed="false" value="${i.index + 1}" /></td>
				<td>${item.amount}</td>
				<td>${item.product_name}</td>
				<td>${item.options}</td>
				<td class="currency"><fmt:formatNumber type='currency' currencySymbol='${currencySymbol}'
						value='${pr * rate}' /></td>
				<td class="currency"><fmt:formatNumber type='currency' currencySymbol='${currencySymbol}'
						value='${tx * rate}' /></td>
				
			</tr>
		</c:forEach>
	</tbody>
	<tfoot class="tablefooter">
		<tr>
			<td class="currency" colspan="5">Subtotal =======></td>
			<td class="currency" ><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
					value="${total * rate}" /></td>
		</tr>
		<tr>
			<td class="currency" colspan="5">Total Tax =======></td>
			<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
					value="${ttax * rate}" /></td>
		</tr>
		<tr>
			<td class="currency" colspan="5">Added Charges ======></td>
			<td class="currency"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
					value="${invoice.added_charges * rate}" /></td>
		</tr>
		<tr>
			<td class="currency" colspan="5">Shipping Charge ======></td>
			<td class="currency" ><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
					value="${invoice.shipping_cost * rate}" /></td>		
		</tr>
		<tr>
			<td class="currency" colspan="5">Total =======></td>
			<td class="currency" ><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}"
					value="${(total + ttax + invoice.added_charges + invoice.shipping_cost) * rate}" /></td>
		</tr>
		<tr>
			<td><button type="button" onclick="followLink('/user/pcinfo')">Submit Order</button></td>
			<td><button type="button" onclick="cancel()">Cancel Order</button></td>
			<td colspan="4"><input type="button" onClick="window.print()"
						value="Print Order" /></td>
		</tr>
	</tfoot>
</table>

<script type="text/javascript" >
function followLink(link) {
	window.location.href = "${pageContext.request.contextPath}" + link;
}

function cancel() {
	if (confirm("Are you sure you want to cancel this order?") == true) {
		window.location.href = "${pageContext.request.contextPath}/user/cancelsale";
	}
}
</script>

