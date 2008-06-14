<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CourseGroupManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['courseGroup']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.courseGroupID' id='courseGroupID' name='courseGroupID' type='hidden' value='#{CourseGroupManagement.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{CourseGroupManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CurricularCourseManagement.toOrder}'/>"/>

		<h:outputText value="<p>#{bolonhaBundle['group']}: " escape="false"/>
		<h:outputText value="<b>#{CourseGroupManagement.name}</b></p><br/>" escape="false"/>
		
		<h:panelGroup rendered="#{!empty CourseGroupManagement.rulesLabels}">
			<h:outputText value="<p><strong>#{bolonhaBundle['participating.curricularRules']}: </strong><br/>" escape="false"/>
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CourseGroupManagement.rulesLabels}" var="curricularRule">
				<h:outputText value="<li>#{curricularRule}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
			<h:outputText styleClass="error0" value="#{bolonhaBundle['confirm.delete.participating.curricularRules']}<br/>" escape="false"/>
			<h:outputText value="<br/>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText styleClass="warning0" value="#{bolonhaBundle['confirmDeleteMessage']}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CourseGroupManagement.deleteCourseGroup}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="editCurricularPlanStructure"/>
		<h:outputText value="</p>" escape="false"/>	
	</h:form>
</ft:tilesView>