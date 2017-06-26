<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld" %>
<h2>Users</h2>

<form:form id="pgform" method="post" modelAttribute="pparam"
	action="${pageLink}">
	<table class="tableview tableshadow" id="listusers">

		<thead>
			<tr>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Email</th>
				<th>user ID</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<c:forEach var="user" items="${objectList.pageList}" varStatus="i"
			begin="0">
			<tr>
				<td>${user.firstname}</td>
				<td>${user.lastname}</td>
				<td>${user.username}</td>
				<td><fmt:formatNumber type="number" pattern="00000000" value="${user.user_id}" />  </td>
				<td><button type="button" onclick="rowRemoved('${user.username}', '${user.firstname}', '${user.lastname}')">Delete</button></td>
				<td><button type="button" onclick="getDetail('${user.user_id}')">Edit</button></td>
			</tr>
		</c:forEach>
		<tfoot class="tablefooter" >
			<tr>
				<td colspan="6"><button type="button" onclick="window.location.href = '/public/home'">OK</button></td>
			</tr>
		</tfoot>
	</table>
</form:form>
<script type="text/javascript">
	function rowRemoved(key, first, last) {
		if (confirm("Are you sure you want to remove "
				+ first + " " + last + " from User Profiles?") == true) {
			window.location.href = "${pageContext.request.contextPath}/admin/deleteuser?deleteKey="
					+ key;
		}
	}

	function getDetail(key) {
		window.location.href = "${pageContext.request.contextPath}/admin/userdetails?detailKey="
				+ key;
	}
	
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}


</script>
