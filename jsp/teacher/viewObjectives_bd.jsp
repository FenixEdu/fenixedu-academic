<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">
	
<jsp:include page="curriculumForm.jsp"/>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" >
<h3><bean:message key="title.objectives"/></h3>
<table>
<tr>
	<td><h2><bean:message key="label.generalObjectives" />	</h2>
	</td>
	<td>
	<bean:define id="generalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectives">
	</bean:define> 
	<bean:write name="generalObjectives" />
	</td>
</tr>
<tr>
	<td>	
	<h2><bean:message key="label.operacionalObjectives" /></h2>
	</td><td><bean:define id="operacionalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectives">
	</bean:define> 
	<bean:write name="operacionalObjectives" />
	</td>
</tr>
<tr>	
	<td>	
	<html:hidden property="method" value="prepareEditObjectives"/> 	
	<html:link page="/objectivesManagerDA.do?method=prepareEditObjectives">
		<bean:message key="button.edit"/>
	</html:link>	 
	
	</td>
</tr>	
</table>
</logic:present>