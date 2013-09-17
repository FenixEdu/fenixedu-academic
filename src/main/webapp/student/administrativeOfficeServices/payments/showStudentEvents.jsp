<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>

<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright" />
	</fr:layout>
</fr:view>


<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" /></strong></p>
<logic:notEmpty name="notPayedEvents">
	<fr:view name="notPayedEvents" schema="AccountingEvent.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight tdleft mtop025" />
			<fr:property name="columnClasses" value="acenter,," />
			<fr:property name="sortBy" value="whenOccured=asc"/>
			<fr:property name="linkFormat(view)" value="/payments.do?eventId=${externalId}&amp;method=showEventDetails"/>
			<fr:property name="key(view)" value="label.payments.viewDetails"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="notPayedEvents">
	<p class="mtop05">
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noEvents" /></em>
	</p>
</logic:empty>


<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.payedEvents2" /></strong></p>
<logic:notEmpty name="payedEntries">
	<fr:view name="payedEntries" schema="entry.view-for-student">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle1 thlight mtop025 width100 mbottom0" />
			<fr:property name="columnClasses" value=",acenter,aright" />

			<fr:property name="sortParameter" value="sortBy"/>
	        <fr:property name="sortUrl" value="/payments.do?method=showEvents" />
    	    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "whenRegistered=asc" : request.getParameter("sortBy") %>"/>
			<fr:property name="sortableSlots" value="whenRegistered, amountWithAdjustment" />
		</fr:layout>
	</fr:view>
	<table class="tstyle1 tgluetop mtop0 width100 aright">
		<tr><td><span style="padding-right: 5px;"><bean:message bundle="APPLICATION_RESOURCES" key="label.Total" /></span> <bean:write name="totalPayed"/></td></tr>
	</table>
</logic:notEmpty>

<logic:empty name="payedEntries">
	<p class="mtop05">
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noPayedEvents" />.</em>
	</p>
</logic:empty>