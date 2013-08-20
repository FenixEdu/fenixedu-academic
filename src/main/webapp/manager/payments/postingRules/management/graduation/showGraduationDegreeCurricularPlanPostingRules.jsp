<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.degreeCurricularPlan.rulesFor"
	bundle="MANAGER_RESOURCES" /> <bean:write name="degreeCurricularPlan"
	property="degree.name" /> - <bean:write name="degreeCurricularPlan"
	property="name" /></h2>

<br />

<bean:define id="className" name="degreeCurricularPlan"
	property="class.simpleName" />
<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
	property="externalId" />
	
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:view name="degreeCurricularPlan"
	property="serviceAgreementTemplate.postingRules"
	schema="PostingRule.view.with.formula.description">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="sortBy" value="eventType=asc,startDate=desc" />		
		
		<fr:property name="linkFormat(delete)"
			value="<%="/postingRules.do?method=deleteGraduationDegreeCurricularPlanPostingRule&amp;postingRuleId=${externalId}&amp;degreeCurricularPlanId=" + degreeCurricularPlanId%>" />
		<fr:property name="key(delete)" value="label.delete" />
		<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
		<fr:property name="visibleIf(delete)" value="mostRecent" />
		<fr:property name="confirmationKey(delete)" value="label.payments.postingRules.confirmDeletePostingRule" />
		<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES"></fr:property>
	</fr:layout>
</fr:view>

<html:link
	action="<%="/postingRules.do?method=manageGraduationRules"%>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
