<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<h2><bean:message key="label.manager.degreeCurricularPlan"
		bundle="MANAGER_RESOURCES" /></h2>
		
	<br/>

	<fr:view name="degreeCurricularPlans"
		schema="DegreeCurricularPlan.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			<fr:property name="sortBy" value="degree.name=asc" />
			<fr:property name="linkFormat(view)"
				value="/postingRules.do?method=showPostingRulesForDFADegreeCurricularPlan&amp;degreeCurricularPlanId=${idInternal}" />
			<fr:property name="key(view)" value="label.view" />
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
		</fr:layout>
	</fr:view>
		
	<html:link
		action="<%="/postingRules.do?method=prepare"%>">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:link>
	
</logic:present>
