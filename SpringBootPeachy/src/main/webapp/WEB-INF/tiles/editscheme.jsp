<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<sf:form action="${pageContext.request.contextPath}/updatescheme" method="post" commandName="schemes">

	<table>
		<tr>
			<td>Scheme:</td>
			<td><sf:input path="scheme" readonly="true" /></td>
			<td>Label:</td>
			<td><sf:input path="label"/></td>
		</tr>
		<tr>
			<td>Name:</td>
			<td><sf:input path="name"/></td>
			<td>Id:</td>
			<td><sf:input path="id"/></td>
		</tr>
		<tr>
			<td>Widget Type:</td>
			<td><sf:select path="widget">
				<sf:option value="input">Input</sf:option>
				<sf:option value="select">Drop Down List</sf:option>
				<sf:option value="radio">Radio Button</sf:option>
				<sf:option value="checkbox">Checkbox</sf:option>
			</sf:select></td>
			<td>Value:</td>
			<td><sf:input path="value"/></td>
		</tr>
		<tr>
			<td>Options:</td>
			<td><sf:input path="options"/></td>
			<td>Comments:</td>
			<td><sf:input path="comments"/></td>
		</tr>
		<tr><td><button type="submit">Save</button></td></tr>
		<sf:hidden path="entryId"/>
	</table>

</sf:form>