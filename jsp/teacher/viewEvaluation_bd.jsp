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
		<td><strong><bean:message key="label.evaluation" /></strong>
		</td>
	</tr>
	<tr>
		<td><bean:define id="evaluationElements" name="<%= SessionConstants.INFO_EVALUATION %>" property="evaluationElements"></bean:define> 
			<bean:write name="evaluationElements" filter="false"/>
	 	</td>
	</tr>
</table>
<br />	

<html:link page="/viewEvaluation.do?method=prepareEditEvaluation">
(<bean:message key="button.edit"/>)
</html:link>
</logic:present>