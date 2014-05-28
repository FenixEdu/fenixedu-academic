<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
<bean:define id="degreeCurricularPlanId" name="paymentPlan" property="serviceAgreementTemplate.degreeCurricularPlan.externalId" />

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
