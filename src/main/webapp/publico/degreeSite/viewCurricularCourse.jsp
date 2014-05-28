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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>

<ft:tilesView definition="definition.public.mainPageIST" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	<f:loadBundle basename="resources/GlobalResources" var="globalBundle"/>
	<f:loadBundle basename="resources/PublicDegreeInformation" var="publicDegreeInfoBundle"/>

	<h:outputText value="<span style='display:none'>#{CurricularCourseManagement.degreeAndSelectSite}</span>" escape="false" />

	<h:outputLink value="#{CurricularCourseManagement.applicationUrl}" >
		<h:outputText value="#{CurricularCourseManagement.institutionAcronym}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;
	<h:outputLink value="#{CurricularCourseManagement.institutionUrl}#{globalBundle['link.institution']}" >
		<h:outputText value="#{publicDegreeInfoBundle['public.degree.information.label.education']}"/>
	</h:outputLink>
	&nbsp;&gt;&nbsp;

	<h:outputText value="<a href='../showDegreeSite.do?method=showDescription&amp;degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.externalId}'>#{CurricularCourseManagement.degreeCurricularPlan.degree.sigla}</a>" escape="false"/>

	&nbsp;&gt;&nbsp;
    <h:outputText value="<a href='../showDegreeSite.do?method=showCurricularPlan&amp;degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.externalId}&amp;degreeCurricularPlanID=#{CurricularCourseManagement.degreeCurricularPlanID}'>#{CurricularCourseManagement.degreeCurricularPlan.name}</a>" escape="false"/>
	
	&nbsp;&gt;&nbsp;

     <h:outputText rendered="#{!empty CurricularCourseManagement.organizeBy}" value="<a href='#{facesContext.externalContext.requestContextPath}/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.externalId}&amp;degreeCurricularPlanID=#{CurricularCourseManagement.degreeCurricularPlanID}&amp;executionPeriodID=#{CurricularCourseManagement.executionYearID}&amp;organizeBy=#{CurricularCourseManagement.organizeBy}&amp;showRules=#{CurricularCourseManagement.showRules}&amp;hideCourses=#{CurricularCourseManagement.hideCourses}'>#{publicDegreeInfoBundle['public.degree.information.label.curriculum']}</a>" escape="false"/>	

     <h:outputText rendered="#{empty CurricularCourseManagement.organizeBy}" value="<a href='#{facesContext.externalContext.requestContextPath}/publico/degreeSite/showDegreeCurricularPlanBolonha.faces?degreeID=#{CurricularCourseManagement.degreeCurricularPlan.degree.externalId}&amp;degreeCurricularPlanID=#{CurricularCourseManagement.degreeCurricularPlanID}&amp;executionPeriodID=#{CurricularCourseManagement.executionYearID}&amp;organizeBy=groups&amp;showRules=false&amp;hideCourses=false'>#{publicDegreeInfoBundle['public.degree.information.label.curriculum']}</a>" escape="false"/>	 	

	&nbsp;&gt;&nbsp;
	<h:outputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.curricularCourseName}"/>
	<h:outputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.curricularCourseNameEn}"/>

	<h:outputText value="<br/><br/>" escape="false"/>

 	<h:outputFormat value="<h1>#{CurricularCourseManagement.degreePresentationName}</h1>" escape="false"/>

	<h:outputFormat value="<h2 class='greytxt'>#{publicDegreeInfoBundle['public.degree.information.label.curricularPlan']} #{CurricularCourseManagement.degreeCurricularPlan.name}</h2>" escape="false"/>
	<h:outputText value="<br/>" escape="false"/>
	<h:outputFormat value="<h2 class='greytxt'>" escape="false"/>
	<h:outputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.curricularCourseName}"/>
	<h:outputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.curricularCourseNameEn}"/>
	<h:outputText rendered="#{!empty CurricularCourseManagement.curricularCourseSemesterBean.acronym}" value=" (#{CurricularCourseManagement.curricularCourseSemesterBean.acronym})"/>
	<h:outputFormat value="</h2>" escape="false"/>


	<!-- Optional CurricularCourse -->
	<h:panelGroup rendered="#{CurricularCourseManagement.curricularCourse.optionalCurricularCourse}">
		<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['label.description']}</h2>" escape="false"/>
		<h:outputText value="<p>#{bolonhaBundle['label.optionalCurricularCourse.description']}</p>" escape="false"/>		
	</h:panelGroup>

	<h:panelGroup rendered="#{!CurricularCourseManagement.curricularCourse.optionalCurricularCourse}">
		<!-- LATERAL NAVIGATION -->
		<!-- COMPETENCE COURSE -->
		<h:outputText value="<div class='col_right'><table class='box' cellspacing='0'>" escape="false"
			rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'} || #{!empty CurricularCourseManagement.curricularCourse.associatedExecutionCourses}"/>
		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
			<h:outputText value="<tr><td class='box_header'>" escape="false"/>
			<h:outputText value="<strong>#{bolonhaBundle['competenceCourse']}</strong></td></tr>" escape="false"/>
			<h:outputText value="<tr><td class='box_cell'>" escape="false"/>
				
				<h:outputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="<a href='#{CurricularCourseManagement.contextPath}/publico/department/showCompetenceCourse.faces?competenceCourseID=#{CurricularCourseManagement.curricularCourse.competenceCourse.externalId}&amp;selectedDepartmentUnitID=#{CurricularCourseManagement.curricularCourseSemesterBean.departmentUnit.externalId}'>#{CurricularCourseManagement.curricularCourseSemesterBean.curricularCourseName}</a>" escape="false"/> 
				<h:outputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{facesContext.externalContext.requestContextPath}/publico/degreeSite/<a href='#{CurricularCourseManagement.contextPath}/publico/department/showCompetenceCourse.faces?competenceCourseID=#{CurricularCourseManagement.curricularCourse.competenceCourse.externalId}&amp;selectedDepartmentUnitID=#{CurricularCourseManagement.curricularCourseSemesterBean.departmentUnit.externalId}'>#{CurricularCourseManagement.curricularCourseSemesterBean.curricularCourseNameEn}</a>" escape="false"/> 			
											
				<h:outputText value=" #{publicDegreeInfoBundle['public.degree.information.label.from.masculine']} "/>
							
				<fc:contentLink content="#{CurricularCourseManagement.curricularCourseSemesterBean.departmentUnit.site}" label="#{CurricularCourseManagement.curricularCourseSemesterBean.departmentUnit.name}"></fc:contentLink>
															
			<h:outputText value="</td></tr>" escape="false"/>
		</h:panelGroup>
		<!-- EXECUTION COURSES -->
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.executionCoursesWithPublicSites}">
			<h:outputText value="<tr><td class='box_header'>" escape="false"/>
			<h:outputText value="<strong>#{publicDegreeInfoBundle['public.degree.information.label.courses']}</strong></td></tr>" escape="false"/>
			<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.associatedExecutionCourses}" var="executionCourse">
				<h:outputText value="<tr><td class='box_cell'>" escape="false"/>
	
				<fc:contentLink content="#{executionCourse.site}" label="#{executionCourse.nome} (#{executionCourse.executionPeriod.executionYear.year} - #{executionCourse.executionPeriod.name})"></fc:contentLink>
				
				<h:outputText value="</td></tr>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGroup>
		<h:outputText value="</table></div>" escape="false"
			rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'} || #{!empty CurricularCourseManagement.curricularCourse.associatedExecutionCourses}"/>
	
		<!-- CONTEXT -->
		<h:outputText value="<h2 class='arrow_bullet'>#{publicDegreeInfoBundle['public.degree.information.label.scope']}</h2>" escape="false"/>
		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.parentContexts}" var="context">
			<h:outputText value="<div class=''>" escape="false"/>
			<h:outputText value="<p>#{publicDegreeInfoBundle['public.group']}: " escape="false"/>
			<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>
			<h:outputText value="<p class='mvert05'>#{publicDegreeInfoBundle['public.curricular.period']}: " escape="false"/>
			<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>
		</fc:dataRepeater>
	
		<!-- WEIGHT -->
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourseSemesterBean.weight && CurricularCourseManagement.curricularCourseSemesterBean.weight != 0.0}">
			<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['weight']}</h2>" escape="false"/>
			<h:outputText value="<p>" escape="false"/>
			<h:outputText value="#{CurricularCourseManagement.curricularCourseSemesterBean.weight} (#{bolonhaBundle['for.average.grade.calculus']})"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<!-- PRE-REQUISITES -->
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourse.prerequisites}">
			<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['prerequisites']}</h2>" escape="false"/>
			<h:outputText value="<p>" escape="false"/>
			<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.prerequisites}" linebreak="true"/>
			<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourse.prerequisitesEn}" linebreak="true"/>		
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<!-- OBJECTIVES -->
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourseSemesterBean.objectives}">
			<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['objectives']}</h2>" escape="false"/>
			<h:outputText value="<p>" escape="false"/>
			<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.objectives}" linebreak="true"/>
			<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.objectivesEn}" linebreak="true"/>		
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<!-- PROGRAM -->
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourseSemesterBean.program}">
			<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['program']}</h2>" escape="false"/>
			<h:outputText value="<p>" escape="false"/>
			<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.program}" linebreak="true"/>
			<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.programEn}" linebreak="true"/>		
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<!-- EVALUATION METHOD -->
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.curricularCourseSemesterBean.evaluationMethod}">
			<h:outputText value="<h2 class='arrow_bullet'>#{bolonhaBundle['evaluationMethod']}</h2>" escape="false"/>
			<h:outputText value="<p>" escape="false"/>
			<fc:extendedOutputText rendered="#{!CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.evaluationMethod}" linebreak="true"/>
			<fc:extendedOutputText rendered="#{CurricularCourseManagement.renderInEnglish}" value="#{CurricularCourseManagement.curricularCourseSemesterBean.evaluationMethodEn}" linebreak="true"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
	</h:panelGroup>
	
	<h:form id="someIdForValidHtmlGeneration">
		<h:outputText escape="false" value="<input alt='input.degreeID' id='degreeID' name='degreeID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.externalId}'/>"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.externalId}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{CurricularCourseManagement.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
	</h:form>

</ft:tilesView>
