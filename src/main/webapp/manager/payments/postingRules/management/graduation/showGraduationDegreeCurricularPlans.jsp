<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- showGraduationDegreeCurricularPlans.jsp -->

<h2><bean:message key="label.manager.degreeCurricularPlan"
	bundle="MANAGER_RESOURCES" /></h2>
	
<br/>

<fr:view name="degreeCurricularPlans"
	schema="DegreeCurricularPlan.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:property name="sortBy" value="degree.name=asc" />
		
		<fr:property name="linkFormat(showPostingRules)"
			value="/postingRules.do?method=showGraduationDegreeCurricularPlanPostingRules&amp;degreeCurricularPlanId=${idInternal}" />
		<fr:property name="key(showPostingRules)" value="label.payments.postingRules" />
		<fr:property name="bundle(showPostingRules)" value="MANAGER_RESOURCES" />
		
		
		<fr:property name="linkFormat(showPaymentPlans)"
			value="/postingRules.do?method=showPaymentPlans&amp;degreeCurricularPlanId=${idInternal}" />
		<fr:property name="key(showPaymentPlans)" value="label.payments.postingRules.gratuityPaymentPlans" />
		<fr:property name="bundle(showPaymentPlans)" value="MANAGER_RESOURCES" />
					
	</fr:layout>
</fr:view>
	
<html:link
	action="/postingRules.do?method=prepareCreatePaymentPlan">
	<bean:message key="label.payments.postingRules.createGratuityPaymentPlan" bundle="MANAGER_RESOURCES" />
</html:link>
<br/><br/>
<html:link
	action="/postingRules.do?method=prepareCreateGraduationGratuityPR">
	<bean:message key="label.payments.postingRules.createGraduationGratuityPostingRule" bundle="MANAGER_RESOURCES" />
</html:link>
<br/><br/>
<html:link
	action="/postingRules.do?method=prepareCreateGraduationStandaloneEnrolmentGratuityPR">
	<bean:message key="label.payments.postingRules.createGraduationStandaloneEnrolmentGratuityPostingRule" bundle="MANAGER_RESOURCES" />
</html:link>
<br/><br/>
<html:link
	action="/postingRules.do?method=prepare">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
