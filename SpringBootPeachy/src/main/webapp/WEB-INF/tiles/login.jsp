<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(document).ready(function() {
		document.f.j_username.focus();
	});
</script>
<div class="page-centered">
	<div class="div-centered">


		<c:if test="${param.error != null}">

			<p class="error">Login failed. Check that your username and password are correct.</p>

		</c:if>

		<form action='/login' method='post'>
			<table class="login-table">
				<tr>
					<td class="login-label">Email</td>
				</tr>
				<tr>
					<td><input type='text' name='username' value=''></td>
				</tr>
				<tr>
					<td class="login-label">Password</td>
				</tr>
				<tr>
					<td><input type='password' name='password' /></td>
				</tr>
				<!-- 
		<tr>
			<td>Remember me:</td>
			<td><input type='checkbox' name='_spring_security_remember_me' 
				id='remember_me' onchange='alertUser()' /></td>
		</tr>
		 -->
				<tr>
					<td><button type="submit" style="width: 100%;">Login</button></td>
				</tr>
				<tr>
					<td><button type="button" onclick="followLink('/public/signup')" style="width: 100%;">Sign Up</button></td>
				</tr>
				<tr>
					<td class="centerHeading"><a href="#" onclick="pwRecovery()">Forgot My Password</a></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<div class="modal" id="popup">

	<div class="modal-content small-modal" id="extraInfo">
		<h3>Enter your Email</h3>
		<form action="/public/passwordrecovery">
			<table>
				<tr>
					<td><input name="username" /></td>
					<td><button>Recover Password</button></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<script type="text/javascript">
	function alertUser() {
		if (document.getElementById("remember_me").checked == true) {
			alert("It is not recommended to use this feature on public computers!");
		}
	}

	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
	function pwRecovery() {
		var mode = document.getElementById("popup");
		mode.style.display = "block";
	}
</script>