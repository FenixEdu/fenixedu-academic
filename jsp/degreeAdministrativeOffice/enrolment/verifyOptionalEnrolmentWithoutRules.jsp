<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>

<br/>
<b><bean:message key="label.curricular.courses.choosen" bundle="STUDENT_RESOURCES"/></b>
<html:form action="/optionalCurricularCourseEnrolmentWithoutRulesManager.do">
	<html:hidden property="method" value="accept"/>
	<ul>
		<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
			<li><bean:write name="curricularScope" property="infoCurricularCourse.name"/></li>
		</logic:iterate>
		<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
			<li>
				<bean:write name="optionalEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/> - <bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
			</li>
		</logic:iterate>
	</ul>	
	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment" bundle="STUDENT_RESOURCES"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment" bundle="STUDENT_RESOURCES"/>	
	</html:cancel>		
</html:form>
