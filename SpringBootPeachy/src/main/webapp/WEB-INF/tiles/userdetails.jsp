<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<sf:form id="details" method="post" action="${pageContext.request.contextPath}/admin/adminsaveuser" modelAttribute="userProfile">
	<input type="hidden" value="${roleIndex}" id="selectedRoles" />
	<table class="signup">
		<thead>
			<tr>
				<th colspan="4">User Information</th>
			</tr>
		</thead>
		<tr>
			<td>First Name:</td>
			<td><sf:input type="text" class="control" path="firstname" name="firstname" /></td>
			<td>Last Name:</td>
			<td><sf:input type="text" class="control" path="lastname" name="lastname" /></td>
			<td><sf:radiobutton path="maleFemale" value="M" />Male<sf:radiobutton path="maleFemale" value="F" />Female</td>

		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<div class="error">
					<sf:errors path="firstname" />
				</div>
			</td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="lastname" />
				</div></td>
		</tr>
		<tr>
			<td>Address 1:</td>
			<td><sf:input type="text" path="address1" class="control" name="address1" /></td>
			<td>Address 2:</td>
			<td><sf:input type="text" path="address2" class="control" name="address2" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="address1" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="address2" />
				</div></td>
		</tr>
		<tr>
			<td>City:</td>
			<td><sf:input type="text" path="city" class="control" name="city" /></td>
			<td>State/Region:</td>
			<td><sf:input type="text" path="region" class="control" name="region" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="city" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="region" />
				</div></td>
		</tr>
		<tr>
			<td>Postal Code:</td>
			<td><sf:input type="text" path="postalCode" class="control" name="postalCode" /></td>
			<td>Country:</td>
			<td><sf:select path="country">
					<sf:option value="">--Select--</sf:option>
					<sf:option value="PHL">Philippines</sf:option>
					<sf:option value="USA">United States</sf:option>
					<sf:option value="AUS">Australia</sf:option>
					<sf:option value="BGR">Bulgaria</sf:option>
					<sf:option value="BRA">Brazil</sf:option>
					<sf:option value="CAN">Canada</sf:option>
					<sf:option value="CHE">Switzerland</sf:option>
					<sf:option value="CHN">China</sf:option>
					<sf:option value="CZE">Czech Republic</sf:option>
					<sf:option value="DNK">Denmark</sf:option>
					<sf:option value="GBR">United Kingdom</sf:option>
					<sf:option value="HKG">Hong Kong</sf:option>
					<sf:option value="HRV">Croatia</sf:option>
					<sf:option value="HUN">Hungary</sf:option>
					<sf:option value="IDN">Indonesia</sf:option>
					<sf:option value="ISR">Israel</sf:option>
					<sf:option value="IND">India</sf:option>
					<sf:option value="JPN">Japan</sf:option>
					<sf:option value="KOR">South Korea</sf:option>
					<sf:option value="MEX">Mexico</sf:option>
					<sf:option value="MYS">Malaysia</sf:option>
					<sf:option value="NOR">Norway</sf:option>
					<sf:option value="NZL">New Zealand</sf:option>
					<sf:option value="POL">Poland</sf:option>
					<sf:option value="ROU">Romania</sf:option>
					<sf:option value="RUS">Russian Federation</sf:option>
					<sf:option value="SWE">Sweden</sf:option>
					<sf:option value="SGP">Singapore</sf:option>
					<sf:option value="THA">Thailand</sf:option>
					<sf:option value="TUR">Turkey</sf:option>
					<sf:option value="ZAF">South Africa</sf:option>
				</sf:select></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="postalCode" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="country" />
				</div></td>
		</tr>
		<tr>
			<td>Home Phone:</td>
			<td><sf:input type="tel" path="homePhone" class="control" name="homePhone" /></td>
			<td>Cell Phone:</td>
			<td><sf:input type="tel" path="cellPhone" class="control" name="cellPhone" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="homePhone" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="cellPhone" />
				</div></td>
		</tr>
		<tr>
			<td>E-Mail:</td>
			<td><sf:input type="username" path="username" class="control" name="username" /></td>
			<td>Currency:</td>
			<td><sf:select path="currency">
					<sf:option value="PHP">Philippines Peso</sf:option>
					<sf:option value="USD">United States Dollar</sf:option>
					<sf:option value="AUD">Australia Dollar</sf:option>
					<sf:option value="BGN">Bulgaria Lev</sf:option>
					<sf:option value="BRL">Brazil Real</sf:option>
					<sf:option value="CAD">Canada Dollar</sf:option>
					<sf:option value="CHF">Switzerland Franc</sf:option>
					<sf:option value="CNY">China Yuan Renminbi</sf:option>
					<sf:option value="CZK">Czech Republic Koruna</sf:option>
					<sf:option value="DKK">Denmark Krone</sf:option>
					<sf:option value="GBP">United Kingdom Pound</sf:option>
					<sf:option value="HKD">Hong Kong Dollar</sf:option>
					<sf:option value="HRK">Croatia Kuna</sf:option>
					<sf:option value="HUF">Hungary Forint</sf:option>
					<sf:option value="IDR">Indonesia Rupiah</sf:option>
					<sf:option value="ILS">Israel Shekel</sf:option>
					<sf:option value="INR">India Rupee</sf:option>
					<sf:option value="JPY">Japan Yen</sf:option>
					<sf:option value="KRW">South Korea Won</sf:option>
					<sf:option value="MXN">Mexico Peso</sf:option>
					<sf:option value="MYR">Malaysia Ringgit</sf:option>
					<sf:option value="NOK">Norway Krone</sf:option>
					<sf:option value="NZD">New Zealand Dollar</sf:option>
					<sf:option value="PLN">Poland Zloty</sf:option>
					<sf:option value="RON">Romania New Leu</sf:option>
					<sf:option value="RUB">Russia Ruble</sf:option>
					<sf:option value="SEK">Sweden Krona</sf:option>
					<sf:option value="SGD">Singapore Dollar</sf:option>
					<sf:option value="THB">Thailand Baht</sf:option>
					<sf:option value="TRY">Turkey Lira</sf:option>
					<sf:option value="ZAR">South Africa Rand</sf:option>
				</sf:select></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="username" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="currency" />
				</div></td>
		</tr>
		<tr>
			<td>Monthly News Letter:</td>
			<td><input type="hidden" value="on" name="_active" /> <sf:checkbox class="control" path="monthlyMailing" /></td>
			<td>Daily Specials:</td>
			<td><input type="hidden" value="on" name="_active" /> <sf:checkbox path="dailySpecials" class="control" /></td>
			
		</tr>


		<tr>
			<td>Shipping Info:</td>
			<td><sf:textarea rows="5" cols="50" path="shippingInfo" class="control" name="shippingInfo" /></td>
			<td>Role(s):</td>
			<td><sf:select path="roles" id="roles" multiselect="true">
					<sf:options items="${roles}" itemValue="id" itemLabel="role" />
				</sf:select></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="shippingInfo" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
				<sf:errors path="roles" />
			</div></td>
		</tr>
		<tr>
			<td><button type="button" onclick="formSubmit()" >Submit</button></td>
			<td><button type="button" onclick="openPopup()" >Employee</button></td>
			<td><button type="button" onclick="followLink('/admin/users')">Cancel</button></td>
		</tr>
		<tr>
			<td>User Enabled:</td>
			<td><input type="hidden" value="on" name="_active" /> <sf:checkbox path="enabled" class="control" /></td>
		</tr>
	</table>
	
	<sf:hidden path="roleString" id="roleString" />
	<sf:hidden path="password" />
	<sf:hidden path="user_id" />
	<sf:hidden path="dateAdded" />

	<!-- Popup to create an employee from a standard user profile -->
	<div class="modal" id="newEmployee">
		<div class="modal-content medium-modal">
			<table>
				<tr>
					<td>Start Date</td>
					<td>End Date</td>
					<td>Position</td>
					<td>Tax ID</td>
				</tr>
				<tr>
					<td><sf:input type="date" path="employee.startDate" /></td>
					<td><sf:input type="date" path="employee.endDate" /></td>
					<td><sf:input type="text" path="employee.position" /></td>
					<td><sf:input type="text" path="employee.taxId" /></td>
				</tr>
				<tr>
					<td>Hourly Rate</td>
					<td>% Federal Tax</td>
					<td>% State Tax</td>
					<td>% Fed Unemployment</td>
				</tr>
				<tr>
					<td><sf:input type="number" step=".01" path="employee.hourlyRate" /></td>
					<td><sf:input type="number" step=".01" path="employee.fTaxPrcnt" /></td>
					<td><sf:input type="number" step=".01" path="employee.sTaxPrcnt" /></td>
					<td><sf:input type="number" step=".01" path="employee.fUnPrcnt" /></td>
				</tr>
				<tr>
					<td>% St Unemployment</td>
					<td>% Medical</td>
					<td>% SSI</td>
					<td>% Ret</td>
				</tr>
				<tr>
					<td><sf:input type="number" step=".01" path="employee.sUnPrcnt" /></td>
					<td><sf:input type="number" step=".01" path="employee.medPrcnt" /></td>
					<td><sf:input type="number" step=".01" path="employee.ssiPrcnt" /></td>
					<td><sf:input type="number" step=".01" path="employee.retirePrcnt" /></td>
				</tr>
				<tr>
					<td>Garnishment</td>
					<td>Other</td>
					<td>Reason</td>
				</tr>
				<tr>
					<td><sf:input type="number" step=".01" path="employee.garnishment" /></td>
					<td><sf:input type="number" step=".01" path="employee.other" /></td>
					<td><sf:input type="text" path="employee.otherExpl" /></td>
				</tr>
				<tr>
					<td>Full Time: <input type="hidden" value="on" name="_active" /> <sf:checkbox path="employee.fullTime" /></td>
					<td>Salary: <input type="hidden" value="on" name="_active" /> <sf:checkbox path="employee.salary" /></td>
					<td>Exempt: <input type="hidden" value="on" name="_active" /> <sf:checkbox path="employee.exempt" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><hr></td>
					<td class="centerHeading" colspan="2">For Automatic deposit only</td>
					<td><hr></td>
				</tr>

				<tr>
					<td>Payment Type</td>
					<td>Account Number</td>
					<td>Routing Number</td>
				</tr>
				<tr>
					<td><sf:input type="text" path="employee.payMethod" /></td>
					<td><sf:input type="text" path="employee.accountNum" /></td>
					<td><sf:input type="text" path="employee.routingNum" /></td>
				</tr>
				<tr>
					<td><sf:hidden path="" />
					<td><button type="button" onclick="closePopup();">Save</button></td>
				</tr>
			</table>
		</div>
	</div>
</sf:form>

<script type="text/javascript">
	$( document ).ready(function() {
		var ndx = $("#selectedRoles").val();
		var selectedOptions = ndx.split(",");
		for(var i in selectedOptions) {
			 var optionVal = selectedOptions[i];
			$("#roles").find("option[value="+optionVal+"]").prop("selected", "selected");
		}
	});
	function openPopup() {
		var modal = document.getElementById('newEmployee');
		modal.style.display = "block"
	}

	function closePopup() {
		var modal = document.getElementById('newEmployee');
		modal.style.display = "none";
	}

	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
	
	function formSubmit() {

		var opt = document.getElementById("roles");
		var userRoles = "";
		for (var i=0; i < opt.options.length; i++) {
			if (opt.options[i].selected) {
				if (userRoles == "") {
					userRoles += opt.options[i].text;
				}else{
					userRoles += ";" + opt.options[i].text;
				}
			}
		}
		var rs = document.getElementById("roleString");
		rs.value = userRoles;
		document.getElementById("details").submit();
	}


</script>
