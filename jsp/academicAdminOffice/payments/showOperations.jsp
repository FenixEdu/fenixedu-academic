<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action='<%= "/payments.do?personId=" + personId %>'>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />

		<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
		<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.management" /></h2>

		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong>
		<fr:view name="person" 	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
				<fr:property name="rowClasses" value="tdhl1,," />
			</fr:layout>
		</fr:view>

		<!-- Operations -->
		<ul>
			<li><html:link action="<%="/payments.do?method=showEvents&amp;personId=" + personId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" />
			</html:link></li>
			<li><html:link action="<%="/payments.do?method=showEventsWithInstallments&amp;personId=" + personId%>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.eventsWithInstallments" />
			</html:link></li>
			<li><html:link
				action="<%="/payments.do?method=showPaymentsWithoutReceipt&amp;personId=" + personId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentsWithoutReceipt" />
			</html:link></li>
			<li><html:link action="<%="/payments.do?method=showReceipts&amp;personId=" + personId%>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipts" />
			</html:link></li>
			<li><html:link action="<%="/payments.do?method=showGratuityEvents&amp;personId=" + personId%>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions" />
			</html:link></li>
			<%-- 
				<li>
					<html:link action="<%="/payments.do?method=showEventsForOtherPartyPayment&personId=" + personId%>">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.otherPartyPayment" />
					</html:link>
				</li>
			--%>
		</ul>
		<ul>
			<li><html:link action="<%="/payments.do?method=showEventsWithPayments&amp;personId=" + personId%>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.extract" />
			</html:link></li>
		</ul>


	</fr:form>

</logic:present>
