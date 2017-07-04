<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="/script/extraProductInfo.js"></script>
<div class="logos">
		<img alt="Image not available"
			src="<c:url value='/images/coffee-beans-in-a-bag.png'/>" width="400"/>
</div>

	
<sf:form id="details" method="post"
	action="${pageContext.request.contextPath}/user/orderproduct"
	commandName="invoiceItem">
	<table class="productdetails">
		<tr class="invoiceItemheader">
			<th>&nbsp;</th>
			<th>Product Description</th>
			<th>Price</th>
			<th>Qty</th>
		</tr>
		<tr>
			<td><img alt="Image Not Available"
				src="<c:url value='${fileLoc}${inventory.image}' />" width="150"></td>
			<td><div class="altFont">${inventory.description}</div></td>
			<td><fmt:formatNumber type='currency'
					currencySymbol='${currencySymbol}'
					value='${invoiceItem.price * rate}' /></td>
			<td><sf:input type="number" path="amount" step="1" min="1"
					value="1" maxlength="3" size="2" id="prodQty"/></td>
			<td><div class="error">
					<sf:errors path="amount" />
				</div>
		</tr>
		<tr>
			<td><button type="button" onclick="buildDiv('${inventory.extra_info_scheme}')">
					<img alt="Image Not Available"
						src="<c:url value='/images/Website-Shopping-Cart.png'/>"><br>Add
					to Cart
				</button></td>

			<td><sf:hidden path="sku_num" /></td>
			<td><sf:hidden path="product_name"/></td>
			<td><sf:input type="hidden" path="amt_in_stock" /></td>
			<td><sf:input type="hidden" path="weight" /></td>
			<td><sf:input type="hidden" path="invoice_num" /></td>
			<td><sf:input type="hidden" path="price" /></td>
			<td><sf:input type="hidden" path="tax" /></td>
			<td><sf:hidden path="options" id="optId" />
			<td>
		</tr>
	</table>
</sf:form>
<div class="modal" id="popup">

	<div class="modal-content medium-modal" id="extraInfo">
		<h3 class="titleText">Options</h3>
	</div>
</div>
<script type="text/javascript">
	var url = "${pageContext.request.contextPath}/public/dynamic-screen/product-info?schemeStr=${schemeStr}";
	var ctx = document.getElementById("extraInfo");
	var mode = document.getElementById("popup");

	function buildDiv(schemeStr) {
		if (schemeStr.length > 0) {
			$.getJSON(url , function(data) {
				var tbl = document.createElement("table");
				ctx.appendChild(tbl);
				tbl.style.borderSpacing = "20px";
				putElement(tbl, data);
				var btn = document.createElement("button");
				btn.onclick = function(data) {
					var options = "";
					$.getJSON(url , function(data) {
						options = readElement(data);
						var qty = document.getElementById("prodQty").value;
						options = "(" + qty + ")" + options;
						var optCtx = document.getElementById("optId");
						optCtx.value = options;
						document.getElementById("details").submit();
					});
				}	
				btn.innerHTML = "Order";
				ctx.appendChild(btn);
				mode.style.display = "block";
			});
		}else{
			document.getElementById("details").submit();
		}
	}
	
	function readDiv(schemeStr) {
		if (schemeStr.length > 0) {
			$.getJSON(url , function(data) {
					var options = readElement(data);
					var optCtx = getElementById("optId");
					optCtx.value = options;
					document.getElementById("productForm").submit();	
			});
		}
		
	}
</script>