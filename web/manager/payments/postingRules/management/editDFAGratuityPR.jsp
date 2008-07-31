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

	<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
		property="idInternal" />
	<bean:define id="postingRuleId" name="postingRuleEditor"
		property="dfaGratuityPR.idInternal" />

	<fr:hasMessages type="conversion" for="postingRuleEditor">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	<fr:edit id="postingRuleEditor" name="postingRuleEditor" schema="DFAGratuityPREditor.edit" 
	action="<%= "/postingRules.do?method=editDFAGratuityPR&degreeCurricularPlanId=" + degreeCurricularPlanId %>">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thmiddle thright thlight mtop05" />
		</fr:layout>
		<fr:destination name="invalid"
			path="<%="/postingRules.do?method=prepareEditDFAGratuityPRInvalid&postingRuleId=" + postingRuleId + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
		<fr:destination name="cancel"
			path="<%="/postingRules.do?method=showPostingRulesForDFADegreeCurricularPlan&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
	</fr:edit>

</logic:present>
