<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.page.structure" attributeName="body-inline">

<style>
	.eo_highlight { background-color: #ffc; }
</style>

<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
<f:loadBundle basename="resources/MessagingResources" var="messagingResources"/>
	
<h:form>
	<h:inputHidden value="#{organizationalStructure.unitID}" />			
	<h:inputHidden value="#{organizationalStructure.choosenExecutionYearID}" />

	<h:panelGroup>	
		<h:outputText value="#{organizationalStructure.title}" escape="false"/>	
	</h:panelGroup>

	<h:outputText value="<br/>" escape="false"/>
	
	<h:panelGrid columns="2">	
		<h:outputText value="<b>#{messagingResources['label.choose.year']}:</b>" escape="false"/>
		<fc:selectOneMenu value="#{organizationalStructure.choosenExecutionYearID}" onchange="this.form.submit();">
			<f:selectItems value="#{organizationalStructure.executionYears}" />
		</fc:selectOneMenu>
		
		<h:outputText value="<b>#{messagingResources['label.find.organization.listing.type']}:</b>" escape="false"/>
		<fc:selectOneMenu value="#{organizationalStructure.listType}" onchange="this.form.submit();">
			<f:selectItems value="#{organizationalStructure.listingType}"/>				
		</fc:selectOneMenu>		
	</h:panelGrid>
	
	<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'" escape="false"/>	
				
	<h:outputText value="<br/>" escape="false"/>
	<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/messaging/organizationalStructure/structurePage.faces">
		<h:outputText value="#{messagingResources['messaging.back.label']}" escape="false"/>
	</h:outputLink>

	<h:outputText value="#{organizationalStructure.functions}" escape="false"/>			

</h:form>

<h:outputText value="<p class='mtop2 mbottom025'><em>" escape="false" /><h:outputText value="#{messagingResources['label.subtitle']}:" escape="false"/><h:outputText value="</em></p>" escape="false" />
<h:outputText value="<p class='mvert025'><div style='width: 10px; height: 10px; background-color: #606080; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>" escape="false" /><h:outputText value="#{messagingResources['label.unit.working.employees']}" escape="false"/><h:outputText value="</em></div></p>" escape="false" />
<h:outputText value="<p class='mvert025'><div style='width: 10px; height: 10px; background-color: #808060; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>" escape="false" /><h:outputText value="#{messagingResources['label.person.function']}" escape="false"/><h:outputText value="</em></div></p>" escape="false" />
			
</ft:tilesView> 