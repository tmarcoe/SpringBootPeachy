<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
<c:when test="${periods.size() > 0}">
<table>
	<tr>
		<td>Select Payroll Period</td>
		<td>
			<select id="payrollPeriod">
				<c:forEach var="item" items="${periods}">
					<option value="${item}">${item}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td><button type="button" onclick="submitPeriod()">Submit Period</button></td>
		<td><button type="button" onclick="cancel()">Cancel</button></td>
	</tr>
</table>
</c:when>
<c:otherwise>
	<h3>No outstanding periods remain.</h3>
	<p>
	<button type="button" onclick="cancel()">Cancel</button>
</c:otherwise>
</c:choose>
<script type="text/javascript">
	function submitPeriod() {
		var p = document.getElementById("payrollPeriod");
		var period = p.options[p.selectedIndex].value;
		window.location.href = "${pageContext.request.contextPath}/vendor/payroll?period=" + period;
	}
	function cancel() {
		window.location.href = "${pageContext.request.contextPath}/timereporting";
	}
</script>
