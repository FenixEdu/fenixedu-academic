<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
		<table width="100%">
			<tr>
				<td class="infoop">
					<bean:message key="label.objectives.explanation" />
				</td>
			</tr>
		</table>	
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">
<jsp:include page="curriculumForm.jsp"/>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" >
<h2><bean:message key="title.objectives"/></h2>
<table>
	<tr>
		<td><strong><bean:message key="label.generalObjectives" /></strong>
		</td>
	</tr>
	<tr>
		<td><bean:define id="generalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectives"></bean:define> 
			<bean:write name="generalObjectives" filter="false"/>
	 	</td>
	</tr>
</table>
<br />
<table>
	<tr>
		<td><strong><bean:message key="label.operacionalObjectives" /></strong>
		</td>
	</tr>
	<tr>
		<td><bean:define id="operacionalObjectives" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectives"></bean:define>
			<bean:write name="operacionalObjectives" filter="false"/>
		</td>
	</tr>
</table>
<br />	
<html:hidden property="method" value="prepareEditObjectives"/> 	
<div class="gen-button"><html:link page="/objectivesManagerDA.do?method=prepareEditObjectives">
<bean:message key="button.edit"/>
</html:link></div>
</logic:present>