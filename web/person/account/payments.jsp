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

<logic:notPresent name="affiliation">
	<p>
		<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.person.payments.no.affiliation"/>
	</p>
</logic:notPresent>

<logic:present name="affiliation">
	<bean:define id="affiliation" name="affiliation" type="net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent"/>
	<% if (affiliation.acceptedTermsAndConditions()) { %>

			TODO : Acrescentar aqui também informação sobre como pode ser usado o saldo... com o cartão do ist ou cartão do cidadão
			       possivelmente dar a opção à pessoa de activar/desactivar cartões...
			       Pensar também em indicar os locais onde pode ser usado.

			<bean:define id="person" name="affiliation" property="person" type="net.sourceforge.fenixedu.domain.Person"/>

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

			<h3>
				<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.paymentMethod.title"/>
			</h3>
			<p class="mbottom025">
				<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.paymentMethod"/>
			</p>
			<logic:notPresent name="paymentCode">
				<p class="mbottom025">
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.paymentCode.none"/>
				</p>
			</logic:notPresent>
			<logic:present name="paymentCode">
				<p class="mbottom025">
					<strong><bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.paymentCode" /> </strong>
				</p>
				<fr:view name="paymentCode">
					<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.domain.accounting.PaymentCode">
						<fr:slot name="entityCode" />
						<fr:slot name="formattedCode" />
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
						<fr:property name="columnClasses" value=",," />
					</fr:layout>
				</fr:view>
			</logic:present>

			<h3>
				<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.transactions.title"/>
			</h3>
			<logic:empty name="payments">
				<p class="mbottom025">
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.payments.none"/>
				</p>
			</logic:empty>
			<logic:notEmpty name="payments">
				<p class="mbottom025">
					<strong><bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.micropayments.transactions" /> </strong>
				</p>
				<table class="tstyle1 thlight mtop025 width100 mbottom0">
					<tr>
						<th>
							<bean:message bundle="ACCOUNTING_RESOURCES" key="label.net.sourceforge.fenixedu.domain.accounting.Entry.description"/>
						</th>
						<th>
							<bean:message bundle="ACCOUNTING_RESOURCES" key="label.net.sourceforge.fenixedu.domain.accounting.Entry.whenRegistered"/>
						</th>
						<th>
							<bean:message bundle="ACCOUNTING_RESOURCES" key="label.net.sourceforge.fenixedu.domain.accounting.Entry.amountWithAdjustment"/>
						</th>
					</tr>
					<logic:iterate id="entry" name="payments" type="net.sourceforge.fenixedu.domain.accounting.Entry">
						<tr>
							<td>
								<% if (entry.getAccountingTransaction().getEvent() == affiliation) { %>
										<bean:message key="label.payment.via" bundle="ACCOUNTING_RESOURCES" />
										<bean:message bundle="ACCOUNTING_RESOURCES" name="entry" property="paymentMode.qualifiedName"/>
										<%
											final User user = entry.getAccountingTransaction().getResponsibleUser();
											if (user != null) {
										%>
												<span style="color: gray;">
													(<bean:message key="label.processedby" bundle="ACCOUNTING_RESOURCES" />
													<%= user.getPerson().getNickname() %>)
												</span>
										<% } %>
								<% } else { %>
										<html:link action="<%= "/payments.do?method=viewEvent&eventId=" + entry.getAccountingTransaction().getEvent().getExternalId() %>">
											<%
												final Properties properties = new Properties();
												properties.put("enum", "ENUMERATION_RESOURCES");
												properties.put("application", "APPLICATION_RESOURCES");
												properties.put("default", "APPLICATION_RESOURCES");
												final IMessageResourceProvider provider = new RendererMessageResourceProvider(properties);
											%>
											<%= entry.getAccountingTransaction().getEvent().getDescription().toString(provider) %>
										</html:link>
								<% } %>
							</td>
							<td>
								<%= entry.getWhenRegistered() %>
							</td>
							<td>
								<%= entry.getAmountWithAdjustment() %>
							</td>
						</tr>
					</logic:iterate>
					<tr class="aright">
						<td colspan="3">
							<span style="padding-right: 5px;"><bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.balance" />: </span> <bean:write name="balance"/>
						</td>
					</tr>
				</table>
			</logic:notEmpty>
	<% } else { %>
			<h3>
				<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.person.payments.affiliation.terms.and.conditions.title"/>
			</h3>
			<p>
				<bean:message bundle="ACCOUNTING_RESOURCES" key="label.accounting.person.payments.affiliation.terms.and.conditions.text"/>
			</p>
			<form action="<%= request.getContextPath() + "/person/payments.do" %>" method="post">
				<html:hidden property="method" value="acceptTermsAndConditions"/>
				<html:hidden property="affiliationOid" value="<%= affiliation.getExternalId() %>"/>
				<p>
					<bean:message bundle="ACCOUNTING_RESOURCES" key="label.termsAndConditions.read"/>:
					<input type="checkbox" name="readTermsAndConditions"/>
				</p>
				<p>
					<html:submit>
						<bean:message bundle="ACCOUNTING_RESOURCES" key="label.acceptTermsAndConditions"/>
					</html:submit>
				</p>
			</form>
	<% } %>
</logic:present>


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
