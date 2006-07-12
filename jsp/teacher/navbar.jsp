<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<logic:present name="siteView">
	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="executionCourse" name="component" property="executionCourse" />
	<bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
     
      
	<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
	<ul>
		<li><html:link page="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.home"/></html:link></li>
		<li><html:link page="/copySiteExecutionCourse.do?method=prepareChooseExecutionPeriod" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.copySiteExecutionCourse"/></html:link></li>
		<br/>
		<li><html:link page="/alternativeSite.do?method=prepareCustomizationOptions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.personalizationOptions"/></html:link></li>
		<li><html:link page="/announcementManagementAction.do?method=showAnnouncements" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.announcements"/></html:link></li>
		<li><html:link page="/showSummaries.do?method=showSummaries&amp;page=0" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.summaries"/></html:link></li>
		<li><html:link page="/sectionViewer.do?method=sectionsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.sectionsManagement"/></html:link></li>
		<br/>
		<li><html:link page="/objectivesManagerDA.do?method=viewObjectives" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.objectives"/></html:link></li>
		<li><html:link page="/programManagerDA.do?method=viewProgram" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.program"/></html:link></li>
		<li><html:link page="/viewEvaluationMethod.do?method=viewEvaluationMethod" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.evaluationMethod"/></html:link></li>
		<li><html:link page="/bibliographicReferenceManager.do?method=viewBibliographicReference" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.bibliography"/></html:link></li>
		<br/>
		<li><html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.teachers"/></html:link></li>
		<li><html:link page="/studentsByCurricularCourse.do?method=prepare" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.students"/></html:link></li>
		<%-- <li><html:link page="/readCurricularCourseList.do?method=read" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.students"/></html:link></li> --%>
		<%-- <li><html:link page="/viewEvaluation.do?method=viewEvaluation" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.evaluation"/></html:link></li> --%>
		<li><html:link page="/evaluation/evaluationIndex.faces" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.evaluation"/></html:link></li>
		<li><html:link page="/testsManagement.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.testsManagement"/></html:link></li>
		<li><html:link page="/viewExecutionCourseProjects.do?method=prepareViewExecutionCourseProjects" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.groupsManagement"/></html:link></li>
		<br/>
		<li>
			<html:link page='<%= "/weeklyWorkLoad.do?method=prepare&amp;executionCourseID=" + executionCourseID.toString() %>'>
				<bean:message key="link.weekly.work.load"/>
			</html:link>
		</li>
		<br/>
		<li><html:link page="/viewCourseInformation.do" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.courseInformationManagement"/></html:link></li>
		<%-- <logic:notEqual name="executionCourse" property="infoExecutionPeriod.state.stateCode" value="C"> --%>
		<li><html:link page="/teachingReport.do?method=prepareEdit&amp;page=0" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal"><bean:message key="link.teachingReportManagement"/></html:link></li>
		<%-- </logic:notEqual> --%>
		<br/>

	</ul>
</logic:present>