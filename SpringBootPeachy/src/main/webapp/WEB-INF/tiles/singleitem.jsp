
<form  method="get" action="${pageContext.request.contextPath}/vendor/replenish">
	<table>
		<tr>
			<td><h4>Please enter the product #</h4></td>
		</tr>
		<tr>
			<td><input name="sku" type="text" /></td>
		</tr>
		<tr>
			<td><div class="error">${error}</div></td>
		</tr>
		<tr>
			<td><input type="submit" value="Submit"></td>
		</tr>
	</table>
</form>
