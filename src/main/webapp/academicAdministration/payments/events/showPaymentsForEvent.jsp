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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.accounting.Discount"%>

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="event" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<p class="mtop15 mbottom05"><strong><bean:message key="label.payments.details" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
<fr:view name="event">
	<fr:schema type="net.sourceforge.fenixedu.domain.accounting.Event" bundle="ACADEMIC_OFFICE_RESOURCES">
		<fr:slot name="whenOccured" key="label.IndividualCandidacy.whenCreated" />
		<fr:slot name="createdBy" key="label.responsible" layout="null-as-label"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
	</fr:layout>
</fr:view>
<logic:notEmpty name="responsible">
	<p class="mtop15 mbottom05">
		<strong><bean:message key="label.responsible.name" bundle="ACADEMIC_OFFICE_RESOURCES" /> :</strong>
		<fr:view name="responsible" property="name" />
	</p>
</logic:notEmpty>


<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.payments.currentEvents" /></strong></p>
<logic:notEmpty name="entryDTOs">
	<fr:view name="entryDTOs" schema="entryDTO.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter thlight tdleft mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="entryDTOs">
	<bean:message key="label.payments.events.noEvents" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<logic:notEmpty name="accountingEventPaymentCodes">
	<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.sibsPayments" /></strong></p>
	<fr:view name="accountingEventPaymentCodes" schema="AccountingEventPaymentCode.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.payedEvents2" /></strong></p>
<logic:empty name="event" property="positiveEntries">
	<bean:message key="label.payments.not.found" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<logic:notEmpty name="event" property="positiveEntries">	
	<fr:view name="event" property="positiveEntries" schema="entry.view-with-payment-mode">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop05" />
			<fr:property name="columnClasses"
				value=",,,aright,aright,aright,acenter" />
				
			<fr:property name="linkFormat(annul)" value="/paymentsManagement.do?method=prepareAnnulTransaction&amp;transactionId=${accountingTransaction.externalId}&amp;eventId=${accountingTransaction.event.externalId}" />
			<fr:property name="key(annul)" value="label.payments.annul" />
			<fr:property name="bundle(annul)" value="MANAGER_RESOURCES" />
			<fr:property name="order(annul)" value="0" />
		</fr:layout>
	</fr:view>

</logic:notEmpty>

<p class="mtop15 mbottom05"><strong><bean:message key="label.accounting.manager.transactions" bundle="APPLICATION_RESOURCES" /></strong></p>
<logic:empty name="event" property="adjustedTransactions">
	<bean:message key="message.accounting.manager.associoted.transactions.empty" bundle="APPLICATION_RESOURCES"/>
</logic:empty>

<logic:notEmpty name="event" property="adjustedTransactions">
	<bean:define id="adjustedTransactions" name="event" property="adjustedTransactions" />
	<logic:iterate id="transaction" name="adjustedTransactions">
	<table class="tstyle4 thlight mtop05">
		<thead>
			<th><bean:message key="label.accounting.manager.transaction.processed" bundle="APPLICATION_RESOURCES" /></th>
			<th><bean:message key="label.accounting.manager.transaction.registered" bundle="APPLICATION_RESOURCES" /></th>
			<th><bean:message key="label.accounting.manager.transaction.responsibleUser" bundle="APPLICATION_RESOURCES" /></th>
			<th><bean:message key="label.accounting.manager.transaction.comments" bundle="APPLICATION_RESOURCES"/></th>
			<th><bean:message key="label.accounting.manager.transaction.credit" bundle="APPLICATION_RESOURCES" /></th>
		</thead>
		<tbody>
			<tr>
			<td><fr:view name="transaction" property="whenProcessed" /></td>
			<td><fr:view name="transaction" property="whenRegistered" /></td>
			<td>
				<logic:notEmpty name="transaction" property="responsibleUser">
					<fr:view name="transaction" property="responsibleUser.username" />
				</logic:notEmpty>
				<logic:empty name="transaction" property="responsibleUser">-</logic:empty>
			</td>
			<td><bean:write name="transaction" property="comments" /></td>
			<td>
				<bean:write name="transaction" property="toAccount.party.partyName.content" />(<bean:write name="transaction" property="toAccountEntry.originalAmount" /> &euro;)
			</td>
			</tr>
			<tr>
			<td colspan="4">
			<logic:empty name="transaction" property="adjustmentTransactions" >
				<bean:message key="message.accounting.manager.adjustment.transactions.empty" />
			</logic:empty>
			
			<logic:notEmpty name="transaction" property="adjustmentTransactions" > 
				<p><bean:message key="message.accounting.manager.associated.adjusting.transactions" bundle="APPLICATION_RESOURCES" /></p>
				<table class="tstyle4 thlight mtop05">
					<thead>
						<th><bean:message key="label.accounting.manager.transaction.processed" bundle="APPLICATION_RESOURCES" /></th>
						<th><bean:message key="label.accounting.manager.transaction.registered" bundle="APPLICATION_RESOURCES" /></th>
						<th><bean:message key="label.accounting.manager.transaction.responsibleUser" bundle="APPLICATION_RESOURCES" /></th>
						<th><bean:message key="label.accounting.manager.transaction.comments" bundle="APPLICATION_RESOURCES"/></th>
						<th><bean:message key="label.accounting.manager.transaction.credit" bundle="APPLICATION_RESOURCES" /></th>
					</thead>
					<logic:iterate id="adjustingTransaction" name="transaction" property="adjustmentTransactions">
					<tbody>
						<tr>
						<td><fr:view name="adjustingTransaction" property="whenProcessed" /></td>
						<td><fr:view name="adjustingTransaction" property="whenRegistered" /></td>
						<td>
							<logic:notEmpty name="adjustingTransaction" property="responsibleUser">
								<fr:view name="adjustingTransaction" property="responsibleUser.username" />
							</logic:notEmpty>
							<logic:empty name="adjustingTransaction" property="responsibleUser">-</logic:empty>
						</td>
						<td><bean:write name="adjustingTransaction" property="comments" /></td>
						<td>
							<bean:write name="adjustingTransaction" property="toAccountEntry.originalAmount" /> &euro;
						</td>
						</tr>
					</tbody>
					</logic:iterate>
				</table>										
			</logic:notEmpty>
			</td>
			</tr>				
		</tbody>
	</table>
	</logic:iterate>

</logic:notEmpty>

<br/><br/>

<logic:notEmpty name="event" property="discounts">
	<strong><bean:message key="label.event.discounts" bundle="MANAGER_RESOURCES" /></strong>
	
	<fr:view name="event" property="discounts">
	
		<fr:schema bundle="MANAGER_RESOURCES" type="<%= Discount.class.getName() %>">
			<fr:slot name="whenCreated" />
			<fr:slot name="amount" />
			<fr:slot name="username" />
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
			
			
			<fr:link name="delete" label="label.delete,MANAGER_RESOURCES"
					 confirmation="label.event.discount.confirm.delete,MANAGER_RESOURCES"
					 link="/paymentsManagement.do?method=deleteDiscount&discountOid=${externalId}" />
			
		</fr:layout>
	</fr:view>

</logic:notEmpty>

<br/><br/>

<bean:define id="personId" name="event" property="person.externalId" />

<fr:form
	action="<%="/paymentsManagement.do?method=backToShowEvents&amp;personId=" + personId.toString()%>">
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:cancel>
</fr:form>
