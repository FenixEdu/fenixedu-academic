<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.unitIDHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.personIDHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.creditsHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.beginDateHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.endDateHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.functionIDHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.executionPeriodHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.durationHidden}"/>
	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.disabledVarHidden}"/>

	<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
	
	<h:outputText styleClass="error" rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[scientificCouncilFunctionsManagementBackingBean.errorMessage]}"/>
	

	<h:outputText value="<p>" escape="false"/>
		<h:outputText value="#{bundle['label.name']}: " escape="false"/>		
		<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.person.name}"/>		
	<h:outputText value="</p>" escape="false"/>

	
	<h:outputText value="<table class='tstyle2 thlight thright'>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>
		<h:outputText value="<th>" escape="false"/>
			<h:outputText value="#{bundle['label.new.function']}" escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.function.name}"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>	
		<h:outputText value="<th>" escape="false"/>			
			<h:outputText value="#{bundle['label.search.unit']}:" escape="false"/>	
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>	
		<h:outputText value="<th>" escape="false"/>			
			<h:outputText value="#{bundle['label.credits']}" escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.credits}"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="<tr>" escape="false"/>	
		<h:outputText value="<th>" escape="false"/>			
			<h:outputText value="#{bundle['label.valid']}" escape="false"/>
		<h:outputText value="</th>" escape="false"/>
		<h:outputText value="<td>" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.beginDate}" escape="false"/>
			<h:outputText value="&nbsp;#{bundle['label.to']}&nbsp;" escape="false"/>
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.endDate}" escape="false"/>
		<h:outputText value="</td>" escape="false"/>
	<h:outputText value="</tr>" escape="false"/>
	<h:outputText value="</table>" escape="false"/>


	<h:outputText value="<p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirme']}" action="#{scientificCouncilFunctionsManagementBackingBean.associateNewFunction}" value="#{bundle['label.confirme']}" styleClass="inputbutton"/>			
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterUnit" immediate="true" value="#{bundle['alter.unit.button']}" styleClass="inputbutton"/>						
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterFunction" immediate="true" value="#{bundle['alter.function.button']}" styleClass="inputbutton"/>								
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>
	<h:outputText value="</p>" escape="false"/>
	
	</h:form>

</ft:tilesView>