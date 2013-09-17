<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentCodes" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="event" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<bean:define id="personId" name="event" property="person.externalId" />

<logic:notEmpty name="accountingEventPaymentCodes">
	<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.sibsPayments" /></strong></p>
	<fr:view name="accountingEventPaymentCodes" schema="AccountingEventPaymentCode.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<html:form action='<%= "/payments.do?method=showEventsWithPaymentCodes&personId=" + personId %>'>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:submit>
</html:form>
