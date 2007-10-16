<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<h2><bean:message
		key="label.payments.postingRules.createDFAGratuityPR"
		bundle="MANAGER_RESOURCES" /></h2>

	<br />

	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<bean:define id="degreeCurricularPlanId"
		name="createDFAGratuityPostingRuleBean"
		property="degreeCurricularPlan.idInternal" />

	<fr:edit id="createDFAGratuityPostingRuleBean"
		name="createDFAGratuityPostingRuleBean"
		schema="CreateDFAGratuityPostingRuleBean.edit"
		action="/postingRules.do?method=createDFAGratuityPR">

		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thmiddle thright thlight mtop05" />
		</fr:layout>
		<fr:destination name="cancel"
			path="<%="/postingRules.do?method=showPostingRulesForDFADegreeCurricularPlan&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
	</fr:edit>

</logic:present>
