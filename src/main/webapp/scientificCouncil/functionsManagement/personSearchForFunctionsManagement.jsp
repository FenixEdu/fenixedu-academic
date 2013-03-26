<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
				
	<h:outputText value="<h2>#{bundle['link.functions.management']}</h2>" escape="false" />	
	<h:form>						
  	    <h:outputText value="<p class='mtop15 mbottom05'><strong>#{bundle['label.find.person.name']}</strong></p>" escape="false"/>
		<h:outputText value="<table class='tstyle5 thlight thright mtop05'>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{bundle['label.name']}:" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.personName']}" id="name" required="true" size="30" value="#{scientificCouncilFunctionsManagementBackingBean.personName}"/>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.searchPerson']}" action="#{scientificCouncilFunctionsManagementBackingBean.searchPersonByName}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>

		<h:message for="name" styleClass="error"/>
	</h:form>
	
	
	<h:form>
		<h:outputText value="<p class='mbottom05'><strong>#{bundle['label.find.person.number']}</strong></p>" escape="false"/>
		<h:outputText value="<table class='tstyle5 thlight thright mtop05'>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{bundle['label.type']}" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
			   	<fc:selectOneMenu value="#{scientificCouncilFunctionsManagementBackingBean.personType}">
					<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.personTypes}"/>
				</fc:selectOneMenu>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>	
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{bundle['label.number']}:" escape="false"/>			
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.personNumber']}" id="number" required="true" size="5" value="#{scientificCouncilFunctionsManagementBackingBean.personNumber}"/>			
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>	
			<h:outputText value="<th>" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:commandButton alt="#{htmlAltBundle['commandButton.searchPerson']}" action="#{scientificCouncilFunctionsManagementBackingBean.searchPersonByNumber}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
				<h:message for="number" styleClass="error"/>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>
	</h:form>	
		
	<h:outputText styleClass="error" rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.errorMessage}"
			value="#{bundle[scientificCouncilFunctionsManagementBackingBean.errorMessage]}"/>
			
	<h:outputText value="<p><em><b>#{scientificCouncilFunctionsManagementBackingBean.personsNumber}</b> #{bundle['label.number.of.persons.finded']}</em></p>" 
		escape="false"/>							
	
	<c:if test="${scientificCouncilFunctionsManagementBackingBean.personsNumber > 0}">
		<c:if test="${scientificCouncilFunctionsManagementBackingBean.numberOfPages != 1}">
			<c:out value="${bundle['label.page']}: "/>
			<c:forEach begin="1" end="${scientificCouncilFunctionsManagementBackingBean.numberOfPages}" var="pageIndex">
				<c:url var="url" value="/scientificCouncil/functionsManagement/personSearchForFunctionsManagement.faces?name=${scientificCouncilFunctionsManagementBackingBean.personName}&pageIndex=${pageIndex}" />
				<c:choose>
					<c:when test="${pageIndex == scientificCouncilFunctionsManagementBackingBean.numberOfPages}">						
						<c:choose>
							<c:when test="${scientificCouncilFunctionsManagementBackingBean.pageIndex == pageIndex}">
								<c:out value="${pageIndex}"/>	
							</c:when>
							<c:otherwise>
								<a href="<c:out value="${url}"/>">
									<c:out value="${pageIndex}" />
								</a>	
							</c:otherwise>
						</c:choose>
				    </c:when>
				    <c:otherwise>					    
						<c:choose>
							<c:when test="${scientificCouncilFunctionsManagementBackingBean.pageIndex == pageIndex}">
								<c:out value="${pageIndex},"/>		
							</c:when>
							<c:otherwise>
								<a href="<c:out value="${url}"/>">
									<c:out value="${pageIndex}" />
								</a>
								<c:out value=" , " />
							</c:otherwise>
						</c:choose>		    
				    </c:otherwise>					
				</c:choose>							
			</c:forEach>							
			<c:out value="<br/><br/>" escapeXml="false" />
		</c:if>
	</c:if>

	<c:if test="${scientificCouncilFunctionsManagementBackingBean.personsNumber > 0}">
		<c:out value="<ul>" escapeXml="false"/>	
		<c:forEach items="${scientificCouncilFunctionsManagementBackingBean.personsList}" var="person">
			<c:url var="url" value="/scientificCouncil/functionsManagement/listPersonFunctions.faces?personID=${person.idInternal}" />
			<c:out value="<li>" escapeXml="false"/>
			<a href="<c:out value="${url}"/>">
				<c:out value="${person.name}" />
			</a>
			<c:out value="</li>" escapeXml="false"/>										
		</c:forEach>
		<c:out value="</ul>" escapeXml="false"/>
	</c:if>
		
</ft:tilesView>