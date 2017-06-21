<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<sf:form id="details" method="post"
	action="${pageContext.request.contextPath}/admin/inventorysaved"
	commandName="inventory">
	<table class="inventorydetails">
		<tr>
			<td>SKU Number:</td>
			<td><sf:input path="sku_num" name="sku_num" class="control"
					type="text" readonly="true" /></td>
			<td>&nbsp;</td>
			<td>Product Name:</td>
			<td><sf:input path="product_name" name="product_name"
					class="control" type="text" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="sku_num"></sf:errors>
				</div></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="product_name"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td>Category:</td>
			<td><sf:input path="category" name="category" class="control"
					type="text" /></td>
			<td>&nbsp;</td>
			<td>Sub Category:</td>
			<td><sf:input path="subcategory" name="subcategory"
					class="control" type="text" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="category"></sf:errors>
				</div></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="subcategory"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td>Amount In Stock:</td>
			<td><sf:input path="amt_in_stock" name="amt_in_stock"
					class="control" type="number" /></td>
			<td>&nbsp;</td>
			<td>Amount Committed:</td>
			<td><sf:input path="amt_committed" name="amt_committed"
					class="control" type="number" /></td>
			<td>Minimum Quantity:</td>
			<td><sf:input path="min_quantity" name="min_quantity"
					class="control" type="number" />
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="amt_in_stock"></sf:errors>
				</div></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="amt_committed"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td>Sale Price:</td>
			<td><sf:input path="sale_price" name="sale_price" class="control"
					type="number" step=".01" /></td>
			<td>&nbsp;</td>
			<td>Discount Price:</td>
			<td><sf:input path="discount_price" name="discount_price"
					class="control" type="number" step=".01" /></td>
			<td>Shipping Weight:</td>
			<td><sf:input path="weight" name="weight" class="control"
					type="number" step=".01" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="sale_price"></sf:errors>
				</div></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="discount_price"></sf:errors>
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="weight"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td>Tax Amount:</td>
			<td><sf:input path="tax_amt" name="tax_amt" class="control"
					type="number" step=".01" /></td>
			<td>&nbsp;</td>
			<td>Product On Sale:</td>
			<td><input type="hidden" value="on" name="_active" /> <sf:checkbox
					path="on_sale" class="control" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="tax_amt"></sf:errors>
				</div></td>
		</tr>
		<tr>
			<td>Image File:</td>
			<td><sf:input type="text" name="image" path="image"
					readonly="true" /></td>
			<td>&nbsp;</td>
			<td>Extra Info Scheme:</td>
			<td><sf:select path="extra_info_scheme">
				<sf:option value="">---------</sf:option>
				<c:forEach var="item" items="${schemeList}">
					<sf:option value="${item}">${item}</sf:option>
				</c:forEach>
			</sf:select></td>
		</tr>
		<tr><td>&nbsp;</td></tr>
	</table>
	<table class="inventorydetails">
		<tr>
			<td>
				<h3>Product Description</h3>
			</td>
		</tr>
		<tr>
			<td><sf:textarea path="description" rows="10" cols="100"></sf:textarea>
			</td>
			<td>
				<div class="error">
					<sf:errors path="description"></sf:errors>
				</div>
			</td>
		</tr>
		<tr>
			<td><input type="submit" value="Save" /></td>
			<td><button type="button"
					onclick="followLink('/admin/manageinventory')">Cancel</button>
		</tr>
	</table>
</sf:form>
<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>