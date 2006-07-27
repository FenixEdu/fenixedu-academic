<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
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
		<h:outputLink value="viewCurricularPlanStructure.faces" rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
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
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
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
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
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
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.showRules == 'false'}">
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
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.showRules == 'true'}">
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
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.hideCourses == 'true'}">
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
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.hideCourses == 'false'}">
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
			executionYear="#{CurricularCourseManagement.executionYear}"/>
	
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

</ft:tilesView>
