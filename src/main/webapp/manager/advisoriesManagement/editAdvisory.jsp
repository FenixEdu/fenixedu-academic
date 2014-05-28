<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<h:form>
	
		<h:outputText value="<h2>#{bundle['label.edit.advisory']}</h2><br/>" escape="false"/>
		
		<h:inputHidden binding="#{advisoriesManagementBackingBean.advisoryIDHidden}"/>
	
		<h:outputText styleClass="error" rendered="#{!empty advisoriesManagementBackingBean.errorMessage}"
				value="#{bundle[advisoriesManagementBackingBean.errorMessage]}"/>
	
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['property.advisory.from']}"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.sender']}" id="sender" required="true" size="50" value="#{advisoriesManagementBackingBean.sender}"/>
				<h:message for="sender" styleClass="error"/>
			</h:panelGroup>
		
			<h:outputText value="#{bundle['property.advisory.subject']}"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.subject']}" id="subject" required="true" size="50" value="#{advisoriesManagementBackingBean.subject}"/>
				<h:message for="subject" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['property.advisory.expirationDate']}"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.expires']}" id="expires" required="true" size="20" value="#{advisoriesManagementBackingBean.expires}" />				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="expires" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['property.advisory.message']}"/>
			<h:panelGroup>
				<h:inputTextarea id="message" required="true" cols="75" rows="8" value="#{advisoriesManagementBackingBean.message}"/>
				<h:message for="message" styleClass="error"/>			
			</h:panelGroup>
		 
		 </h:panelGrid>	
		 
		 <h:outputText value="<br/>#{bundle['property.advisory.recipients']}" escape="false"/>
		 <h:outputText value="<b>#{bundleEnumeration[advisoriesManagementBackingBean.peopleOfAdvisory.name]}</b><br/>" 
		 	rendered="#{advisoriesManagementBackingBean.peopleOfAdvisory != null}" escape="false"/>
	
	  	 <h:outputText value="<br/>" escape="false"/>
		 <h:commandButton alt="#{htmlAltBundle['commandButton.modifications']}" styleClass="inputbutton" action="#{advisoriesManagementBackingBean.editAdvisory}" value="#{bundle['label.manager.save.modifications']}" />
		 <h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" action="editAdvisory" immediate="true" value="#{bundle['label.return']}" />
					 		 		 	
	</h:form>
</f:view>