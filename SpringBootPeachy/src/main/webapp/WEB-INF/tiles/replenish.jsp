<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>


<sf:form method="post"
	action="${pageContext.request.contextPath}/vendor/stockshelves"
	commandName="order">
	<table>
		<tr>
			<td>Product:</td>
			<td>${order.inventory.product_name}</td>
			<td>&nbsp;</td>
			<td>In Stock:</td>
			<td>${order.inventory.amt_in_stock}</td>
			<td>Minimum Stock:</td>
			<td>${order.inventory.min_quantity}</td>
		</tr>
		<tr>
			<td>Amount to Add:</td>
			<td><sf:input type="number" path="amount"/></td>
			<td>Order Price:</td>
			<td><sf:input type="number" path="price"/></td>
			<td>Order Tax</td>
			<td><sf:input type="number" path="tax"/></td>
		</tr>
		<tr><td><sf:input type="hidden" path="inventory.sku_num"/></td></tr>
		<tr><td><input type="submit" value="Restock" ></td></tr>
	</table>

</sf:form>