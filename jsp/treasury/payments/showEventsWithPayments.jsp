<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="TREASURY">

	
<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.extract" /></h2>

<strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person" /></strong>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="person" property="eventsWithPayments">
	<fr:view name="person" property="eventsWithPayments" schema="AccountingEvent.view-with-extra-payed-amount">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop025" />
			<fr:property name="columnClasses" value=",," />
			<fr:property name="sortBy" value="whenOccured=asc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="person" property="eventsWithPayments">
	<em><bean:message bundle="TREASURY_RESOURCES" key="label.payments.events.noEvents" />.</em>
</logic:empty>
	
<bean:define id="personId" name="person" property="idInternal" />
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
<fr:form action='<%= "/payments.do?personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>'>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="paymentsForm" property="method" />
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>
</fr:form>

</logic:present>
