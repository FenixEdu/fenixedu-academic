<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<li>
	<html:link module="/manager" page="<%= "/readDegree.do?degreeId=" + request.getParameter("degreeId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegree" />
	</html:link>
</li>
<li>
	<html:link module="/manager" page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getAttribute("degreeId") + "&degreeCurricularPlanId=" + request.getAttribute("degreeCurricularPlanId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegreeCurricularPlan" />
	</html:link>
</li>
<li>
	<html:link module="/manager" page="<%= "/readCurricularCourse.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") %>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadCurricularCourse" />
	</html:link>
</li>
<br/>

<jsp:include page="commons/commonNavLocalManager.jsp"></jsp:include>
