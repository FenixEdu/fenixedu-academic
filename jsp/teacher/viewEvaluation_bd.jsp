<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />


<%-- <logic:present name="<%= SessionConstants.INFO_EVALUATION %>" > --%>

<logic:present name="siteView">
<bean:define id="evaluation" name="siteView" property="component"/>

<h2><bean:message key="title.evaluation"/></h2>
<table>
	<tr>
		<td> 
			<bean:write name="evaluation" property="evaluationElements" filter="false"/>
	 	</td>
	</tr>
</table>
<br />
<logic:notEmpty name="evaluation" property="evaluationElementsEn">	
<h2><bean:message key="title.evaluation.eng"/></h2>
<table>	
	<tr>
		<td> 
			<bean:write name="evaluation" property="evaluationElementsEn" filter="false"/>
	 	</td>
	</tr>
</table>
<br/>	
</logic:notEmpty>
<div class="gen-button">
	<html:link page="<%= "/editEvaluation.do?method=prepareEditEvaluation&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="button.edit"/>
	</html:link>
</div>
</logic:present>