<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.title.statitics" bundle="MESSAGING_RESOURCES"/></h2>

<logic:present name="date">
	<fr:form action="/profileReport.do?method=viewReports">
		<fr:edit id="date" name="date" slot="localDate" type="org.joda.time.LocalDate"/>
	<html:submit><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>

<bean:define id="localDate" value="<%= request.getParameter("date") != null ? request.getParameter("date") : request.getAttribute("date") != null ? ((net.sourceforge.fenixedu.dataTransferObject.VariantBean)request.getAttribute("date")).getLocalDate().toString() : ""%>" toScope="request"/>

<p>
<logic:notEmpty name="servers">
	<em>Servidores</em>
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
		<em>Módulos</em> (<%= request.getParameter("serverName") %>)
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
		<em>Requests</em> (<%= request.getParameter("serverName") + " > " + request.getParameter("moduleName") %>)
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
</p>