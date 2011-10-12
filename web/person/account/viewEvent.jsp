<%@page import="net.sourceforge.fenixedu.domain.User"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.util.RendererMessageResourceProvider"%>
<%@page import="pt.utl.ist.fenix.tools.resources.IMessageResourceProvider"%>
<%@page import="java.util.Properties"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.accounting.person.payments.title" bundle="ACCOUNTING_RESOURCES" /></h2>

<bean:define id="event" name="event" type="net.sourceforge.fenixedu.domain.accounting.Event"/>
											<%
												final Properties properties = new Properties();
												properties.put("enum", "ENUMERATION_RESOURCES");
												properties.put("application", "APPLICATION_RESOURCES");
												properties.put("default", "APPLICATION_RESOURCES");
												final IMessageResourceProvider provider = new RendererMessageResourceProvider(properties);
											%>

<bean:define id="affiliation" name="person" property="openAffiliationEvent" type="net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent"/>

			<table class="width100"> 
				<tr> 
					<td style="width: 50%; min-width: 475px; padding-right: 20px;"> 
						<div class="infoop7" style="width: auto; height: 150px; padding: 15px;"> 
							<div style="border: 1px solid #ddd; float: left; padding: 8px; margin: 0 20px 20px 0;">
								<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
								<img src="<%= request.getContextPath() + url %>" style="float: left;"/>
							</div> 
							<table class="tstyle2 thleft thlight mtop0"> 
								<tr>
									<th>
										<bean:message bundle="ACCOUNTING_RESOURCES" key="label.person.name"/>:
									</th> 
									<td>
										<bean:write name="person" property="name"/>
									</td> 
								</tr> 
								<tr> 
									<th>
										<bean:message bundle="ACCOUNTING_RESOURCES" key="label.person.username"/>:
									</th> 
									<td>
										<bean:write name="person" property="username"/>
									</td> 
								</tr> 
								<tr>
									<th>
										<bean:message bundle="ACCOUNTING_RESOURCES" key="label.person.document.type"/>:
									</th>
									<td>
										<bean:write name="person" property="idDocumentType.localizedName"/>
									</td> 
								</tr> 
								<tr>
									<th>
										<bean:message bundle="ACCOUNTING_RESOURCES" key="label.person.document.number"/>:
									</th>
									<td>
										<bean:write name="person" property="documentIdNumber"/>
									</td> 
								</tr> 
								<tr>
									<th>
										<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.balance"/>:
									</th>
									<td class="bold">
										&euro;
										<%= affiliation.getBalance().toPlainString() %>
									</td> 
								</tr> 
							</table> 
						</div> 
					</td>
				</tr> 
			</table>

	<table  class="tstyle1" width="75%">
		<tr>
			<th style="text-align: center; width: 13%;">
				<bean:message key="label.date" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: left;">
				<bean:message key="label.description" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center; width: 7%;">
				<bean:message key="label.value" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center; width: 7%;">
				<bean:message key="label.value.payed" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center; width: 7%;">
				<bean:message key="label.value.to.pay" bundle="TREASURY_RESOURCES" />
			</th>
		</tr>
		<jsp:include page="../../treasury/showEventInTalbe.jsp"/>
	</table>

	<%
		if (event.getPayedAmount().isPositive() && event.getAmountToPay().isZero()) {
	%>
			<a>
				Download Reciept (work in progress)
			</a>
	<%
		}
	%>

			<h3>
				<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.transactions.title"/>
			</h3>

<bean:define id="adjustedTransactions" name="event" property="sortedTransactionsForPresentation"/>
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
				<bean:message key="label.transaction.description" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center;">
				<bean:message key="label.comments" bundle="TREASURY_RESOURCES" />
			</th>
			<th style="text-align: center;">
				<bean:message key="label.value" bundle="TREASURY_RESOURCES" />
			</th>
		</tr>
		<logic:iterate id="adjustedTransaction" name="adjustedTransactions" type="net.sourceforge.fenixedu.domain.accounting.AccountingTransaction">
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
								<%= adjustedTransaction.getEvent().getDescription().toString(provider) %>
							</html:link>
					<% } %>
				</td>
				<td>
					<bean:write name="transactionDetail" property="comments"/>
				</td>
				<td style="text-align: right;">
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


<style>
<!--


#operations {
    margin:20px 0 20px;
}

.grey-box {
    max-width:340px;
    height:110px;
    display:block;
    margin:0 0 10px 0;
    padding:5px 20px 10px;
    float:left;
}

.grey-box,
.infoop7 {
    background:whiteSmoke !important;
    border:1px solid #ececec !important;
    border-radius:3px;
}

.first-box {
    margin-right:30px;
}
.micro-pagamentos .infoop7 .tstyle2 td,
.micro-pagamentos .infoop7 .tstyle2 th {
    background:transparent;
    border-bottom: 1px solid #ddd;
    border-top: 1px solid #ddd;
}


.montante input[type="text"] {
    font-size:18px;
}
.montante input[type="text"] {
    padding:4px;
    text-align:right;
}


.cf:before,
.cf:after {
    content:"";
    display:block;
}
.cf:after {
    clear:both;
}
.cf {
    zoom:1;
}
-->
</style>
