<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<jsp:include page="/coordinator/context.jsp" />

<div class="container">
	<a href="${base}/degreeSiteManagement.do?method=subMenu&degreeCurricularPlanID=${degreeCurricularPlanID}" class="back-btn">
		« <bean:message key="label.back" />
	</a>

	<jsp:include page="${coordinator$actual$page}" />
</div>