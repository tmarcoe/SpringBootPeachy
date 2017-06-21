<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="logos">
		<img alt="Image not available"
			src="<c:url value='/images/Cappuccino-1.png'/>" width="400"/>
</div>

<div class="pageheading">
	<h2>Daily Specials</h2>
</div>

<c:if test="${inventory.size() > 0}">
	<table class="dailyspecials">

		<caption>To see a full list of products click the 'shop'
			link at the top of the page.</caption>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<th>&nbsp;</th>
			<th>Product Name</th>
			<th>Now On Sale For...</th>
		</tr>

		<c:forEach var="inventory" items="${inventory}">
			<c:set var="price" value="${inventory.discount_price}" />
			<tr class="inventoryrow">
				<td><a
					href="${pageContext.request.contextPath}/public/productdetails?skuNum=${inventory.sku_num}">
						<img alt="Image Not Available"
						src='<c:url value="${fileLoc}${inventory.image}"></c:url>'
						width="80">
				</a></td>
				<td class="name" width="500">${inventory.product_name}</td>
				<td class="price"><fmt:formatNumber type="currency"
						currencySymbol="${currencySymbol}" value="${price * rate}" /></td>
			</tr>
			<tr>
				<td colspan="3"><hr /></td>
			</tr>
		</c:forEach>
		<tr>
			<td><button class="standout" type="button"
					onclick="followLink('/public/pickcategory')">More Products</button>
		</tr>

	</table>
</c:if>

<c:if test="${inventory.size() == 0}">
	<h2>Sorry, no daily specials today.</h2>
	<h3>Click 'Shop' at the top menu to see a full list of products.</h3>
</c:if>
<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>