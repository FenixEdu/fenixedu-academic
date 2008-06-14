<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['context']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularCourseID' id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.contextIDToDelete' id='contextIDToDelete' name='contextIDToDelete' type='hidden' value='#{CurricularCourseManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>

		<h:outputText value="<p><strong>#{bolonhaBundle['name']}:</strong> " escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.name}</p>" escape="false"/>		

		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.parentContexts}" var="context">
			<h:panelGroup rendered="#{context.idInternal == CurricularCourseManagement.contextID}">								
				<h:outputText value="<p><strong>#{bolonhaBundle['courseGroup']}:</strong> " escape="false"/>
				<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>			
				<h:outputText value="<p><strong>#{bolonhaBundle['curricularPeriod']}:</strong> " escape="false"/>
				<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
			</h:panelGroup>
		</fc:dataRepeater>
		
		<h:panelGroup rendered="#{CurricularCourseManagement.toDelete && !empty CurricularCourseManagement.rulesLabels}">
			<h:outputText value="<br/><strong>#{bolonhaBundle['participating.curricularRules']}: </strong>" escape="false"/>
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CurricularCourseManagement.rulesLabels}" var="curricularRule">
				<h:outputText value="<li>#{curricularRule}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>

		</h:panelGroup>

		<h:outputText value="<p class='mtop2 mbottom2'><span class='warning0'>#{bolonhaBundle['confirmDeleteMessage']}</span></p>" escape="false"/>
		
		<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="buildCurricularPlan"
			actionListener="#{CurricularCourseManagement.deleteContext}" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="buildCurricularPlan"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>