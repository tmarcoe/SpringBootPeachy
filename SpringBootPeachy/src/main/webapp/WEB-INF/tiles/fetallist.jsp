<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="/WEB-INF/tld/security.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<sf:form modelAttribute="objectList">

	<table class="tableview tableborder tableshadow">

		<tr>
			<th>File Name</th>
			<th>Date Modified</th>
			<th>Status</th>
			<th>Type</th>
			<th>Version</th>
			<th>Comments</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
		<c:forEach var="item" items="${objectList.pageList}">
			<tr>
				<td>${item.file_name}</td>
				<td><fmt:formatDate value="${item.modified}" /></td>
				<td>${item.status}</td>
				<td>${item.type}</td>
				<td>${item.version}</td>
				<td>${item.comments}</td>
				<td><button type="button"
						onclick="window.location.href = '/admin/edittransaction?id=${item.id}'">Edit</button></td>
				<td>&nbsp; <sec:hasRole role="ADMIN">
						<c:choose>
							<c:when test="${item.status == 'PRODUCTION'}">
							&nbsp;
						</c:when>
							<c:otherwise>
								<button type="button"
									onclick="window.location.href = '/admin/postToProd?id=${item.id}'">Post
									To Production</button>
							</c:otherwise>
						</c:choose>
					</sec:hasRole>
				</td>
			</tr>
		</c:forEach>
	</table>
</sf:form>