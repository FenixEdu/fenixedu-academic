<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<logic:present role="MANAGER">
	<h2><bean:message key="label.payments.postingRules.editPostingRule" bundle="MANAGER_RESOURCES" /></h2>

	<br/>

	<bean:define id="className" name="postingRule" property="class.simpleName" />
	<bean:define id="postingRuleId" name="postingRule" property="externalId" />

	<bean:define id="phdProgramId" name="phdProgram" property="externalId" />

	<fr:edit id="postingRule" name="postingRule" schema="<%=className + ".edit"%>">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thmiddle thright thlight mtop05" />
		</fr:layout>
		<fr:destination name="success"
			path="<%= "/phdPostingRules.do?method=showPhdProgramPostingRules&phdProgramId=" + phdProgramId %>" />
		<fr:destination name="invalid"
			path="<%= String.format("/phdPostingRules.do?method=editPhdProgramPostingRuleInvalid&postingRuleId=%s&amp;phdProgramId=%s", postingRuleId, phdProgramId) %>" />
		<fr:destination name="cancel"
			path="<%= "/phdPostingRules.do?method=showPhdProgramPostingRules&phdProgramId=" + phdProgramId %>" />
	</fr:edit>

</logic:present>
