<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="footer">
	
	<h1></h1>
	<c:if test="${objectList.getPageCount() > 1}">
		<div class="paging">
			<c:if test="${objectList.isFirstPage()==false}">
				<a href="${pageContext.request.contextPath}${pagelink}?page=prev"><img alt="[Prev]"
					src="<c:url value='/images/left-arrow.png'/>"></a>
			</c:if>
			<c:forEach begin="1" end="${objectList.getPageCount()}" var="i">

				<c:choose>
					<c:when test="${(i-1)!= objectList.getPage()}">
						<a href="${pageContext.request.contextPath}${pagelink}?page=${i-1}"><span class="paging"><c:out
									value="${i}" /></span></a>
					</c:when>
					<c:otherwise>
						<span class="paging"><c:out value="${i}" /></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<%--For displaying Next link --%>
			<c:if test="${objectList.isLastPage()==false}">
				<a href="${pageContext.request.contextPath}${pagelink}?page=next"><img alt="[Next]"
					src="<c:url value='/images/right-arrow.png'/>"></a>
			</c:if>
		</div>
	</c:if>

</div>