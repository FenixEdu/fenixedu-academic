<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">

	<h2><bean:message key="label.prices" /></h2>
	<hr/>
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
		<tr>
			<td colspan="2" align="left">
				<h3><bean:message name="insurancePostingRule" property="eventType.qualifiedName" bundle="ENUMERATION_RESOURCES" /></h3>
				<hr />
			</td>
		</tr>
		<tr>
			<td>
				<bean:define id="insurancePostingRuleClassName" name="insurancePostingRule" property="class.simpleName" />
				<fr:view name="insurancePostingRule" schema="<%= insurancePostingRuleClassName + ".view-student" %>" layout="tabular" />
			</td>
		</tr>

		<logic:iterate id="entry" name="postingRulesByAdminOfficeType">
			<bean:define id="adminOfficeType" name="entry" property="key" />
			<bean:define id="postingRules" name="entry" property="value" />
			
			<tr>
				<th colspan="2" align="left">
					<br/>
					<h2><em><bean:message name="adminOfficeType" property="qualifiedName" bundle="ENUMERATION_RESOURCES" /></em></h2>
				</th>
			
			</tr>
			
			<logic:iterate id="postingRule" name="postingRules">
			<tr>
				<td colspan="2" align="left">
					<h3><bean:message name="postingRule" property="eventType.qualifiedName" bundle="ENUMERATION_RESOURCES" /></h3>
					<hr />
				</td>
			</tr>

			<tr>
				<td>
					<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" />
					<fr:view name="postingRule" schema="<%=postingRuleClassName + ".view-student" %>" layout="tabular" />
				</td>
			</tr>
			</logic:iterate>	
			
		</logic:iterate>
		
	</table>
	
</logic:present>