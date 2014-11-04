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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

	<h:inputHidden binding="#{functionsManagementBackingBean.unitIDHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.creditsHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.beginDateHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.endDateHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.functionIDHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.executionPeriodHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.durationHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.disabledVarHidden}"/>

	<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
	<h:outputText value="<br/>" escape="false" />
	
	<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
				value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
	
	<h:panelGrid columns="2">
		<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
		<h:outputText value="#{functionsManagementBackingBean.person.name}"/>		
	</h:panelGrid>	
	
	<h:outputText value="<br/>" escape="false" />
	
	<h:panelGrid columns="2" columnClasses="valigntop">		
		<h:outputText value="<b>#{bundle['label.new.function']}</b>" escape="false"/>	
		<h:outputText value="#{functionsManagementBackingBean.function.name}"/>
				
		<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>
		<h:outputText value="#{functionsManagementBackingBean.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>				

		<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>	
		<h:outputText value="#{functionsManagementBackingBean.credits}"/>
	
		<h:outputText value="<b>#{bundle['label.valid']}</b>" escape="false"/>	
		<h:panelGroup>
			<h:outputText value="#{functionsManagementBackingBean.beginDate}" escape="false"/>	
			<h:outputText value="<b>&nbsp;#{bundle['label.to']}&nbsp;</b>" escape="false"/>	
			<h:outputText value="#{functionsManagementBackingBean.endDate}" escape="false"/>		
		</h:panelGroup>
	</h:panelGrid>
		
	<h:outputText value="<br/>" escape="false" />
	<h:panelGrid columns="4">
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirme']}" action="#{functionsManagementBackingBean.associateNewFunction}" value="#{bundle['label.confirme']}" styleClass="inputbutton"/>			
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterUnit" immediate="true" value="#{bundle['alter.unit.button']}" styleClass="inputbutton"/>						
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterFunction" immediate="true" value="#{bundle['alter.function.button']}" styleClass="inputbutton"/>								
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>
	</h:panelGrid>
	
	</h:form>

</f:view>