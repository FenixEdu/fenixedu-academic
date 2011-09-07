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


		<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
		<%
			final List<Event> events = new ArrayList<Event>(person.getEvents());
			Collections.sort(events, Event.COMPARATOR_BY_DATE);
			Money total = Money.ZERO;
			Money totalPayed = Money.ZERO;
			Money totalToPay = Money.ZERO;
		%>
		<logic:iterate id="event" name="person" property="events" type="net.sourceforge.fenixedu.domain.accounting.Event">
			<%
				final Money amountToPay = event.getAmountToPay();
			%>		
			<bean:define id="style">text-align: right; <% if (!amountToPay.isZero()) { %>font-weight: bold;<% } %></bean:define>
			<tr>
				<td style="<%= style + "text-align: center;" %>">
					<%= event.getWhenOccured().toString("yyyy-MM-dd HH:mm") %>
				</td>
				<td style="text-align: left;">
					<html:link action="/paymentManagement.do?method=viewEvent" paramId="eventId" paramName="event" paramProperty="externalId">
						<bean:write name="event" property="description"/>
					</html:link>
				</td>
				<td style="<%= style + "text-align: right;" %>">
					<%
						total = total.add(event.getOriginalAmountToPay());
					%>
					<%= event.getOriginalAmountToPay().toString() %>
				</td>
				<td style="<%= style + "text-align: right;" %>">
					<%
						totalPayed = totalPayed.add(event.getPayedAmount());
					%>
					<%= event.getPayedAmount().toString() %>
				</td>				
				<td style="<%= style %>">
					<%
						totalToPay = totalToPay.add(amountToPay);
					%>
					<%= amountToPay.toString() %>
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan="2" style="text-align: right;">
				<bean:message key="label.total" bundle="TREASURY_RESOURCES" />:
			</td>
			<td style="text-align: right;">
				<%= total.toString() %>
			</td>
			<td style="text-align: right;">
				<%= totalPayed.toString() %>
			</td>
			<bean:define id="styleTotal">text-align: right; <% if (!totalToPay.isZero()) { %>font-weight: bold;<% } %></bean:define>
			<td style="<%= styleTotal %> ;">
				<%= totalToPay.toString() %>
			</td>
		</tr>
