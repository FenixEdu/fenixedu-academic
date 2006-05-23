<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<jsp:include page="commons/commonNavLocalHeader.jsp"></jsp:include>

<li>
	<html:link module="/manager" page="<%= "/readDegree.do?degreeId=" + request.getParameter("degreeId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegree" />
	</html:link>
</li>

<jsp:include page="commons/commonNavLocalManager.jsp"></jsp:include>
