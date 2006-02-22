<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularCourse']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'/>"/>
		<h:outputText escape="false" value="<input id='contextIDToDelete' name='contextIDToDelete' type='hidden' value='#{CurricularCourseManagement.contextIDToDelete}'/>"/>
		<h:outputText escape="false" value="<input id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
				
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: <br/>"  escape="false"/>		
		<h:outputText value="#{bolonhaBundle['name']}: "/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.name}"/>		
		<br/><br/>
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['context']}: <br/>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
			<h:panelGroup rendered="#{context.idInternal == CurricularCourseManagement.contextIDToDelete}">								
				<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
				<h:outputText value="#{context.courseGroup.name}</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['curricularPeriod']}:</label>" escape="false"/>
				<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
			</h:panelGroup>
		</fc:dataRepeater>
		
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.rulesLabels}">
			<h:outputText value="<br/><strong>#{bolonhaBundle['participating.curricularRules']}: </strong>" escape="false"/>
			<h:outputText value="<ul>" escape="false"/>
			<fc:dataRepeater value="#{CurricularCourseManagement.rulesLabels}" var="curricularRule">
				<h:outputText value="<li>#{curricularRule}</li>" escape="false"/>
			</fc:dataRepeater>
			<h:outputText value="</ul>" escape="false"/>
			<h:outputText value="<strong>#{bolonhaBundle['note']}:</strong> " escape="false"/>
			<h:outputText value="#{bolonhaBundle['confirm.delete.participating.curricularRules']}" escape="false"/>
		</h:panelGroup>
		<h:outputText value="</fieldset><br/>" escape="false"/>

		<h:outputText styleClass="success0" value="#{bolonhaBundle['deleteLastCurricularCourseContext']}<br/>" escape="false"/>
		<h:outputText value="<br/>" escape="false"/>
		<h:outputText styleClass="error0" value="#{bolonhaBundle['confirmDeleteMessage']}<br/>" escape="false"/>
		<h:outputText value="<br/><hr>" escape="false"/>
		
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			 action="buildCurricularPlan" actionListener="#{CurricularCourseManagement.forceDeleteContext}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>