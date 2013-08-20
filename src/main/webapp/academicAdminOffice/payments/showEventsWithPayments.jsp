<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.extract" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<bean:define id="personId" name="person" property="externalId" />

<logic:notEmpty name="person" property="eventsWithPayments">
	<fr:view name="person" property="eventsWithPayments" schema="AccountingEvent.view-with-extra-payed-amount">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
			
			<fr:property name="linkFormat(detail)" value="/payments.do?method=showPaymentsForEvent&eventId=${externalId}&personId=${person.externalId}"/>
			<fr:property name="key(detail)" value="label.details"/>
			<fr:property name="bundle(detail)" value="APPLICATION_RESOURCES"/>

			<fr:property name="sortParameter" value="sortBy"/>
	        <fr:property name="sortUrl" value="<%= "/payments.do?method=showEventsWithPayments&personId=" + personId %>" />
    	    <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "whenOccured=asc" : request.getParameter("sortBy") %>"/>
			<fr:property name="sortableSlots" value="lastPaymentDate, payedAmount, reimbursableAmount" />
		</fr:layout>
	</fr:view>	
</logic:notEmpty>

<logic:empty name="person" property="eventsWithPayments">
	<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noEvents" />.</em>
</logic:empty>
	
<fr:form action='<%= "/payments.do?personId=" + personId %>'>
	
	<input type="hidden" name="method" value=""/>
	
<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
</fr:form>
