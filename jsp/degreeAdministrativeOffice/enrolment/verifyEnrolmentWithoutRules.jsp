<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>

<br/>
<b><bean:message key="label.curricular.courses.choosen"/></b>
<html:form action="/curricularCourseEnrolmentWithoutRulesManager.do">
	<html:hidden property="method" value="accept"/>
	<ul>
		<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="actualEnrolment" indexId="index">
			<li><bean:write name="curricularScope" property="infoCurricularCourse.name"/></li>
		</logic:iterate>
	</ul>	
	<html:submit styleClass="inputbutton">	
		<bean:message key="button.finalize.enrolment"/>
	</html:submit>
	<html:cancel styleClass="inputbutton">
		<bean:message key="button.change.enrolment"/>	
	</html:cancel>		
</html:form>
