<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration" %>
<%@ page import="org.fenixedu.bennu.core.i18n.BundleUtil" %>
<%@ page import="org.fenixedu.academic.util.Bundle" %><%--

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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication$ScientificCompetenceCoursesManagement" />


<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<h2>" escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.code} - " rendered="#{!empty CompetenceCourseManagement.code}"/>
		<h:outputText value="#{CompetenceCourseManagement.name} " escape="false"/>
		<h:outputText rendered="#{!empty CompetenceCourseManagement.acronym}" value="(#{CompetenceCourseManagement.acronym})" escape="false"/>
	<h:outputText value="</h2>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>

		<fc:selectOneMenu value="#{CompetenceCourseManagement.executionSemesterID}" onchange="submit()">
			<f:selectItems binding="#{CompetenceCourseManagement.competenceCourseExecutionSemesters}"/>
		</fc:selectOneMenu>	
	</h:form>
	
	<h:outputText value="<ul class='nobullet padding1 indent0 mtop15'>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['label.start.period']}: </strong>" escape="false"/>
	<h:outputText rendered="#{!empty CompetenceCourseManagement.competenceCourse.startExecutionSemester}" value="<span class='highlight4'>#{CompetenceCourseManagement.competenceCourse.startExecutionSemester.qualifiedName}<span></li>" escape="false"/>	
	<h:outputText rendered="#{empty CompetenceCourseManagement.competenceCourse.startExecutionSemester}" value="-</li>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['department']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.departmentRealName}</li>" escape="false"/>
	<h:outputText value="<li><strong>#{scouncilBundle['area']}: </strong>" escape="false"/>
	<h:outputText value="#{CompetenceCourseManagement.scientificAreaUnitName} > #{CompetenceCourseManagement.competenceCourseGroupUnitName}</li>" escape="false"/>
	<h:outputText value="</ul>" escape="false"/>

    <logic:present role="(role(SCIENTIFIC_COUNCIL) | role(BOLONHA_MANAGER))">
        <h:outputLink value="#{facesContext.externalContext.requestContextPath}/scientificCouncil/competenceCourses/transferCompetenceCourse.faces">
            <h:outputText value="#{scouncilBundle['transfer']}"/>
            <f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.externalId}"/>
            <f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
        </h:outputLink>
        <h:form>
        <h:panelGroup rendered="#{CompetenceCourseManagement.stage != 'DRAFT'}">
            <fc:commandLink rendered="#{CompetenceCourseManagement.stage == 'PUBLISHED'}" action="#{CompetenceCourseManagement.changeCompetenceCourseState}" value="#{scouncilBundle['approve']}">
                <f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.externalId}"/>
                <f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
            </fc:commandLink>
            <fc:commandLink rendered="#{CompetenceCourseManagement.stage == 'APPROVED'}" action="#{CompetenceCourseManagement.changeCompetenceCourseState}" value="#{scouncilBundle['disapprove']}">
                <f:param name="competenceCourseID" value="#{CompetenceCourseManagement.competenceCourse.externalId}"/>
                <f:param name="selectedDepartmentUnitID" value="#{CompetenceCourseManagement.selectedDepartmentUnitID}"/>
            </fc:commandLink>
        </h:panelGroup>
        </h:form>
    </logic:present>
	<logic:present role="role(SCIENTIFIC_COUNCIL)">
		<a href="<%= CoreConfiguration.getConfiguration().applicationUrl() %>/competence-management/${CompetenceCourseManagement.competenceCourse.externalId}/externalUrl">
			<%= BundleUtil.getString(Bundle.SCIENTIFIC, "label.external.url") %>
		</a>
	</logic:present>
	<h:outputText value="<p class='mtop15 mbottom0'><strong>#{scouncilBundle['activeCurricularPlans']}: </strong></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<i>#{scouncilBundle['noCurricularCourses']}</i>" escape="false"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}">
		<h:outputText value="<ul class='mtop0 mbottom3'>" escape="false"/>
		<fc:dataRepeater value="#{CompetenceCourseManagement.competenceCourse.associatedCurricularCourses}" var="curricularCourse">			
			<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="#{CompetenceCourseManagement.request.contextPath}/scientificCouncil/curricularPlans/viewCurricularPlan.faces" target="_blank">
				<h:outputText value="#{curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="dcpId" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
			</h:outputLink>
			<h:outputText value=" > "/>
			<h:outputLink value="#{CompetenceCourseManagement.request.contextPath}/scientificCouncil/curricularPlans/viewCurricularCourse.faces" target="_blank">
				<h:outputText value="#{curricularCourse.name}" escape="false"/>
				<f:param name="action" value="close"/>
				<f:param name="curricularCourseID" value="#{curricularCourse.externalId}"/>
				<f:param name="degreeCurricularPlanID" value="#{curricularCourse.parentDegreeCurricularPlan.externalId}"/>
			</h:outputLink>
			<h:outputText value="</li>" escape="false"/>
		</fc:dataRepeater>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>	

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p>#{scouncilBundle['state']}: " escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.stage == 'DRAFT'}" value="<span class='highlight1'>#{enumerationBundle[CompetenceCourseManagement.stage]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.stage == 'PUBLISHED'}" value="<span class='highlight3'>#{enumerationBundle[CompetenceCourseManagement.stage]}</span></p>" escape="false"/>
	<h:outputText rendered="#{CompetenceCourseManagement.stage == 'APPROVED'}" value="<span class='highlight4'>#{enumerationBundle[CompetenceCourseManagement.stage]}</span></p>" escape="false"/>		
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>	
	<h:outputText value="<li>#{scouncilBundle['name']} (pt):" escape="false"/>
	<h:outputText value="<strong>#{CompetenceCourseManagement.name}</strong></li>" escape="false"/>
	<h:outputText value="<li>#{scouncilBundle['nameEn']} (en): " escape="false"/>
	<h:outputText value="<strong>#{CompetenceCourseManagement.nameEn}</strong></li>" escape="false" />
	<h:panelGroup rendered="#{!empty CompetenceCourseManagement.acronym}">
		<h:outputText value="<li>#{scouncilBundle['acronym']}: " escape="false"/>
		<h:outputText value="#{CompetenceCourseManagement.acronym}</li>" escape="false"/>
	</h:panelGroup>
	<c:if test="${not empty CompetenceCourseManagement.competenceCourse.externalUrl}">
		<li>
			<a href="${CompetenceCourseManagement.competenceCourse.externalUrl}">
				${CompetenceCourseManagement.competenceCourse.externalUrl}
			</a>
		</li>
	</c:if>
	<h:outputText value="<li>#{scouncilBundle['competenceCourseLevel']}: " escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourseLevel]}</li>" escape="false" rendered="#{!empty CompetenceCourseManagement.competenceCourseLevel}"/>	
	<h:outputText value="<em>#{scouncilBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourseLevel}"/>	
    <h:outputText value="<li>#{scouncilBundle['competenceCourseType']}: " escape="false"/>
    <h:outputText value="#{enumerationBundle[CompetenceCourseManagement.competenceCourse.type]}</li>" escape="false" rendered="#{!empty CompetenceCourseManagement.competenceCourse.type}"/>  
    <h:outputText value="<em>#{scouncilBundle['label.notDefined']}</em></li>" escape="false" rendered="#{empty CompetenceCourseManagement.competenceCourse.type}"/>
	<h:outputText value="#{scouncilBundle['basic']}</li>" rendered="#{CompetenceCourseManagement.basic}" escape="false"/>
	<h:outputText value="#{scouncilBundle['nonBasic']}</li>" rendered="#{!CompetenceCourseManagement.basic}" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<ul class='nobullet padding1 indent0 mbottom0'>" escape="false"/>
	<h:outputText value="<li>#{scouncilBundle['regime']}: " escape="false"/>
	<h:outputText value="#{enumerationBundle[CompetenceCourseManagement.regime]}</li>" escape="false"/>	
	<h:outputText value="<li>#{scouncilBundle['lessonHours']}: " escape="false"/>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.sortedCompetenceCourseLoads}" var="competenceCourseLoad" rowCountVar="numberOfElements">
		<h:outputText value="<p class='mbotton0'><em>#{competenceCourseLoad.order}º #{scouncilBundle['semester']}</em></p>" escape="false"
			rendered="#{CompetenceCourseManagement.regime == 'ANUAL' && numberOfElements == 2}"/>
		
		<h:outputText value="<ul class='mvert0'>" escape="false"/>
		<h:outputText value="<li>#{scouncilBundle['theoreticalLesson']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.theoreticalHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="<li>#{scouncilBundle['problemsLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.problemsHours != 0.0}" value="#{competenceCourseLoad.problemsHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="<li>#{scouncilBundle['laboratorialLesson']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.laboratorialHours != 0.0}" value="#{competenceCourseLoad.laboratorialHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="<li>#{scouncilBundle['seminary']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.seminaryHours != 0.0}" value="#{competenceCourseLoad.seminaryHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="<li>#{scouncilBundle['fieldWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.fieldWorkHours != 0.0}" value="#{competenceCourseLoad.fieldWorkHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="<li>#{scouncilBundle['trainingPeriod']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.trainingPeriodHours != 0.0}" value="#{competenceCourseLoad.trainingPeriodHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="<li>#{scouncilBundle['tutorialOrientation']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.tutorialOrientationHours != 0.0}" value="#{competenceCourseLoad.tutorialOrientationHours} h/#{scouncilBundle['lowerCase.week']}</li>" escape="false"/>

		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="<li>#{scouncilBundle['autonomousWork']}: " escape="false"/>
		<h:outputText rendered="#{competenceCourseLoad.autonomousWorkHours != 0.0}" value="#{competenceCourseLoad.autonomousWorkHours} h/#{scouncilBundle['lowerCase.semester']}</li>" escape="false"/>

		<h:outputText value="<li><strong>#{scouncilBundle['ectsCredits']}: " escape="false"/>
		<h:outputText value="#{competenceCourseLoad.ectsCredits}</strong></li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</fc:dataRepeater>	
	<h:outputText value="</li>" escape="false"/>
	<h:outputText value="</ul></div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>
	<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['portuguese']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['objectives']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.objectives}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.objectives}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['program']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.program}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.program}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['evaluationMethod']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.evaluationMethod}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.evaluationMethod}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	
	<h:outputText value="<p class='mbottom0'><em>#{scouncilBundle['english']}: </em></p>" escape="false"/>
	<h:outputText value="<table class='showinfo1 emphasis2'>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['objectivesEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.objectivesEn}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.objectivesEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['programEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.programEn}" linebreak="true"/>
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.programEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="<tr><th class='aleft'>#{scouncilBundle['evaluationMethodEn']}:</th>" escape="false"/>
	<h:outputText value="<td>" escape="false"/>
	<fc:extendedOutputText value="#{CompetenceCourseManagement.evaluationMethodEn}" linebreak="true"/>	
	<h:outputText value="<i>#{scouncilBundle['empty.field']}</i>" escape="false" rendered="#{empty CompetenceCourseManagement.evaluationMethodEn}"/>
	<h:outputText value="</td></tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>
	<h:outputText value="</div>" escape="false"/>

	<h:outputText value="<div class='simpleblock3 mtop2'>" escape="false"/>

	<h:outputText value="<p><b>#{scouncilBundle['bibliographicReference']} #{enumerationBundle['MAIN']}</b></p>" escape="false"/>
	
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>
	
	<fc:dataRepeater value="#{CompetenceCourseManagement.mainBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.mainBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'MAIN'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>			
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	
	<h:outputText value="<p class='mtop15'><b>#{scouncilBundle['bibliographicReference']} #{enumerationBundle['SECONDARY']}</b></p>" escape="false"/>
	<h:panelGroup rendered="#{empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:outputText value="<i>#{scouncilBundle['noBibliographicReferences']}</i><br/>" escape="false"/>
	</h:panelGroup>	
	<fc:dataRepeater value="#{CompetenceCourseManagement.secondaryBibliographicReferences}" var="bibliographicReference" rendered="#{!empty CompetenceCourseManagement.secondaryBibliographicReferences}">
		<h:panelGroup rendered="#{bibliographicReference.type.name == 'SECONDARY'}">
			<h:outputText value="<ul class='nobullet cboth mbottom2'>" escape="false"/>					
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['title']}:</span>" escape="false"/>
			<h:outputText value="<a href='#{bibliographicReference.url}'>#{bibliographicReference.title}</a></li>" rendered="#{bibliographicReference.url != 'http://'}" escape="false"/>
			<h:outputText value="#{bibliographicReference.title}</li>" rendered="#{bibliographicReference.url == 'http://'}" escape="false"/>			
				
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['author']}:</span>" escape="false"/>
			<h:outputText value="<em>#{bibliographicReference.authors}</em></li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['year']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.year}</li>" escape="false"/>
			
			<h:outputText value="<li><span class='fleft width100px' style='padding-right: 10px;'>#{scouncilBundle['reference']}:</span>" escape="false"/>
			<h:outputText value="#{bibliographicReference.reference}</li>" escape="false"/>
			
			<h:outputText value="</ul>" escape="false"/>
		</h:panelGroup>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.competenceCourseID' id='competenceCourseID' name='competenceCourseID' type='hidden' value='#{CompetenceCourseManagement.competenceCourse.externalId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CompetenceCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.selectedDepartmentUnitID' id='selectedDepartmentUnitID' name='selectedDepartmentUnitID' type='hidden' value='#{CompetenceCourseManagement.selectedDepartmentUnitID}'/>"/>
		
		<h:panelGroup rendered="#{!empty CompetenceCourseManagement.action}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" action="competenceCoursesManagement" value="#{scouncilBundle['back']}" />
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CompetenceCourseManagement.action}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.close']}" immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{scouncilBundle['close']}" />
		</h:panelGroup>
	</h:form>
</f:view>