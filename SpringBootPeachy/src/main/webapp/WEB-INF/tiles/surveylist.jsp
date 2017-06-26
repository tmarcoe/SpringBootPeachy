<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:setDataSource var="ds" driver = "com.mysql.jdbc.Driver"
         url = "jdbc:mysql://localhost/donzalma_peachys"
         user = "root"  password = "In_heaven3"/>

	<table class="tableview" id="viewsurvey">
		<tr>
			<th>Date Submitted</th>
			<th>Submitted By</th>
			<th>Satisfaction</th>
			<th>Navigation</th>
			<th>Prices</th>
		</tr>
		<c:forEach var="item" items="${objectList.pageList}">
			<tr>
				<td><fmt:formatDate value="${item.filledOut}" /></td>
				<sql:query var="rs" dataSource="${ds}">SELECT firstname, lastname FROM user_profile WHERE user_id=${item.user_id}</sql:query>
				<c:forEach var="row" items="${rs.rows}">
					<c:set var="firstName" value="${row.firstname}" />
					<c:set var="lastName" value="${row.lastname}" />
				</c:forEach>
				<td>${firstName} ${lastName}</td>
				<td>${item.question3 + 1}</td>
				<td>${item.question5 + 1}</td>
				<td>${item.question7 + 1}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="5"><button type="button"
					onclick="followLink('/public/home')">OK</button></td>
		</tr>
	</table>

<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>