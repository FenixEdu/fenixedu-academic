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

<br/>
<br/>
<logic:iterate id="person" name="people" type="net.sourceforge.fenixedu.domain.Person">
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person" toScope="request"/>
	<table class="tstyle1" width="75%">
		<tr>
			<th>
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
				<img width="60" height="60" src="<%= request.getContextPath() + url %>"/>
			</th>
			<td style="padding-left: 10px;">
				<strong>
					<bean:write name="person" property="name"/>
				</strong>
				(<bean:write name="person" property="username"/>)
				<br/>
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
		<logic:present name="event">
			<jsp:include page="showEventInTalbe.jsp"/>
		</logic:present>
		<logic:notPresent name="event">
			<jsp:include page="listEvents.jsp"/>
		</logic:notPresent>
	</table>
<%-- 
			<tr>
				<td colspan="2">
				</td>
        private java.lang.String cancelJustification;
        private net.sourceforge.fenixedu.domain.accounting.EventState eventState;
        private org.joda.time.DateTime eventStateDate;
        private net.sourceforge.fenixedu.domain.accounting.EventType eventType;
        private org.joda.time.DateTime whenOccured;
        private java.lang.String createdBy;
        private org.joda.time.LocalDate whenSentLetter;
        private java.lang.String ojbConcreteClass;
        private net.sourceforge.fenixedu.domain.Person person;
        private net.sourceforge.fenixedu.domain.RootDomainObject rootDomainObject;
        private net.sourceforge.fenixedu.domain.Employee employeeResponsibleForCancel;
 --%>
	<logic:present name="event">
		<jsp:include page="showEvent.jsp"/>
	</logic:present>
</logic:iterate>
