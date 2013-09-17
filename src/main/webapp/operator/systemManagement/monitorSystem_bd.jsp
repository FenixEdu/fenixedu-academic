<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@page import="java.util.Enumeration"%>
<%@page import="java.util.Calendar"%>
<%@page import="pt.utl.ist.fenix.tools.util.FileUtils"%>
<%@page import="java.io.InputStream"%>
<html:xhtml/><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.SessionCounterFilter" %><h2><bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.title"/></h2><br /><html:link module="/operator" page="/monitorSystem.do?method=monitor">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.refresh"/></html:link><br /><br /><br />
<dt:format pattern="yyyy/MM/dd HH:mm:ss"><%= Calendar.getInstance().getTimeInMillis() %></dt:format>
<br /><br />

<%
	final InputStream inputStream = this.getClass().getResourceAsStream("/.build.version");
%>

BuildVersion: <%= FileUtils.readFile(inputStream).toString() %>
<br /><br />
<logic:present name="systemInfoApplicationServer">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.applicationServer"/>	<br /><br />
	<bean:message bundle="MANAGER_RESOURCES" key="label.server.name"/><%= request.getServerName() %>
	<br/>
	<bean:message bundle="MANAGER_RESOURCES" key="label.real.server.name"/><%= System.getenv("HOSTNAME") %>
	<br/>
	<br/>	<bean:write name="startMillis"/>	<br/>	<bean:write name="endMillis"/>	<br/>	<bean:write name="chronology"/>	<br/>	<br/>	<strong><bean:message bundle="MANAGER_RESOURCES" key="label.note"/></strong>
	<bean:message bundle="MANAGER_RESOURCES" key="message.memory.units"/>	<table>		<tr>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.availableProcessors"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.freeMemory"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.totalMemory"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.maxMemory"/>			</th>		</tr>		<tr>			<td class="listClasses">							<bean:write name="systemInfoApplicationServer" property="availableProcessors"/>			</td>			<td class="listClasses">				<bean:write name="systemInfoApplicationServer" property="freeMemory"/>			</td>			<td class="listClasses">				<bean:write name="systemInfoApplicationServer" property="totalMemory"/>			</td>			<td class="listClasses">				<bean:write name="systemInfoApplicationServer" property="maxMemory"/>			</td>		</tr>	</table>
	<br/>
	<strong><bean:message bundle="MANAGER_RESOURCES" key="label.system.properties"/></strong>
	<br/>	<logic:iterate id="property" name="systemInfoApplicationServer" property="properties">		<bean:write name="property"/><br />	</logic:iterate></logic:present><logic:notPresent name="systemInfoApplicationServer">	<span class="error"><!-- Error messages go here -->		Error obtaining log information for application server.	</span></logic:notPresent><br /><br />
<strong><bean:message bundle="MANAGER_RESOURCES" key="label.system.env.properties"/></strong>
<br/><% request.setAttribute("systemEnv", System.getenv()); %>
<logic:iterate id="property" name="systemEnv">
	<bean:write name="property" property="key"/>=<bean:write name="property" property="value"/><br />
</logic:iterate>

<br/><br/>
<strong><bean:message bundle="MANAGER_RESOURCES" key="label.request.headers"/></strong>
<br/>
<%
	for (final Enumeration e = request.getHeaderNames(); e.hasMoreElements(); ) {
	    final Object o = e.nextElement();
	    %>
	    <strong>
	    <%= o.toString() %>
	    </strong>
	    =
	    <%= request.getHeader(o.toString()) %>
	    <br/>
	    <%
	}
%>