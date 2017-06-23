<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="pageheading">
	<h2>Client Email</h2>
</div>
<c:choose>
	<c:when test="${objectList.pageList.size() > 0}">
		<table class="tableview tableshadow">
			<thead class="emailheader">
				<tr>
					<th>From</th>
					<th>Subject</th>
				</tr>
			</thead>
			<c:forEach var="item" items="${objectList.getPageList()}">
				<tr>
					<td>${item.getFrom()}</td>
					<td>${item.getSubject()}</td>
				</tr>
			</c:forEach>
			<tfoot class="tablefooter">
				<tr>
					<td colspan="2"><button type="button" onclick="followLink('/public/home');">Back</button></td>
				</tr>
			</tfoot>
		</table>
	</c:when>
	<c:otherwise>
		<h1>No Email</h1>
		<button type="button" onclick="followLink('/admin');">Back</button>	
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>