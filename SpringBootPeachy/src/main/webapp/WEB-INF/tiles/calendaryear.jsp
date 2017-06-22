
<table>
	<tr>
		<td>Please select the calendar year:</td>
		<td><select id="reportYear" onchange="getReport('${reportType}')">
				<option value="----">----</option>
				<option value="2017">2017</option>
				<option value="2018">2018</option>
				<option value="2019">2019</option>
				<option value="2020">2020</option>
				<option value="2021">2021</option>
				<option value="2022">2022</option>
				<option value="2023">2023</option>
				<option value="2024">2024</option>
				<option value="2025">2025</option>
				<option value="2026">2026</option>
				<option value="2027">2027</option>
				<option value="2028">2028</option>
				<option value="2029">2029</option>
				<option value="2030">2030</option>
		</select></td>
	</tr>
</table>
<script type="text/javascript">
	function getReport(reportType) {
		var e = document.getElementById("reportYear");
		var year = e.options[e.selectedIndex].value;
		window.location.href = "${pageContext.request.contextPath}/vendor/salesreport?reportType="
				+ reportType + "&year=" + year;
	}
</script>