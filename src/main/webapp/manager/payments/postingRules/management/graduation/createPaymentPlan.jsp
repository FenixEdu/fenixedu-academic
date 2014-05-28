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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- createPaymentPlan.jsp -->

<h2><bean:message
	key="label.payments.postingRules.createGratuityPaymentPlan"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<fr:hasMessages for="installmentEditor">
	<ul>
		<fr:messages>
			<li><fr:message/></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<logic:messagesPresent message="true" property="installment">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="installment" bundle="MANAGER_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>



<fr:form action="/postingRules.do">

	<input type="hidden" name="method" value="" />
	

	<fr:edit id="paymentPlanEditor" name="paymentPlanEditor"
		schema="PaymentPlanBean.edit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			<fr:destination name="invalid" path="/postingRules.do?method=prepareCreatePaymentPlanInvalid" />
			<fr:destination name="changeExecutionYearPostback" path="/postingRules.do?method=changeExecutionYearForPaymentPlanCreate"/>
			
		</fr:layout>
	</fr:edit>
	


	<strong>
		<bean:message  key="label.net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.PaymentPlanBean.installments" bundle="APPLICATION_RESOURCES"/>
	</strong>
	<br/>
	<logic:empty name="paymentPlanEditor" property="installments">
		<bean:message  key="label.payments.postingRules.paymentPlan.noInstallments" bundle="MANAGER_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="paymentPlanEditor" property="installments">
		<fr:edit id="installmentsEditor"  name="paymentPlanEditor"
			property="installments"
			schema="InstallmentBean.view.selectable">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			</fr:layout>
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='removeInstallments';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.delete" />
		</html:submit>
	</logic:notEmpty>
	
	
	<br/><br/>
	
	<fr:edit id="installmentEditor" name="installmentEditor"
		schema="InstallmentBean.edit">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			<fr:property name="columnClasses" value="width14em,width35em,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/postingRules.do?method=addInstallmentInvalid"/>
	</fr:edit>		
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		onclick="this.form.method.value='addInstallment';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.add" />
	</html:submit>
			
	<br/><br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		onclick="this.form.method.value='createPaymentPlan';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.create" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
		onclick="this.form.method.value='manageGraduationRules';">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
	</html:cancel>

</fr:form>
