<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<li>
	<html:link module="/manager" page="<%= "/readDegree.do?degreeId=" + request.getParameter("degreeId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegree" />
	</html:link>
</li>
<li>
	<html:link module="/manager" page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegreeCurricularPlan" />
	</html:link>
</li>
<br/>

<jsp:include page="commons/commonNavLocalManager.jsp"></jsp:include>
