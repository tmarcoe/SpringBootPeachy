<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="logos">
	<img alt="Image not available" src="<c:url value='/images/Cappuccino-1.png'/>" width="400"  align="right" /><p>
	<img id="lowerImage" alt="Image not available" src="<c:url value='/images/FetalLogo.png'/>" width="200" align="right" />
</div>
<c:choose>
	<c:when test="${inventory.size() > 0}">
		<div class="pageheading">
			<h2>Daily Specials</h2>
			<h3>To see a full list of products click the 'Shop' menu item.</h3>
		</div>
		<table class="dailyspecials rjthird">
			<tr>
				<th>&nbsp;</th>
				<th>Product Name</th>
				<th>Now On Sale For...</th>
			</tr>
			<c:forEach var="inventory" items="${inventory}">
				<c:set var="price" value="${inventory.discount_price}" />
				<tr class="inventoryrow">
					<td><a href="${pageContext.request.contextPath}/public/productdetails?skuNum=${inventory.sku_num}"> <img
							alt="Image Not Available" src='<c:url value="${fileLoc}${inventory.image}"></c:url>' width="80">
					</a></td>
					<td class="name" width="500">${inventory.product_name}</td>
					<td class="price"><fmt:formatNumber type="currency" currencySymbol="${currencySymbol}" value="${price * rate}" /></td>
				</tr>
				<tr>
					<td colspan="3"><hr /></td>
				</tr>
			</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<div class="pageheading">
			<h2>Sorry, no daily specials today.</h2>
			<h3>To see a full list of products click the 'Shop' menu item.</h3>
		</div>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>