<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" >
<table>
<tr>
	<td>
		<h2><bean:message key="label.generalObjectives" />	</h2>
	</td>
</tr>
<tr>
	<td>
		<bean:define id="generalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectives">
		</bean:define> 
		<bean:write name="generalObjectives" filter="false"/>
	</td>
</tr>
<tr>
	<td>	
		<h2><bean:message key="label.operacionalObjectives" /></h2>
	</td>
</tr>
<tr>
	<td>
		<bean:define id="operacionalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectives">
		</bean:define> 
		<bean:write name="operacionalObjectives" filter="false"/>
	</td>
</tr>
</table>
</logic:present>
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">
<h2><bean:message key="message.objectives.not.available"/>
</h2>
</logic:notPresent>