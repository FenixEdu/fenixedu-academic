<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>
<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" /> <br />

<html:form action="curricularCourseEnrolmentManager">
	<html:hidden property="step" value="0"/>
	<html:hidden property="method" value="chooseOptionalCourse" />
	<logic:iterate id="curricularCourse" name="infoEnrolmentContext" property="optionalInfoCurricularCoursesToChooseFromDegree" indexId="index">
		<html:radio property="optionalCurricularCourse" value="<%= pageContext.findAttribute("index").toString() %>"/>
		<bean:write name="curricularCourse" property="name"/><br/>
	</logic:iterate>
	<html:submit value="Continuar"/>
</html:form>