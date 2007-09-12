<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.card.list"/>
</h2>

<fr:form action="/cardManagement.do?method=showUsers">
	<fr:edit id="libraryCardSearch" name="libraryCardSearch" schema="library.card.search">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5"/>
			<fr:property name="columnClasses" value=",,tdclear error0"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>
</fr:form>

<bean:define id="sortedBy">
	<%= request.getParameter("sortBy") == null ? "number" : request.getParameter("sortBy") %>
</bean:define>
	
<bean:size id="numberOfElements" name="libraryCardSearch" property="search"/>
<p class="mtop15">
	<span class="warning0"><bean:message key="message.card.numberOfPersons" bundle="LIBRARY_RESOURCES" arg0="<%= numberOfElements.toString() %>"/></span>
</p>
		
<fr:view name="libraryCardSearch" property="search" schema="library.card.list">
	<fr:layout name="tabular-sortable">
		<fr:property name="classes" value="tstyle1"/>
		
		<fr:property name="sortParameter" value="sortBy"/>
        <fr:property name="sortUrl" value="/cardManagement.do?method=showUsers"/>
        <fr:property name="sortBy" value="<%= sortedBy %>"/>
        
        <fr:property name="link(generate)" value="/cardManagement.do?method=prepareGenerateCard"/>
		<fr:property name="param(generate)" value="person.idInternal/personID"/>
		<fr:property name="key(generate)" value="link.card.generate"/>
		<fr:property name="visibleIf(generate)" value="isToGenerate"/>
		
		<fr:property name="link(details)" value="/cardManagement.do?method=showDetails"/>
		<fr:property name="param(details)" value="libraryCardID"/>
		<fr:property name="key(details)" value="link.card.details"/>
		<fr:property name="visibleIf(details)" value="isToViewDetails"/>
		
		<fr:property name="link(letter)" value="/cardManagement.do?method=generatePdfLetter"/>
		<fr:property name="param(letter)" value="libraryCardID"/>
		<fr:property name="key(letter)" value="link.card.emitLetter"/>
		<fr:property name="visibleIf(letter)" value="isToViewDetails"/>
	</fr:layout>
</fr:view>
