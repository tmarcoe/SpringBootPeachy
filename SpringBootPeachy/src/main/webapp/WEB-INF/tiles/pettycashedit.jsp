<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<h3>
	Originated By User ID: ${pettyCashReg.userId}, On:
	<fmt:formatDate value="${pettyCashReg.transactionDate}" pattern="yyyy-MM-dd" />
</h3>

<sf:form method="post" action="${pageContext.request.contextPath}/pettycashupdate" commandName="pettyCashReg">
	<table>
		<tr>
			<td>Payable To</td>
			<td><sf:input type="text" path="payableTo" /></td>
			<td>Payment Method</td>
			<td><sf:input type="text" path="paymentMethod" /></td>
			<td>Amount</td>
			<td><sf:input path="amount" />
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="payableTo" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="paymentMethod" />
				</div></td>
			<td>&nbsp;</td>
			<td><div class="error">
					<sf:errors path="amount" />
				</div></td>
		</tr>
		<tr>
			<td>Reason</td>
		</tr>
		<tr>
			<td colspan="6"><sf:textarea rows="5" cols="50" path="reason" /></td>
		</tr>
		<tr>
			<td><div class="error">
					<sf:errors path="reason" />
				</div></td>
		</tr>
		<tr>
			<td><sf:button type="submit">Submit</sf:button>
		</tr>
	</table>
	<sf:hidden path="registerId" />
	<sf:hidden path="userId" />
	<sf:hidden path="transactionDate" />
	<input type="hidden" name="diffAmount" value="${diffAmount}" />
</sf:form>
