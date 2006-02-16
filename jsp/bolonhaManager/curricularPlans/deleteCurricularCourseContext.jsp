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
		<f:param value="#{bolonhaBundle['context']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'/>"/>
		<h:outputText escape="false" value="<input id='contextIDToDelete' name='contextIDToDelete' type='hidden' value='#{CurricularCourseManagement.contextID}'/>"/>

		<h:outputText value="<p><strong>#{bolonhaBundle['name']}:</strong> " escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.name}</p>" escape="false"/>		

		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
			<h:panelGroup rendered="#{context.idInternal == CurricularCourseManagement.contextID}">								
				<h:outputText value="<p><strong>#{bolonhaBundle['courseGroup']}:</strong> " escape="false"/>
				<h:outputText value="#{context.courseGroup.name}</p>" escape="false"/>			
				<h:outputText value="<p><strong>#{bolonhaBundle['curricularPeriod']}:</strong> " escape="false"/>
				<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
			</h:panelGroup>
		</fc:dataRepeater>
		
		<h:outputText value="<br/><p>" escape="false"/>
		<h:outputText styleClass="error0" value="#{bolonhaBundle['confirmDeleteMessage']}" escape="false"/>		
		<h:outputText value="</p><br/>" escape="false"/>
		
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CurricularCourseManagement.deleteCurricularCourseContextReturnPath}"
			actionListener="#{CurricularCourseManagement.deleteContext}" />
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>