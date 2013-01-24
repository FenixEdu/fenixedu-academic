<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.layout.two-column.contents" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['view.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularPlan']}"/>
	</h:outputFormat>
	
	<h:panelGroup rendered="#{empty AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
		<h:outputText value="<p><em>#{bolonhaBundle['error.curricularPlanHasNoExecutionDegrees']}</em><p>" escape="false"/>
	</h:panelGroup>

	<h:panelGroup rendered="#{!empty AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.executionDegrees}">
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

		<h:outputText value="<br/>" escape="false"/>
		
		<h:outputText value="<div class='invisible'>" escape="false"/>
		<h:outputText value="<ul><li>" escape="false"/>
		<h:outputLink value="viewCurricularPlanStructure.faces" rendered="#{!empty AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
			<h:outputFormat value="#{bolonhaBundle['view.param']}" escape="false">
				<f:param value="#{bolonhaBundle['curricularPlan.structure']}"/>
			</h:outputFormat>
			<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
			<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
		</h:outputLink>
		<h:outputText value="</li></ul>" escape="false"/>
	
		<h:outputText value="<p class='mtop2 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{!empty AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
			<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'years'}">
				<h:outputText value="#{bolonhaBundle['groups']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="groups"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['groups']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'groups'}">
				<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="years"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p class='mtop05 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{!empty AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.root.childContexts}">	
			<h:outputText value="#{bolonhaBundle['curricularRules']}: " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.showRules == 'false'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="true"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.showRules == 'true'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.showRules == 'true'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>				
				<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="false"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.showRules == 'false'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
	
		<h:outputText value="<p class='mtop05 mbottom0'>" escape="false"/>
		<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.showRules == 'true' && AcademicAdministrationCurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{bolonhaBundle['curricularCourses']}: " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'true'}">
				<h:outputText value="#{bolonhaBundle['show']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="false"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['show']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'false'}" escape="false"/>
			<h:outputText value=" , " escape="false"/>
			<h:outputLink value="viewCurricularPlan.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'false'}">
				<h:outputText value="#{bolonhaBundle['hide']}" />
				<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}"/>
				<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
				<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
				<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
				<f:param name="hideCourses" value="true"/>
				<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}" />
			</h:outputLink>
			<h:outputText value="<span class='highlight3'>#{bolonhaBundle['hide']}</span>" rendered="#{AcademicAdministrationCurricularCourseManagement.hideCourses == 'true'}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		<h:outputText value="<br/>" escape="false"/>
		<fc:degreeCurricularPlanRender 
			dcp="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan}" 
			organizeBy="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"
			showRules="#{AcademicAdministrationCurricularCourseManagement.showRules}"
			hideCourses="#{AcademicAdministrationCurricularCourseManagement.hideCourses}" 
			executionYear="#{AcademicAdministrationCurricularCourseManagement.executionYear}"
			module="/academicAdministration/bolonha"/>
	
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<p>" escape="false"/>		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}" action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
	</h:panelGroup>

</ft:tilesView>
