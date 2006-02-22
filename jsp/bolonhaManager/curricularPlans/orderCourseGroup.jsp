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
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CourseGroupManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['order.group']} </h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='courseGroupID' name='courseGroupID' type='hidden' value='#{CourseGroupManagement.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input id='contextID' name='contextID' type='hidden' value='#{CourseGroupManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input id='position' name='position' type='hidden' value='#{CourseGroupManagement.position}'/>"/>		
		<h:outputText escape="false" value="<input id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>

		<h:outputText value="<p>#{bolonhaBundle['group']}: " escape="false"/>
		<h:outputText value="<b>#{CourseGroupManagement.name}</b></p><br/>" escape="false"/>

		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText styleClass="warning0" value="#{bolonhaBundle['confirmMoveMessage']}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CourseGroupManagement.orderCourseGroup}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="editCurricularPlanStructure"/>
		<h:outputText value="</p>" escape="false"/>	
	</h:form>
</ft:tilesView>