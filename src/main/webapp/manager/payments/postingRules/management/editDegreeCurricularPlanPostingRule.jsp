<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.payments.postingRules.editPostingRule" bundle="MANAGER_RESOURCES" /></h2>

<br/>

<bean:define id="className" name="postingRule"
	property="class.simpleName" />
<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
	property="externalId" />
<bean:define id="postingRuleId" name="postingRule"
	property="externalId" />

<fr:edit id="postingRule" name="postingRule" schema="<%=className + ".edit"%>">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
	</fr:layout>
	<fr:destination name="success"
		path="<%="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
	<fr:destination name="invalid"
		path="<%="/postingRules.do?method=prepareEditDegreeCurricularPlanPostingRuleInvalid&postingRuleId=" + postingRuleId + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
	<fr:destination name="cancel"
		path="<%="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
</fr:edit>
