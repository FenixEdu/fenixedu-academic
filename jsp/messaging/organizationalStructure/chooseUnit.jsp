<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.page.structure" attributeName="body-inline">

<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
<f:loadBundle basename="resources/MessagingResources" var="messagingResources"/>
	
<h:form>

	<h:inputHidden binding="#{organizationalStructure.unitIDHidden}" />			

	<h:outputText value="#{organizationalStructure.title}" escape="false"/>	

	
	<h:outputText value="<table class='tstyle2 thlight thright thbgnone'>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{messagingResources['label.unit.webAddress']}:" escape="false" rendered="#{!empty organizationalStructure.unit.webAddress}"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:outputText value="<a href='#{organizationalStructure.unit.webAddress}' target='_blank'>#{organizationalStructure.unit.webAddress}</a>" escape="false" rendered="#{!empty organizationalStructure.unit.webAddress}"/>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{messagingResources['label.unit.costCenterCode']}:" escape="false" rendered="#{!empty organizationalStructure.unit.costCenterCode}"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:outputText value="#{organizationalStructure.unit.costCenterCode}" escape="false" rendered="#{!empty organizationalStructure.unit.costCenterCode}"/>		
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{messagingResources['label.choose.year']}:" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<fc:selectOneMenu value="#{organizationalStructure.choosenExecutionYearID}" onchange="this.form.submit();">
					<f:selectItems value="#{organizationalStructure.executionYears}" />
				</fc:selectOneMenu>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{messagingResources['label.find.organization.listing.type']}:" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<fc:selectOneMenu value="#{organizationalStructure.listType}" onchange="this.form.submit();">
					<f:selectItems value="#{organizationalStructure.listingType}"/>				
				</fc:selectOneMenu>	
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>


	<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>	

	<h:outputText value="<p>" escape="false"/>
		<h:outputLink value="#{displayEvaluationsToEnrol.contextPath}/messaging/organizationalStructure/structurePage.faces">
			<h:outputText value="« #{messagingResources['messaging.back.label']}" escape="false"/>
		</h:outputLink>
	<h:outputText value="</p>" escape="false"/>

	<h:outputText value="#{organizationalStructure.functions}" escape="false"/>			

</h:form>


<h:outputText value="<div class='mtop2 mbottom025'><em>" escape="false" /><h:outputText value="#{messagingResources['label.subtitle']}:" escape="false"/><h:outputText value="</em></div>" escape="false" />
<h:outputText value="<div class='mvert025'><div style='width: 10px; height: 10px; background-color: #86705a; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>" escape="false" /><h:outputText value="#{messagingResources['label.unit.working.employees']}" escape="false"/><h:outputText value="</em></div></div>" escape="false" />
<h:outputText value="<div class='mvert025'><div style='width: 10px; height: 10px; background-color: #5a7086; margin-top: 4px; float: left;'></div> <div style='padding-left: 15px;'><em>" escape="false" /><h:outputText value="#{messagingResources['label.person.function']}" escape="false"/><h:outputText value="</em></div></div>" escape="false" />

			
</ft:tilesView> 