<%@page import="net.sourceforge.fenixedu.util.Money"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.Event"%>
<%@page import="java.util.Set"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<bean:define id="event" name="event" type="net.sourceforge.fenixedu.domain.accounting.Event"/>

<h3>
	<bean:message key="label.transactions" bundle="TREASURY_RESOURCES" />
</h3>
<bean:define id="adjustedTransactions" name="event" property="adjustedTransactions"/>
<logic:empty name="adjustedTransactions">
	<bean:message key="label.none" bundle="TREASURY_RESOURCES" />
</logic:empty>
<logic:notEmpty name="adjustedTransactions">
	<table  class="tstyle1" width="75%">
		<tr>
			<th style="text-align: left;">
				<bean:message key="label.whenRegistered" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center;">
				<bean:message key="label.whenProcessed" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center;">
				<bean:message key="label.paymentMode" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center;">
				<bean:message key="label.comments" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center;">
				<bean:message key="label.value" bundle="TREASURY_RESOURCES" />
			</th>
		</tr>
		<logic:iterate id="adjustedTransaction" name="adjustedTransactions">
			<bean:define id="transactionDetail" name="adjustedTransaction" property="transactionDetail"
					type="net.sourceforge.fenixedu.domain.accounting.AccountingTransactionDetail"/>
			<tr>
				<td>
					<%= transactionDetail.getWhenRegistered().toString("yyyy-MM-dd HH:mm") %>
				</td>
				<td>
					<%= transactionDetail.getWhenProcessed().toString("yyyy-MM-dd HH:mm") %>
				</td>
				<td>
					<bean:write name="transactionDetail" property="paymentMode.localizedName"/>
				</td>
				<td>
					<bean:write name="transactionDetail" property="comments"/>
				</td>
				<td style="text-align: right;">
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
	<br/>
	<br/>
	<h3>
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
