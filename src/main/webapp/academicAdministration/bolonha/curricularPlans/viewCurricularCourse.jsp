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
	
	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['viewCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
		<h:outputText value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseSemesterBean.departmentUnit.name}</p>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
		<h:outputText value="<span class='attention'>#{AcademicAdministrationCurricularCourseManagement.curricularCourseSemesterBean.curricularCourseName}</span> (#{AcademicAdministrationCurricularCourseManagement.curricularCourseSemesterBean.executionYear.qualifiedName})</p>" escape="false"/>	
		<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
		<h:outputLink value="#{AcademicAdministrationCurricularCourseManagement.contextPath}/academicAdministration/bolonha/competenceCourses/showCompetenceCourse.faces" target="_blank">
			<h:outputText value="(#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']})"/>
			<f:param name="competenceCourseID" value="#{AcademicAdministrationCurricularCourseManagement.curricularCourse.competenceCourse.externalId}"/>
			<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYear.externalId}"/>
		</h:outputLink>
		<h:outputText value="</p></fieldset></div>" escape="false"/>
	</h:panelGroup>

	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularCourseInformation']}</h4>" escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
		<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
		<h:outputText value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseSemesterBean.weight} (#{bolonhaBundle['for.average.grade.calculus']})</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisites']}:</label>" escape="false"/>
		<h:outputText value="<div style='margin-left: 11em;'>#{AcademicAdministrationCurricularCourseManagement.curricularCourse.prerequisites}</div></p>" escape="false" rendered="#{!empty AcademicAdministrationCurricularCourseManagement.curricularCourse.prerequisites}"/>	
		<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i></p>" escape="false" rendered="#{empty AcademicAdministrationCurricularCourseManagement.curricularCourse.prerequisites}"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
		<h:outputText value="<div style='margin-left: 11em;'>#{AcademicAdministrationCurricularCourseManagement.curricularCourse.prerequisitesEn}</div></p>" escape="false" rendered="#{!empty AcademicAdministrationCurricularCourseManagement.curricularCourse.prerequisitesEn}"/>	
		<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i></p>" escape="false" rendered="#{empty AcademicAdministrationCurricularCourseManagement.curricularCourse.prerequisitesEn}"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
		<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
		<h:outputText value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseSemesterBean.curricularCourseName}</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
		<h:outputText value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseSemesterBean.curricularCourseNameEn}</p>" escape="false"/>
	</h:panelGroup>
	<h:outputText value="</fieldset></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['contexts']}</h4>" escape="false"/>	
	<fc:dataRepeater value="#{AcademicAdministrationCurricularCourseManagement.curricularCourse.parentContexts}" var="context">
		<h:outputText value="<fieldset class='lfloat mbottom1'>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularPeriod']}:</label>" escape="false"/>
		<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<h:outputText value="#{context.beginExecutionPeriod.qualifiedName}</p>" escape="false"/>
					
		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<h:outputText value="#{context.endExecutionPeriod.qualifiedName}</p>" escape="false" rendered="#{!empty context.endExecutionPeriod}"/>
		<h:outputText value="#{bolonhaBundle['opened']}</p>" escape="false" rendered="#{empty context.endExecutionPeriod}"/>
		
		<h:outputText value="</fieldset>" escape="false"/>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.externalId}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>

		<h:panelGroup rendered="#{!empty AcademicAdministrationCurricularCourseManagement.action && AcademicAdministrationCurricularCourseManagement.action == 'view'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="viewCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty AcademicAdministrationCurricularCourseManagement.action && AcademicAdministrationCurricularCourseManagement.action == 'build'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="buildCurricularPlan"/>
		</h:panelGroup>
	</h:form>
</f:view>