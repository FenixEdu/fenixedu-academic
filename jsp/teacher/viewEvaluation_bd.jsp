<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />
<logic:present name="<%= SessionConstants.INFO_EVALUATION %>" >
<h2><bean:message key="title.evaluation"/></h2>
<table>
	<tr>
		<td><bean:define id="evaluationElements" name="<%= SessionConstants.INFO_EVALUATION %>" property="evaluationElements"></bean:define> 
			<bean:write name="evaluationElements" filter="false"/>
	 	</td>
	</tr>
</table>
<br />
<logic:notEmpty name="<%= SessionConstants.INFO_EVALUATION %>" property="evaluationElementsEn">	
<h2><bean:message key="title.evaluation.eng"/></h2>
<table>	
	<tr>
		<td><bean:define id="evaluationElementsEn" name="<%= SessionConstants.INFO_EVALUATION %>" property="evaluationElementsEn"></bean:define> 
			<bean:write name="evaluationElementsEn" filter="false"/>
	 	</td>
	</tr>
</table>
<br/>	
</logic:notEmpty>
<div class="gen-button"><html:link page="/viewEvaluation.do?method=prepareEditEvaluation">
<bean:message key="button.edit"/>
</html:link></div>
</logic:present>