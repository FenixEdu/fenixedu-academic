<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
		
	<h:form>	
		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>

		<h:outputText value="<h2>#{bundle['link.new.function2']}</h2><br/>" escape="false" />
	
		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.unit.name}" escape="false"/>												
		</h:panelGrid>	

		<h:outputText value="<br/>" escape="false" />	
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>

		<h:panelGrid columns="2" styleClass="infoop">		
		
			<h:outputText value="<b>#{bundle['title.FunctionName']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.functionName']}" id="name" required="true" size="30" value="#{organizationalStructureBackingBean.functionName}"/>
				<h:message for="name" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['title.FunctionName.en']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.functionName']}" id="nameEn" required="false" size="30" value="#{organizationalStructureBackingBean.functionNameEn}"/>
				<h:message for="nameEn" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.initialDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.functionBeginDate']}" maxlength="10" id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.functionBeginDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.endDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.functionEndDate']}" maxlength="10" id="endDate" size="10" value="#{organizationalStructureBackingBean.functionEndDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>
				<h:message for="endDate" styleClass="error"/>					
			</h:panelGroup>
		
			<h:outputText value="<b>#{bundle['message.functionType']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.functionTypeName}">
				<f:selectItems value="#{organizationalStructureBackingBean.validFunctionType}"/>				
			</fc:selectOneMenu>		
		
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.function2']}" action="#{organizationalStructureBackingBean.createFunction}" value="#{bundle['link.new.function2']}" styleClass="inputbutton"/>				
			<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
		</h:panelGrid>		
				
	</h:form>
	
</ft:tilesView>