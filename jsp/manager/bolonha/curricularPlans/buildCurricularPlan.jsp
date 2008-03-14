<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{ManagerCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ManagerCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['buildCurricularPlan']}</h2>" escape="false"/>

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
	
		<h:outputText value="<ul><li>" escape="false"/>
		<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/editCurricularPlanStructure.faces">
			<h:outputText value="#{bolonhaBundle['edit.curricularPlan.structure']}" escape="false" />
			<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
			<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="build"/>
			<f:param name="toOrder" value="false"/>
		</h:outputLink>
		<h:outputText value="</li>" escape="false"/>
		
		<h:panelGroup rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
			<h:outputText value="<li>" escape="false"/>
				<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/setCurricularRules.faces" >
				<h:outputText value="#{bolonhaBundle['setCurricularRules']}" escape="false"/>
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="build"/>
			</h:outputLink>
		<h:outputText value="</li>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="</ul>" escape="false"/>
		
		<h:panelGroup rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
			<h:outputText value="<p class='mtop2'>" escape="false"/>
			<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
			<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/buildCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'years'}">
				<h:outputText value="#{bolonhaBundle['groups']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="build"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="#{ManagerCurricularCourseManagement.request.contextPath}/manager/bolonha/curricularPlans/buildCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}">
				<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="years"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="build"/>
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
	
		<h:outputText value="<div class='mvert1'>" escape="false"/>
		<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
		<h:outputText value="</div>" escape="false"/>
		
		<fc:degreeCurricularPlanRender 
			dcp="#{ManagerCurricularCourseManagement.degreeCurricularPlan}"
			toEdit="true"
			organizeBy="#{ManagerCurricularCourseManagement.organizeBy}"
			executionYear="#{ManagerCurricularCourseManagement.executionYear}"
			module="/manager/bolonha"/>

		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{ManagerCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{ManagerCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{ManagerCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{ManagerCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{ManagerCurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
		
	</h:form>
	
</ft:tilesView>
