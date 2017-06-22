<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form modelAttribute="objectList" method="get">

	<table class="tableview" id="listinventory">
		<thead>
			<tr>
				<th>SKU Number</th>
				<th>Product Name</th>
				<th>On Sale?</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			</tr>
		</thead>

		<tbody>

			<c:forEach items="${objectList.pageList}" var="item" varStatus="i"
				begin="0">
				<tr class="account">
					<td>${item.sku_num}</td>
					<td>${item.product_name}</td>
					<td>${item.on_sale}</td>
					
					<td><button type="button" onclick="rowRemoved('${item.sku_num}', '${item.product_name}')">Delete</button></td>
					<td><button type="button" onclick="productDetail('${item.sku_num}')">Edit</button></td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="8">&nbsp;</td>
			</tr>

			<tr>
				<td><button type="button" onclick="followLink('/vendor/uploadfile')">Add
						Product</button>
				<td><button type="button"
						onclick="followLink('/public/home')">Back</button></td>
				<td><button type="button" onclick="openPopup()" >Search Products</button></td>
			</tr>
		</tfoot>
	</table>


</sf:form>
<div class="modal" id="newSearch">
	<div class="modal-content">
		<table>
			<tr>
				<td>Enter Search</td>
				<td><input id="prodSearch" /></td>
			</tr>
			<tr>
				<td><button type="button" onclick="beginSearch()">Search</button></td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
	function rowRemoved(key, name) {	
		    if (confirm("Are you sure you want to remove '" + name + "' from inventroy?") == true) {
		   		window.location.href = "${pageContext.request.contextPath}/vendor/deleteinventory?deleteKey=" + key;		    
		   	} 
	}
	function openPopup() {
		var modal = document.getElementById('newSearch');
		modal.style.display = "block"
	}
	function beginSearch() {
		var srch = document.getElementById('prodSearch').value;
		window.location.href = "${pageContext.request.contextPath}/vendor/adminsearch?mySearch=" + srch;
	}
	function productDetail(key) {	
		window.location.href = "${pageContext.request.contextPath}/vendor/inventorydetails?InventoryKey=" + key;	
	}
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>
