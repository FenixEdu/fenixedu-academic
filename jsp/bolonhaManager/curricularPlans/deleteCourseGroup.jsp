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
	
	<h:outputText value="#{CourseGroupManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['delete.param']} </h2>" escape="false">
		<f:param value="#{bolonhaBundle['courseGroup']}"/>
	</h:outputFormat>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='courseGroupID' name='courseGroupID' type='hidden' value='#{CourseGroupManagement.courseGroupID}'/>"/>
		<br/>
		<h:panelGroup styleClass="bgcolor1">
			<h:outputText style="font-weight: bold" value="#{bolonhaBundle['name']}: "/>
			<h:outputText value="#{CourseGroupManagement.name}<br/>" escape="false"/>
		</h:panelGroup>
		<br/><br/>
		<h:outputText styleClass="error0" value="#{bolonhaBundle['confirmDeleteMessage']}"/>
		<br/>
		<hr>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CourseGroupManagement.deleteCourseGroup}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="editCurricularPlanStructure"/>		
	</h:form>
</ft:tilesView>