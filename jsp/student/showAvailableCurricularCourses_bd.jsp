<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:define id="actualEnrolment" name="infoEnrolmentContext" property="actualEnrolment"/>

<h5>
<html:errors />
</h5>
<br />
<br />

<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" /> <br />

<%-- Verificar se isto funciona quando está empty --%>
<logic:present  name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
	Disciplinas a que se encontra automaticaticamente inscrito <br />
	<logic:iterate id="curricularCourseScope" name="infoEnrolmentContext" property="infoCurricularCoursesScopesAutomaticalyEnroled">
		<bean:write name="curricularCourseScope" property="infoCurricularCourse.name"/> <br />
	</logic:iterate>
</logic:present>
<br />

Disciplinas para inscrição <br />
<html:form action="curricularCourseEnrolmentManager">
	<html:hidden property="step" value="0"/>
	<html:hidden property="method" value="verifyEnrolment" />
	<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">
		<html:multibox property="curricularCourses" value="<%= pageContext.findAttribute("index").toString() %>"/>
		<bean:write name="curricularScope" property="infoCurricularCourse.name"/><br/>
	</logic:iterate>
	<html:submit value="Continuar"/>
</html:form>

Actual Enrolment <br />
<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
	<bean:write name="curricularScope" property="infoCurricularCourse.name"/><br/>
</logic:iterate>
