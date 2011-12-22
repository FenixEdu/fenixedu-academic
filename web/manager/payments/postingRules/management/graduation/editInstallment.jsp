<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<h2>Editar prestação do plano de propina de <em>nome do curso</em></h2>
	
	<h3>Tipo de plano</h3>
	
	<logic:messagesPresent message="true" property="error">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="installment" bundle="MANAGER_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<bean:define id="paymentPlanId" name="paymentPlan" property="externalId" />
	<bean:define id="installmentId" name="installment" property="externalId" />
	<bean:define id="degreeCurricularPlanId" name="paymentPlan" property="serviceAgreementTemplate.degreeCurricularPlan.idInternal" />
	
	<fr:form action="<%= String.format("/postingRules.do?method=editInstallment&amp;installmentId=%s&amp;paymentPlanId=%s&amp;degreeCurricularPlanId=%s", installmentId, paymentPlanId, degreeCurricularPlanId) %>" >
		<fr:edit id="installmentBean" name="installmentBean" visible="false" />
		
		<fr:edit id="installmentBean-edit" name="installmentBean">
			<fr:schema
				type="net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean"
				bundle="APPLICATION_RESOURCES">
				<fr:slot name="amount" />
				<fr:slot name="ectsForAmount" />
				<fr:slot name="executionSemesters" layout="option-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.paymentPlans.ExecutionSemestersForInstallmentCreationProvider" />
					<fr:property name="format" value="${name}" />
			        <fr:property name="saveOptions" value="true" />        
					<fr:property name="eachSchema" value="ExecutionSemester.view.name.only"/>
			        <fr:property name="eachLayout" value="values"/>
					<fr:property name="classes" value="nobullet noindent"/>
				</fr:slot>
				<fr:slot name="startDate" />
				<fr:slot name="endDate" />
				<fr:slot name="penaltyAppliable" />
				<fr:slot name="montlyPenaltyPercentage" />
				<fr:slot name="whenToStartApplyPenalty" />
				<fr:slot name="maxMonthsToApplyPenalty" />
				<fr:slot name="numberOfDaysToStartApplyingPenalty" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2" />
				<fr:property name="columnClasses" value=",,tderror1" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= String.format("/postingRules.do?method=editInstallmentInvalid&amp;installmentId=%s&amp;paymentPlanId=%s&amp;degreeCurricularPlanId=%s", installmentId, paymentPlanId, degreeCurricularPlanId) %>" />
			<fr:destination name="cancel" path="<%= String.format("/postingRules.do?method=showPaymentPlans&amp;degreeCurricularPlanId=%s",  degreeCurricularPlanId) %>" />
		</fr:edit>
		
		<html:submit><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</fr:form>

</logic:present>
