<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
<logic:present name="evaluationElements" >
	<table>
		<tr>
			<td>
				<h2><bean:message key="label.evaluation" /></h2>	
			</td>
		</tr>
		<tr>
			<td>
				<bean:write name="evaluationElements" filter="false"/>
			</td>
		</tr>
	</table>
</logic:present>
<logic:notPresent name="evaluationElements">
<h2><bean:message key="message.evaluation.not.available"/>
</h2>
</logic:notPresent>