<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form method="post" action="${pageContext.request.contextPath}/employee/savetime" commandName="timeSheet">
	<table>
		<caption>Starting Period: <fmt:formatDate value="${timeSheet.startPeriod}"/></caption>

		<tr>
			<th>Select Activity</th>
			<th>Su</th>
			<th>Mo</th>
			<th>Tu</th>
			<th>We</th>
			<th>Th</th>
			<th>Fr</th>
			<th>Sa</th>
		</tr>
		<tr>
			<td>
				<sf:select path="accountNum" >
					<sf:option value="">---Select---</sf:option>
					<c:forEach var="item" items="${accounts}">
						<sf:option value="${item.accountNum}" >${item.name}</sf:option>
					</c:forEach>
				</sf:select>
			</td>
			<td><sf:input type="number" path="sunday" step=".01" /></td>
			<td><sf:input type="number" path="monday" step=".01" /></td>
			<td><sf:input type="number" path="tuesday" step=".01" /></td>
			<td><sf:input type="number" path="wednesday" step=".01" /></td>
			<td><sf:input type="number" path="thursday" step=".01" /></td>
			<td><sf:input type="number" path="friday" step=".01" /></td>
			<td><sf:input type="number" path="saturday" step=".01" /></td>
		</tr>
		<tr>
			<td>
				<div class="error">
					<sf:errors path="accountNum" />
				</div>
			<td>
				<div class="error">
					<sf:errors path="sunday" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="monday" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="tuesday" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="wednesday" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="thursday" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="friday" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="saturday" />
				</div>
			</td>
		</tr>
		<tr>
			<td><sf:hidden path="startPeriod"/></td>
			<td><sf:hidden path="userId"/></td>
			<td><sf:hidden path="entered"/></td>
		</tr>
		<tr>
			<td><sf:button type="Save" >Save</sf:button></td>
		</tr>
	</table>
</sf:form>
<script type="text/javascript">
    $(document).ready(function () {});
    updateName
</script>