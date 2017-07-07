<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/codemirror.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/codemirror.css">
<script
	src="${pageContext.request.contextPath}/script/mode/fetal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/download.js"></script>
<div class="editContainer">
<div class="nav-bar">
	<div class="container">
		<ul class="nav">
			<li><a href="#" onclick="writeFile('${transFile}')" >Save</a></li>
			<li><a href="#" onclick="cancel()">Exit</a></li>
			<li class="lightText">File: ${transFile}</li>
		</ul>
	</div>
</div>
	<div id="myTextArea" class="editor"></div>
</div>

<script type="text/javascript" >
	var ta = document.getElementById("myTextArea");
	var url = "${pageContext.request.contextPath}/vendor/fetal-data/trans-file?transFile=${transFile}&fetalUrl=${fetalUrl}";
	var outUrl = "${pageContext.request.contextPath}/vendor/fetal-data/save-file?transFile=${transFile}";
	var fn = "${transFile}";
	var status = "${status}";
	var  myCodeMirror;
	if (status != "NEW") {
		$.ajax({
			url: url,
			type: "GET",
	    	dataType: "text",
	    	success: function(data) {
	    		myCodeMirror = CodeMirror(ta, {
	    			value: data,
	    			mode : "fetal",
	    			lineNumbers : "true",
	    			indentWithTabs : "true"
	    		});
	    	}
		});
	}else{
		myCodeMirror = CodeMirror(ta, {
			mode : "fetal",
			lineNumbers : "true",
			indentWithTabs : "true"
		});

	}
	
	function saveIt(fileName) {
		var text = myCodeMirror.getDoc().getValue();
		download(text, fileName, "text/plain");
		myCodeMirror.getDoc().markClean();
	}
	function writeFile(fileName) {
		var text = myCodeMirror.getDoc().getValue();
		$.ajax({
			url: outUrl,
			type: "PUT",
			data: "&transData=" + text,
			success: function(data) {
			    alert(fileName + ' was saved to quarantine and is awaiting approval.');
				myCodeMirror.getDoc().markClean();
			  }

		});
	}
	function cancel() {	
		if (myCodeMirror.getDoc().isClean() == true) {
			window.history.back();
		}else{
			if (confirm("Are you sure you want to discard changes?") == true) {
				window.location.href = "/vendor/fetallist";
			}
		}	
	}
	

</script>