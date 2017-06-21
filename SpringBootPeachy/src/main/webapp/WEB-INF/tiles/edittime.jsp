<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form method="post" action="${pageContext.request.contextPath}/employee/updatetime" commandName="timeSheet">
	<table>
		<caption>Starting Period: <fmt:formatDate value="${timeSheet.startPeriod}"/></caption>

		<tr>
			<th>Account Name</th>
			<th>Su</th>
			<th>Mo</th>
			<th>Tu</th>
			<th>We</th>
			<th>Th</th>
			<th>Fr</th>
			<th>Sa</th>
		</tr>
		<tr>
			<td><sf:input type="text" path="name" readonly="true" /></td>
			<td><sf:input type="number" path="sunday" step=".01" /></td>
			<td><sf:input type="number" path="monday" step=".01" /></td>
			<td><sf:input type="number" path="tuesday" step=".01" /></td>
			<td><sf:input type="number" path="wednesday" step=".01" /></td>
			<td><sf:input type="number" path="thursday" step=".01" /></td>
			<td><sf:input type="number" path="friday" step=".01" /></td>
			<td><sf:input type="number" path="saturday" step=".01" /></td>
		</tr>
		<tr>
			<td><sf:hidden path="accountNum"/></td>
			<td><sf:hidden path="startPeriod"/></td>
			<td><sf:hidden path="userId"/></td>
			<td><sf:hidden path="entered"/></td>
			<td><sf:hidden path="entryId"/></td>
		</tr>
		<tr>
			<td><sf:button type="submit" >Save</sf:button></td>
		</tr>
	</table>
</sf:form>