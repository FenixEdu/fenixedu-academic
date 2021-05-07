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

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputFormat value="<h2>#{scouncilBundle['create.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree.officialPublication']}"/>
	</h:outputFormat>
	<h:form>

		<h:outputText escape="false"
			value="<input alt='input.degreeId' id='degreeId' name='degreeId' type='hidden' value='#{DegreeManagement.degreeId}'/>" />

		<h:outputText escape="false"
		value="<input alt='input.selectedExecutionYearId' id='selectedExecutionYearId' name='selectedExecutionYearId' type='hidden' value='#{DegreeManagement.selectedExecutionYearId}'/>" />
			
		<h:outputText styleClass="error0" rendered="#{!empty DegreeManagement$OfficialPublicationBean.errorMessage}"
			value="#{DegreeManagement.errorMessage}<br/>" escape="false"/>
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>			

		
		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>

		<h:outputText value="<tr>" escape="false"/>
  
		
		<h:outputText value="<th><span ></span> #{scouncilBundle['name']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="officialReference" value="#{DegreeManagement.officialPublicationBean.officialReference}" maxlength="50" size="50"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
				<h:outputText value="</tr>" escape="false"/>
	
		<h:outputText value="<tr>" escape="false"/>
  
		
		<h:outputText value="<th><span ></span> #{scouncilBundle['label.degree.officialPublication.linkReference']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="linkReference" value="#{DegreeManagement.officialPublicationBean.linkReference}" maxlength="100" size="100"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
				<h:outputText value="</tr>" escape="false"/>
				
						
				<h:outputText value="<th><span ></span> #{scouncilBundle['label.degree.officialPublication.creation.date']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="date" value="#{DegreeManagement.officialPublicationBean.date}" maxlength="100" size="10"/>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>
			<h:outputText value="</tr>" escape="false"/>
			
		<h:outputText value="<th><span ></span> #{scouncilBundle['label.degree.officialPublication.includeInDiplomaSuplement']}:</th><td>" escape="false"/>
		<h:panelGroup>
			<h:selectBooleanCheckbox id="includeInDiplomaSuplement" value="#{DegreeManagement.officialPublicationBean.includeInDiplomaSuplement}">
			</h:selectBooleanCheckbox>
			<h:message for="nameEn" errorClass="error0" rendered="#{empty DegreeManagement.errorMessage}"/>
		</h:panelGroup>


<h:outputText value="</table>" escape="false"/>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{scouncilBundle['create']}"
			action="#{DegreeManagement.officialPublicationBean.makeAndInsertDegreeOfficialPublication}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="editDegree"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</f:view>
