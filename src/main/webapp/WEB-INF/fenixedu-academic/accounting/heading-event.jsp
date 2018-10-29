<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>

<c:if test="${fn:containsIgnoreCase(requestScope['javax.servlet.forward.request_uri'], 'owner-accounting-events')}">
    <a href="<%= request.getContextPath() %>/owner-accounting-events/${event.externalId}/details">
        ${event.description}
    </a>
</c:if>
<c:if test="${not fn:containsIgnoreCase(requestScope['javax.servlet.forward.request_uri'], 'owner-accounting-events')}">
    <a href="<%= request.getContextPath() %>/accounting-management/${event.externalId}/details">
        ${event.description}
    </a>
</c:if>
