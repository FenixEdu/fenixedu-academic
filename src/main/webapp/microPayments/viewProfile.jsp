<%@page import="net.sourceforge.fenixedu.domain.User"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent"%>
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

<style>

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

</style>

<html:xhtml />

<h2><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.person.header"/></h2>

<p>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.person.description"/>
</p>

<jsp:include page="searchPersonForm.jsp"/>


<div class="micro-pagamentos"> 

	<h3 class="mtop3 mbottom05">
		<bean:write name="person" property="nickname"/>
		<span class="color777" style="font-weight:normal;">(
		<bean:write name="person" property="username"/>
		)</span>
	</h3>

	<table class="width100"> 
		<tr> 
			<td style="width: 50%; min-width: 475px; padding-right: 20px;"> 
				<div class="infoop7" style="width: auto; height: 150px; padding: 15px;"> 
					<div style="border: 1px solid #ddd; float: left; padding: 8px; margin: 0 20px 20px 0;">
						<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
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
								<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
								<%
									final InstitutionAffiliationEvent event = person.getOpenAffiliationEvent();
								%>
								<%= event == null ? "0.00" : event.getBalance().toPlainString() %>
							</td> 
						</tr> 
					</table> 
				</div> 
			</td> 

			<td style="width: 50%; vertical-align: top; min-width: 350px;"> 
				<div class="infoop7" style="border: 1px solid #ddd; width: auto; height: 150px; padding: 15px;"> 
					<h3 class="mtop0">
						<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.person.register.service"/>
					</h3>

					<fr:form action="/operator.do?method=createMicroPayment">
						<table>
							<tr>
								<td colspan="2">
									<fr:edit id="microPayment" name="microPayment">
										<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator$MicroPaymentCreationBean">
											<fr:slot name="unit" layout="menu-select" key="label.unit" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
												<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator$MicroPaymentUnitsProvider" />
												<fr:property name="format" value="${presentationName}" />
											</fr:slot>
										</fr:schema>
										<fr:layout name="tabular">
											<bean:define id="postBackUrl">/operator.do?method=showPerson&personOid=<bean:write name="person" property="externalId"/></bean:define>
											<fr:destination name="invalid" path="<%= postBackUrl %>"/>
										</fr:layout>
									</fr:edit>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<fr:edit id="microPaymentVerificationCode" name="microPayment">
										<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator$MicroPaymentCreationBean">
											<fr:slot name="paymentTicket" key="label.microPaymentVerificationCode"/>
										</fr:schema>
										<fr:layout name="tabular">
											<bean:define id="postBackUrl">/operator.do?method=showPerson&personOid=<bean:write name="person" property="externalId"/></bean:define>
											<fr:destination name="invalid" path="<%= postBackUrl %>"/>
										</fr:layout>
									</fr:edit>									
								</td>
							</tr>
							<tr>
								<td>
									<fr:edit id="microPaymentAmount" name="microPayment">
										<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.microPayments.MicroPaymentsOperator$MicroPaymentCreationBean">
											<fr:slot name="amount"  key="label.amount" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
										</fr:schema>
										<fr:layout name="tabular">
											<bean:define id="postBackUrl">/operator.do?method=showPerson&personOid=<bean:write name="person" property="externalId"/></bean:define>
											<fr:destination name="invalid" path="<%= postBackUrl %>"/>
										</fr:layout>
									</fr:edit>
								</td>
								<td>
									<html:submit>
										<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.process.payment" />
									</html:submit>
								</td>
							</tr>
						</table>
					</fr:form>

					<logic:messagesPresent message="true">
						<p>
							<span class="error0"><!-- Error messages go here -->
								<html:messages id="message" message="true" bundle="ACCOUNTING_RESOURCES">
									<%= message %>
								</html:messages>
							</span>
						<p>
					</logic:messagesPresent>
				</div> 
			</td> 
		</tr> 
	</table> 
</div> 
	
<h3 class="mtop3 mbottom05">
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.recent.entrie"/>
	<bean:write name="person" property="username"/>
</h3> 

<logic:notPresent name="person" property="openAffiliationEvent">
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.recent.entrie.none"/>
</logic:notPresent>
<logic:present name="person" property="openAffiliationEvent">
	<bean:define id="microPaymentEvents" name="person" property="openAffiliationEvent.sortedMicroPaymentEvents"/>
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
				<th>
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.client"/>
				</th>
				<th style="text-align: right;">
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.amount"/>
				</th> 
			</tr>
			<logic:iterate id="microPaymentEvent" name="microPaymentEvents" type="net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent">
				<tr> 
					<td>
						<%= microPaymentEvent.getWhenOccured().toString("yyyy-MM-dd HH:mm:ss") %>
					</td> 
					<td>
						<bean:write name="microPaymentEvent" property="destinationUnit.presentationName"/>
					</td>
					<td>
						<%
							final User user = User.readUserByUserUId(microPaymentEvent.getCreatedBy());
							if (user != null) {
						%>
								<%= user.getPerson().getNickname() %>
						<%
							}
						%>
						(<%= microPaymentEvent.getCreatedBy() %>)
					</td>
					<td>
						<%= microPaymentEvent.getPerson().getNickname() %>
						(<%= microPaymentEvent.getPerson().getUsername() %>)
					</td> 
					<td class="aright">
						<bean:write name="microPaymentEvent" property="payedAmount"/>
					</td> 
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>

