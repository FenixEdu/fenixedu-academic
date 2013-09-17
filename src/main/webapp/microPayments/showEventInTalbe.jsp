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

<bean:define id="event" name="event" type="net.sourceforge.fenixedu.domain.accounting.Event"/>

<tr>
	<td style="text-align: center;">
		<%= event.getWhenOccured().toString("yyyy-MM-dd HH:mm") %>
	</td>
	<td style="text-align: left;">
		<bean:write name="event" property="description"/>
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
