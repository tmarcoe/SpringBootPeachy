<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
	<c:when test="${objectList.pageList.size() > 0}">
		<form>
			<table class="tableview tableshadow">
				<thead>
					<tr>
						<th>Product</th>
						<th>Price</th>
						<th>Tax</th>
						<th>In Stock</th>
						<th>&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${objectList.pageList}">
						<tr>
							<td>${item.product_name}</td>
							<td><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${item.sale_price}" /></td>
							<td><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${item.tax_amt}" /></td>
							<td>${item.amt_in_stock}</td>
							<td><button type="button" onclick="followLink('/vendor/replenish?sku=${item.sku_num}')">Order</button></td>
						</tr>
					</c:forEach>
					<tr>
						<th colspan="5"><button type="button" onclick="followLink('/vendor/manageinventory')">OK</button></th>
					</tr>
				</tbody>

			</table>
		</form>
	</c:when>
	<c:otherwise>
		<h2>Inventory is at normal levels</h2>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>