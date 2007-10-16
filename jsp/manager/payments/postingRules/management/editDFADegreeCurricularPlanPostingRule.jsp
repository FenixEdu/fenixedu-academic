<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<h2><bean:message
		key="label.payments.postingRules.editPostingRule"
		bundle="MANAGER_RESOURCES" /></h2>

	<br />

	<bean:define id="className" name="postingRule"
		property="class.simpleName" />
	<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
		property="idInternal" />
	<bean:define id="postingRuleId" name="postingRule"
		property="idInternal" />

	<fr:edit name="postingRule" schema="<%=className + ".edit"%>">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thmiddle thright thlight mtop05" />
		</fr:layout>
		<fr:destination name="success"
			path="<%="/postingRules.do?method=showPostingRulesForDFADegreeCurricularPlan&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
		<fr:destination name="invalid"
			path="<%="/postingRules.do?method=prepareEditDFADegreeCurricularPlanPostingRuleInvalid&postingRuleId=" + postingRuleId + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
		<fr:destination name="cancel"
			path="<%="/postingRules.do?method=showPostingRulesForDFADegreeCurricularPlan&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
	</fr:edit>

</logic:present>
