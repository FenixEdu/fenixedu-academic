<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:form action="/payments.do">
	<html:hidden property="method" />

	<h2>
		<bean:message key="label.masterDegree.administrativeOffice.payments.events" />
	</h2>
	
	
	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>

	<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person" /></strong>:
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
		</fr:layout>
	</fr:view>


	<logic:notEmpty name="paymentsManagementDTO" property="entryDTOs">
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO"
			visible="false" />

		<br />
		<fr:edit id="payment-entries" name="paymentsManagementDTO"
			property="entryDTOs" schema="entryDTO.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value="listClasses,," />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=prepareShowEventsInvalid"/>
		</fr:edit>
		<html:submit styleClass="inputbutton" onclick="this.form.method.value='preparePrintGuide';"><bean:message key="button.masterDegree.administrativeOffice.payments.guide"/></html:submit>
		<html:submit styleClass="inputbutton" onclick="this.form.method.value='doPayment';"><bean:message key="button.masterDegree.administrativeOffice.payments.pay"/></html:submit>
	</logic:notEmpty>


	<logic:empty name="paymentsManagementDTO" property="entryDTOs">
		<span class="error"><bean:message key="label.masterDegree.administrativeOffice.payments.events.noEvents" /></span>
	</logic:empty>

</html:form>
