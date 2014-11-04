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
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.messaging.MessagingApplication$OrganizationalStructurePage" />

<f:view>

<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
<f:loadBundle basename="resources/MessagingResources" var="messagingResources"/>
	
<h:form>

	<h:inputHidden binding="#{organizationalStructure.unitIDHidden}" />			

	<h:outputText value="#{organizationalStructure.title}" escape="false"/>	

	
	<h:outputText value="<table class='tstyle2 thlight thright thbgnone'>" escape="false"/>

		<h:panelGroup rendered="#{!empty organizationalStructure.unit.webAddress}">
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{messagingResources['label.unit.webAddress']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:outputText value="<a href='#{organizationalStructure.unit.webAddress}' target='_blank'>#{organizationalStructure.unit.webAddress}</a>" escape="false" rendered="#{!empty organizationalStructure.unit.webAddress}"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
		</h:panelGroup>

		<h:panelGroup rendered="#{!empty organizationalStructure.unit.costCenterCode}">
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{messagingResources['label.unit.costCenterCode']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:outputText value="#{organizationalStructure.unit.costCenterCode}" escape="false"/>		
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{messagingResources['label.choose.year']}:" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<fc:selectOneMenu value="#{organizationalStructure.choosenExecutionYearID}" onchange="this.form.submit();">
					<f:selectItems value="#{organizationalStructure.executionYears}" />
				</fc:selectOneMenu>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{messagingResources['label.find.organization.listing.type']}:" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<fc:selectOneMenu value="#{organizationalStructure.listType}" onchange="this.form.submit();">
					<f:selectItems value="#{organizationalStructure.listingType}"/>				
				</fc:selectOneMenu>	
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>

	<h:outputText value="<p>" escape="false"/>
		<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/messaging/organizationalStructure/structurePage.faces">
			<h:outputText value="« #{messagingResources['messaging.back.label']}" escape="false"/>
		</h:outputLink>
	<h:outputText value="</p>" escape="false"/>

	<h:outputText value="#{organizationalStructure.functions}" escape="false"/>			

</h:form>


<h:outputText value="<div class='mtop2 mbottom025'><em>" escape="false" /><h:outputText value="#{messagingResources['label.subtitle']}:" escape="false"/><h:outputText value="</em></div>" escape="false" />
<h:outputText value="<div class='mvert025'><div style='width: 10px; height: 10px; background-color: #86705a; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>" escape="false" /><h:outputText value="#{messagingResources['label.unit.working.employees']}" escape="false"/><h:outputText value="</em></div></div>" escape="false" />
<h:outputText value="<div class='mvert025'><div style='width: 10px; height: 10px; background-color: #5a7086; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>" escape="false" /><h:outputText value="#{messagingResources['label.person.function']}" escape="false"/><h:outputText value="</em></div></div>" escape="false" />

			
</f:view> 