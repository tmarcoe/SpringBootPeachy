<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form method="post" commandName="timeSheetAccounts" action="${pageContext.request.contextPath}/vendor/saveactivity">
	<table>
		<tr>
			<th>Account Number</th>
			<th>Name</th>
			<th>Hrs Billed</th>
			<th>Max Hours</th>
		</tr>
		<tr>
			<td><sf:input type="text" path="AccountNum" /></td>
			<td><sf:input type="text" path="name" /></td>
			<td><sf:input type="number" step=".01" path="hoursBilled" /></td>
			<td><sf:input type="number" step=".01" path="maxHours" /></td>
		</tr>
		<tr>
			<td><sf:button type="submit" >Add Activity</sf:button></td>
		</tr>
	</table>

</sf:form>