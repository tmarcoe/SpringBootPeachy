<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>


<sf:form method="post"
	action="${pageContext.request.contextPath}/vendor/savecoupon"
	commandName="coupon">
	<table class="coupontable">
		<tr>
			<th>Coupon ID</th>
			<th>Coupon Name</th>
			<th>Rule Name</th>
			<th>Date Expired</th>
			<th>Maximum Usage</th>
		</tr>
		<tr>
			<td>CPN<sf:input path="coupon_id" type="text" /></td>
			<td><sf:input path="name" type="text" /></td>
			<td><sf:input path="ruleName" type="text" id="myRuleName" /></td>
			<td><sf:input path="expires" type="date" /></td>
			<td><sf:input path="useage" type="number"/></td>
		</tr>
		<tr>
			<td>
				<div class="error">
					<sf:errors path="coupon_id" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="name" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="ruleName" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="expires" />
				</div>
			</td>
			<td>
				<div class="error">
					<sf:errors path="useage" />
				</div>
			</td>
		</tr>
		<tr>
			<td>Active&nbsp;<input type="hidden" value="on" name="_active" /> <sf:checkbox
					path="active" /></td>
			<td>Exclusive&nbsp;<input type="hidden" value="on" name="_active" /> <sf:checkbox
					path="exclusive" /></td>
		</tr>
	</table>
	<table>
		<tr>
			<th>Description</th>
		</tr>
		<tr>
			<td><sf:textarea path="description" rows="10" cols="50" /></td>
		</tr>
		<tr>
			<td><button type="submit" >Save</button></td>
			<td><button type="button" onclick="window.history.back()">Cancel</button></td>
		</tr>
	</table>
</sf:form>
