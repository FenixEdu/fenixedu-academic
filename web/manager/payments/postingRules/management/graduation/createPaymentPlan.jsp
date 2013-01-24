<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
