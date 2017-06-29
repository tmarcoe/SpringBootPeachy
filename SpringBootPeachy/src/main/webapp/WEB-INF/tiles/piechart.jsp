
<div class="pieChart">
	<canvas class="reportCharts  divshadow" id="myChart"></canvas>
</div>
<table class="buttonTable">
	<tr>
		<td><button type="button"
				onclick="followLink('/public/home')">OK</button></td>
		<td><button type="button" onclick="renderCanvas()">View/Save
				Image</button></td>
	</tr>
</table>

<script>
	var ctx = document.getElementById("myChart");
	$(document).ready(function() {
		$.getJSON("/reports/genders", function(data) {
			var myChart = new Chart(ctx, data);
		})	
		.fail(function(jqXHR, textStatus, errorThrown) {
	        alert("error " + textStatus + "\n" + "incoming Text " + jqXHR.responseText);
	    });

	});
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
	function renderCanvas() {
		var canvas = document.getElementById("myChart");
		var img = canvas.toDataURL("image/png");

		document.write('<img src="'+img+'"/>');
	}
</script>

