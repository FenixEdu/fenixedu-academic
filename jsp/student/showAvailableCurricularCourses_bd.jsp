<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />

<bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name" /> <br />

<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled">
	<bean:write name="curricularScope" property="infoCurricularCourse.name"/> <br/>
</logic:iterate>