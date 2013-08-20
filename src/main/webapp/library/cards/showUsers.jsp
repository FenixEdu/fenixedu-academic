<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>
<html:xhtml/>

<h2>
	<bean:message key="title.library.card.list"/>
</h2>

<p>
	<span class="warning0">
		<html:messages id="message" property="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>
<fr:form action="/cardManagement.do?method=showUsers">
	<fr:edit id="libraryCardSearch" name="libraryCardSearch" schema="library.card.search">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear error0"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.confirm" bundle="LIBRARY_RESOURCES"/></html:submit>
</fr:form>

<logic:notPresent name="dontSearch">

	<bean:define id="query" value=""/>
	<logic:notEmpty name="libraryCardSearch" property="partyClassification">
		<bean:define id="partyClassification" name="libraryCardSearch" property="partyClassification"/>
		<bean:define id="query" value="<%="&partyClassification="+ partyClassification.toString()%>"/>
	</logic:notEmpty>
	<logic:notEmpty name="libraryCardSearch" property="userName">
		<bean:define id="userName" name="libraryCardSearch" property="userName"/>
		<bean:define id="query" value="<%=query+"&userName="+ userName%>"/>
	</logic:notEmpty>
	<logic:notEmpty name="libraryCardSearch" property="number">
		<bean:define id="number" name="libraryCardSearch" property="number"/>
		<bean:define id="query" value="<%=query+"&number="+ number.toString()%>"/>
	</logic:notEmpty>
		
	<bean:define id="sortedBy">
		<%= request.getParameter("sortBy") == null ? "number" : request.getParameter("sortBy") %>
	</bean:define>
	
	<logic:messagesPresent name="message">	
		<p class="mtop15">
			<em><bean:message key="message.card.numberOfPersons" bundle="LIBRARY_RESOURCES" arg0="0"/></em>
		</p>
	</logic:messagesPresent>
		
	
	<logic:messagesNotPresent name="message">	
		<bean:define id="libraryCardSearch" name="libraryCardSearch" type="net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardSearch"/>
		<logic:equal name="libraryCardSearch" property="partyClassification" value="PERSON">
			<p class="mtop15">
				<html:link page="<%= "/cardManagement.do?method=prepareCreatePerson&amp;userName=" + libraryCardSearch.getUserName() %>">
					<bean:message key="link.card.create.person"/>
				</html:link>
			</p>
		</logic:equal>
		
		<bean:size id="numberOfElements" name="libraryCardSearch" property="searchResult"/>		
		<p class="mtop15">
			<em><bean:message key="message.card.numberOfPersons" bundle="LIBRARY_RESOURCES" arg0="<%= numberOfElements.toString() %>"/></em>
		</p>
		
		<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
		<html:link page="<%= "/cardManagement.do?method=exportToExcel" + query.toString()%>">
			<bean:message key="link.exportToExcel" bundle="LIBRARY_RESOURCES"/>
		</html:link>
		
		<bean:define id="classification" name="libraryCardSearch" property="partyClassification"/>
		<bean:define id="searchName" value="" type="java.lang.String"/>
		<logic:notEmpty name="libraryCardSearch" property="userName">
			<bean:define id="searchName" name="libraryCardSearch" property="userName" type="java.lang.String"/>
		</logic:notEmpty>
		<bean:define id="searchNumber" name="libraryCardSearch" property="numberString"/>

		<br/>
		<br/>

		<bean:define id="url" type="java.lang.String">/library/cardManagement.do?method=showUsersFromPageSelect&amp;partyClassification=<bean:write name="libraryCardSearch" property="partyClassification"/>&amp;userName=<bean:write name="libraryCardSearch" property="userName"/>&amp;number=<bean:write name="libraryCardSearch" property="number"/></bean:define>
		<cp:collectionPages url="<%= url %>" 
			pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>

		<fr:view name="resultPage" schema="library.card.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="columnClasses" value="acenter,acenter,acenter,,,acenter,acenter,,acenter,nowrap"/>
				<fr:property name="rowClasses" value=",bgfafafa"/>
				
				<fr:property name="sortParameter" value="sortBy"/>
		        <fr:property name="sortUrl" value="<%="/cardManagement.do?method=showUsers"+query.toString()%>"/>
		        <fr:property name="sortBy" value="<%= sortedBy %>"/>
		        
		        <fr:property name="link(generate)" value="<%= "/cardManagement.do?method=prepareGenerateCard&name=" + searchName + "&number=" + searchNumber %>"/>
				<fr:property name="param(generate)" value="person.externalId/personID"/>
				<fr:property name="key(generate)" value="link.card.create"/>
				<fr:property name="visibleIf(generate)" value="isToGenerate"/>
				
				<fr:property name="link(details)" value="<%= "/cardManagement.do?method=showDetails&classification=" + classification + "&name=" + searchName + "&number=" + searchNumber %>"/>
				<fr:property name="param(details)" value="libraryCardID"/>
				<fr:property name="key(details)" value="link.card.details"/>
				<fr:property name="visibleIf(details)" value="isToViewDetails"/>
				
				<fr:property name="link(letter)" value="/cardManagement.do?method=generatePdfLetter"/>
				<fr:property name="param(letter)" value="libraryCardID"/>
				<fr:property name="key(letter)" value="link.card.emitLetter"/>
				<fr:property name="visibleIf(letter)" value="isToViewDetails"/>
			</fr:layout>
		</fr:view>
	</logic:messagesNotPresent>
</logic:notPresent>