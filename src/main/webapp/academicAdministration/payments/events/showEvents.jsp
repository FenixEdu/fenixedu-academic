<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


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
	<fr:view name="person" property="events" schema="AccountingEvent.view.by.manager">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop05" />
			<fr:property name="columnClasses"
				value=",acenter,,aright,aright,aright,acenter" />
			
			<fr:property name="linkFormat(detail)" value="/paymentsManagement.do?method=showPaymentsForEvent&amp;eventId=${externalId}" />
			<fr:property name="key(detail)" value="label.details" />
			<fr:property name="bundle(detail)" value="APPLICATION_RESOURCES" />
			<fr:property name="visibleIf(detail)" value="notCancelled" />
			<fr:property name="order(detail)" value="0" />
			
			<fr:property name="linkFormat(deposit)" value="/paymentsManagement.do?method=prepareDepositAmount&amp;eventId=${externalId}" />
			<fr:property name="key(deposit)" value="label.deposit" />
			<fr:property name="bundle(deposit)" value="APPLICATION_RESOURCES" />
			<fr:property name="visibleIf(deposit)" value="depositSupported" />
			<fr:property name="order(deposit)" value="1" />
			
			<fr:property name="linkFormat(transferPaymentsAndCancel)" value="/paymentsManagement.do?method=prepareTransferPaymentsToOtherEventAndCancel&amp;eventId=${externalId}" />
			<fr:property name="key(transferPaymentsAndCancel)" value="label.payments.transferPaymentsAndCancel" />
			<fr:property name="bundle(transferPaymentsAndCancel)" value="ACADEMIC_OFFICE_RESOURCES" />
			<fr:property name="visibleIf(transferPaymentsAndCancel)" value="notCancelled" />
			<fr:property name="order(transferPaymentsAndCancel)" value="2" />
			
			<fr:property name="linkFormat(open)" value="/paymentsManagement.do?method=openEvent&amp;eventId=${externalId}" />
			<fr:property name="key(open)" value="label.open" />
			<fr:property name="bundle(open)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(open)" value="3" />
			<fr:property name="visibleIfNot(open)" value="open" />
			<fr:property name="confirmationKey(open)" value="label.payments.events.confirmOpen" />
			<fr:property name="confirmationBundle(open)" value="ACADEMIC_OFFICE_RESOURCES"></fr:property>
			
			<fr:property name="linkFormat(changePaymentPlan)" value="/paymentsManagement.do?method=prepareChangePaymentPlan&amp;eventId=${externalId}" />
			<fr:property name="key(changePaymentPlan)" value="label.change.paymentPlan" />
			<fr:property name="bundle(changePaymentPlan)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(changePaymentPlan)" value="4" />
			<fr:property name="visibleIf(changePaymentPlan)" value="paymentPlanChangeAllowed" />

			<fr:property name="linkFormat(cancel)" value="/paymentsManagement.do?method=prepareCancelEvent&amp;eventId=${externalId}" />
			<fr:property name="key(cancel)" value="label.cancel" />
			<fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES" />
			<fr:property name="order(cancel)" value="5" />
			<fr:property name="visibleIfNot(cancel)" value="cancelled" />

		</fr:layout>
	</fr:view>
</logic:notEmpty>


<bean:define id="personId" name="person" property="externalId" />

<fr:form
	action="<%="/paymentsManagement.do?method=showOperations&amp;personId=" + personId%>">
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:cancel>
</fr:form>

<bean:define id="personId" name="person" property="externalId" />
<p>
	<html:link action="/paymentsManagement.do?method=prepareViewEventsToCancel" paramId="personId" paramName="personId">
		<bean:message key="link.view.events.for.cancellation" bundle="MANAGER_RESOURCES" />
	</html:link>
</p>
