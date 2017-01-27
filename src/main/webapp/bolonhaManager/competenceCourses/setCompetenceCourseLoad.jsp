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

	<h:outputText value="<em>#{CompetenceCourseManagement.personDepartment.fullName.content}</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['setCompetenceCourseLoad']}</h2>" rendered="#{CompetenceCourseManagement.action == 'create'}" escape="false" />
	
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.param']}</h2>" rendered="#{CompetenceCourseManagement.action == 'edit'}" escape="false">
		<f:param value=" #{bolonhaBundle['CompetenceCourseLoad']}"/>
	</h:outputFormat>
	
	<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
		<h:outputText value="<p class='breadcumbs'><span>#{bolonhaBundle['step']} 1: </strong>" escape="false"/>
		<h:outputFormat value="#{bolonhaBundle['create.param']}</span>" escape="false">
			<f:param value=" #{bolonhaBundle['competenceCourse']}"/>
		</h:outputFormat>
		<h:outputText value=" > "/>
		<h:outputText value="<span class='actual'><strong>#{bolonhaBundle['step']} 2:</strong> #{bolonhaBundle['setCompetenceCourseLoad']}</span>" escape="false"/>
		<h:outputText value=" > <span><strong>#{bolonhaBundle['step']} 3:</strong> #{bolonhaBundle['setCompetenceCourseAdditionalInformation']}</span>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
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
	
	
	<h:messages infoClass="success0" errorClass="error0" layout="table"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:form>
		<fc:viewState binding="#{CompetenceCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['regime']}: </label>" escape="false"/>
		<fc:selectOneMenu value="#{CompetenceCourseManagement.regime}"
				onchange="this.form.submit();" valueChangeListener="#{CompetenceCourseManagement.resetCourseLoad}">
			<f:selectItem itemValue="SEMESTRIAL" itemLabel="#{enumerationBundle['SEMESTRIAL']}"/>
			<f:selectItem itemValue="ANUAL" itemLabel="#{enumerationBundle['ANUAL']}"/>
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['setDifferentLoadsForEachSemester']}? </label>" escape="false" rendered="#{CompetenceCourseManagement.regime == 'ANUAL'}"/>
		<h:selectOneRadio value="#{CompetenceCourseManagement.numberOfPeriods}" styleClass="nospace" rendered="#{CompetenceCourseManagement.regime == 'ANUAL'}"
				onchange="this.form.submit();" valueChangeListener="#{CompetenceCourseManagement.resetCorrespondentCourseLoad}">
			<f:selectItems value="#{CompetenceCourseManagement.periods}"/>
		</h:selectOneRadio>
<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</p>" escape="false" rendered="#{CompetenceCourseManagement.regime == 'ANUAL'}"/>
		<h:outputText value="</p></fieldset>" escape="false"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:dataTable value="#{CompetenceCourseManagement.courseLoads}" var="courseLoad">
			<h:column rendered="#{courseLoad.action != 'delete'}">								
				<h:outputText value="<p><label>#{bolonhaBundle['lessonHours']}: </label>" escape="false"/>				
				<h:panelGrid columns="2" style="background-color: #fff; padding: 0.5em; border: 1px solid #ccc;" headerClass="pbottom1">
					<f:facet name="header">					
						<h:outputText value="<strong>#{courseLoad.order}º #{bolonhaBundle['semester']}</strong>" escape="false"
							rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && CompetenceCourseManagement.numberOfPeriods == 2}"/>		
					</f:facet>			
					
					<h:outputText value="#{bolonhaBundle['contactLessonHours']}: "/>
					<h:outputText value=" "/>
		
					<h:outputText value="#{bolonhaBundle['theoreticalLesson']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.theoreticalHours']}" maxlength="5" size="5" value="#{courseLoad.theoreticalHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['problemsLesson']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.problemsHours']}" maxlength="5" size="5" value="#{courseLoad.problemsHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>	
					
					<h:outputText value="#{bolonhaBundle['laboratorialLesson']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.laboratorialHours']}" maxlength="5" size="5" value="#{courseLoad.laboratorialHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['seminary']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.seminaryHours']}" maxlength="5" size="5" value="#{courseLoad.seminaryHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>		
					
					<h:outputText value="#{bolonhaBundle['fieldWork']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.fieldWorkHours']}" maxlength="5" size="5" value="#{courseLoad.fieldWorkHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>		
					
					<h:outputText value="#{bolonhaBundle['trainingPeriod']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.trainingPeriodHours']}" maxlength="5" size="5" value="#{courseLoad.trainingPeriodHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['tutorialOrientation']}: "/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.tutorialOrientationHours']}" maxlength="5" size="5" value="#{courseLoad.tutorialOrientationHours}"/>
						<h:outputText value=" h/#{bolonhaBundle['lowerCase.week']}"/>
					</h:panelGroup>
					
					<h:outputText value="#{bolonhaBundle['autonomousWork']}: " style="font-weight: bold"/>
					<h:panelGroup>
						<h:inputText alt="#{htmlAltBundle['inputText.autonomousWorkHours']}" maxlength="5" size="5" value="#{courseLoad.autonomousWorkHours}"/>
						<h:outputText value=" <b>h/#{bolonhaBundle['lowerCase.semester']}</b>" escape="false"/>
					</h:panelGroup>
				</h:panelGrid>
				<h:outputText value="<p><label>#{bolonhaBundle['ectsCredits']}: </label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.ectsCredits']}" id="ectsCredits" required="true" maxlength="5" size="5" value="#{courseLoad.ectsCredits}"/>
				<h:outputText value=" (#{bolonhaBundle['by']} #{bolonhaBundle['semester']})" rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && CompetenceCourseManagement.numberOfPeriods == 1}"/>
				<h:outputText value=" (#{courseLoad.order}º #{bolonhaBundle['semester']})" rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && CompetenceCourseManagement.numberOfPeriods == 2}"/>
				<h:message styleClass="error0" for="ectsCredits" />
				<h:outputText value="</p>" escape="false"/>
			</h:column>
		</h:dataTable>
		<h:outputText value="</p></fieldset>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" styleClass="inputbutton" value="#{bolonhaBundle['submit']}" action="#{CompetenceCourseManagement.createCompetenceCourseLoad}"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{bolonhaBundle['save']}" action="#{CompetenceCourseManagement.editCompetenceCourseLoad}"/>
		</h:panelGroup>		
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'create'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="competenceCoursesManagement"/>			
		</h:panelGroup>
		<h:panelGroup rendered="#{CompetenceCourseManagement.action == 'edit'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" action="editCompetenceCourseMainPage"/>			
		</h:panelGroup>
	</h:form>
	
</f:view>