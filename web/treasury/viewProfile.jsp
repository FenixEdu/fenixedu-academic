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

	<table> 
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
		</tr> 
	</table> 
</div> 

<logic:notPresent name="event">
	<h3 class="mtop3 mbottom05">
		<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.recent.entrie"/>
		<bean:write name="person" property="username"/>
	</h3>
</logic:notPresent>

	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person" toScope="request"/>
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
		<logic:present name="event">
			<jsp:include page="showEventInTalbe.jsp"/>
		</logic:present>
		<logic:notPresent name="event">
			<jsp:include page="listEvents.jsp"/>
		</logic:notPresent>
	</table>
	<logic:present name="event">
		<jsp:include page="showEvent.jsp"/>
	</logic:present>
