<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<logic:present name="siteView">
	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="executionCourse" name="component" property="executionCourse" />
	<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
	<ul>
	<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.home"/></html:link></li>
	</ul>
	<p>
	<ul>
	<li><html:link page="/alternativeSite.do?method=prepareCustomizationOptions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.personalizationOptions"/></html:link></li>
	<li><html:link page="/announcementManagementAction.do?method=showAnnouncements" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.announcements"/></html:link></li>
	<li><html:link page="/summariesManager.do?method=showSummaries" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.summaries"/></html:link></li>
	<li><html:link page="/sectionViewer.do?method=sectionsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.sectionsManagement"/></html:link></li>
	</ul>
	<p>
	<ul>
	<li><html:link page="/objectivesManagerDA.do?method=viewObjectives" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.objectives"/></html:link></li>
	<li><html:link page="/programManagerDA.do?method=viewProgram" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.program"/></html:link></li>
	<li><html:link page="/viewEvaluationMethod.do?method=viewEvaluationMethod" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.evaluationMethod"/></html:link></li>
	<li><html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.bibliography"/></html:link></li>
	</ul>
	<p>
	<ul>
	<li><html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.teachers"/></html:link></li>
	<li><html:link page="/studentsByCurricularCourse.do?method=readStudents" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.students"/></html:link></li>
	<%-- <li><html:link page="/readCurricularCourseList.do?method=read" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.students"/></html:link></li> --%>
	<%-- <li><html:link page="/viewEvaluation.do?method=viewEvaluation" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.evaluation"/></html:link></li> --%>
	<logic:equal name="userView" property="utilizador" value="D2543">
	<li><html:link page="/testsManagement.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.testsManagement"/></html:link></li>
	</logic:equal>
<li><html:link page="/viewExecutionCourseProjects.do?method=viewExecutionCourseProjects" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.groupsManagement"/></html:link></li>
	</ul>
	<p>
	<ul>
	<li><html:link href="<%= request.getContextPath()+"/publico/viewSiteExecutionCourse.do?method=firstPage" %>" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal" target="_blank">Ver Página da Disciplina</html:link>
	<li><html:link page="/courseInformation.do?method=prepareEdit&amp;page=0" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.courseInformationManagement"/></html:link></li>
	</ul>
</logic:present>