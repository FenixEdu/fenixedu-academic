<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.pricesManagement" /></h2>

	<hr />
	<br />

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
				<h3><bean:message name="postingRule" property="eventType.qualifiedName"
					bundle="ENUMERATION_RESOURCES" /></h3>
				<hr />
				</td>
			</tr>

			<tr>
				<td>
					<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" /> 
					
					<fr:view name="postingRule"	schema="<%=postingRuleClassName + ".view" %>">
						<fr:layout name="tabular">
						</fr:layout>
					</fr:view>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<bean:define id="postingRuleId" name="postingRule" property="idInternal"/>
					<html:link page="<%= "/pricesManagement.do?method=prepareEditPrice&postingRuleId=" + postingRuleId %>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="link.pricesManagement.edit"/></html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>

</logic:present>
