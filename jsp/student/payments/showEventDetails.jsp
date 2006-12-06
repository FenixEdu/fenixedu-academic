<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.viewDetails" />:</strong>
<fr:view name="event" schema="AccountingEvent.view-with-amountToPay">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thcenter tdleft" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="entryDTOs">
<br/>
<fr:view name="entryDTOs" schema="entryDTO.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thcenter tdleft" />
		<fr:property name="columnClasses" value="listClasses,," />
	</fr:layout>
</fr:view>
</logic:notEmpty>

<br/>	
<br/>
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.sibsPayments" />:</strong>
<logic:notEmpty name="accountingEventPaymentCodes">
	<fr:view name="accountingEventPaymentCodes" schema="AccountingEventPaymentCode.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thcenter tdcenter tdcenter" />
			<fr:property name="columnClasses" value="listClasses,," />
		</fr:layout>
	</fr:view>
	<br/>
</logic:notEmpty>

<html:form action="/payments.do?method=showEvents">
	<html:submit styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:submit>
</html:form>
