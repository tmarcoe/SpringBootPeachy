<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="schemeStr"></c:set>
<table class="tableview" >
	<tr class="jeftJustify">
		<th>Scheme</th>
		<th>Label</th>
		<th>Widget</th>
		<th>value</th>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach items="${objectList}" var="item">
		<c:set var="schemeStr" value="${item.scheme}"></c:set>
		<tr>
			<td>${item.scheme}</td>
			<td>${item.label}</td>
			<td>${item.widget}</td>
			<td>${item.value}</td>
			<td><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/editscheme?schemeId=${item.entryId}'">Edit</button></td>
			<td><button type="button" onclick="remove(${item.entryId})">Delete</button>
		</tr>
	</c:forEach>
	<tfoot class="tablefooter">
		<tr>
			<td><button  type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/newscheme?schemeStr=${schemeStr}'" >New</button></td>
			<td colspan="5"><button type="button" onclick="window.location.href = '${pageContext.request.contextPath}/vendor/schemes'" >Back</button></td>
		</tr>
	</tfoot>
</table>
<script type="text/javascript">
	function remove(entryId) {
		if (confirm("Are you sure you want to remove this entry.") == true) {
			window.location.href = "${pageContext.request.contextPath}/vendor/deletesinglescheme?entryId=" + entryId;
		}
	}
</script>
