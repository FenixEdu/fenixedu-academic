<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnumeration"/>
	
	<h:form>
	
		<h:outputText value="<h2>#{bundle['label.edit.advisory']}</h2><br/>" escape="false"/>
		
		<h:inputHidden binding="#{advisoriesManagementBackingBean.advisoryIDHidden}"/>
	
		<h:outputText styleClass="error" rendered="#{!empty advisoriesManagementBackingBean.errorMessage}"
				value="#{bundle[advisoriesManagementBackingBean.errorMessage]}"/>
	
		<h:panelGrid columns="2">
			<h:outputText value="#{bundle['property.advisory.from']}"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.sender']}" id="sender" required="true" size="50" value="#{advisoriesManagementBackingBean.sender}"/>
				<h:message for="sender" styleClass="error"/>
			</h:panelGroup>
		
			<h:outputText value="#{bundle['property.advisory.subject']}"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.subject']}" id="subject" required="true" size="50" value="#{advisoriesManagementBackingBean.subject}"/>
				<h:message for="subject" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['property.advisory.expirationDate']}"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.expires']}" id="expires" required="true" size="20" value="#{advisoriesManagementBackingBean.expires}" />				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="expires" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="#{bundle['property.advisory.message']}"/>
			<h:panelGroup>
				<h:inputTextarea id="message" required="true" cols="75" rows="8" value="#{advisoriesManagementBackingBean.message}"/>
				<h:message for="message" styleClass="error"/>			
			</h:panelGroup>
		 
		 </h:panelGrid>	
		 
		 <h:outputText value="<br/>#{bundle['property.advisory.recipients']}" escape="false"/>
		 <h:outputText value="<b>#{bundleEnumeration[advisoriesManagementBackingBean.peopleOfAdvisory.name]}</b><br/>" 
		 	rendered="#{advisoriesManagementBackingBean.peopleOfAdvisory != null}" escape="false"/>
	
	  	 <h:outputText value="<br/>" escape="false"/>
		 <h:commandButton alt="#{htmlAltBundle['commandButton.modifications']}" styleClass="inputbutton" action="#{advisoriesManagementBackingBean.editAdvisory}" value="#{bundle['label.manager.save.modifications']}" />
		 <h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" action="editAdvisory" immediate="true" value="#{bundle['label.return']}" />
					 		 		 	
	</h:form>
</ft:tilesView>