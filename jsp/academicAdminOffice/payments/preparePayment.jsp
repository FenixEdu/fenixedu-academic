<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="personId" name="paymentsManagementDTO" property="person.idInternal"/>
<fr:form action="<%="/payments.do?personId=" + personId %>">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.preparePayment" /></h2>
	<hr/>
	<br />

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/>:</strong>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
	
	<fr:edit id="paymentsManagementDTO-edit" name="paymentsManagementDTO" schema="paymentsManagementDTO.edit-with-paymentDate">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
		<fr:destination name="invalid" path="/payments.do?method=preparePaymentInvalid"/>
	</fr:edit>
	
	<table>
		<tr>
			<td>
				<fr:view name="paymentsManagementDTO" property="selectedEntries" schema="entryDTO.view">
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle4"/>
				        <fr:property name="columnClasses" value="listClasses,,"/>
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td align="right">
				<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount"/></strong>:<bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol"/>
			</td>
		</tr>
	</table>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='doPayment';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.pay"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
</fr:form>

</logic:present>
