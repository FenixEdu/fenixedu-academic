<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>

<spring:url var="eventDetailsAbsoluteUrl" value="${eventDetailsUrl}"/>
<a href="${eventDetailsAbsoluteUrl}">
    <c:out value="${event.description}"/>
</a>