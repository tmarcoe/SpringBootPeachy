<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="tableview">
	<tr>
		<th colspan="3">Scheme</th>
	</tr>
	<c:forEach items="${objectList}" var="item">
		<tr>
			<td>${item}</td>
			<td><button type="button" onclick="remove('${item}')">Delete</button></td>
			<td><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/singlescheme?scheme=${item}'" >Edit Scheme</button>
		</tr>
	</c:forEach>
	<tfoot class="tablefooter">
		<tr>
			<td><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/newscheme?schemeStr='">New Scheme</button></td>
			<td colspan="2"><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/manageinventory'" >Back</button></td>
		</tr>
	</tfoot>
</table>
<script type="text/javascript">
	function remove(scheme) {
		if (confirm("This will delete the entire " + scheme + " scheme. Are you sure  you want to proceed?") == true) {
			window.location.href = "${pageContext.request.contextPath}/vendor/deletescheme?scheme=" + scheme;
		}
	}
</script>