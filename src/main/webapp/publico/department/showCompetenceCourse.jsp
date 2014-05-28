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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="definition.public.department" attributeName="body-inline">
	
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/PublicDepartmentResources" var="publicDepartmentBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>

	<h:outputText value="<span style='display:none'>#{CurricularCourseManagement.departmentAndSelectSite}</span>" escape="false" />

    <h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
	<h:outputLink value="#{CurricularCourseManagement.applicationUrl}" >
		<h:outputText value="#{CurricularCourseManagement.institutionAcronym}"/>
	</h:outputLink>
	
	&nbsp;&gt;&nbsp;
	<h:outputLink target="_blank" value="#{CurricularCourseManagement.institutionUrl}#{globalBundle['link.institution.structure']}" >
		<h:outputText value="#{publicDepartmentBundle['structure']}"/>
	</h:outputLink>
	
	&nbsp;&gt;&nbsp;
	<h:outputLink value="#{facesContext.externalContext.requestContextPath}/publico/department/showDepartments.faces">
		<h:outputText value="#{publicDepartmentBundle['academic.units']}"/>
	</h:outputLink>
	
	&nbsp;&gt;&nbsp;
	<fc:contentLink label="#{CompetenceCourseManagement.selectedDepartmentUnit.department.realName}" content="#{CompetenceCourseManagement.selectedDepartmentUnit.site}" />
		
	&nbsp;&gt;&nbsp;	
	<h:outputLink value="#{CompetenceCourseManagement.contextPath}/publico/department/showDepartmentCompetenceCourses.faces">
		<h:outputText value="#{publicDepartmentBundle['department.courses']}"/>
		<f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.competenceCourse.departmentUnit.externalId}"/>
	</h:outputLink>
	
	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.name}"/>
	<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.nameEn}"/>
    <h:outputText value="</div>" escape="false"/>
	
	<h:outputText value="<h1>" escape="false"/>
	<h:outputText rendered="#{!CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.name}"/>
	<h:outputText rendered="#{CompetenceCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.nameEn}"/>
	<h:outputText rendered="#{!empty CompetenceCourseManagement.acronym}" value="(#{CompetenceCourseManagement.acronym})" escape="false"/>
	<h:outputText value="</h1>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
		
		<fc:selectOneMenu value="#{CompetenceCourseManagement.executionSemesterID}" onchange="submit()">
			<f:selectItems binding="#{CompetenceCourseManagement.competenceCourseExecutionSemesters}"/>
		</fc:selectOneMenu>
	</h:form>

	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['area']}</h2>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.parentUnits}" var="scientificAreaUnit">
		<h:outputText value="<p style='margin-left: 0px;'>#{scientificAreaUnit.name} > #{CompetenceCourseManagement.competenceCourse.competenceCourseGroupUnit.name}</p>" escape="false"/>
	</fc:dataRepeater>

	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['activeCurricularPlans']}</h2>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<p style='margin-left: 0px;'><i>#{bolonhaBundle['noCurricularCourses']}</i></p>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">
			<h:outputText value="<p style='margin-left: 0px;'><i>" escape="false"/>
						
			<fc:contentLink label="#{curricularCourse.parentDegreeCurricularPlan.name}" content="#{curricularCourse.parentDegreeCurricularPlan.degree.site}" />
			
			<h:outputText value=" > "/>
								
			<h:outputLink value="#{CompetenceCourseManagement.contextPath}/publico/degreeSite/viewCurricularCourse.faces">
				<h:outputText value="#{curricularCourse.oneFullName}" escape="false"/>
				<f:param name="degreeID" value="#{curricularCourse.parentDegreeCurricularPlan.degree.externalId}"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.externalId}"/>
			</h:outputLink>
			
			<h:outputText value="</i></p>" escape="false"/>
		</fc:dataRepeater>
	</h:panelGroup>	

	<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.competenceCourseLevel}" value="<h2 class='arrow_bullet'>#{bolonhaBundle['competenceCourseLevel']}</h2>" escape="false"/>
	<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.competenceCourseLevel}" value="<p style='margin-left: 0px;'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.competenceCourseLevel]}</p>" escape="false"/>
		
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['type']}</h2>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.competenceCourse.basic}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['basic']}</p>" escape="false"/>
	<h:outputText rendered="#{!CompetenceCourseManagement.competenceCourse.basic}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['nonBasic']}</p>" escape="false"/>
		
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['regime']}</h2>" escape="false"/>
	<h:outputText value="<p style='margin-left: 0px;'>#{enumerationBundle[CompetenceCourseManagement.competenceCourse.regime.name]}</p>" escape="false"/>

	<!-- LESSON HOURS -->
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['lessonHours']}</h2>" escape="false"/>
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p style='margin-top: 30px; margin-left: 0px;'><strong><em>#{competenceCourseLoad.order}º #{bolonhaBundle['semester']}</em></strong></p>" escape="false"
			rendered="#{CompetenceCourseManagement.competenceCourse.regime.name == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<p style='margin-left: 0px;'>#{bolonhaBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['problemsLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="#{competenceCourseLoad.problemsHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="#{competenceCourseLoad.laboratorialHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['seminary']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="#{competenceCourseLoad.seminaryHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['fieldWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="#{competenceCourseLoad.fieldWorkHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="#{competenceCourseLoad.trainingPeriodHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="#{competenceCourseLoad.tutorialOrientationHours} h/#{bolonhaBundle['lowerCase.week']}</p>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="<p style='margin-left: 0px;'>#{bolonhaBundle['autonomousWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="#{competenceCourseLoad.autonomousWorkHours} h/#{bolonhaBundle['lowerCase.semester']}</p>" escape="false"/>

		<h:outputText value="<p style='margin-left: 0px;'><strong>#{bolonhaBundle['ectsCredits']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></p>" escape="false"/>
	</fc:dataRepeater>	

	<!-- OBJECTIVES -->
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.objectives}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['objectives']}</h2>" escape="false"/>
		<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.objectives}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.objectivesEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- PROGRAM -->
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.program}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['program']}</h2>" escape="false"/>
		<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.program}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.programEn}" linebreak="true"/>		
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	<!-- EVALUATION METHOD -->
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.evaluationMethod}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['evaluationMethod']}</h2>" escape="false"/>
		<h:outputText value="<p style='margin-left: 0px;'>" escape="false"/>
		<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.evaluationMethod}" linebreak="true"/>
		<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CompetenceCourseManagement.competenceCourse.evaluationMethodEn}" linebreak="true"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>
	
	
	<!-- BIBLIOGRAPHIC REFERENCE -->
	<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['bibliographicReference']}</h2>" escape="false"/>
	<h:outputText value="<h2 class='greytxt' style='margin-top: 15px; margin-left: 0px;'>#{enumerationBundle['MAIN']}</h2>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<p style='margin-left: 0px;'><i>#{bolonhaBundle['noBibliographicReferences']}</i></p>" escape="false"/>
	</h:panelGroup>
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<div class='lfloat mbottom2'>" escape="false"/>
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['title']}:</label>" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.hasChecksumString}" escape="false"/><h:outputText rendered="#{bibliographicReference.url != 'http://'}" value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a>" escape="false"/>
			<h:outputText rendered="#{bibliographicReference.url == 'http://'}" value="#{bibliographicReference.title}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['author']}:</label>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>			
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['year']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p class='mvert0'>" escape="false"/>			
			<h:outputText value="<label>#{bolonhaBundle['reference']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>			
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<h2 class='greytxt' style='margin-top: 10px; margin-left: 0px;'>#{enumerationBundle['SECONDARY']}</h2>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<p style='margin-left: 0px;'><i>#{bolonhaBundle['noSecundaryBibliographicReferences']}</i></p>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<div class='lfloat mbottom2'>" escape="false"/>
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['title']}:</label>" escape="false"/>
			<h:outputText value="#{CompetenceCourseManagement.hasChecksumString}" escape="false"/><h:outputText rendered="#{bibliographicReference.url != 'http://'}" value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a>" escape="false"/>
			<h:outputText rendered="#{bibliographicReference.url == 'http://'}" value="#{bibliographicReference.title}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['author']}:</label>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>	
			
			<h:outputText value="<p class='mvert0'>" escape="false"/>
			<h:outputText value="<label>#{bolonhaBundle['year']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p class='mvert0'>" escape="false"/>			
			<h:outputText value="<label>#{bolonhaBundle['reference']}:</label>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>			
		</h:panelGroup>
	</fc:dataRepeater>

	<h:form id="SomeIdFoRvAlidHtmlGeneration">
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{CurricularCourseManagement.executionPeriodOID}'/>"/>
	</h:form>

</ft:tilesView>
