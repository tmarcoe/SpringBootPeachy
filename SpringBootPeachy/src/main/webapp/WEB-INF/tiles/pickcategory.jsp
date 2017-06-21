<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<sf:form modelAttribute="catList" method="post" >
	<div class="category" >
	<c:set var="tablewidth" value="6" />
	<c:set var="i" value="0" />
	<table class="catlist" >
		<c:forEach items="${catList}"  var="cat">
			<c:if test="${(i % tablewidth) == 0}">
				<tr>
			</c:if>
				<td><a href="/public/setcategory?cat=${cat[0]}" ><img
						alt="Image Not Available"
						src="<c:url value='${fileLoc}${cat[12]}'/>"
						width="90"><br>${cat[0]}</a></td>
			<c:set var="i" value="${i + 1}" />
			
			<c:if test="${(i % tablewidth) == 0}">
				</tr>
			</c:if>
		</c:forEach>
	</table>
	<a href="/public/setcategory?cat=" >All Categories</a>
	</div>
</sf:form>