<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	$(document).ready(function() {
		document.f.j_username.focus();
	});
</script>

<h3>Login with Email and Password</h3>

<c:if test="${param.error != null}">

	<p class="error">Login failed. Check that your username and
		password are correct.</p>

</c:if>

<form action='/login' method='post'>
	<table class="formtable">
		<tr>
			<td>Email:</td>
			<td><input type='text' name='username' value=''></td>
		</tr>
		<tr>
			<td>Password:</td>
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
			<td colspan='2'><input name="submit" type="submit" value="Login" /></td>
			<td><button type="button" onclick="followLink('/signup')" >New User</button></td>
		</tr>
	</table>
</form>
<script type="text/javascript">

function alertUser() {
	if (document.getElementById("remember_me").checked == true) {
		alert("It is not recommended to use this feature on public computers!");
	}
}

function followLink(link) {
	window.location.href = "${pageContext.request.contextPath}" + link;
}
</script>