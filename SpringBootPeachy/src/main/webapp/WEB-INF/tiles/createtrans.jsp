<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form action="${pageContext.request.contextPath}/vendor/savetrans" modelAttribute="fetalScripts" method="post">
	<table>
		<tr>
		<th>File Name</th>
		<th>Comments</th>
		<th>Type</th>
		<th>Version</th>
		<th>File Status</th>
		</tr>
		<tr>
			<td><sf:input path="file_name" /></td>
			<td><sf:input path="comments" /></td>
			<td><sf:select path="type">
				<sf:option value="RULE">Rule</sf:option>
				<sf:option value="COUPON">Coupon</sf:option>
				<sf:option value="BLOCK">Block</sf:option>
			</sf:select></td>
			<td><sf:input path="version" value="1.00.00" readonly="true" /></td>
			<td><sf:input path="status" value="NEW" readonly="true" /></td>
		</tr>
		<tr><td><button type="submit">Save</button></td>
		</tr>
	</table>
	
</sf:form>

