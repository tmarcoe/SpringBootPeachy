<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="tableview" id="employeelist">
	<tr>
		<th>First Name</th>
		<th>Last Name</th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach var="item" items="${userProfile}">
		<tr>
			<td>${item.firstname}</td>
			<td>${item.lastname}</td>
			<td><button type="button" onclick="followLink('/vendor/approvetime?userID=${item.user_id}')">Approve</button>
		</tr>
	
	</c:forEach>
</table>

<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>