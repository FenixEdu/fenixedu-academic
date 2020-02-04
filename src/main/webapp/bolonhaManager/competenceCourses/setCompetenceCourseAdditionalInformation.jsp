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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication$CompetenceCoursesManagement"/>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>


	<h:outputText value="<em>#{CompetenceCourseManagement.personDepartment.realName}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['set.param']}</h2>" rendered="#{CompetenceCourseManagement.action == 'create'}" escape="false">
	 	<f:param value=" #{bolonhaBundle['additionalInformation']}"/>
	</h:outputFormat>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" rendered="#{CompetenceCourseManagement.action == 'edit'}" escape="false">
		<f:param value=" #{bolonhaBundle['additionalInformation']}"/>
	</h:outputFormat>

	<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
		<h:outputText value="<p class='breadcumbs'><span>#{bolonhaBundle['step']} 1: </strong>" escape="false"/>
		<h:outputFormat value="#{bolonhaBundle['create.param']}</span>" escape="false">
			<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
		</h:outputFormat>
		<h:outputText value=" > "/>
		<h:outputText value="<span><strong>#{bolonhaBundle['step']} 2:</strong> #{bolonhaBundle['setCompetenceCourseLoad']}</span>" escape="false"/>
		<h:outputText value=" > <span  class='actual'><strong>#{bolonhaBundle['step']} 3:</strong> #{bolonhaBundle['setCompetenceCourseAdditionalInformation']}</span>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li><strong>#{bolonhaBundle['name']} (pt): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['nameEn']} (en): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.nameEn}</li>" escape="false" />
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}">
		<h:outputText value="<li><strong>#{bolonhaBundle['acronym']}: </strong>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</li>" escape="false"/>
	</h:panelGroup>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</li>" escape="false"/>
	</fc:dataRepeater>
	<h:outputText value="</ul>" escape="false"/>	

	<h:form>				
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h3 id='portuguese' class='first mbottom1'>#{bolonhaBundle['portuguese']}</h3><fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['objectives']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="objectives" value="#{CompetenceCourseManagement.objectives}" cols="60" rows="10"/>
			<h:message for="objectives" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['program']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="program" value="#{CompetenceCourseManagement.program}" cols="60" rows="10">
				<f:validateLength maximum="1820" />
			</h:inputTextarea>
			<h:outputText value="(#{bolonhaBundle['max']} 1800 #{bolonhaBundle['characters']})" />
			<h:message for="program" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['evaluationMethod']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="evaluationMethod" value="#{CompetenceCourseManagement.evaluationMethod}" cols="60" rows="10">
				<f:validateLength maximum="500" />
			</h:inputTextarea>			
			<h:outputText value="(#{bolonhaBundle['max']} 500 #{bolonhaBundle['characters']})" />
			<h:message for="evaluationMethod" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisites']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="prerequisites" value="#{CompetenceCourseManagement.prerequisites}" cols="60" rows="10"/>
			<h:message for="prerequisites" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['laboratorialComponent']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="laboratorialComponent" value="#{CompetenceCourseManagement.laboratorialComponent}" cols="60" rows="10"/>
			<h:message for="laboratorialComponent" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['programmingAndComputingComponent']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="programmingAndComputingComponent" value="#{CompetenceCourseManagement.programmingAndComputingComponent}" cols="60" rows="10"/>
			<h:message for="programmingAndComputingComponent" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['crossCompetenceComponent']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="crossCompetenceComponent" value="#{CompetenceCourseManagement.crossCompetenceComponent}" cols="60" rows="10"/>
			<h:message for="crossCompetenceComponent" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['ethicalPrinciples']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="ethicalPrinciples" value="#{CompetenceCourseManagement.ethicalPrinciples}" cols="60" rows="10"/>
			<h:message for="ethicalPrinciples" styleClass="error0"/>
		</h:panelGrid>	
		<h:outputText value="</p></fieldset></div>" escape="false"/>
		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h3 id='english' class='first mbottom1'>#{bolonhaBundle['english']}</h3><fieldset class='lfloat'>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['objectivesEn']}: </a></label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="objectivesEn" value="#{CompetenceCourseManagement.objectivesEn}" cols="60" rows="10"/>
			<h:message for="objectivesEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>			
		<h:outputText value="<p><label>#{bolonhaBundle['programEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="programEn" value="#{CompetenceCourseManagement.programEn}" cols="60" rows="10">
				<f:validateLength maximum="1820" />
			</h:inputTextarea>	
			<h:outputText value="(#{bolonhaBundle['max']} 1800 #{bolonhaBundle['characters']})" />
			<h:message for="programEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['evaluationMethodEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="evaluationMethodEn" value="#{CompetenceCourseManagement.evaluationMethodEn}" cols="60" rows="10">
				<f:validateLength maximum="500" />
			</h:inputTextarea>			
			<h:outputText value="(#{bolonhaBundle['max']} 500 #{bolonhaBundle['characters']})" />
			<h:message for="evaluationMethodEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="prerequisitesEn" value="#{CompetenceCourseManagement.prerequisitesEn}" cols="60" rows="10"/>
			<h:message for="prerequisitesEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['laboratorialComponentEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="laboratorialComponentEn" value="#{CompetenceCourseManagement.laboratorialComponentEn}" cols="60" rows="10"/>
			<h:message for="laboratorialComponentEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['programmingAndComputingComponentEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="programmingAndComputingComponentEn" value="#{CompetenceCourseManagement.programmingAndComputingComponentEn}" cols="60" rows="10"/>
			<h:message for="programmingAndComputingComponentEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['crossCompetenceComponentEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="crossCompetenceComponentEn" value="#{CompetenceCourseManagement.crossCompetenceComponentEn}" cols="60" rows="10"/>
			<h:message for="crossCompetenceComponentEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['ethicalPrinciplesEn']}: </label>" escape="false"/>
		<h:panelGrid columns="2">
			<h:inputTextarea id="ethicalPrinciplesEn" value="#{CompetenceCourseManagement.ethicalPrinciplesEn}" cols="60" rows="10"/>
			<h:message for="ethicalPrinciplesEn" styleClass="error0"/>
		</h:panelGrid>
		<h:outputText value="</p></fieldset></div>" escape="false"/>
		
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>		

		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" styleClass="inputbutton" value="#{bolonhaBundle['submit']}"
				action="#{CompetenceCourseManagement.createCompetenceCourseAdditionalInformation}"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{bolonhaBundle['save']}"
				action="#{CompetenceCourseManagement.editCompetenceCourseAdditionalInformation}"/>
		</h:panelGroup>		
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="competenceCoursesManagement"/>			
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="editCompetenceCourseMainPage"/>			
		</h:panelGroup>

	</h:form>
</f:view>