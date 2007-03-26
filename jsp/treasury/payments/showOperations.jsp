<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="TREASURY">

	<bean:define id="personId" name="person" property="idInternal" />
	<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
	<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
	<fr:form action='<%= "/payments.do?personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>'>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />

		<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.operations" /></h2>
		
		<strong><bean:write name="administrativeOfficeUnit" property="nameWithAcronym"/></strong>
		<br/><br/>

		<strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person" /></strong>
		<fr:view name="person" 	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
				<fr:property name="rowClasses" value="tdhl1,," />
			</fr:layout>
		</fr:view>

		<!-- Operations -->
		<ul>
			<li>
				<html:link action="<%="/payments.do?method=showEvents&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
					<bean:message bundle="TREASURY_RESOURCES" key="label.payments.currentEvents" />
				</html:link>
			</li>
			<li>
				<html:link action="<%="/payments.do?method=showEventsWithInstallments&amp;personId=" + personId+ "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
					<bean:message bundle="TREASURY_RESOURCES" key="label.payments.eventsWithInstallments" />
				</html:link>
			</li>
			<li><html:link
				action="<%="/receipts.do?method=showPaymentsWithoutReceipt&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
				<bean:message bundle="TREASURY_RESOURCES" key="label.payments.emitReceipts" />
			</html:link></li>
			<li><html:link action="<%="/receipts.do?method=showReceipts&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
				<bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts" />
			</html:link></li>
		</ul>


		<ul>
			<li><html:link action="<%="/payments.do?method=showEventsWithPayments&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
				<bean:message bundle="TREASURY_RESOURCES" key="label.payments.extract" />
			</html:link></li>
		</ul>
		

	</fr:form>

</logic:present>
