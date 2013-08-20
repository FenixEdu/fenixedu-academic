<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions" /></h2>
<br />


<logic:messagesPresent message="true" property="context">
	<ul>
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="context">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul>
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>


<fr:hasMessages for="create-installment-penalty-exemption-bean" type="conversion">
	<ul>
		<fr:messages>
			<li><span class="error0"><fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="gratuityEvent" name="createInstallmentPenaltyExemptionBean"
	property="gratuityEventWithPaymentPlan" />

<bean:define id="person" name="gratuityEvent" property="person" />
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br />

<bean:define id="gratuityEventId" name="gratuityEvent" property="externalId" />
<fr:form action='<%="/exemptionsManagement.do?eventId=" + gratuityEventId%>'>

	<html:hidden property="method" value="" />

	<fr:edit id="create-installment-penalty-exemption-bean"
		schema="CreateInstallmentPenaltyExemptionBean.edit" name="createInstallmentPenaltyExemptionBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:destination name="invalid"
			path='<%="/exemptionsManagement.do?method=prepareCreateInstallmentPenaltyExemptionInvalid&amp;eventId=" + gratuityEventId%>'/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		onclick="this.form.method.value='createInstallmentPenaltyExemption';">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" 
		onclick="this.form.method.value='showExemptions';">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.cancel" />
	</html:cancel>
</fr:form>
