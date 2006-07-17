<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>



<h2><bean:message key="label.masterDegree.administrativeOffice.pricesManagement" /></h2>

<hr><br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<table>
<logic:iterate id="postingRule" name="postingRules">
	<tr>
		<td colspan="2" align="left">
			<h3><bean:message name="postingRule" property="eventType.name" bundle="ENUMERATION_RESOURCES"/></h3>
			<hr/>
		</td>
	</tr>
	<tr>
		<td>
			<strong><bean:message key="label.masterDegree.administrativeOffice.pricesManagement.price"/></strong>
		</td>
		<td>
			<bean:write name="postingRule" property="baseAmount"/> <strong><bean:message key="label.currencySymbol"/></strong>
		</td>
	</tr>
	<logic:notEqual name="postingRule" property="amountPerUnit" value="0">
	<tr>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.pricesManagement.pricePerUnit"/></strong></td>
		<td><bean:write name="postingRule" property="amountPerUnit"/> <strong><bean:message key="label.currencySymbol"/></strong></td>
	</tr>
	</logic:notEqual>
	<tr>
		<td><strong><bean:message key="label.masterDegree.administrativeOffice.pricesManagement.pricePerPage"/></strong></td>
		<td><bean:write name="postingRule" property="amountPerPage"/> <strong><bean:message key="label.currencySymbol"/></strong></td>
	</tr>
	<tr>
		<td colspan="2">
			<bean:define id="postingRuleId" name="postingRule" property="idInternal"/>
			<html:link page="<%= "/pricesManagement.do?method=prepareEditPrice&postingRuleId=" + postingRuleId %>"><bean:message key="link.masterDegree.administrativeOffice.pricesManagement.edit"/></html:link>
		</td>
	</tr>
</logic:iterate>
</table>
