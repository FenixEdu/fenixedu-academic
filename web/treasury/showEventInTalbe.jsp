<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.util.RendererMessageResourceProvider"%>
<%@page import="pt.utl.ist.fenix.tools.resources.IMessageResourceProvider"%>
<%@page import="java.util.Properties"%>
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

<tr>
	<td style="text-align: center;">
		<%= event.getWhenOccured().toString("yyyy-MM-dd HH:mm") %>
	</td>
	<td style="text-align: left;">
		<%
			final Properties properties = new Properties();
			properties.put("enum", "ENUMERATION_RESOURCES");
			properties.put("application", "APPLICATION_RESOURCES");
			properties.put("default", "APPLICATION_RESOURCES");
			final IMessageResourceProvider provider = new RendererMessageResourceProvider(properties);
		%>
		<%= event.getDescription().toString(provider) %>
	</td>
	<td style="text-align: right;">
		<%= event.getOriginalAmountToPay().toString() %>
	</td>
	<td style="text-align: right;">
		<%= event.getPayedAmount().toString() %>
	</td>				
	<td style="text-align: right;">
		<%= event.getAmountToPay().toString() %>
	</td>
</tr>
