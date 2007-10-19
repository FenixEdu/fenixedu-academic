<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events" /></h2>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events" /></strong></p>
	<logic:empty name="person" property="events">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.payments.events.noEvents" />
	</logic:empty>

	<logic:notEmpty name="person" property="events">
		<fr:view name="person" property="events" schema="AccountingEvent.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop05" />
				<fr:property name="columnClasses"
					value=",,,aright,aright,aright,acenter" />
				
				<fr:property name="linkFormat(detail)" value="/payments.do?method=showPaymentsForEvent&amp;eventId=${idInternal}" />
				<fr:property name="key(detail)" value="label.details" />
				<fr:property name="bundle(detail)" value="APPLICATION_RESOURCES" />
				<fr:property name="visibleIf(detail)" value="notCancelled" />
				<fr:property name="order(detail)" value="0" />
				
				<fr:property name="linkFormat(transferPaymentsAndCancel)" value="/payments.do?method=prepareTransferPaymentsToOtherEventAndCancel&amp;eventId=${idInternal}" />
				<fr:property name="key(transferPaymentsAndCancel)" value="label.payments.transferPaymentsAndCancel" />
				<fr:property name="bundle(transferPaymentsAndCancel)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="visibleIf(transferPaymentsAndCancel)" value="notCancelled" />
				<fr:property name="order(transferPaymentsAndCancel)" value="1" />
				
				<fr:property name="linkFormat(open)" value="/payments.do?method=openEvent&amp;eventId=${idInternal}" />
				<fr:property name="key(open)" value="label.open" />
				<fr:property name="bundle(open)" value="APPLICATION_RESOURCES" />
				<fr:property name="order(open)" value="3" />
				<fr:property name="visibleIf(open)" value="closed" />
				<fr:property name="confirmationKey(open)" value="label.payments.events.confirmOpen" />
				<fr:property name="confirmationBundle(open)" value="ACADEMIC_OFFICE_RESOURCES"></fr:property>
				
				<fr:property name="linkFormat(cancel)" value="/payments.do?method=prepareCancelEvent&amp;eventId=${idInternal}" />
				<fr:property name="key(cancel)" value="label.cancel" />
				<fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES" />
				<fr:property name="order(cancel)" value="3" />
				<fr:property name="visibleIfNot(cancel)" value="cancelled" />

			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
	<bean:define id="personId" name="person" property="idInternal" />
	
	<fr:form
		action="<%="/payments.do?method=showOperations&amp;personId=" + personId%>">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
		</html:cancel>
	</fr:form>

</logic:present>