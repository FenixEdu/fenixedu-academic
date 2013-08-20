<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.payments.postingRules.postingRuleDetails" bundle="MANAGER_RESOURCES" /></h2>

<br/>

<bean:define id="className" name="postingRule" property="class.simpleName" />
<fr:view name="postingRule" schema="<%= className + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
	</fr:layout>
</fr:view>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="externalId" />
<html:link action="<%="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
