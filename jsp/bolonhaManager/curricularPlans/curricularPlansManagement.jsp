<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>
.italic {
font-style: italic
}
</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="Person Department" styleClass="italic"/>
	
	<h2><h:outputText value="#{bolonhaBundle['curricularPlansManagement']}"/></h2>

	<h:outputLink value="createCurricularPlan.faces">
		<h:outputText value="* #{bolonhaBundle['createCurricularPlan']}" />
	</h:outputLink>
	<br/>
	<h3><h:outputText value="#{bolonhaBundle['curricularPlans']}" /></h3>
	<h:outputText value="#{bolonhaBundle['draft']}: " styleClass="italic"/>
	<h:outputText value="#{bolonhaBundle['published']}: " styleClass="italic"/>
	<h:outputText value="#{bolonhaBundle['approved']}: " styleClass="italic"/>
	
</ft:tilesView>