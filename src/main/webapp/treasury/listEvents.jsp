<%@page import="net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent"%>
<%@page import="pt.utl.ist.fenix.tools.resources.IMessageResourceProvider"%>
<%@page import="java.util.Properties"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.LabelFormatterRenderer"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.util.RendererMessageResourceProvider"%>
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
			for (final Event event : events) {
			    if (event instanceof MicroPaymentEvent) {
					continue;
			    }
				final Money amountToPay = event.getAmountToPay();
		%>
			<bean:define id="style">class="<% if (!amountToPay.isZero()) { %>debtRow<% } %>"</bean:define>
			<tr <%= style %>>
				<td class="acenter">
					<%= event.getWhenOccured().toString("yyyy-MM-dd HH:mm") %>
				</td>
				<td>
					<html:link action="<%= "/paymentManagement.do?method=viewEvent&eventId=" + event.getExternalId() %>">
						<%
							final Properties properties = new Properties();
							properties.put("enum", "ENUMERATION_RESOURCES");
							properties.put("application", "APPLICATION_RESOURCES");
							properties.put("default", "APPLICATION_RESOURCES");
							final IMessageResourceProvider provider = new RendererMessageResourceProvider(properties);
						%>
						<%= event.getDescription().toString(provider) %>
					</html:link>
				</td>
				<td class="aright">
					<%
						total = total.add(event.getOriginalAmountToPay());
					%>
					<%= event.getOriginalAmountToPay().toString() %>
				</td>
				<td class="aright">
					<%
						totalPayed = totalPayed.add(event.getPayedAmount());
					%>
					<%= event.getPayedAmount().toString() %>
				</td>				
				<td class="aright">
					<%
						totalToPay = totalToPay.add(amountToPay);
					%>
					<%= amountToPay.toString() %>
				</td>
			</tr>
		<% } %>
		<tr>
			<td colspan="2" class="aright">
				<bean:message key="label.total" bundle="TREASURY_RESOURCES" />:
			</td>
			<td class="aright">
				<%= total.toString() %>
			</td>
			<td class="aright">
				<%= totalPayed.toString() %>
			</td>
			<bean:define id="styleTotal">text-align: right; <% if (!totalToPay.isZero()) { %>font-weight: bold;<% } %></bean:define>
			<td style="<%= styleTotal %>">
				<%= totalToPay.toString() %>
			</td>
		</tr>
