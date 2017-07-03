<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form id="account" action="${pageContext.request.contextPath}/vendor/saveaccount"
method="post" commandName="chartOfAccounts">
	<div class="addaccount">
		<p></p>
		<table>
			<thead>
				<tr>
					<th colspan="2" class="accountheader">Account Information</th>
				</tr>
				<tr> <th colspan="2">&nbsp;</th></tr>
			</thead>

			<tr>
				<td>Account Number:</td>
				<td><sf:input class="control" type="text" path="accountNum" name="accountNum" /></td>
				<td><div class="error"><sf:errors path="accountNum"></sf:errors></div>
			</tr>
			<tr>
				<td>Account Name:</td>
				<td><sf:input class="control" type="text" path="accountName" name="accountName" /></td>
				<td><div class="error"><sf:errors path="accountName"></sf:errors></div>
			</tr>
			<tr>
				<td>Account Balance:</td>
				<td><sf:input class="control" type="number" path="accountBalance" step=".01" min="-10000000" max="10000000"
					name="accountBalance" /></td>
				<td><div class="error"><sf:errors path="accountBalance"></sf:errors></div>
			</tr>
			<tr>
				<td>Description:</td>
				<td><sf:textarea class="control" rows="10" cols="50" path="Description" name="Description"></sf:textarea></td>
				<td><div class="error"><sf:errors path="Description"></sf:errors></div>
			</tr>
			<tr>
				<td>Asset Account:</td>
				<td><input type="hidden" value="on" name="_active"/>
			<sf:checkbox path="debitAccount" class="control" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Update Account" /></td>
				<td><button type="button" onclick="followLink('/vendor/manageaccount');">Cancel</button></td>
			</tr>
		</table>
	</div>
</sf:form>
<script type="text/javascript">
function followLink(link) {
	window.location.href = "${pageContext.request.contextPath}" + link;
}

</script>