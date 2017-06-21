<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="logos">
		<img alt="Image not available"
			src="<c:url value='/images/spilt-coffee-beans-sac.png'/>" width="400"/>
</div>


<h4>${filter}</h4>


<form:form id="pgform" method="post" modelAttribute="pparam"
	action="${pageLink}">
	<c:choose>
		<c:when test="${objectList.pageList.size() > 0}">
			<table class="productlist">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Product Name</th>
						<th class="currency">Price</th>
						<th class="currency">On Sale For...</th>
					</tr>
				</thead>
				<c:forEach var="inventory" items="${objectList.pageList}">
					<fmt:formatNumber type="currency"
						currencySymbol="${currencySymbol}"
						value="${inventory.sale_price * rate}" var="saleprice" />
					<fmt:formatNumber type="currency"
						currencySymbol="${currencySymbol}"
						value="${inventory.discount_price * rate}" var="discountprice" />
					<c:choose>
						<c:when test="${inventory.on_sale == true}">
							<c:set var="saleprice" value="<s>${saleprice}</s>" />
							<c:set var="discountprice" value="<b>${discountprice}</b>" />
						</c:when>
						<c:otherwise>
							<c:set var="discountprice" value="" />
						</c:otherwise>
					</c:choose>

					<tr>
						<td><a href="/public/productdetails?skuNum=${inventory.sku_num}">
								<img alt="Image Not Available"
								src="<c:url value='${fileLoc}${inventory.image}' />" width="70">
						</a></td>
						<td width="500">${inventory.product_name}</td>
						<td class="currency">${saleprice}</td>
						<td class="currency">${discountprice}</td>
					</tr>
					<tr>
						<td colspan="4">
							<hr />
						</td>
					</tr>
				</c:forEach>

			</table>
		</c:when>
		<c:otherwise>
			<h3>No Results</h3>
		</c:otherwise>
	</c:choose>

</form:form>
