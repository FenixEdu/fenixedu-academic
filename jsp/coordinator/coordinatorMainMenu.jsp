<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>
<logic:present name="<%= SessionConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= SessionConstants.MASTER_DEGREE %>" scope="session"/>
	<logic:equal name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
		<p><strong>&raquo; <bean:message key="link.coordinator.candidate"/></html:link></strong>
		<ul>
	        <li><html:link page="/candidateOperation.do?method=getCandidates&action=visualize&page=0"><bean:message key="link.coordinator.visualizeCandidate" /></html:link></li>
	        <li><html:link page="/prepareCandidateApproval.do?method=chooseExecutionDegree&page=0"><bean:message key="link.coordinator.approveCandidates" /></html:link></li>
			<li><html:link page="/displayCandidateListToMakeStudyPlan.do?method=prepareSelectCandidates&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.makeStudyPlan" /></html:link></li>
		</ul></p>
		<p><strong>&raquo; <bean:message key="link.coordinator.student"/></strong>
		<ul>
			<li><html:link page="/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0"><bean:message key="link.coordinator.studentListByDegree" /></html:link><br></li>
			<li><html:link page="/studentListByDegree.do?method=getCurricularCourses&jspTitle=title.studentListByCourse&page=0"><bean:message key="link.studentListByCourse" /></html:link></li>
		</ul></p>	
	</logic:equal>
	<logic:notEqual name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso" value="<%= TipoCurso.MESTRADO_OBJ.toString() %>">
	<%--		<html:link forward="equivalenceForCoordinator"><bean:message key="link.coordinator.equivalence"/></html:link>
		<br/>--%>
		<html:link page="/executionCoursesInformation.do?method=prepareChoice"><bean:message key="link.coordinator.executionCoursesInformation"/></html:link>
		<br/>
	</logic:notEqual> 
	<br/>
</logic:present>
