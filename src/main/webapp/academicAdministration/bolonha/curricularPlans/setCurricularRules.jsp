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
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication$CurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['setCurricularRules']}</h2>" escape="false"/>
	
	<h:form>
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['executionYear']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}" onchange="this.form.submit();">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.executionYearItems}" />
		</h:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="</fieldset></div>" escape="false"/>
		
        <h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
        <h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.executionYearID}'/>"/>
        
		<h:outputText value="<ul><li>" escape="false"/>
		<h:commandLink value="#{bolonhaBundle['return']}"
			action="buildCurricularPlan"/>
		<h:outputText value="</li></ul>" escape="false"/>
	
		<h:panelGroup rendered="#{!empty AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
			<h:outputText value="<p class='mtop1 mbottom0'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
			<h:outputLink value="#{AcademicAdministrationCurricularCourseManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'years'}">
				<h:outputText value="#{bolonhaBundle['groups']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{AcademicAdministrationCurricularCourseManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'groups'}">
				<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="years"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'years'}" escape="false"/>		
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
	
		<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="<p class='mtop05 mbottom1'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
			<h:outputLink value="#{AcademicAdministrationCurricularCourseManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'true'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{AcademicAdministrationCurricularCourseManagement.request.contextPath}/academicAdministration/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'false'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="true"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
	
		<h:outputText value="<div class='mbottom1'>" escape="false"/>
		<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
		<h:outputText value="</div>" escape="false"/>
	
		<fc:degreeCurricularPlanRender 
			dcp="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan}"
			toEdit="true"
			showRules="true"
			organizeBy="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"
			hideCourses="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"
			executionYear="#{AcademicAdministrationCurricularCourseManagement.executionYear}"
			module="/academicAdministration/bolonha"/>

		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>

		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="buildCurricularPlan"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</f:view>
