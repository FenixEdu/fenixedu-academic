<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@page import="java.util.Enumeration"%>
<%@page import="java.util.Calendar"%>
<%@page import="pt.utl.ist.fenix.tools.util.FileUtils"%>
<%@page import="java.io.InputStream"%>
<html:xhtml/><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.SessionCounterFilter" %><h2><bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.title"/></h2><br /><html:link module="/manager" page="/monitorSystem.do?method=monitor">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.refresh"/></html:link><br /><html:link module="/manager" page="/monitorSystem.do?method=dumpThreadTrace" target="_blank">        Thread Dump</html:link><br /><br /><br /><html:link module="/manager" page="/monitorSystem.do?method=testRestlet">	Testar restlet</html:link><br /><br /><br /><html:link module="/manager" page="/monitorSystem.do?method=warmUpCacheForEnrolmentPeriodStart">	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.warm.up.cache.for.enrolment.period.start"/></html:link><br /><br /><br />
<dt:format pattern="yyyy/MM/dd HH:mm:ss"><%= Calendar.getInstance().getTimeInMillis() %></dt:format>
<br /><br />

<%
	final InputStream inputStream = this.getClass().getResourceAsStream("/build.version");
%>

BuildVersion: <%= FileUtils.readFile(inputStream).toString() %>
<br /><br />
	<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.applicationServer"/>	<br /><br />		<%-- Barra as authentication broker Switch --%>	<style>		.barra-auth-div { background-color: rgba(166,193,199,0.1); border-style: dashed; border-width: 1px; border-color: #8FC0D9; padding-bottom: 8px; padding-top: 8px; margin-bottom: 15px; width: 300px; }		.barra-auth-label { padding-left: 15px; }		.field { display:inline-block; vertical-align: middle; padding-left: 15px; }		.cb-enable, .cb-disable, .cb-enable span, .cb-disable span { background: url('<%= request.getContextPath() + "/images/switch.gif" %>') repeat-x; display: block; float: left; }	    .cb-enable span, .cb-disable span { line-height: 30px; display: block; background-repeat: no-repeat; font-weight: bold; }	    .cb-enable span { background-position: left -90px; padding: 0 10px; }	    .cb-disable span { background-position: right -180px;padding: 0 10px; }	    .cb-disable.selected { background-position: 0 -30px; }	    .cb-disable.selected span { background-position: right -210px; color: #fff; }	    .cb-enable.selected { background-position: 0 -60px; }	    .cb-enable.selected span { background-position: left -150px; color: #fff; }	    .switch label { cursor: pointer; }	    .switch input { display: none; }	    .noAnchor { color: #333 !important; border-bottom: none !important; }	</style>	<div class="barra-auth-div">		<span class="barra-auth-label"><strong><em>Barra</em> authentication:</strong></span>		<div class="field switch">			<logic:equal name="useBarraAsAuth" value="true" >				<a class="noAnchor cb-enable selected" id="barraOn" href="#">					<span>Enable</span>				</a>				<a class="noAnchor cb-disable" id="barraOff" href="<%= request.getContextPath() + "/manager/monitorSystem.do?method=switchBarraAsAuthenticationBroker&useBarraAsAuth=false" %>">					<span>Disable</span>				</a>	    	</logic:equal>	    	<logic:equal name="useBarraAsAuth" value="false" >	    		<a class="noAnchor cb-enable" id="barraOn" href="<%= request.getContextPath() + "/manager/monitorSystem.do?method=switchBarraAsAuthenticationBroker&useBarraAsAuth=true" %>">					<span>Enable</span>				</a>				<a class="noAnchor cb-disable selected" id="barraOff" href="#">					<span>Disable</span>				</a>	    	</logic:equal>		</div>	</div>	<%-- End Barra as authentication broker Switch --%>	
	<bean:message bundle="MANAGER_RESOURCES" key="label.server.name"/><%= request.getServerName() %>
	<br/>
	<bean:message bundle="MANAGER_RESOURCES" key="label.real.server.name"/><%= System.getenv("HOSTNAME") %>
	<br/>
	<br/>	<bean:write name="startMillis"/>	<br/>	<bean:write name="endMillis"/>	<br/>	<bean:write name="chronology"/>	<br/>	<br/>	<bean:message bundle="MANAGER_RESOURCES" key="cache.domain.number"/>:	<bean:write name="cacheSize"/>	<br /> <br />	<strong><bean:message bundle="MANAGER_RESOURCES" key="label.note"/></strong>
	<bean:message bundle="MANAGER_RESOURCES" key="message.memory.units"/>	<table>		<tr>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.availableProcessors"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.freeMemory"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.totalMemory"/>			</th>			<th class="listClasses-header">				<bean:message bundle="MANAGER_RESOURCES" key="manager.monitor.system.maxMemory"/>			</th>		</tr>		<tr>			<td class="listClasses">							<%= Runtime.getRuntime().availableProcessors() %>			</td>			<td class="listClasses">				<%= Runtime.getRuntime().freeMemory() %>			</td>			<td class="listClasses">				<%= Runtime.getRuntime().totalMemory() %>			</td>			<td class="listClasses">				<%= Runtime.getRuntime().maxMemory() %>			</td>		</tr>	</table>
	<br/>
	<strong><bean:message bundle="MANAGER_RESOURCES" key="label.system.properties"/></strong>
	<br/>	<logic:iterate id="property" name="properties">		<bean:write name="property"/><br />	</logic:iterate><br /><br />
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