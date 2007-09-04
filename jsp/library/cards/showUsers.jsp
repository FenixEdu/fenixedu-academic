<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.list"/>
</h2>

<bean:define id="sortedBy">
	<%= request.getParameter("sortBy") == null ? "number" : request.getParameter("sortBy") %>
</bean:define>
	
<fr:view name="libraryCardsList" schema="library.card.list">
	<fr:layout name="tabular-sortable">
		<fr:property name="classes" value="tstyle1"/>
		
		<fr:property name="sortParameter" value="sortBy"/>
        <fr:property name="sortUrl" value="/cardManagement.do?method=showUsers"/>
        <fr:property name="sortBy" value="<%= sortedBy %>"/>
        
        <fr:property name="link(generate)" value="/cardManagement.do?method=prepareGenerateCard"/>
		<fr:property name="param(generate)" value="person.idInternal/personID"/>
		<fr:property name="key(generate)" value="link.card.generate"/>
	</fr:layout>
</fr:view>
