<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.guide"/></h2>
<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments.receiptOwner"/>:</strong>
<fr:view name="paymentsManagementDTO" property="person"
	schema="person.view-with-name-address-and-fiscalCode">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments"/>:</strong>
<logic:notEmpty name="paymentsManagementDTO" property="selectedEntries">
	<table>
		<tr>
			<td>
				<fr:view name="paymentsManagementDTO" property="selectedEntries" schema="entryDTO.view-guide">
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
	<br/>
	<html:form action="/payments.do" target="_blank">
		<html:hidden property="method" />
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false"/>
		<br/>
		<html:submit styleClass="inputbutton" onclick="this.form.method.value='printGuide';"><bean:message key="button.masterDegree.administrativeOffice.payments.guide"/></html:submit>
	</html:form>
</logic:notEmpty>
<logic:empty name="paymentsManagementDTO" property="selectedEntries">
	<em><bean:message key="label.masterDegree.administrativeOffice.payments.not.found"/></em>
</logic:empty>
