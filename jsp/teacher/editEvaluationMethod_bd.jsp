<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<span class="error"><html:errors /></span> 
<logic:present name="siteView">
	<bean:define id="evaluation" name="siteView" property="component"/>
	<h2><bean:message key="label.evaluation"/></h2> 
	<html:form action="/editEvaluationMethod">
		<html:hidden property="page" value="1"/>
		<table>	
			<tr>
				<td><strong><bean:message key="label.evaluation"/></strong></td>
			</tr>
			<tr>
				<td><html:textarea  property="evaluationElements" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.evaluation.eng"/></strong></td>
			</tr>	
			<tr>
				<td><html:textarea  property="evaluationElementsEn" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td>
					<html:submit styleClass="inputbutton">
						<bean:message key="button.save"/>
					</html:submit>
					<html:reset  styleClass="inputbutton">
						<bean:message key="label.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>   
		<html:hidden property="method" value="editEvaluationMethod" />
		<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	</html:form>
</logic:present>