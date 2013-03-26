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
	<h:outputFormat value="<h2>#{bolonhaBundle['view.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularPlan']}"/>
	</h:outputFormat>
	
	<h:panelGroup rendered="#{empty ManagerCurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
		<h:outputText value="<p><em>#{bolonhaBundle['error.curricularPlanHasNoExecutionDegrees']}</em><p>" escape="false"/>
	</h:panelGroup>

	<h:panelGroup rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
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

		<h:outputText value="<br/>" escape="false"/>
		
		<h:outputText value="<div class='invisible'>" escape="false"/>
		<h:outputText value="<ul><li>" escape="false"/>
		<h:outputLink value="viewCurricularPlanStructure.faces" rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
			<h:outputFormat value="#{bolonhaBundle['view.param']}" escape="false">
				<f:param value="#{bolonhaBundle['curricularPlan.structure']}"/>
			</h:outputFormat>
			<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
			<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
		</h:outputLink>
		<h:outputText value="</li></ul>" escape="false"/>
	
		<h:outputText value="<p class='mtop2 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
			<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'years'}">
				<h:outputText value="#{bolonhaBundle['groups']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'groups'}">
				<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="years"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{ManagerCurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p class='mtop05 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{!empty ManagerCurricularCourseManagement.degreeCurricularPlan.root.childContexts}">	
			<h:outputText value="#{bolonhaBundle['curricularRules']}: " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.showRules == 'false'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="true"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{ManagerCurricularCourseManagement.showRules == 'true'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.showRules == 'true'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>				
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{ManagerCurricularCourseManagement.showRules == 'false'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
	
		<h:outputText value="<p class='mtop05 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{ManagerCurricularCourseManagement.showRules == 'true' && ManagerCurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'true'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'false'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="true"/>
				<f:param name="action" value="#{ManagerCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{ManagerCurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		<h:outputText value="<br/>" escape="false"/>
		<fc:degreeCurricularPlanRender 
			dcp="#{ManagerCurricularCourseManagement.degreeCurricularPlan}" 
			organizeBy="#{ManagerCurricularCourseManagement.organizeBy}"
			showRules="#{ManagerCurricularCourseManagement.showRules}"
			hideCourses="#{ManagerCurricularCourseManagement.hideCourses}" 
			executionYear="#{ManagerCurricularCourseManagement.executionYear}"
			module="/manager/bolonha"/>
	
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{ManagerCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{ManagerCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{ManagerCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{ManagerCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{ManagerCurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<p>" escape="false"/>		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}" action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
	</h:panelGroup>

</ft:tilesView>
