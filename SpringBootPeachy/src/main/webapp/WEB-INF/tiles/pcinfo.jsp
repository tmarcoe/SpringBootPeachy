<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/demo.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/jquery.lettering-0.6.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/jquery-2.1.4.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/response.js"></script>

<div class="wrapper">
	<div class="checkout container">
		<sf:form id="payment-form" method="post"
			action="${pageContext.request.contextPath}/user/processcart"
			commandName="payment">
			<table class="pcinfoTable">
				<tr>
					<td>First Name</td>
					<td>Last Name</td>
					<td>Address 1</td>
					<td>Address 2</td>
				</tr>
				<tr>
					<td><sf:input type="text" path="firstName" /></td>
					<td><sf:input type="text" path="lastName" /></td>
					<td><sf:input type="text" path="address1" /></td>
					<td><sf:input type="text" path="address2" /></td>
				</tr>
				<tr>
					<td>City</td>
					<td>Region</td>
					<td>Country</td>
				</tr>
				<tr>
					<td><sf:input type="text" path="city" /></td>
					<td><sf:input type="text" path="region" /></td>
					<td><sf:input type="text" path="country" size="3" maxlength="3" /></td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>

			<section>
				<div class="bt-drop-in-wrapper">
					<div id="bt-dropin"></div>
				</div>
			</section>
			<sf:button id="sbutton" type="submit" hidden="true" >Submit</sf:button>
			<input id="cToken" type="hidden" value="${clientToken}" />
		</sf:form>
	</div>
</div>
<script src="https://js.braintreegateway.com/v2/braintree.js">

</script>

<script type="text/javascript">
	/*<![CDATA[*/
	var checkout = new Demo({
		formID : 'payment-form'
	});
	var input = document.getElementById("cToken");
	var client_token = input.value;

	braintree.setup(client_token, "dropin", {
		container : "bt-dropin",
		onReady: function(event) {
			document.getElementById("sbutton").hidden=false;
	    }
	});
	/*]]>*/
	/*
	$(document).ready(function() {
		document.getElementById("sbutton").hidden=false;
	});
	*/

</script>
