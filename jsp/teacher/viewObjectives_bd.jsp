<%@ page language="java" %>
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
<%--
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">
<jsp:include page="curriculumForm.jsp"/>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" > --%>
	
<logic:present name="siteView">
<bean:define id="objectives" name="siteView" property="component"/>

<h2><bean:message key="title.objectives"/></h2>
<table>
	<tr>
		<td><strong><bean:message key="label.generalObjectives" /></strong>
		</td>
	</tr>
	<tr>
		<td> 
			<bean:write name="objectives" property="generalObjectives" filter="false"/>
	 	</td>
	</tr>
	<logic:notEmpty name="objectives" property="generalObjectivesEn">
		<tr>
			<td><strong><bean:message key="label.generalObjectives.eng" /></strong>
			</td>
		</tr>
		<tr>
			<td>
				<bean:write name="objectives" property="generalObjectivesEn" filter="false"/>
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
		<td>
			<bean:write name="objectives" property="operacionalObjectives" filter="false"/>
		</td>
	</tr>
	<tr>
	</tr>
	<logic:notEmpty name="objectives" property="operacionalObjectivesEn">
	<tr>
		<td><strong><bean:message key="label.operacionalObjectives.eng" /></strong>
		</td>
	</tr>
	<br />
	<tr>
		<td>
			<bean:write name="objectives" property="operacionalObjectivesEn" filter="false"/>
		</td>
	</tr>
	</logic:notEmpty>
</table>
<br />	
<div class="gen-button">
	<html:link page="<%= "/objectivesManagerDA.do?method=prepareEditObjectives&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:present>