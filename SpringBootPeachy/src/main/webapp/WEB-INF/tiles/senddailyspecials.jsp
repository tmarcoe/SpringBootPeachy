
<center>
	<h3>Daily specials were sent to ${mailCount} users</h3>
</center>
<button type="button" onclick="followLink('/public/home')">OK</button>

<script type="text/javascript">
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
</script>