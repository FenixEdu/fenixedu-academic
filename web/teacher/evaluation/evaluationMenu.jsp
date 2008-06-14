<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<ul>
	<logic:present name="executionCourse">
		<bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
	</logic:present>
	<logic:notPresent name="executionCourse">
		<bean:define id="executionCourseID" name="executionCourseID" />
	</logic:notPresent>

	<li><html:link action="/teacherAdministrationViewer.do?method=instructions" paramId="objectCode" paramName="executionCourseID"><bean:message key="label.back"/></html:link></li>

	<li class="navheader"><bean:message key="link.evaluation"/></li>
	<li><html:link page="/evaluation/adHocEvaluationIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.adHocEvaluations"/></html:link></li>	
	<li><html:link page="/evaluation/onlineTestsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.onlineTests"/></html:link></li>
	<li><html:link page="/evaluation/projectsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.projects"/></html:link></li>
	<li><html:link page="/evaluation/writtenTestsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.writtenTests"/></html:link></li>
	<li><html:link page="/evaluation/examsIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.exams"/></html:link></li>
	<li><html:link page="/evaluation/finalEvaluationIndex.faces" paramId="executionCourseID" paramName="executionCourseID"><bean:message key="link.finalEvaluation"/></html:link><br/></li>
</ul>
