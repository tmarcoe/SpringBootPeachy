<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="tableview" id="viewcoupons">
	<tr>
		<th>Coupon Name</th>
		<th>Coupon Description</th>
		<th>Active?</th>
		<th>Exclusive?</th>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach var="item" items="${objectList.pageList}">
		<tr>
			<td>${item.name}</td>
			<td>${item.description}</td>
			<td>${item.active}</td>
			<td>${item.exclusive}</td>
			<td><button type="button" onclick="followLink('/vendor/editcoupon?key=${item.coupon_id}')">Edit</button></td>
			<td><button type="button" onclick="rowRemoved('${item.coupon_id}')">Delete</button></td>
		</tr>
	</c:forEach>
	<tfoot class="viewcouponfoot">
		<tr>
			<td><button type="button" onclick="followLink('/vendor/createcoupon')">Create Coupon</button></td>
			<td colspan="5"><button type="button" onclick="followLink('/public/home')">Back</button></td>
		</tr>
	</tfoot>
</table>
<script type="text/javascript">
	function rowRemoved(key) {

		if (confirm("Are you sure you want to remove '" + key
				+ "' from the coupon database?") == true) {
			window.location.href = "${pageContext.request.contextPath}/vendor/deletecoupon?key=" + key;
		}
	}
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>