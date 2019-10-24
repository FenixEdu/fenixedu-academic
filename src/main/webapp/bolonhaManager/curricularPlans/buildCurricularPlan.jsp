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
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication$CurricularPlansManagement"/>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['buildCurricularPlan']}</h2>" escape="false"/>

	<h:outputText value="<ul><li>" escape="false"/>
	<h:outputLink value="#{CurricularCourseManagement.request.contextPath}/bolonhaManager/curricularPlans/editCurricularPlanStructure.faces">
		<h:outputText value="#{bolonhaBundle['edit.curricularPlan.structure']}" escape="false" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
		<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
		<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
		<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		<f:param name="toOrder" value="false"/>
	</h:outputLink>
	<h:outputText value="</li>" escape="false"/>
	
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
		<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="#{CurricularCourseManagement.request.contextPath}/bolonhaManager/curricularPlans/setCurricularRules.faces" >
			<h:outputText value="#{bolonhaBundle['setCurricularRules']}" escape="false"/>
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		</h:outputLink>
		<h:outputText value="</li>" escape="false"/>
		<h:outputText value="<li>" escape="false"/>
		<h:outputLink value="#{CurricularCourseManagement.request.contextPath}/bolonhaManager/curricularPlans/resetCurricularPlan.faces">
			<h:outputText value="#{bolonhaBundle['reset.curricularPlan.structure']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		</h:outputLink>
		<h:outputText value="</li>" escape="false"/>
	</h:panelGroup>
	
	<h:outputText value="</ul>" escape="false"/>
	
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
		<h:outputLink value="#{CurricularCourseManagement.request.contextPath}/bolonhaManager/curricularPlans/buildCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
			<h:outputText value="#{bolonhaBundle['groups']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="groups"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		</h:outputLink>
		<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="#{CurricularCourseManagement.request.contextPath}/bolonhaManager/curricularPlans/buildCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="years"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		</h:outputLink>
		<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
	</h:panelGroup>

	<h:outputText value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>" escape="false" />
	<h:outputText value="<div class='mvert1'>" escape="false"/>
	<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
	<h:outputText value="</div>" escape="false"/>
	
	<fc:degreeCurricularPlanRender 
		dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
		toEdit="true" 
		organizeBy="<%=request.getParameter("organizeBy")%>"
		module="/bolonhaManager/curricularPlans"
		currentPage="buildCurricularPlan.faces"
		groupExpandEnabled="true"
		/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</f:view>
