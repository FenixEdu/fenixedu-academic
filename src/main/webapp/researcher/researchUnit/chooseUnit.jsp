<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h3><bean:message key="label.select.unit" bundle="DEPARTMENT_MEMBER_RESOURCES"></bean:message></h3>
<br />

<c:if test="${empty units}">
	<bean:message key="message.error.notAuthorizedContents" bundle="APPLICATION_RESOURCES" />
</c:if>

<ul>
	<c:forEach var="unit" items="${units}">
		<blockquote><a href="?unitId=${unit.externalId}">${unit.name} (${unit.acronym})</a></blockquote>
	</c:forEach>
</ul>
