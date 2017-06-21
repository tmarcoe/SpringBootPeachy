<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table class="tableview" id="activities">
	<thead>
		<tr>
			<th>Account Number</th>
			<th>Name</th>
			<th>Hrs Billed</th>
			<th>Max Hours</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${objectList.pageList}" var="item">
			<tr>
				<td>${item.accountNum}</td>
				<td>${item.name}</td>
				<td>${item.hoursBilled}</td>
				<td>${item.maxHours}</td>
				<td><button type="button" onclick="followLink('/editactivity?accountNum=${item.accountNum}')">Edit</button></td>
				<td><button type="button" onclick="rowRemoved('${item.accountNum}')">Delete</button></td>
			</tr>
		</c:forEach>
		<tr>
			<td><button type="button" onclick="followLink('/createactivity')">Add</button></td>
			<td colspan="5"><button type="button" onclick="followLink('/timereporting')">Cancel</button></td>
		</tr>
	</tbody>
</table>
<script type="text/javascript">

function rowRemoved(accountNum) {
	    if (confirm("Are you sure you want to remove Account #" + accountNum + " from Approved Activities?") == true) {
	   		window.location.href = "${pageContext.request.contextPath}/deleteactivity?accountNum=" + accountNum;		    
	   	} 
}

function followLink(link) {
	window.location.href = "${pageContext.request.contextPath}" + link;
}
</script>