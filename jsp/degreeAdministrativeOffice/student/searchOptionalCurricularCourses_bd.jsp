<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" />
<bean:define id="infoDegreesList" name="infoEnrolmentContext" property="infoDegreesForOptionalCurricularCourses"/>

	<html:form action="curricularCourseEnrolmentManager">
		<html:hidden property="method" value="showOptionalCurricularCourses" />

		<html:select property="infoDegree" size="1">
       		<html:options collection="infoDegree" property="nome" labelProperty="sigla"/>
       </html:select>
	   <html:submit styleClass="inputbutton">
			<bean:message key="button.continue.enrolment"/>
		</html:submit>
	</html:form>
