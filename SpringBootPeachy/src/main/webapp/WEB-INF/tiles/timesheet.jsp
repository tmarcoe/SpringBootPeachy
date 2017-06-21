<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form method="post" command="timeSheets">
	<fmt:formatDate type="date"  pattern="yyyy-MM-dd" value='${startPeriod}' var="strDate"/>
	<c:if test="${periodClosed == true}">
		<h3>Period Closed</h3>
	</c:if>
	<table class="tableview" id="tmview">
		<caption>Starting Period: ${strDate}</caption>
		<tr>
			<th>Account Number</th>
			<th>Account Name</th>
			<th>Su</th>
			<th>Mo</th>
			<th>Tu</th>
			<th>We</th>
			<th>Th</th>
			<th>Fr</th>
			<th>Sa</th>
			<th>Weekly Totals</th>
			<th>&nbsp;</th>
		</tr>
		<c:set var="TotalSu" value="0" />
		<c:set var="TotalMo" value="0" />
		<c:set var="TotalTu" value="0" />
		<c:set var="TotalWe" value="0" />
		<c:set var="TotalTh" value="0" />
		<c:set var="TotalFr" value="0" />
		<c:set var="TotalSa" value="0" />

		<c:forEach var="item" items="${timeSheets}">

			<c:set var="TotalSu" value="${TotalSu + item.sunday}" />
			<c:set var="TotalMo" value="${TotalMo + item.monday}" />
			<c:set var="TotalTu" value="${TotalTu + item.tuesday}" />
			<c:set var="TotalWe" value="${TotalWe + item.wednesday}" />
			<c:set var="TotalTh" value="${TotalTh + item.thursday}" />
			<c:set var="TotalFr" value="${TotalFr + item.friday}" />
			<c:set var="TotalSa" value="${TotalSa + item.saturday}" />
			<tr>
				<td>${item.accountNum}</td>
				<td>${item.name}</td>
				<td>${item.sunday}</td>
				<td>${item.monday}</td>
				<td>${item.tuesday}</td>
				<td>${item.wednesday}</td>
				<td>${item.thursday}</td>
				<td>${item.friday}</td>
				<td>${item.saturday}</td>
				<td>${item.sunday + item.monday + item.tuesday + item.wednesday + item.thursday + item.friday + item.saturday}</td>
				<td>
					<c:if test="${periodClosed == false}">
						<button type="button"
							onclick="followLink('/employee/edittime?entryId=${item.entryId}')">Edit</button>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		<tfoot class="tablefooter">
			<tr>
				<td colspan="2">Totals -> </td>
				<td>${TotalSu}</td>
				<td>${TotalMo}</td>
				<td>${TotalTu}</td>
				<td>${TotalWe}</td>
				<td>${TotalTh}</td>
				<td>${TotalFr}</td>
				<td>${TotalSa}</td>
				<td>${TotalSu + TotalMo + TotalTu + TotalWe + TotalTh + TotalFr + TotalSa}</td>
				<td colspan="2">&nbsp;</td>

			</tr>
			<tr>
				<c:if test="${periodClosed == false}">
					<td><button type="button" onclick="followLink('/employee/entertime')">Add Activity</button></td>
					<td><button type="button" onclick="followLink('/employee/submittime?startPeriod=${strDate}')">Submit Timesheet</button>
				</c:if>
				<td colspan="9"><button type="button" onclick="followLink('/public/home')">Back</button></td>
			</tr>
		</tfoot>
	</table>
</sf:form>

<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>