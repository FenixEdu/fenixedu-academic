<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="personId" name="paymentsManagementDTO" property="person.idInternal"/>
<html:form action="<%="/payments.do?personId=" + personId %>">
	<html:hidden property="method" />
	<h2><bean:message key="label.masterDegree.administrativeOffice.payments.payment" /></h2>
	<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false" nested="true"/>
	<br />
	
	<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person"/>:</strong>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
	
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
				<strong><bean:message key="label.masterDegree.administrativeOffice.payments.totalAmount"/></strong>:<bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message key="label.masterDegree.administrativeOffice.payments.currencySymbol"/>
			</td>
		</tr>
	</table>
	
	<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false"/>
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='showPaymentsWithoutReceipt';"><bean:message key="button.masterDegree.administrativeOffice.payments.payment"/></html:submit>
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message key="button.masterDegree.administrativeOffice.payments.back"/></html:submit>
</html:form>
