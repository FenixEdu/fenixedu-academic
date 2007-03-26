<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="TREASURY">

<bean:define id="personId" name="paymentsManagementDTO" property="person.idInternal"/>
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
<fr:form action="<%="/payments.do?personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />
	<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.preparePayment" /></h2>
	
	<strong><bean:write name="administrativeOfficeUnit" property="nameWithAcronym"/></strong>
	<br/><br/>
	
	<logic:messagesPresent message="true" property="context">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="context" bundle="TREASURY_RESOURCES">
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
	
	<strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person"/></strong>
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
			<td class="width15em aright" style="background-color: #fdfbdd;"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.totalAmount"/>: <bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message bundle="APPLICATION_RESOURCES" key="label.currencySymbol"/></td>
		</tr>
	</table>


	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='doPayment';"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.pay"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>
</fr:form>

</logic:present>
