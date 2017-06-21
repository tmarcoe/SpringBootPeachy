<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<sf:form commandName="datePicker">
	<fmt:formatDate type="date" pattern="yyyy-MM-dd" value="${datePicker.start}" var="startDt" />
	<fmt:formatDate type="date" pattern="yyyy-MM-dd" value="${datePicker.end}" var="endDt"/>
	<table class="tableview" id="viewledger">
		<thead>
			<tr>
				<th>Date</th>
				<th>Account #</th>
				<th>Description</th>
				<th>Debit</th>
				<th>Credit</th>
			</tr>
		</thead>

		<tbody>

			<c:forEach items="${objectList.pageList}" var="item">
				<c:choose>
					<c:when test="${item.debitAmt != 0}">
						<fmt:formatNumber type="currency" currencySymbol="P"
							value="${item.debitAmt}" var="debit" />
					</c:when>
					<c:otherwise>
						<c:set var="debit" value="" />
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${item.creditAmt != 0}">
						<fmt:formatNumber type="currency" currencySymbol="P"
							value="${item.creditAmt}" var="credit" />
					</c:when>
					<c:otherwise>
						<c:set var="credit" value="" />
					</c:otherwise>
				</c:choose>
				<tr class="ledgerrecord">
					<td><fmt:formatDate value="${item.entryDate}" /></td>
					<td>${item.accountNum}</td>
					<td>${item.description}</td>
					<td class="currency">${debit}</td>
					<td class="currency">${credit}</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot class="tablefooter">
			<tr>
				<td><sf:hidden path="${start}"/></td>
				<td><sf:hidden path="${end}"/></td>
				<td><button type="button" onclick="followLink('/admin/exportledger?startDt=${startDt}&endDt=${endDt}')">Export</button></td>
				<td>&nbsp;</td>
			</tr>
		</tfoot>
	</table>
</sf:form>
<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>