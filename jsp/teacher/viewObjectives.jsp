<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>	


	<bean:message key="label.generalObjectives" />	
	<bean:define id="generalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectives">
	</bean:define> 
	<bean:write name="generalObjectives" />
	<bean:message key="label.operacionalObjectives" />
	<bean:define id="operacionalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectives">
	</bean:define> 
	<bean:write name="operacionalObjectives" />
	<html:submit property="method" value="prepareEditObjectives" titleKey="button.edit">
	</html:submit>

