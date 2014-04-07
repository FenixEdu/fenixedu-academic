<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty actual$content}">
	${actual$content.name} -
</c:if>
${actual$site.executionCourse.name} - 