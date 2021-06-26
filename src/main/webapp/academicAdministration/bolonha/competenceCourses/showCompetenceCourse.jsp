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

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication$CurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<h2>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.code} - " rendered="#{!empty CompetenceCourseManagement.code}"/>
		<h:outputText value="#{CompetenceCourseManagement.name} " escape="false"/>
		<h:outputText rendered="#{!empty CompetenceCourseManagement.acronym}" value="(#{CompetenceCourseManagement.acronym})" escape="false"/>
	<h:outputText value="</h2>" escape="false"/>

	<h:outputText value="<ul class='nobullet padding1 indent0 mtop3'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.department.realName}</li>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<li><strong>#{bolonhaBundle['area']}: </strong>" escape="false"/>
		<h:outputText value="#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</li>" escape="false"/>
	</fc:dataRepeater>		
	<h:outputText value="</ul>" escape="false"/>
		
	<h:outputText value="<p class='mtop2 mbottom0'><strong>#{bolonhaBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<i>#{bolonhaBundle['noCurricularCourses']}</i>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="#{CompetenceCourseManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
				<f:param name="executionYearID" value="#{CompetenceCourseManagement.executionYearID}"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="#{CompetenceCourseManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.externalId}"/>
				<f:param name="executionYearID" value="#{CompetenceCourseManagement.executionYearID}"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p><strong>#{bolonhaBundle['state']}: </strong>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.curricularStage.name == 'DRAFT'}" value="<span class='highlight1'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.curricularStage.name == 'PUBLISHED'}" value="<span class='highlight3'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.curricularStage.name == 'APPROVED'}" value="<span class='highlight4'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.curricularStage.name]}</span></p>" escape="false"/>		
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>

	<h:outputText value="<li>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.executionYear.name}</li>" escape="false"/>

	<h:outputText value="<li><strong>#{bolonhaBundle['name']} (pt): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.name}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['nameEn']} (en): </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.nameEn}</li>" escape="false" />
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.acronym}">
		<h:outputText value="<li><strong>#{bolonhaBundle['acronym']}: </strong>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.acronym}</li>" escape="false"/>
	</h:panelGroup>
	<h:outputText value="<li><strong>#{bolonhaBundle['competenceCourseLevel']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourseLevel]}</li>" escape="false" rendered="#{!empty CompetenceCourseManagement.competenceCourseLevel}"/>
	<h:outputText value="<em>#{bolonhaBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourseLevel}"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['type']}: </strong>" escape="false"/>
	<h:outputText value="#{bolonhaBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.basic}" escape="false"/>
	<h:outputText value="#{bolonhaBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.basic}" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['regime']}: </strong>" escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.regime]}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{bolonhaBundle['lessonHours']}: </strong>" escape="false"/>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}º #{bolonhaBundle['semester']}</em></p>" escape="false"
			rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<ul class='mvert0'>" escape="false"/>
		<h:outputText value="<li>#{bolonhaBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="<li>#{bolonhaBundle['problemsLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="#{competenceCourseLoad.problemsHours} h/#{bolonhaBundle['lowerCase.week']}</li>" escape="false"/>

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
	<h:outputText value="</ul></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['portuguese']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['objectives']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.objectives}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.objectives}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['program']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.program}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.program}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['evaluationMethod']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.evaluationMethod}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.evaluationMethod}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	
	<h:outputText value="<p class='mbottom0'><em>#{bolonhaBundle['english']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['objectivesEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.objectivesEn}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.objectivesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['programEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.programEn}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.programEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th>#{bolonhaBundle['evaluationMethodEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.evaluationMethodEn}" linebreak="true"/>
	<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.evaluationMethodEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<h3 class='mbottom0'>#{bolonhaBundle['bibliographicReference']}</h3>" escape="false"/>	
	<h:outputText value="<p><b>#{enumerationBundle['MAIN']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<i>#{bolonhaBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{(!empty bibliographicReference.url) and (bibliographicReference.url != 'http://')}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{(empty bibliographicReference.url) or (bibliographicReference.url == 'http://')}" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<p><b>#{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<i>#{bolonhaBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{(!empty bibliographicReference.url) and (bibliographicReference.url != 'http://')}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{(empty bibliographicReference.url) or (bibliographicReference.url == 'http://')}" escape="false"/>
				
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{bolonhaBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>

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