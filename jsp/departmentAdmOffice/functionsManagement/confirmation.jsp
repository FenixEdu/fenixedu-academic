<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

	<h:inputHidden binding="#{functionsManagementBackingBean.unitIDHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.creditsHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.beginDateHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.endDateHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.functionIDHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.executionPeriodHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.durationHidden}"/>
	<h:inputHidden binding="#{functionsManagementBackingBean.disabledVarHidden}"/>

	<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
	<h:outputText value="<br/>" escape="false" />
	
	<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
				value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
	
	<h:panelGrid columns="2">
		<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
		<h:outputText value="#{functionsManagementBackingBean.person.nome}"/>		
	</h:panelGrid>	
	
	<h:outputText value="<br/>" escape="false" />
	
	<h:panelGrid columns="2">		
		<h:outputText value="<b>#{bundle['label.new.function']}</b>" escape="false"/>	
		<h:outputText value="#{functionsManagementBackingBean.function.name}"/>
				
		<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>
		<h:panelGroup>
			<h:outputText value="#{functionsManagementBackingBean.unit.parentUnitsPresentationNameWithBreakLine}" escape="false"/>				
			<h:outputText value="<br/>" escape="false"/>			
			<h:outputText value="#{functionsManagementBackingBean.unit.presentationName}"/>	
		</h:panelGroup>	
	
		<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>	
		<h:outputText value="#{functionsManagementBackingBean.credits}"/>
	
		<h:outputText value="<b>#{bundle['label.valid']}</b>" escape="false"/>	
		<h:panelGroup>
			<h:outputText value="#{functionsManagementBackingBean.beginDate}" escape="false"/>	
			<h:outputText value="<b>&nbsp;#{bundle['label.to']}&nbsp;</b>" escape="false"/>	
			<h:outputText value="#{functionsManagementBackingBean.endDate}" escape="false"/>		
		</h:panelGroup>
	</h:panelGrid>
		
	<h:outputText value="<br/>" escape="false" />
	<h:panelGrid columns="4">
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirme']}" action="#{functionsManagementBackingBean.associateNewFunction}" value="#{bundle['label.confirme']}" styleClass="inputbutton"/>			
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterUnit" immediate="true" value="#{bundle['alter.unit.button']}" styleClass="inputbutton"/>						
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterFunction" immediate="true" value="#{bundle['alter.function.button']}" styleClass="inputbutton"/>								
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>
	</h:panelGrid>
	
	</h:form>

</ft:tilesView>