<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" scope="session"/>
<bean:define id="removedCurriCularCourses" name="<%= SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY %>" scope="session"/>
<bean:size id="removedCurriCularCoursesSize" name="removedCurriCularCourses"/>
<bean:size id="infoOptionalCurricularCoursesEnrolmentsSize" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments"/>
<bean:size id="actualEnrolmentSize" name="infoEnrolmentContext" property="actualEnrolment"/>

<br/>
<html:form action="/curricularCourseEnrolmentWithRulesManager.do">
	<html:hidden property="method" value="accept"/>
	<html:hidden property="step" value="1"/>	

	<logic:notEqual name="actualEnrolmentSize" value="0">
		<b><bean:message key="label.curricular.courses.choosen" bundle="STUDENT_RESOURCES"/></b>
		<ul>
	</logic:notEqual>
	<logic:equal name="actualEnrolmentSize" value="0">
		<logic:notEqual name="infoOptionalCurricularCoursesEnrolmentsSize" value="0">
			<b><bean:message key="label.curricular.courses.choosen" bundle="STUDENT_RESOURCES"/></b>
			<ul>
		</logic:notEqual>
	</logic:equal>

	<logic:notEqual name="actualEnrolmentSize" value="0">
		<logic:iterate id="curricularCourse" name="infoEnrolmentContext" property="actualEnrolment">
			<li><bean:write name="curricularCourse" property="name"/></li>
		</logic:iterate>
	</logic:notEqual>

	<logic:notEqual name="infoOptionalCurricularCoursesEnrolmentsSize" value="0">
		<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
			<li>
				<bean:write name="optionalEnrolment" property="infoCurricularCourse.name"/> - <bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
			</li>
		</logic:iterate>
	</logic:notEqual>

	<logic:notEqual name="actualEnrolmentSize" value="0">
		</ul>
	</logic:notEqual>
	<logic:equal name="actualEnrolmentSize" value="0">
		<logic:notEqual name="infoOptionalCurricularCoursesEnrolmentsSize" value="0">
			<ul>
		</logic:notEqual>
	</logic:equal>

	<logic:present name="removedCurriCularCourses">
		<logic:notEqual name="removedCurriCularCoursesSize" value="0">
			<b><bean:message key="label.curricular.course.to.remove" bundle="STUDENT_RESOURCES"/></b>
			<ul>
				<logic:iterate id="curricularCourse" name="removedCurriCularCourses">
					<li><bean:write name="curricularCourse" property="name"/></li>
				</logic:iterate>
			</ul>	
		</logic:notEqual>
	</logic:present>

	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment" bundle="STUDENT_RESOURCES"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment" bundle="STUDENT_RESOURCES"/>	
	</html:cancel>		
</html:form>
