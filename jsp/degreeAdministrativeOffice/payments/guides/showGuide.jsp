<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<h2><bean:message key="label.payments.guide"/></h2>
<br/>
<strong><bean:message key="label.payments.person"/>:</strong>
<fr:view name="paymentsManagementDTO" property="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message key="label.payments"/>:</strong>
<logic:notEmpty name="paymentsManagementDTO" property="selectedEntries">
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
				<strong><bean:message key="label.payments.totalAmount"/></strong>:<bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message key="label.payments.currencySymbol"/>
			</td>
		</tr>
	</table>
	<br/>
	<html:form action="/payments.do" target="_blank">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" />
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false"/>
		<br/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='printGuide';"><bean:message key="label.payments.print"/></html:submit>
	</html:form>
</logic:notEmpty>
<logic:empty name="paymentsManagementDTO" property="selectedEntries">
	<span class="error"><!-- Error messages go here --><bean:message key="label.payments.not.found"/></span>
</logic:empty>

</logic:present>
