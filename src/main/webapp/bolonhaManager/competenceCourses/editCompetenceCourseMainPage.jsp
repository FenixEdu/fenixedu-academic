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

	<h:outputText value="<em>#{bolonhaBundle['competenceCourse']}</em>" escape="false" />
	<h:outputText value="<h2>" style="font-weight: bold" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.code} - " rendered="#{!empty CompetenceCourseManagement.competenceCourse.code}" style="font-weight: bold" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.name} " style="font-weight: bold" escape="false"/>	
		<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}" value="(#{CompetenceCourseManagement.competenceCourse.acronym})" style="font-weight: bold" escape="false"/>
	<h:outputText value="</h2>" style="font-weight: bold" escape="false"/>		

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop2'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.personDepartment.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
		
	<h:outputText value="<p class='mtop15 mbottom0'><strong>#{bolonhaBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<i>#{bolonhaBundle['noCurricularCourses']}</i>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
				<f:param name="action" value="close"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}" escape="false"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.externalId}"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
				<f:param name="action" value="close"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p>#{bolonhaBundle['state']}: " escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.curricularStage.name == 'DRAFT'}" value="<span class='highlight1'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.curricularStage.name == 'PUBLISHED'}" value="<span class='highlight3'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.curricularStage.name == 'APPROVED'}" value="<span class='highlight4'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>		
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li>#{bolonhaBundle['name']} (pt): " escape="false"/>
	<h:outputText value="<strong>#{CompetenceCourseManagement.competenceCourse.name}</strong></li>" escape="false"/>
	<h:outputText value="<li>#{bolonhaBundle['nameEn']} (en): " escape="false"/>
	<h:outputText value="<strong>#{CompetenceCourseManagement.competenceCourse.nameEn}</strong></li>" escape="false" />
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.acronym}">
		<h:outputText value="<li>#{bolonhaBundle['acronym']}: " escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.competenceCourse.acronym}</li>" escape="false"/>
	</h:panelGroup>
	<h:outputText value="<li>#{bolonhaBundle['competenceCourseLevel']}: " escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.competenceCourseLevel]}</li>" escape="false" rendered="#{!empty CompetenceCourseManagement.competenceCourse.competenceCourseLevel}"/>	
	<h:outputText value="<em>#{bolonhaBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.competenceCourseLevel}"/>	
    <h:outputText value="<li>#{bolonhaBundle['competenceCourseType']}: " escape="false"/>
    <h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.type]}</li>" escape="false" rendered="#{!empty CompetenceCourseManagement.competenceCourse.type}"/>  
    <h:outputText value="<em>#{bolonhaBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.type}"/> 
	<h:outputText value="<li>#{bolonhaBundle['type']}: " escape="false"/>
	<h:outputText value="#{bolonhaBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" escape="false"/>
	<h:outputText value="</ul>" escape="false"/>
	<h:outputText value="<p class='mtop1'>" escape="false"/>
	<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/editCompetenceCourse.faces">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.externalId}"/>
		<f:param name="action" value="viewccm"/>
	</h:outputLink>
	<h:outputText value="</p></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
	<h:outputText value="<li>#{bolonhaBundle['regime']}: " escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.regime.name]}</li>" escape="false"/>	
	<h:outputText value="<li>#{bolonhaBundle['lessonHours']}: " escape="false"/>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}º #{bolonhaBundle['semester']}</em></p>" escape="false"
			rendered="#{CompetenceCourseManagement.competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<ul class='mvert0'>" escape="false"/>
		<h:outputText value="<li>#{bolonhaBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['problemsLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.problemsHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.laboratorialHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['seminary']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.seminaryHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['fieldWork']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.fieldWorkHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.trainingPeriodHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.tutorialOrientationHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText value="<li>#{bolonhaBundle['autonomousWork']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.autonomousWorkHours} h/#{bolonhaBundle['lowerCase.semester']}</li>" escape="false"/>

		<h:outputText value="<li><strong>#{bolonhaBundle['ectsCredits']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</fc:dataRepeater>	
	<h:outputText value="</li>" escape="false"/>
	<h:outputText value="</ul>" escape="false"/>
	<h:outputText value="<p class='mtop1'>" escape="false"/>
	<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/setCompetenceCourseLoad.faces">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.externalId}"/>
		<f:param name="action" value="edit"/>
	</h:outputLink>
	<h:outputText value="</p></div>" escape="false"/>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['portuguese']}</em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>	
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['objectives']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.objectives}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.objectives}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['program']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.program}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.program}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['evaluationMethod']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethod}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.evaluationMethod}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['prerequisites']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.prerequisites}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.prerequisites}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['laboratorialComponent']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.laboratorialComponent}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.laboratorialComponent}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['programmingAndComputingComponent']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.programmingAndComputingComponent}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.programmingAndComputingComponent}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['crossCompetenceComponent']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.crossCompetenceComponent}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.crossCompetenceComponent}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['ethicalPrinciples']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.ethicalPrinciples}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.ethicalPrinciples}"/>
	<h:outputText value="</td></tr>" escape="false"/>

	<h:outputText value="</table><p class='mtop1'>" escape="false"/>
	<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.externalId}&action=edit#portuguese">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<h:outputText value="</p>" escape="false"/>
	
	<h:outputText value="<br/>" escape="false"/>
	
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['english']}</em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>	
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['objectivesEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.objectivesEn}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.objectivesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>

	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['programEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.programEn}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.programEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['evaluationMethodEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}" linebreak="true"/>	
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.evaluationMethodEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['prerequisitesEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.prerequisitesEn}" linebreak="true"/>	
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.prerequisitesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['laboratorialComponentEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.laboratorialComponentEn}" linebreak="true"/>	
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.laboratorialComponentEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['programmingAndComputingComponentEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.programmingAndComputingComponentEn}" linebreak="true"/>	
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.programmingAndComputingComponentEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['crossCompetenceComponentEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.crossCompetenceComponentEn}" linebreak="true"/>	
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.crossCompetenceComponentEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="<tr><th class='aleft'>#{bolonhaBundle['ethicalPrinciplesEn']}: </th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.competenceCourse.ethicalPrinciplesEn}" linebreak="true"/>	
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.ethicalPrinciplesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	
	<h:outputText value="</table><p class='mtop1'>" escape="false"/>
	<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/setCompetenceCourseAdditionalInformation.faces?competenceCourseID=#{CompetenceCourseManagement.competenceCourse.externalId}&action=edit#english">
		<h:outputText value="#{bolonhaBundle['edit']}"/>
	</h:outputLink>
	<h:outputText value="</p></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>

	<h:outputText value="<p><strong>#{bolonhaBundle['bibliographicReference']} #{enumerationBundle['MAIN']}</strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<em>#{bolonhaBundle['noBibliographicReferences']}</em><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			

			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	<h:outputText value="<p class='mtop2'><strong>#{bolonhaBundle['bibliographicReference']} #{enumerationBundle['SECONDARY']}</strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<em>#{bolonhaBundle['noBibliographicReferences']}</em><br/>" escape="false"/>
	</h:panelGroup>
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px'>#{bolonhaBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	<h:outputText value="<p>" escape="false"/>
	<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/competenceCourses/setCompetenceCourseBibliographicReference.faces">
		<h:outputText value="#{bolonhaBundle['edit']}" />
		<f:param name="action" value="edit"/>
		<f:param name="bibliographicReferenceID" value="-1"/>
		<f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.externalId}"/>
	</h:outputLink>
	<h:outputText value="</p></div>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.action}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{bolonhaBundle['back']}" />
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.action}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.close']}" immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{bolonhaBundle['close']}" />
		</h:panelGroup>
	</h:form>
</f:view>
