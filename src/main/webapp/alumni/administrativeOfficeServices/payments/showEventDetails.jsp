<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumni/administrativeOffice/Services/payments/showEventDetails.jsp -->

<html:xhtml/>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.eventDetails" /></h2>

<%--
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright" />
	</fr:layout>
</fr:view>
--%>

<fr:view name="event" schema="AccountingEvent.view-with-amountToPay">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright tdleftm" />
			<fr:property name="rowClasses" value=",,tdhl1" />
	</fr:layout>
</fr:view>

<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.viewDetails" /></strong></p>
<logic:notEmpty name="entryDTOs">
<fr:view name="entryDTOs" schema="entryDTO.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thcenter thlight tdleft mtop025" />
		<fr:property name="columnClasses" value=",," />
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:notEmpty name="accountingEventPaymentCodes">
<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.sibsPayments" /></strong></p>
	<fr:view name="accountingEventPaymentCodes" schema="AccountingEventPaymentCode.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<fr:form action="/payments.do?method=showEvents">
	<p class="mtop1">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/>
		</html:submit>
	</p>
</fr:form>
