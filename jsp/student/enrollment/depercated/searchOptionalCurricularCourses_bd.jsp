<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:define id="infoDegreesList" name="infoEnrolmentContext" property="infoDegreesForOptionalCurricularCourses"/>

<html:form action="/curricularCourseEnrolmentManager.do">
	<html:hidden property="method" value="showOptionalCurricularCourses"/>

	<html:select property="infoDegree" size="1">
		<html:options collection="infoDegreesList" property="idInternal" labelProperty="nome"/>
	</html:select>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.continue.enrolment"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.cancel"/>
	</html:cancel>
</html:form>
