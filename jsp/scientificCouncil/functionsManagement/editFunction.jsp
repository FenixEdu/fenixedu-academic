<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.personIDHidden}"/>
    	<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.personFunctionIDHidden}"/>
		
		<h:outputText value="<H2>#{bundle['label.edit.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2" columnClasses="valigntop">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>				
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[scientificCouncilFunctionsManagementBackingBean.errorMessage]}"/>
	
		<h:outputText value="<br/>" escape="false" />
	
		<h:panelGrid columns="2" styleClass="infoop">			
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{scientificCouncilFunctionsManagementBackingBean.functionID}">
				<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.validFunctions}"/>				
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.credits']}" id="credits" required="true" size="5" maxlength="5" value="#{scientificCouncilFunctionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginDate']}" maxlength="10" id="beginDate" required="true" size="10" value="#{scientificCouncilFunctionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>			
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endDate']}" maxlength="10" id="endDate" required="true" size="10" value="#{scientificCouncilFunctionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>				
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.next']}" action="#{scientificCouncilFunctionsManagementBackingBean.editFunction}" value="#{bundle['label.next']}" styleClass="inputbutton"/>				
			<h:commandButton alt="#{htmlAltBundle['commandButton.person']}" action="alterFunction" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>								
		</h:panelGrid>	

	</h:form>

</ft:tilesView>