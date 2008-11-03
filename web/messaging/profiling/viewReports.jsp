<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>


<logic:present name="date">
	<fr:form action="/profileReport.do?method=viewReports">
		<fr:edit id="date" name="date" slot="localDate" type="org.joda.time.LocalDate"/>
	<html:submit>wee</html:submit>
	</fr:form>

</logic:present>

<bean:define id="localDate" value="<%= request.getParameter("date") != null ? request.getParameter("date") : ""%>" toScope="request"/>

<logic:notEmpty name="servers">
	<h2>Servidores</h2>
	<fr:view name="servers" schema="serverTimings">
			<fr:layout name="tabular-sortable">
				<fr:property name="sortParameter" value="sort"/>
				<fr:property name="sortBy" value="<%= request.getParameter("sort") == null ? "maxTimeSpent" : request.getParameter("sort")%>"/>
				<fr:property name="sortUrl" value="<%= "/profileReport.do?method=viewReports&date=" + localDate%>"/>
				<fr:property name="classes" value="tstyle3"/>
			</fr:layout>
	</fr:view>
</logic:notEmpty>	
	
<logic:notEmpty name="modules">
		<h2><%= request.getParameter("serverName") %></h2>
		<h3>Módulos</h3>
		<ul>
			<li>
				<html:link page="<%= "/profileReport.do?method=viewReports&date=" + localDate%>">Voltar</html:link>
			</li>
		</ul>
		<fr:view name="modules" schema="moduleTimings">
			<fr:layout name="tabular-sortable">
				<fr:property name="sortParameter" value="sort"/>
				<fr:property name="sortBy" value="<%= request.getParameter("sort") == null ? "maxTimeSpent" : request.getParameter("sort")%>"/>
				<fr:property name="sortUrl" value="<%= "/profileReport.do?method=viewModules&date=" + localDate + "&serverName=" + request.getParameter("serverName") %>"/>
				<fr:property name="classes" value="tstyle3"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:notEmpty name="requests">
		<h2>Requests</h2>
		<h3><%= request.getParameter("serverName") + " > " + request.getParameter("moduleName") %></h3>
		<ul>
			<li>
				<html:link page="<%= "/profileReport.do?method=viewModules&date=" + localDate + "&serverName=" + request.getParameter("serverName") %>">Voltar</html:link>
			</li>
		</ul>
		<fr:view name="requests" schema="requestTimings">
			<fr:layout name="tabular-sortable">
				<fr:property name="sortParameter" value="sort"/>
				<fr:property name="sortBy" value="<%= request.getParameter("sort") == null ? "maxTimeSpent" : request.getParameter("sort")%>"/>
				<fr:property name="sortUrl" value="<%= "/profileReport.do?method=viewRequests&date=" + localDate + "&serverName=" + request.getParameter("serverName") + "&moduleName=" + request.getParameter("moduleName")%>"/>
				<fr:property name="classes" value="tstyle3"/>
			</fr:layout>
		</fr:view>
</logic:notEmpty>