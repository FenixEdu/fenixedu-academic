<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
<!--
.alignRight {
	text-align: right;
}
-->
</style>
<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>

	<h:outputText value="#{CurricularCourseManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['context']}"/>
	</h:outputFormat>
	<h:outputText styleClass="error" rendered="#{!empty CurricularCourseManagement.errorMessage}"
			value="#{bolonhaBundle[CurricularCourseManagement.errorMessage]}<br/>" escape="false"/>			
	<h:messages styleClass="infomsg"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/>
		<h:outputText escape="false" value="<input id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'"/>
		<h:outputText escape="false" value="<input id='contextIDToDelete' name='contextIDToDelete' type='hidden' value='#{CurricularCourseManagement.contextID}'"/>
		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['curricularCourseInformation']}: <br/>"  escape="false"/>		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{bolonhaBundle['name']}: "/>
			<h:outputText value="#{CurricularCourseManagement.curricularCourse.name}"/>
		</h:panelGrid>
		<br/>		
		<h:outputText style="font-weight: bold" value="#{bolonhaBundle['context']}: <br/>" escape="false"/>
		<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.degreeModuleContexts}" var="context">
			<h:panelGrid columnClasses="alignRight infocell, infocell," columns="2" border="0"
				rendered="#{context.idInternal == CurricularCourseManagement.contextID}">
								
				<h:outputText value="#{bolonhaBundle['courseGroup']}: "/>
				<h:outputText value="#{context.courseGroup.name}"/>
				
				<h:outputText value="#{bolonhaBundle['curricularYear']}: " />
				<h:outputText value="#{context.curricularSemester.curricularYear.year}"/>
				
				<h:outputText value="#{bolonhaBundle['semester']}: " />
				<h:outputText value="#{context.curricularSemester.semester}"/>
			</h:panelGrid>
		</fc:dataRepeater>
		<br/>
		<h:outputText styleClass="error" value="#{bolonhaBundle['confirmDeleteMessage']}<br/><hr>" escape="false"/>		
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CurricularCourseManagement.deleteCurricularCourseContextReturnPath}"
			actionListener="#{CurricularCourseManagement.deleteContext}" />
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>