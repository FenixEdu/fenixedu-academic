<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong>:
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright" />
	</fr:layout>
</fr:view>

<br/>	
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" />:</strong>
<logic:notEmpty name="notPayedEvents">
	<fr:view name="notPayedEvents" schema="AccountingEvent.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleft" />
			<fr:property name="columnClasses" value="listClasses,," />
			<fr:property name="sortBy" value="whenOccured=asc"/>
			
			<fr:property name="linkFormat(view)" value="/payments.do?method=showEventDetails&eventId=${idInternal}"/>
			<fr:property name="key(view)" value="label.payments.viewDetails"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:notEmpty>

<logic:empty name="notPayedEvents">
	<br/>
	<span class="error0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noEvents" /></span>
</logic:empty>


<br/>	
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.payedEvents" />:</strong>
<logic:notEmpty name="payedEntries">
	<fr:view name="payedEntries" schema="entry.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleft tdcenter" />
			<fr:property name="columnClasses" value="listClasses,," />
			<fr:property name="sortBy" value="whenRegistered=asc"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:notEmpty>

<logic:empty name="payedEntries">
	<br/>
	<span class="error0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noPayedEvents" /></span>
</logic:empty>