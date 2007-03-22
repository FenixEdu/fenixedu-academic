<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="personId" name="paymentsManagementDTO" property="person.idInternal" />
<fr:form action='<%= "/payments.do?personId=" + personId %>'>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />

	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.eventsWithInstallments" /></h2>
	
	
	<logic:messagesPresent message="true">
		<ul class="nobullet list2">
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<fr:hasMessages for="paymentsManagementDTO" type="conversion">
		<ul class="nobullet list2">
			<fr:messages>
				<li><span class="error0"><fr:message/></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	

	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<p class="mbottom05 mtop15"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" /></strong></p>
	<logic:notEmpty name="paymentsManagementDTO" property="entryDTOs">
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false" />

		<fr:edit id="payment-entries" name="paymentsManagementDTO"
			property="entryDTOs" schema="entryDTO.edit-with-installments">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value=",,,aright,aright,aright,acenter" />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=prepareShowEventsWithInstallmentsInvalid"/>
		</fr:edit>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePrintGuideWithInstallments';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.guide"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePaymentWithInstallments';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.preparePayment"/></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
		</p>
	</logic:notEmpty>

	<logic:empty name="paymentsManagementDTO" property="entryDTOs">
		<p>
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noEvents" />.</em>
		</p>
	</logic:empty>

</fr:form>

</logic:present>
