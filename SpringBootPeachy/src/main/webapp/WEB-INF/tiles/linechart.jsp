<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="lineChart">
	<canvas class="reportCharts divshadow" id="myChart" ></canvas>
</div>
<table class="buttonTable">
	<tr>
		<td><button type="button" onclick="followLink('/public/home')">OK</button></td>
		<td><button type="button" onclick="renderCanvas()" >View/Save Image</button></td>
	</tr>
</table>
<script>

var ctx = document.getElementById("myChart");

var sData = "${salesData}";
var year = "?year=${year}";

$(document).ready(function () { $.getJSON(sData + year, function(data) {
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
	var img    = canvas.toDataURL("image/png");
	
	document.write('<img src="'+img+'"/>');
}

</script>

