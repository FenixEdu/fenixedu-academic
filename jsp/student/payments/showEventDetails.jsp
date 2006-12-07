<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>


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
			<fr:property name="classes" value="tstyle4 thlight thright tdleftm" />
			<fr:property name="columnClasses" value=",tdhl1" />
	</fr:layout>
</fr:view>


<p class="mtop15 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.viewDetails" /></strong></p>
<logic:notEmpty name="entryDTOs">
<fr:view name="entryDTOs" schema="entryDTO.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thcenter thlight tdleft mtop025" />
		<fr:property name="columnClasses" value=",," />
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:notEmpty name="accountingEventPaymentCodes">
	<p class="mtop15 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.sibsPayments" /></strong></p>
	<fr:view name="accountingEventPaymentCodes" schema="AccountingEventPaymentCode.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p>
	<html:form action="/payments.do?method=showEvents">
		<html:submit styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:submit>
	</html:form>
</p>
