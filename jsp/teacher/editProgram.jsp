<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>	
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" scope="session">
	<bean:message key="message.unavailableObjectives" />	
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" scope="session">
	<bean:message key="label.program" />	
	<bean:write name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program>
	</bean:write>
	<html:submit property="method" value="acessObjectives" titleKey="button.edit">
	</html:submit>
</logic:present>