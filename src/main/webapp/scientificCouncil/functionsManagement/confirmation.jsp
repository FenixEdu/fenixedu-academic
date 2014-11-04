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

	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.unitIDHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.personIDHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.creditsHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.beginDateHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.endDateHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.functionIDHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.executionPeriodHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.durationHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.disabledVarHidden}"/>

	<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
	
	<h:outputText styleClass="error" rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[scientificCouncilFunctionsManagementBackingBean.errorMessage]}"/>
	

	<h:outputText value="<p>" escape="false"/>
		<h:outputText value="#{bundle['label.name']}: " escape="false"/>		
		<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.person.name}"/>		
	<h:outputText value="</p>" escape="false"/>

	
	<h:outputText value="<table class='tstyle2 thlight thright'>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>" escape="false"/>
			<h:outputText value="#{bundle['label.new.function']}" escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.function.name}"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>	
		<h:outputText value="<th>" escape="false"/>			
			<h:outputText value="#{bundle['label.search.unit']}:" escape="false"/>	
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>	
		<h:outputText value="<th>" escape="false"/>			
			<h:outputText value="#{bundle['label.credits']}" escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.credits}"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>	
		<h:outputText value="<th>" escape="false"/>			
			<h:outputText value="#{bundle['label.valid']}" escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.beginDate}" escape="false"/>
			<h:outputText value="&nbsp;#{bundle['label.to']}&nbsp;" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.endDate}" escape="false"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>


	<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirme']}" action="#{scientificCouncilFunctionsManagementBackingBean.associateNewFunction}" value="#{bundle['label.confirme']}" styleClass="inputbutton"/>			
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterUnit" immediate="true" value="#{bundle['alter.unit.button']}" styleClass="inputbutton"/>						
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterFunction" immediate="true" value="#{bundle['alter.function.button']}" styleClass="inputbutton"/>								
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>
	<h:outputText value="</p>" escape="false"/>
	
	</h:form>

</f:view>