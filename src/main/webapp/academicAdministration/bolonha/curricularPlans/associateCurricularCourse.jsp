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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication$CurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['associateCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<fc:viewState binding="#{AcademicAdministrationCurricularCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCurricularCourseAndContext']}:</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	

		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.courseGroups}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularCourse']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularCourses}"/>
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularYears}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularSemesters}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.term}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.terms}" />
		</h:selectOneMenu>
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
	
		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
			action="#{AcademicAdministrationCurricularCourseManagement.addContext}"/>	
		<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</f:view>