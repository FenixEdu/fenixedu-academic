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

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication$CurricularPlansManagement"/>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['associateCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCurricularCourseAndContext']}:</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	

		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{CurricularCourseManagement.courseGroups}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularCourse']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.curricularCourseID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularCourses}"/>
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularSemesters}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.term}">
			<f:selectItems value="#{CurricularCourseManagement.terms}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.beginExecutionPeriodID}">
			<f:selectItems value="#{CurricularCourseManagement.beginExecutionPeriodItems}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.endExecutionPeriodID}">
			<f:selectItems value="#{CurricularCourseManagement.endExecutionPeriodItems}" />
		</h:selectOneMenu>
		<h:outputText value="</p></fieldset></div>" escape="false"/>

		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
			action="#{CurricularCourseManagement.addContext}"/>	
		<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</f:view>