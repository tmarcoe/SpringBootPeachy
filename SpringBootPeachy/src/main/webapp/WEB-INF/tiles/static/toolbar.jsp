<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--<img class="logo" alt="FlowerLogo.png"
	src="<c:url value='/static/web/images/FlowerLogo.jpg' />" />
  -->

<div class="nav-bar">
	<div class="container">
		<ul class="nav">
			<li><button class="dropbtn" onclick="window.location.href = '/public/home'">Home</button></li>
			<li><button class="dropbtn" onclick="window.location.href = '/public/pickcategory'">Shop</button></li>
			<sec:isNotAuthenticated>
				<li><button class="dropbtn" onclick="window.location.href = '/login'">Sign In</button></li>
				<li><button class="dropbtn" onclick="window.location.href = '/public/signup'">Sign Up</button></li>
			</sec:isNotAuthenticated>
			<sec:isAuthenticated>
				<li><button class="dropbtn" onclick="window.location.href = '/logout'">Sign Out</button></li>
				<li><div class="dropdown">
						<button class="dropbtn">My Peachy's Coffee</button>
						<div class="dropdown-content" >
							<a href="/user/myprofile">Edit My Profile</a> 
							<a href="/user/shoppinghistory">Purchase History</a> 
							<a href="/user/changepassword">Change password</a>
							<a href="/user/surveyinput">Take the survey</a>
						</div>
					</div>
				</li>
				<li><button class="dropbtn" onclick="window.location.href = '/user/cart'">Shopping Cart</button></li>
			</sec:isAuthenticated>
			<li><button class="dropbtn" onclick="window.location.href = '/public/contactus'">Contact Us</button></li>
			<li><button class="dropbtn" onclick="window.location.href = '/public/aboutus'">About Us</button></li>
			<sec:hasRole role="EMPLOYEE,ADMIN,VENDOR">
				<li><div class="dropdown">
					<button class="dropbtn">Employees</button>
						<div class="dropdown-content">
							<a href="/employee/timesheet">Time Sheets</a>
							<sec:hasRole role="ADMIN,VENDOR">
								<a href="/admin/activitylist" >Approved Activities</a>
								<a href="/admin/employeelist">Approve Timesheets</a>
								<a href="/admin/payrollperiod">Payroll</a>
							</sec:hasRole>
						</div>
					</div>
				</li>
			</sec:hasRole>
			<sec:hasRole role="VENDOR,ADMIN">
				<li><div class="dropdown">
					<button class="dropbtn">Admin</button>
						<div class="dropdown-content">
							<a href="/admin/admin" >Ship Product</a>
							<a href="#">View Surveys</a>
							<a href="/admin/manageinventory">Inventory...</a>
							<a href="/admin/users">User Profiles...</a>
							<a href="#">Manage Coupons...</a>
							<a href="#">Business Reports...</a>
							<a href="/admin/fetallist">Fetal Scripts...</a>
							<a href="/admin/manageaccount">Accounting...</a>
						</div>
					</div>
				</li>
			</</sec:hasRole>
		</ul>
	</div>
</div>