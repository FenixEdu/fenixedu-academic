<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<h2><bean:message
		key="label.payments.postingRules.degreeCurricularPlan.rules"
		bundle="MANAGER_RESOURCES" /> <bean:write name="degreeCurricularPlan" property="degree.name"/> - <bean:write name="degreeCurricularPlan" property="name"/></h2>

	<br />

	<bean:define id="className" name="degreeCurricularPlan"
		property="class.simpleName" />
	<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
		property="idInternal" />

	<fr:view name="degreeCurricularPlan"
		property="serviceAgreementTemplate.postingRules"
		schema="PostingRule.view-with-eventType">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			<fr:property name="sortBy" value="eventType=asc,startDate=desc" />

			<fr:property name="linkFormat(details)"
				value="<%="/postingRules.do?method=viewDFAPostingRuleDetails&amp;postingRuleId=${idInternal}&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
			<fr:property name="key(details)" value="label.details" />
			<fr:property name="bundle(details)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(details)" value="0" />

			<fr:property name="linkFormat(edit)"
				value="<%="/postingRules.do?method=prepareEditDFADegreeCurricularPlanPostingRule&amp;postingRuleId=${idInternal}&amp;degreeCurricularPlanId=" + degreeCurricularPlanId%>" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			<fr:property name="visibleIf(edit)" value="active" />
			<fr:property name="order(edit)" value="1" />
		</fr:layout>
	</fr:view>

	<br />

	<logic:equal name="allowCreateGratuityPR" value="true">
		<html:link
			action="<%="/postingRules.do?method=prepareCreateDFAGratuityPR&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>">
			<bean:message key="label.payments.postingRules.createDFAGratuityPR"
				bundle="MANAGER_RESOURCES" />
		</html:link>
		<br />
		<br />
	</logic:equal>
	
	
	<html:link
		action="<%="/postingRules.do?method=chooseDFADegreeCurricularPlan"%>">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:link>

</logic:present>