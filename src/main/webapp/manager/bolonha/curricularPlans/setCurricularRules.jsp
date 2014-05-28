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

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{ManagerCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ManagerManagerCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['setCurricularRules']}</h2>" escape="false"/>
	
	<h:form>
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['executionYear']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{ManagerCurricularCourseManagement.executionYearID}" onchange="this.form.submit();">
			<f:selectItems value="#{ManagerCurricularCourseManagement.executionYearItems}" />
		</h:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="</fieldset></div>" escape="false"/>
		
        <h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{ManagerCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
        <h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{ManagerCurricularCourseManagement.executionYearID}'/>"/>
        
		<h:outputText value="<ul><li>" escape="false"/>
		<h:commandLink value="#{bolonhaBundle['return']}"
			action="buildCurricularPlan"/>
		<h:outputText value="</li></ul>" escape="false"/>
	
		<h:panelGroup rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
			<h:outputText value="<p class='mtop1 mbottom0'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
			<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'years'}">
				<h:outputText value="#{bolonhaBundle['groups']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}">
				<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="years"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'years'}" escape="false"/>		
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
	
		<h:panelGroup rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="<p class='mtop05 mbottom1'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
			<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'true'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/setCurricularRules.faces" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'false'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="true"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
	
		<h:outputText value="<div class='mbottom1'>" escape="false"/>
		<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
		<h:outputText value="</div>" escape="false"/>
	
		<fc:degreeCurricularPlanRender 
			dcp="#{ManagerCurricularCourseManagement.degreeCurricularPlan}"
			toEdit="true"
			showRules="true"
			organizeBy="#{ManagerCurricularCourseManagement.organizeBy}"
			hideCourses="#{ManagerCurricularCourseManagement.hideCourses}"
			executionYear="#{ManagerCurricularCourseManagement.executionYear}"
			module="/manager/bolonha"/>

		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{ManagerCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{ManagerCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{ManagerCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{ManagerCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{ManagerCurricularCourseManagement.action}'/>"/>

		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="buildCurricularPlan"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</f:view>
