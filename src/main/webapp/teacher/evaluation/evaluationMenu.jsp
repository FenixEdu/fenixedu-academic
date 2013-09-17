<%@page import="net.sourceforge.fenixedu.domain.ExecutionCourse"%>
<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<ul>
	<logic:present name="executionCourse">
		<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>
	</logic:present>
	<logic:notPresent name="executionCourse">
		<bean:define id="executionCourseID" name="executionCourseID" type="java.lang.String"/>
		<%
			final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
			request.setAttribute("executionCourse", executionCourse);
		%>
	</logic:notPresent>
	
	<bean:define id="professorship" name="executionCourse" property="professorshipForCurrentUser"/>
	<bean:define id="professorshipPermissions" name="professorship" property="permissions"/>
	
	
	<li><html:link action="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourseID"><bean:message key="label.back"/></html:link></li>

	<li class="navheader"><bean:message key="link.evaluation"/></li>
	
	<logic:equal name="professorshipPermissions" property="evaluationSpecific" value="true">
	<li><html:link page="/evaluation/adHocEvaluationIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.adHocEvaluations"/></html:link></li>
	</logic:equal>

	<logic:equal name="professorshipPermissions" property="evaluationWorksheets" value="true">	
	<li><html:link page="/evaluation/onlineTestsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.onlineTests"/></html:link></li>
	</logic:equal>
	
	<logic:equal name="professorshipPermissions" property="evaluationProject" value="true">
	<li><html:link page="/evaluation/projectsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.projects"/></html:link></li>
	</logic:equal>
	
	<logic:equal name="professorshipPermissions" property="evaluationTests" value="true">
	<li><html:link page="/evaluation/writtenTestsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.writtenTests"/></html:link></li>
	</logic:equal>
	
	<logic:equal name="professorshipPermissions" property="evaluationExams" value="true">
	<li><html:link page="/evaluation/examsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.exams"/></html:link></li>
	</logic:equal>
	
	<logic:present role="TEACHER">
		<logic:equal name="professorshipPermissions" property="evaluationFinal" value="true">
		<li><html:link page="/evaluation/finalEvaluationIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.finalEvaluation"/></html:link><br/></li>
		</logic:equal>
	</logic:present>
</ul>
