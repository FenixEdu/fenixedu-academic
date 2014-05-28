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
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<f:view>" definition="coordinatorDegreeCurricularPlan" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['view.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularPlan']}"/>
	</h:outputFormat>
	
	<h:panelGroup rendered="#{empty CurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
		<h:outputText value="<p><em>#{bolonhaBundle['error.curricularPlanHasNoExecutionDegrees']}</em><p>" escape="false"/>
	</h:panelGroup>

	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
	<h:form>	
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['executionYear']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{CurricularCourseManagement.executionYearID}" onchange="this.form.submit();">
			<f:selectItems value="#{CurricularCourseManagement.executionYearItems}" />
		</h:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="</fieldset></div>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		
		<h:outputText value="<div class='invisible'>" escape="false"/>
		<h:outputText value="<ul><li>" escape="false"/>
		<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlanStructure.faces" rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
			<h:outputFormat value="#{bolonhaBundle['view.param']}" escape="false">
				<f:param value="#{bolonhaBundle['curricularPlan.structure']}"/>
			</h:outputFormat>
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}" />
		</h:outputLink>
		<h:outputText value="</li></ul>" escape="false"/>
	
		<h:outputText value="<p class='mtop2 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
			<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
				<h:outputText value="#{bolonhaBundle['groups']}" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{CurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
				<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="years"/>
				<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{CurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p class='mtop05 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">	
			<h:outputText value="#{bolonhaBundle['curricularRules']}: " escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.showRules == 'false'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="true"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{CurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{CurricularCourseManagement.showRules == 'true'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.showRules == 'true'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>				
				<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{CurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{CurricularCourseManagement.showRules == 'false'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
	
		<h:outputText value="<p class='mtop05 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{CurricularCourseManagement.showRules == 'true' && CurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.hideCourses == 'true'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{CurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{CurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{facesContext.externalContext.requestContextPath}/coordinator/degreeCurricularPlan/viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.hideCourses == 'false'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="true"/>
				<f:param name="action" value="#{CurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{CurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		<h:outputText value="<br/>" escape="false"/>
		<fc:degreeCurricularPlanRender 
			dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
			organizeBy="#{CurricularCourseManagement.organizeBy}"
			showRules="#{CurricularCourseManagement.showRules}"
			hideCourses="#{CurricularCourseManagement.hideCourses}" 
			executionYear="#{CurricularCourseManagement.executionYear}"
			module="/coordinator"/>
	
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<p>" escape="false"/>		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}" action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
	</h:panelGroup>

</f:view>
