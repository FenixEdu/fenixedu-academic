<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />

Actual Enrolment <br />
<html:form action="curricularCourseEnrolmentManager">
	<html:hidden property="method" value="accept" />
	<html:hidden property="step" value="1"/>	
	
	<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
		<bean:write name="curricularScope" property="infoCurricularCourse.name"/><br/>
	</logic:iterate>
	
	<logic:iterate id="optionalEnrolment" name="infoEnrolmentContext" property="infoOptionalCurricularCoursesEnrolments">
			<bean:write name="optionalEnrolment" property="infoCurricularCourse.name"/> - <bean:write name="optionalEnrolment" property="infoCurricularCourseForOption.name"/>
	</logic:iterate>
	
	<html:submit value="Finalizar"/>
	<html:cancel value="Cancelar"/>		
</html:form>

