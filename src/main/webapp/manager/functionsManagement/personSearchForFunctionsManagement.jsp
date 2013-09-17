<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<ft:tilesView definition="df.page.functionsManagement" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
				
	<h:outputText value="<H2>#{bundle['label.search.person']}</H2><br/>" escape="false" />	
	<h:form>						
  	    <h:outputText value="<p><strong>#{bundle['label.find.person.name']}</strong></p>" escape="false"/>
		<h:panelGrid columns="3" styleClass="infoop">			
			<h:outputText value="#{bundle['label.name']}:" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.personName']}" id="name" required="true" size="30" value="#{managerFunctionsManagementBackingBean.personName}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.searchPerson']}" action="#{managerFunctionsManagementBackingBean.searchPersonByName}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
		</h:panelGrid>		
		<h:message for="name" styleClass="error"/>
	</h:form>
	
	<h:outputText value="<br/>" escape="false" />							
	
	<h:form>
		<h:outputText value="<p><strong>#{bundle['label.find.person.number']}</strong></p>" escape="false"/>
	    <h:panelGrid columns="5" styleClass="infoop" width="36%">	    	 
	    	<h:outputText value="#{bundle['label.type']}" escape="false"/>
	    	<fc:selectOneMenu value="#{managerFunctionsManagementBackingBean.personType}">
				<f:selectItems value="#{managerFunctionsManagementBackingBean.personTypes}"/>
			</fc:selectOneMenu>
	    	<h:outputText value="#{bundle['label.number']}:" escape="false"/>	    		    				
			<h:inputText alt="#{htmlAltBundle['inputText.personNumber']}" id="number" required="true" size="5" value="#{managerFunctionsManagementBackingBean.personNumber}"/>			
			<h:commandButton alt="#{htmlAltBundle['commandButton.searchPerson']}" action="#{managerFunctionsManagementBackingBean.searchPersonByNumber}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
		</h:panelGrid>
		<h:message for="number" styleClass="error"/>
	</h:form>	
		
	<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
			value="#{bundle[managerFunctionsManagementBackingBean.errorMessage]}"/>
			
	<h:outputText value="<br/><br/><b>#{managerFunctionsManagementBackingBean.personsNumber}</b> #{bundle['label.number.of.persons.finded']}" 
		escape="false"/>			
			
	<h:outputText value="<br/><br/>" escape="false" />							
	
	<c:if test="${managerFunctionsManagementBackingBean.personsNumber > 0}">
		<c:if test="${managerFunctionsManagementBackingBean.numberOfPages != 1}">
			<c:out value="${bundle['label.page']}"/>
			<c:forEach begin="1" end="${managerFunctionsManagementBackingBean.numberOfPages}" var="pageIndex">
				<c:url var="url" value="/manager/functionsManagement/personSearchForFunctionsManagement.faces?name=${managerFunctionsManagementBackingBean.personName}&pageIndex=${pageIndex}" />
				<c:choose>
					<c:when test="${pageIndex == managerFunctionsManagementBackingBean.numberOfPages}">						
						<c:choose>
							<c:when test="${managerFunctionsManagementBackingBean.pageIndex == pageIndex}">
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
							<c:when test="${managerFunctionsManagementBackingBean.pageIndex == pageIndex}">
								<c:out value="${pageIndex},"/>		
							</c:when>
							<c:otherwise>
								<a href="<c:out value="${url}"/>">
									<c:out value="${pageIndex}," />
								</a>	
							</c:otherwise>
						</c:choose>		    
				    </c:otherwise>					
				</c:choose>							
			</c:forEach>							
			<c:out value="<br/><br/>" escapeXml="false" />
		</c:if>
	</c:if>
	
	<c:if test="${managerFunctionsManagementBackingBean.personsNumber > 0}">
		<c:out value="<ul>" escapeXml="false"/>	
		<c:forEach items="${managerFunctionsManagementBackingBean.personsList}" var="person">
			<c:url var="url" value="/manager/functionsManagement/listPersonFunctions.faces?personID=${person.externalId}" />
			<c:out value="<li>" escapeXml="false"/>
			<a href="<c:out value="${url}"/>">
				<c:out value="${person.name}" />
			</a>
			<c:out value="</li>" escapeXml="false"/>										
		</c:forEach>
		<c:out value="</ul>" escapeXml="false"/>
	</c:if>
			
</ft:tilesView>