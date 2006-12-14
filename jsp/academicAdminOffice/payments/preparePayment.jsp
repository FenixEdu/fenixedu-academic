<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="personId" name="paymentsManagementDTO" property="person.idInternal"/>
<fr:form action="<%="/payments.do?personId=" + personId %>">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />
	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.preparePayment" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet">
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	
	<fr:edit id="paymentsManagementDTO-edit" name="paymentsManagementDTO" schema="paymentsManagementDTO.edit-with-paymentDate">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear" />
		</fr:layout>
		<fr:destination name="invalid" path="/payments.do?method=preparePaymentInvalid"/>
	</fr:edit>
	

	<fr:view name="paymentsManagementDTO" property="selectedEntries" schema="entryDTO.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05 mbottom0" />
			<fr:property name="columnClasses" value="width30em acenter,width15em aright"/>
		</fr:layout>
	</fr:view>


	<table class="tstyle4 mtop0">
		<tr>
			<td class="width30em"></td>
			<td class="width15em aright" style="background-color: #fdfbdd;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount"/>: <bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol"/></td>
		</tr>
	</table>


	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='doPayment';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.pay"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
</fr:form>

</logic:present>
