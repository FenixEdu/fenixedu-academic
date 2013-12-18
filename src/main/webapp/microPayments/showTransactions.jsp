<%@page import="org.fenixedu.bennu.core.domain.User"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent"%>
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

<html:xhtml />

<h2><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.transactions.header"/></h2>

<p>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.person.description"/>
</p>

<jsp:include page="searchMovementsForm.jsp"/>

<h3>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.transactions.table.title"/>
</h3>
<logic:empty name="microPaymentEvents">
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.transactions.none"/>
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
						final User user = User.findByUsername(microPaymentEvent.getCreatedBy());
						if (user != null) {
					%>
							<%= user.getPerson().getNickname() %>
					<%
						}
					%>
					(<%= microPaymentEvent.getCreatedBy() %>)
				</td>
				<td>
					<bean:define id="person" name="microPaymentEvent" property="person"/>
					<html:link action="/operator.do?method=showPerson" paramId="personOid" paramName="person" paramProperty="externalId">
						<%= microPaymentEvent.getPerson().getNickname() %>
						(<%= microPaymentEvent.getPerson().getUsername() %>)
					</html:link>
				</td> 
				<td class="aright">
					<bean:write name="microPaymentEvent" property="payedAmount"/>
				</td> 
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
