<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.receipt" /></h2>

<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent>

<br/>
<fr:view name="receipt" schema="receipt.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:view>
<br/>
<h3><bean:message key="label.masterDegree.administrativeOffice.payments.receiptOwner" /></h3>
<fr:view name="receipt" property="person" schema="person.view-with-name-address-and-fiscalCode">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:view>

<br/>
<h3><bean:message key="label.masterDegree.administrativeOffice.payments.contributor" /></h3>
<fr:view name="receipt" property="contributor" schema="contributor.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:view>

<h3><bean:message key="label.masterDegree.administrativeOffice.payments=Pagamentos" /></h3>
<fr:view name="receipt" property="entries" schema="entry.view">
	<fr:layout name="tabular-editable" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments.totalAmount"/></strong>:<bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message key="label.masterDegree.administrativeOffice.payments.currencySymbol"/>

<html:form action="/payments.do">
	<html:hidden property="method" value="printReceipt" />

	<fr:edit id="receipt" name="receipt" visible="false" />
	<html:submit><bean:message key="label.masterDegree.administrativeOffice.payments.print"/></html:submit>
</html:form>
