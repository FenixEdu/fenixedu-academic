<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

 <!-- showPaymentPlans.jsp -->

<h2><bean:message key="label.payments.postingRules.gratuityPaymentPlans"
	bundle="MANAGER_RESOURCES" /></h2>
	
<br/>


<fr:view name="degreeCurricularPlan"
	schema="DegreeCurricularPlan.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="linkFormat(view)"
			value="/postingRules.do?method=showPaymentPlans&amp;degreeCurricularPlanId=${externalId}" />
		<fr:property name="key(view)" value="label.view" />
		<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />
	</fr:layout>
</fr:view>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="externalId" />

<html:form action="<%="/postingRules.do?method=changeExecutionYearForPaymentPlans&degreeCurricularPlanId=" + degreeCurricularPlanId  %>">
	<html:select property="executionYearId" onchange="this.form.submit();">
		<html:options collection="executionYears" property="externalId" labelProperty="year"/>
	</html:select>
</html:form>

<br/>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<br/>

<logic:empty name="paymentPlans">
	<bean:message  key="label.payments.postingRules.noGratuityPaymentPlans" bundle="MANAGER_RESOURCES"/>
	<br/>
</logic:empty>

<logic:iterate id="paymentPlan" name="paymentPlans">
	<bean:define id="paymentPlanId" name="paymentPlan" property="externalId" />
	
	<strong>
		<bean:write name="paymentPlan" property="description"/>
		<logic:equal name="paymentPlan" property="default" value="true">
			(<bean:message  key="label.main" bundle="APPLICATION_RESOURCES"/>)
		</logic:equal>
	</strong>
	<fr:view name="paymentPlan" property="installmentsSortedByEndDate" schema="Installment.view-description-and-amount">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			<fr:link name="edit" link="<%= "/postingRules.do?method=prepareEditInstallment&amp;installmentId=${externalId}&amp;paymentPlanId=" + paymentPlanId %>" label="link.edit,APPLICATION_RESOURCES" />
		</fr:layout>
	</fr:view>
	<bean:define id="executionYearId" name="paymentPlan" property="executionYear.externalId" />
	<bean:define id="paymentPlanId" name="paymentPlan" property="externalId" />
	<html:link
		action="<%="/postingRules.do?method=deletePaymentPlan&amp;executionYearId=" + executionYearId + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId + "&amp;paymentPlanId=" + paymentPlanId.toString()  %>">
		<bean:message key="label.delete" bundle="APPLICATION_RESOURCES" />
	</html:link><br/><br/>
</logic:iterate>

<br/><br/><br/>
<html:link
	action="<%="/postingRules.do?method=manageGraduationRules"%>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
