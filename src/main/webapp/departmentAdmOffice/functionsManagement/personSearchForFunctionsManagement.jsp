<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
				
	<h:outputText value="<H2>#{bundle['label.search.person']}</H2><br/>" escape="false" />	
	<h:form>						
  	    <h:outputText value="<p><strong>#{bundle['label.find.person.name']}</strong></p>" escape="false"/>
		<h:panelGrid columns="3" styleClass="infoop">			
			<h:outputText value="#{bundle['label.name']}:" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.personName']}" id="name" required="true" size="30" value="#{functionsManagementBackingBean.personName}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.searchPerson']}" action="#{functionsManagementBackingBean.searchPersonByName}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
		</h:panelGrid>		
		<h:message for="name" styleClass="error"/>
	</h:form>
	
	<h:outputText value="<br/>" escape="false" />							
	
	<h:form>
		<h:outputText value="<p><strong>#{bundle['label.find.person.number']}</strong></p>" escape="false"/>
	    <h:panelGrid columns="5" styleClass="infoop">	    	 
	    	<h:outputText value="#{bundle['label.type']}" escape="false"/>
	    	<fc:selectOneMenu value="#{functionsManagementBackingBean.personType}">
				<f:selectItems value="#{functionsManagementBackingBean.personTypes}"/>
			</fc:selectOneMenu>
	    	<h:outputText value="#{bundle['label.number']}:" escape="false"/>	    		    				
			<h:inputText alt="#{htmlAltBundle['inputText.personNumber']}" id="number" required="true" size="5" value="#{functionsManagementBackingBean.personNumber}"/>			
			<h:commandButton alt="#{htmlAltBundle['commandButton.searchPerson']}" action="#{functionsManagementBackingBean.searchPersonByNumber}" styleClass="inputbutton" value="#{bundle['button.searchPerson']}"/>
		</h:panelGrid>
		<h:message for="number" styleClass="error"/>
	</h:form>	
		
	<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
			value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
			
	<h:outputText value="<br/><br/><b>#{functionsManagementBackingBean.personsNumber}</b> #{bundle['label.number.of.persons.finded']}" 
		escape="false"/>			
			
	<h:outputText value="<br/><br/>" escape="false" />							
	
	<c:if test="${functionsManagementBackingBean.personsNumber > 0}">
		<c:if test="${functionsManagementBackingBean.numberOfPages != 1}">
			<c:out value="${bundle['label.page']}"/>
			<c:forEach begin="1" end="${functionsManagementBackingBean.numberOfPages}" var="pageIndex">
				<c:url var="url" value="/departmentAdmOffice/functionsManagement/personSearchForFunctionsManagement.faces?name=${functionsManagementBackingBean.personName}&page=${pageIndex}" />
				<c:choose>
					<c:when test="${pageIndex == functionsManagementBackingBean.numberOfPages}">						
						<c:choose>
							<c:when test="${functionsManagementBackingBean.page == pageIndex}">
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
							<c:when test="${functionsManagementBackingBean.page == pageIndex}">
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
	
	<%-- 
	<h:panelGroup rendered="#{!empty functionsManagementBackingBean.personsList}">
		<h:outputText value="<ul>" escape="false" />
		<h:dataTable value="#{functionsManagementBackingBean.personsList}" var="person">		
			<h:column>
				<h:outputText value="<li>" escape="false" />				
					<h:outputLink value="#{functionsManagementBackingBean.contextPath}/departmentAdmOffice/teacher/functionsManagement/unitChoose.faces?personID=#{person.externalId}">
						<h:outputText value="#{person.name}"/>
					</h:outputLink>		
				<h:outputText value="</li>" escape="false"/>
			</h:column>		
		</h:dataTable>
		<h:outputText value="</ul>" escape="false" />
	</h:panelGroup>		
	--%>
	
	<c:if test="${functionsManagementBackingBean.personsNumber > 0}">
		<c:out value="<ul>" escapeXml="false"/>	
		<c:forEach items="${functionsManagementBackingBean.personsList}" var="person">
			<c:url var="url" value="/departmentAdmOffice/functionsManagement/listPersonFunctions.faces?personID=${person.externalId}" />
			<c:out value="<li>" escapeXml="false"/>
			<a href="<c:out value="${url}"/>">
				<c:out value="${person.name}" />
			</a>
			<c:out value="</li>" escapeXml="false"/>										
		</c:forEach>
		<c:out value="</ul>" escapeXml="false"/>
	</c:if>
		
</f:view>