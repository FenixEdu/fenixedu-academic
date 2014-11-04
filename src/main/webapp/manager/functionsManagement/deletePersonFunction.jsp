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

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.manager.ManagerApplications$ManagementFunctionsPage" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.personIDHidden}"/>
	   	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.personFunctionIDHidden}"/>
	
		<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
		<h:outputText value="<br/>" escape="false" />
		
		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
					value="#{bundle[managerFunctionsManagementBackingBean.errorMessage]}"/>
		
		<h:panelGrid columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{managerFunctionsManagementBackingBean.person.name}"/>		
		</h:panelGrid>	
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2" columnClasses="valigntop">		
			<h:outputText value="<b>#{bundle['label.function']}</b>" escape="false"/>	
			<h:outputText value="#{managerFunctionsManagementBackingBean.personFunction.function.name}"/>
					
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>								
			<h:outputText value="#{managerFunctionsManagementBackingBean.personFunction.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>	
		
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>	
			<h:outputText value="#{managerFunctionsManagementBackingBean.personFunction.credits}"/>
		
			<h:outputText value="<b>#{bundle['label.valid']}</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{managerFunctionsManagementBackingBean.personFunction.beginDate}" escape="false"/>	
				<h:outputText value="<b>&nbsp;#{bundle['label.to']}&nbsp;</b>" escape="false"/>	
				<h:outputText value="#{managerFunctionsManagementBackingBean.personFunction.endDate}" escape="false"/>		
			</h:panelGroup>
		</h:panelGrid>
			
		<h:outputText value="<br/>" escape="false" />
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['button.delete']}" action="#{managerFunctionsManagementBackingBean.deletePersonFunction}" value="#{bundle['button.delete']}" styleClass="inputbutton"/>							
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>								
		</h:panelGrid>
	
	</h:form>

</f:view>