<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<sf:form action="${pageContext.request.contextPath}/vendor/accountdetail"
	modelAttribute="objectList" method="get" id="accountsListForm"
	name="manageAccounts">

	<table class="tableview tableshadow" id="listaccounts">
		<thead>
			<tr>
				<th>Number</th>
				<th>Name</th>
				<th>Balance</th>
				<th>Asset Account</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
			</tr>
		</thead>

		<tbody>

			<c:forEach items="${objectList.pageList}" var="item" varStatus="i"
				begin="0">
				<tr class="account">
					<td>${item.accountNum}</td>
					<td>${item.accountName}</td>
					<td><fmt:formatNumber type="currency" currencySymbol="P" value="${item.accountBalance}"/></td>
					<td>${item.debitAccount}</td>
					<td><button type="button" onclick="rowRemoved('${item.accountNum}')">Delete</button></td>
					<td><button type="button" onclick="getDetail('${item.accountNum}')">Edit</button></td>
				</tr>
			</c:forEach>
			<tr>
				<td>
					<button type="button" onclick="followLink('/vendor/addaccount')">+New</button>
				</td>
				<td colspan="5">
					<button type="button" onclick="followLink('/vendor/printaccounts')" >Print Chart Of Accounts</button>
				</td>
			</tr>
		</tbody>
	</table>
</sf:form>

<script type="text/javascript">
	function rowRemoved(key) {
		    if (confirm("Are you sure you want to remove Account #" + key + " from Chart of Accounts?") == true) {
		   		window.location.href = "${pageContext.request.contextPath}/vendor/deleteaccount?deleteKey=" + key;		    
		   	} 
	}
	
	function getDetail(key) {
		window.location.href = "${pageContext.request.contextPath}/vendor/accountdetail?detailKey=" + key;	
	}
	
	function followLink(link) {
		window.location.href = "${pageContext.request.contextPath}" + link;
	}
 
 
</script>
