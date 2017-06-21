/**
 * 
 */

function putElement(context, element) {

	for (i = 0; i < element.length; i++) {
		index = i % 2;
		if (index == 0) {
			var row = context.insertRow(0);
		}

		switch (element[i].widget) {
		case "input":
			var inp = document.createElement(element[i].widget);
			inp.id = element[i].id;
			var label = document.createTextNode(element[i].label + ":");
			var cell = row.insertCell(-1);
			cell.appendChild(label);
			cell.appendChild(inp);

			break;
			
		case "select":
			var sel = document.createElement(element[i].widget);
			sel.id = element[i].id;
			var label = document.createTextNode(element[i].label + ":");
			var opts = element[i].value.split(",");

			for (j=0 ; j < opts.length; j++) {
				sel.options.add(new Option(opts[j],opts[j]));
			}
			var cell = row.insertCell(-1);
			cell.appendChild(label);
			cell.appendChild(sel);
			break
			
		case "radio":
			rlabels = element[i].value.split(",");
			var label = document.createTextNode(element[i].label + ":");
			var cell = row.insertCell(-1);
			cell.appendChild(label);
			for (j=0; j < rlabels.length; j++) {
				var radio = document.createElement("input");
				var txt = document.createTextNode(rlabels[j])
				radio.type = "radio";
				radio.value = rlabels[j];
				radio.name = element[i].name;
				cell.appendChild(txt);
				cell.appendChild(radio);
			}
			break;
			
		case "checkbox":
			var label = document.createTextNode(element[i].label + ":");
			var check = document.createElement("input");
			check.id = element[i].id;
			check.name = element[i].name;
			check.type = "checkbox"
			var cell = row.insertCell(-1);
			cell.appendChild(label); 
			cell.appendChild(check);
			break;
		}

		
	}
}

function readElement(element) {
	var outString = "";
	var val = "";
	for (i = 0; i < element.length; i++) {
		debugger;
		switch (element[i].widget) {
		
		case "radio":
			val = document.querySelector('input[name="' + element[i].name + '"]:checked').value;
			break;
			
		case "select":
			var e = document.getElementById(element[i].id);
			val = e.options[e.selectedIndex].value;
			break;
			
		case "checkbox":
			var cb = document.getElementById(element[i].id);
			if (cb.checked == true) {
				val = "true";
			}else{
				val = "false";
			}
			break;
			
		default:
			val = document.getElementById(element[i].id).value;
			break;
		}
		debugger;
		outString += (element[i].label + "=" + val + "  ");

	}

	return outString;
}