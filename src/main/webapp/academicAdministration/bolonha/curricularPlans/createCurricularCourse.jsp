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
	<f:loadBundle basename="resources/ManagerResources" var="managerBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>	
	<h:outputText value="<h2>#{bolonhaBundle['createCurricularCourse']}</h2>" escape="false"/>
	
	<h:messages infoClass="success0" errorClass="error0" globalOnly="true"/>

	<h:form>
		<fc:viewState binding="#{AcademicAdministrationCurricularCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearID' id='curricularYearID' name='curricularYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.curricularYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularSemesterID' id='curricularSemesterID' name='curricularSemesterID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.curricularSemesterID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>
		
		<h:panelGroup>
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCompetenceCourse']}</h4>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['type']}:</label>" escape="false"/>
			<h:selectOneRadio value="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType}" styleClass="nospace" onchange="this.form.submit();">
				<f:selectItem itemValue="NORMAL_COURSE" itemLabel="#{bolonhaBundle['NORMAL_COURSE']}" />
				<f:selectItem itemValue="OPTIONAL_COURSE" itemLabel="#{bolonhaBundle['OPTIONAL_COURSE']}" />
			</h:selectOneRadio>
			<h:outputText value="</p>" escape="false"/>
			
			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
						valueChangeListener="#{AcademicAdministrationCurricularCourseManagement.resetCompetenceCourse}">
					<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.departmentUnits}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>			
				<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
					<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.competenceCourses}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>		
				<h:panelGroup rendered="#{(!empty AcademicAdministrationCurricularCourseManagement.competenceCourseID) && (AcademicAdministrationCurricularCourseManagement.competenceCourseID != 0) }">
					<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
					<h:outputLink value="#{AcademicAdministrationCurricularCourseManagement.contextPath}/bolonhaManager/competenceCourses/showCompetenceCourse.faces" target="_blank">
						<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
						<f:param name="competenceCourseID" value="#{AcademicAdministrationCurricularCourseManagement.competenceCourseID}"/>
					</h:outputLink>
					<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
					<h:outputText value="</p>" escape="false"/>
				</h:panelGroup>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" size="50" maxlength="200" value="#{AcademicAdministrationCurricularCourseManagement.name}"/>
				<h:message for="name" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" size="50" maxlength="200" value="#{AcademicAdministrationCurricularCourseManagement.nameEn}"/>
				<h:message for="nameEn" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>		
			</h:panelGroup>
			
			<h:outputText value="</fieldset></div>" escape="false"/>

			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularInformation']}</h4>" escape="false"/>
				<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weight" maxlength="5" size="5" value="#{AcademicAdministrationCurricularCourseManagement.weight}" />
				<h:message for="weight" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesPt']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisites" cols="55" rows="5" value="#{AcademicAdministrationCurricularCourseManagement.prerequisites}"/>
				<h:outputText value="</p>" escape="false"/>
		
				<h:outputText value="" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisitesEn" cols="55" rows="5" value="#{AcademicAdministrationCurricularCourseManagement.prerequisitesEn}"/>
				<h:outputText value="</p></fieldset></div>" escape="false"/>
			</h:panelGroup>
		
		</h:panelGroup>
		
		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['context']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.courseGroups}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularYears}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularSemesters}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.term}">
			<f:selectItems value="#{CurricularCourseManagement.terms}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.beginExecutionPeriodID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.beginExecutionPeriodItems}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.endExecutionPeriodID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.endExecutionPeriodItems}" />
		</fc:selectOneMenu>

		<h:outputText value="</p></fieldset></div>" escape="false"/>		

		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}" 
			action="#{AcademicAdministrationCurricularCourseManagement.createCurricularCourse}" />

		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="buildCurricularPlan"/>
	</h:form>
</f:view>