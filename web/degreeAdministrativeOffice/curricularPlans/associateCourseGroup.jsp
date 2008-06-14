<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.degreeAdministrativeOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CourseGroupManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CourseGroupManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['associate.course.group']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CourseGroupManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.parentCourseGroupID' id='parentCourseGroupID' name='parentCourseGroupID' type='hidden' value='#{CourseGroupManagement.parentCourseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CourseGroupManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CourseGroupManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CourseGroupManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CourseGroupManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CourseGroupManagement.toOrder}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
		
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroupAssociateTo']}:</label>" escape="false"/>
		<h:outputText value="<strong>#{CourseGroupManagement.parentName}</strong>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroupToAssociate']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{CourseGroupManagement.courseGroupID}">
			<f:selectItems value="#{CourseGroupManagement.courseGroups}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{CourseGroupManagement.beginExecutionPeriodID}">
			<f:selectItems value="#{CourseGroupManagement.beginExecutionPeriodItems}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<h:selectOneMenu value="#{CourseGroupManagement.endExecutionPeriodID}">
			<f:selectItems value="#{CourseGroupManagement.endExecutionPeriodItems}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="</fieldset></div>" escape="false"/>
		
		<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
			action="#{CourseGroupManagement.addContext}"/>	
		<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="editCurricularPlanStructure"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</ft:tilesView>