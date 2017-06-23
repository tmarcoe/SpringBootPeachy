<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<script type="text/javascript">

function scorePassword(pass) {
    var score = 0;
    if (!pass)
        return score;

    // award every unique letter until 5 repetitions
    var letters = new Object();
    for (var i=0; i<pass.length; i++) {
        letters[pass[i]] = (letters[pass[i]] || 0) + 1;
        score += 5.0 / letters[pass[i]];
    }

    // bonus points for mixing it up
    var variations = {
        digits: /\d/.test(pass),
        lower: /[a-z]/.test(pass),
        upper: /[A-Z]/.test(pass),
        nonWords: /\W/.test(pass),
    }

    variationCount = 0;
    for (var check in variations) {
        variationCount += (variations[check] == true) ? 1 : 0;
    }
    score += (variationCount - 1) * 10;

    return parseInt(score);
}

function checkPassStrength(pass) {
    var score = scorePassword(pass);
    var bgColor;
    var elem = document.getElementById("pStrength"); 
    elem.style.width = score + '%'; 
    if (score < 30) {
    	bgColor = "#ff0000";
    	document.getElementById('pLabel').innerHTML = 'too weak';
    }else if ( score < 50 ) {
    	document.getElementById('pLabel').innerHTML = 'weak';
    	bgColor = "#f27a0d";
    }else if( score < 70) {
    	document.getElementById('pLabel').innerHTML = 'average';
    	bgColor = "#ffff00";
    }else if ( score < 90) {
    	bgColor = "#9fea15";
    	document.getElementById('pLabel').innerHTML = 'good';
    }else {
    	bgColor = "#00ff00";
    	document.getElementById('pLabel').innerHTML = 'excellent';
    }
    elem.style.backgroundColor = bgColor;
}

function onLoad() {

		$("#password").keyup(checkPasswordsMatch);
		$("#confirmpass").keyup(checkPasswordsMatch);

		$("#details").submit(canSubmit);
	}

	function canSubmit() {
		var password = $("#password").val();
		var confirmpass = $("#confirmpass").val();
		if (scorePassword(pass) < 30) {
			return false;
		}
		if (password != confirmpass) {
			alert("<fmt:message key='UnmatchedPasswords.userProfile.password' />")
			return false;
		} else {
			return true;
		}
	}

	function checkPasswordsMatch() {
		var password = $("#password").val();
		var confirmpass = $("#confirmpass").val();

		if (password.length > 3 || confirmpass.length > 3) {

			if (password == confirmpass) {
				$("#matchpass").text("<fmt:message key='MatchedPasswords.userProfile.password'/>");
				$("#matchpass").addClass("valid");
				$("#matchpass").removeClass("error");
			} else {
				$("#matchpass").text("<fmt:message key='UnmatchedPasswords.userProfile.password' />");
				$("#matchpass").addClass("error");
				$("#matchpass").removeClass("valid");
			}
		}
		checkPassStrength(password);
	}

	$(document).ready(onLoad);
</script>
