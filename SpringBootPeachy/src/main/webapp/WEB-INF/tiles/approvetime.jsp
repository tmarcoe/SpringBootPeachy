<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:choose>
	<c:when test="${timeSheets.size() > 0}">
		<sf:form method="post" command="timeSheets">
			<fmt:formatDate type="date" pattern="yyyy-MM-dd"
				value='${startPeriod}' var="strDate" />
			<c:set var="userId" value="0" />
			<table class="tableview" id="tmview">
				<caption>Starting Period: ${strDate}</caption>
				<tr>
					<th>Account Number</th>
					<th>Su</th>
					<th>Mo</th>
					<th>Tu</th>
					<th>We</th>
					<th>Th</th>
					<th>Fr</th>
					<th>Sa</th>
					<th>Weekly Totals</th>

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
						<td>${item.sunday}</td>
						<td>${item.monday}</td>
						<td>${item.tuesday}</td>
						<td>${item.wednesday}</td>
						<td>${item.thursday}</td>
						<td>${item.friday}</td>
						<td>${item.saturday}</td>
						<td>${item.sunday + item.monday + item.tuesday + item.wednesday + item.thursday + item.friday + item.saturday}</td>
					</tr>
					<c:set var="userId" value="${item.userId}" />
				</c:forEach>
				<tfoot class="tablefooter">
					<tr>
						<td>Totals -></td>
						<td>${TotalSu}</td>
						<td>${TotalMo}</td>
						<td>${TotalTu}</td>
						<td>${TotalWe}</td>
						<td>${TotalTh}</td>
						<td>${TotalFr}</td>
						<td>${TotalSa}</td>
						<td>${TotalSu + TotalMo + TotalTu + TotalWe + TotalTh + TotalFr + TotalSa}</td>
					</tr>
					<tr>
						<td><button type="button"
								onclick="followLink('/vendor/submitapproval?userId=${userId}&startPeriod=${strDate}')">Approve
								Timesheet</button>
					</tr>
				</tfoot>
			</table>
		</sf:form>
	</c:when>
	<c:otherwise>
		<h1>This user has no available timesheets for this period</h1>
	</c:otherwise>
</c:choose>
<button type="button" onclick="openPopup()">Other Periods</button>
<div class="modal" id="prevPeriod">
	<div class="modal-content">
		<table>
			<tr>
				<td>Enter the starting Period:</td>
				<td><input type="date" id="prevDate" /></td>
			</tr>
			<tr>
				<td>
					<button type="button" onclick="getPeriod('${userID}')">View Other
						Period</button>
				</td>
				<td>
					<button type="button" onclick="cancel()">Cancel</button>
				</td>
			</tr>
			<tr>
				<td><div class="error"><span id="errMsg"></span></div>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
	function openPopup() {
		var modal = document.getElementById('prevPeriod');
		modal.style.display = "block"
	}
	function cancel() {
		var modal = document.getElementById('prevPeriod');
		modal.style.display = "none";
	}

	function getPeriod(userId) {
		var period = document.getElementById('prevDate').value;
		var dt = new Date(period);
		if (dt.getDay() != 0) {
			document.getElementById("errMsg").textContent="Starting period must be on a Sunday";
		}else{
			window.location.href = "${pageContext.request.contextPath}/vendor/previousperiod?startPeriod=" + period + "&userID=" + userId;
		}

	}

	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>