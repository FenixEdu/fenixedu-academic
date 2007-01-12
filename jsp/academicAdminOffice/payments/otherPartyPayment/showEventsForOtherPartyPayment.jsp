<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.otherPartyPayment" /></h2>
	<br />
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />
	<bean:define id="events" name="person" property="eventsSupportingPaymentByOtherParties" />
	<logic:notEmpty name="events">
		<fr:view name="events" schema="AccountingEvent.view">
			<fr:layout name="tabular">
				
				<fr:property name="classes" value="tstyle4" />
				
				<fr:property name="linkFormat(view)" value="/payments.do?eventId=${idInternal}&amp;method=showOtherPartyPaymentsForEvent" />
				<fr:property name="key(view)" value="label.view" />
				<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
							
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="events">
		<span class="error0">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.otherPartyPayment.noEvents" />
		</span>
	</logic:empty>
	
	<bean:define id="personId" name="person" property="idInternal" />
	<fr:form action="<%="/payments.do?personId=" + personId + "&amp;method=backToShowOperations"%>">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back" />
		</html:submit>
	</fr:form>

</logic:present>
