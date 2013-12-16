<%@page import="pt.ist.bennu.core.domain.User"%>
<%@page import="java.util.Properties"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.util.RendererMessageResourceProvider"%>
<%@page import="pt.utl.ist.fenix.tools.resources.IMessageResourceProvider"%>
<%@page import="net.sourceforge.fenixedu.util.Money"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.Event"%>
<%@page import="java.util.Set"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<bean:define id="event" name="event" type="net.sourceforge.fenixedu.domain.accounting.Event"/>

<h3 class="mtop2">
	<bean:message key="label.transactions" bundle="TREASURY_RESOURCES" />
</h3>
<bean:define id="adjustedTransactions" name="event" property="sortedTransactionsForPresentation"/>
<logic:empty name="adjustedTransactions">
	<p><bean:message key="label.none" bundle="TREASURY_RESOURCES" /></p>
</logic:empty>
<logic:notEmpty name="adjustedTransactions">
	<table class="tstyle1" width="100%">
		<tr>
			<th class="acenter">
				<bean:message key="label.whenRegistered" bundle="TREASURY_RESOURCES" />
			</th>
			<th class="acenter">
				<bean:message key="label.whenProcessed" bundle="TREASURY_RESOURCES" />
			</th>
			<th class="aleft">
				<bean:message key="label.transaction.description" bundle="TREASURY_RESOURCES" />
			</th>
			<th class="acenter">
				<bean:message key="label.comments" bundle="TREASURY_RESOURCES" />
			</th>
			<th class="acenter">
				<bean:message key="label.value" bundle="TREASURY_RESOURCES" />
			</th>
		</tr>
		<logic:iterate id="adjustedTransaction" name="adjustedTransactions" type="net.sourceforge.fenixedu.domain.accounting.AccountingTransaction">
			<bean:define id="transactionDetail" name="adjustedTransaction" property="transactionDetail"
					type="net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail"/>
			<tr>
				<td class="acenter">
					<%= transactionDetail.getWhenRegistered().toString("yyyy-MM-dd HH:mm") %>
				</td>
				<td class="acenter">
					<%= transactionDetail.getWhenProcessed().toString("yyyy-MM-dd HH:mm") %>
				</td>
				<td>
					<% if (event == adjustedTransaction.getEvent()) { %>
							<bean:message key="label.payment.via" bundle="TREASURY_RESOURCES" />
							<bean:message bundle="TREASURY_RESOURCES" name="transactionDetail" property="paymentMode.qualifiedName"/>
							<%
								final User user = adjustedTransaction.getResponsibleUser();
								if (user != null) {
							%>
									<span style="color: gray;">
									(<bean:message key="label.processedby" bundle="TREASURY_RESOURCES" />
									<%= user.getPerson().getNickname() %>)
									</span>
							<% } %>
					<% } else { %>
							<html:link action="<%= "/paymentManagement.do?method=viewEvent&eventId=" + adjustedTransaction.getEvent().getExternalId() %>">
								<%
									final Properties properties = new Properties();
									properties.put("enum", "ENUMERATION_RESOURCES");
									properties.put("application", "APPLICATION_RESOURCES");
									properties.put("default", "APPLICATION_RESOURCES");
									final IMessageResourceProvider provider = new RendererMessageResourceProvider(properties);
								%>
								<%= adjustedTransaction.getEvent().getDescription().toString(provider) %>
							</html:link>
					<% } %>
				</td>
				<td>
					<bean:write name="transactionDetail" property="comments"/>
				</td>
				<td class="aright">
					<% if (event != adjustedTransaction.getEvent()) { %>
							-
					<% } %>
					&euro;
					<bean:write name="adjustedTransaction" property="amountWithAdjustment"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<%--
<br/>
<h3>
	<bean:message key="label.discounts" bundle="TREASURY_RESOURCES" />
</h3>
<logic:empty name="event" property="discounts">
	<bean:message key="label.none" bundle="TREASURY_RESOURCES" />
</logic:empty>
<logic:notEmpty name="event" property="discounts">
</logic:notEmpty>

<br/>
<h3>
	<bean:message key="label.exemptions" bundle="TREASURY_RESOURCES" />
</h3>
<logic:empty name="event" property="exemptions">
	<bean:message key="label.none" bundle="TREASURY_RESOURCES" />
</logic:empty>
<logic:notEmpty name="event" property="exemptions">
</logic:notEmpty>

<br/>
<h3>
	<bean:message key="label.payment.codes" bundle="TREASURY_RESOURCES" />
</h3>
<logic:empty name="event" property="paymentCodes">
	<bean:message key="label.none" bundle="TREASURY_RESOURCES" />
</logic:empty>
<logic:notEmpty name="event" property="paymentCodes">
</logic:notEmpty>
 --%>


<logic:present name="paymentBean">
	<h3 class="mtop2">
		<bean:message key="label.make.payment" bundle="TREASURY_RESOURCES" />
	</h3>
	<fr:form action="/paymentManagement.do">
		<html:hidden property="method" value="payEvent"/>

		<fr:edit id="paymentBean" name="paymentBean">
			<fr:schema bundle="TREASURY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$PaymentBean">
				<fr:slot name="paymentDateTime" bundle="TREASURY_RESOURCES" key="label.paymentDateTime"/>
				<fr:slot name="contributorNumber" bundle="TREASURY_RESOURCES" key="label.contributorNumber">
					<fr:property name="size" value="50"/>
				</fr:slot>
				<fr:slot name="contributorName" bundle="TREASURY_RESOURCES" key="label.contributorName">
					<fr:property name="size" value="50"/>
				</fr:slot>
				<fr:slot name="value" bundle="TREASURY_RESOURCES" key="label.value">
					<fr:property name="size" value="6"/>
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mbottom0" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="TREASURY_RESOURCES" key="label.register.payment" />
		</html:submit>
	</fr:form>
</logic:present>

<!--
<logic:notPresent name="person" property="openInstitutionAffiliationEvent">
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.recent.entrie.none"/>
</logic:notPresent>
<logic:present name="person" property="openInstitutionAffiliationEvent">
	<bean:define id="microPaymentEvents" name="person" property="openInstitutionAffiliationEvent.sortedMicroPaymentEvents"/>
	<logic:empty name="microPaymentEvents">
		<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.recent.entrie.none"/>
	</logic:empty>
	<logic:notEmpty name="microPaymentEvents">
		<table class="tstyle1 thlight width100 tdcenter mtop05"> 
			<tr> 
				<th>
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.date"/>
				</th> 
				<th>
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.unit"/>
				</th> 
				<th>
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.operator"/>
				</th>
				<th style="text-align: right;">
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.amount"/>
				</th> 
			</tr>
			<logic:iterate id="microPaymentEvent" name="microPaymentEvents">
				<tr> 
					<td>
						<bean:write name="microPaymentEvent" property="whenOccured"/>
					</td> 
					<td>
						<bean:write name="microPaymentEvent" property="destinationUnit.presentationName"/>
					</td>
					<td>
						<bean:write name="microPaymentEvent" property="createdBy"/>
					</td> 
					<td class="aright">
						<bean:write name="microPaymentEvent" property="payedAmount"/>
					</td> 
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>
 -->