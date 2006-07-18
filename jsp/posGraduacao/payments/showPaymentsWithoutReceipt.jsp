<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="personId" name="createReceiptBean" property="person.idInternal"/>
<fr:form action="<%="/payments.do?personId=" + personId%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />

	<h2><bean:message
		key="label.masterDegree.administrativeOffice.payments.paymentsWithoutReceipt" /></h2>
	<hr>
	<br/>
	
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person" /></strong>:
	<fr:view name="createReceiptBean" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>


	<logic:notEmpty name="createReceiptBean" property="entries">

		<strong><bean:message key="label.masterDegree.administrativeOffice.payments.contributor" /></strong>:
		<fr:edit id="createReceiptBean" name="createReceiptBean"
			schema="createReceiptBean.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=prepareShowPaymentsWithoutReceiptInvalid"/>
		</fr:edit>

		<strong><bean:message key="label.masterDegree.administrativeOffice.payments" /></strong>:
		<fr:edit id="createReceiptBean-entries-part" name="createReceiptBean"
			property="entries" schema="selectableEntryBean.view-selectable">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="sortBy" value="entry.whenRegistered=desc"/>
			</fr:layout>
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='confirmCreateReceipt';"><bean:message key="button.masterDegree.administrativeOffice.payments.continue"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message key="button.masterDegree.administrativeOffice.payments.back"/></html:cancel>
	</logic:notEmpty>

	<logic:empty name="createReceiptBean" property="entries">
		<span class="error0">
			<bean:message key="label.masterDegree.administrativeOffice.payments.noPaymentsWithoutReceipt" />
		</span>
	</logic:empty>
</fr:form>
