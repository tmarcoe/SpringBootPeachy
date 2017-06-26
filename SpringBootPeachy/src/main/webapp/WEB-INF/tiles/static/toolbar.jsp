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
							<a href="/user/changepassword">Change password</a>
							<a href="/user/shoppinghistory">Purchase History</a> 
							<a href="/user/surveyinput">Take the survey</a>
						</div>
					</div>
				</li>
				<li><button class="dropbtn" onclick="window.location.href = '/user/cart'">Shopping Cart</button></li>
			</sec:isAuthenticated>
			<li><button class="dropbtn" onclick="window.location.href = '/public/contactus'">Contact Us</button></li>
			<li><button class="dropbtn" onclick="window.location.href = '/public/aboutus'">About Us</button></li>
			<sec:hasRole role="EMPLOYEE,VENDOR">
				<li><div class="dropdown">
					<button class="dropbtn">Employees</button>
						<div class="dropdown-content">
							<a href="/employee/timesheet">Time Sheets</a>
							<sec:hasRole role="VENDOR">
								<a href="/vendor/activitylist" >Approved Activities</a>
								<a href="/vendor/employeelist">Approve Timesheets</a>
								<a href="/vendor/payrollperiod">Payroll</a>
							</sec:hasRole>
						</div>
					</div>
				</li>
			</sec:hasRole>
			<sec:hasRole role="VENDOR,ADMIN">
				<li><div class="dropdown">
					<button class="dropbtn">Admin</button>
						<div class="dropdown-content">
							<a href="/vendor/admin" >Ship Product</a>
							<sec:hasRole role="ADMIN">
								<a href="/admin/users">User Profiles</a>
							</sec:hasRole>
							<a href="/vendor/surveylist">View Surveys</a>
							<a href="/vendor/manageinventory">Inventory...</a>
							<a href="/vendor/listcoupons">Manage Coupons...</a>
							<a href="/vendor/calendaryear?reportType=sales">Reports...</a>
							<a href="/vendor/fetallist">Fetal Scripts...</a>
							<a href="/vendor/manageaccount">Accounting...</a>
							<a href="/vendor/checkemail">E-mail...</a>
						</div>
					</div>
				</li>
			</</sec:hasRole>
		</ul>
	</div>
</div>