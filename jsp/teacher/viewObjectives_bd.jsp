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
	<tr>
	</tr>
	<logic:notEmpty name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectivesEn">
		<tr>
			<td><strong><bean:message key="label.generalObjectives.eng" /></strong>
			</td>
		</tr>
		<tr>
			<td><bean:define id="generalObjectivesEn" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectivesEn"></bean:define> 
				<bean:write name="generalObjectivesEn" filter="false"/>
		 	</td>
		</tr>
	</logic:notEmpty>

</table>
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
	<tr>
	</tr>
	<logic:notEmpty name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectivesEn">
	<tr>
		<td><strong><bean:message key="label.operacionalObjectives.eng" /></strong>
		</td>
	</tr>
	<br />
	<tr>
		<td><bean:define id="operacionalObjectivesEn" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectivesEn"></bean:define>
			<bean:write name="operacionalObjectivesEn" filter="false"/>
		</td>
	</tr>
	</logic:notEmpty>
</table>
<br />	
<html:hidden property="method" value="prepareEditObjectives"/> 	
<div class="gen-button"><html:link page="/objectivesManagerDA.do?method=prepareEditObjectives">
<bean:message key="button.edit"/>
</html:link></div>
</logic:present>