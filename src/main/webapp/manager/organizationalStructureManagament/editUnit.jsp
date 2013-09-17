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
		<h:inputHidden binding="#{organizationalStructureBackingBean.chooseUnitIDHidden}"/>
				
		<h:outputText value="<h2>#{bundle['link.editUnit']}</h2><br/>" escape="false" />
	
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">		
		
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="name" required="true" size="60" value="#{organizationalStructureBackingBean.unitName}"/>
				<h:message for="name" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.name.en']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="nameEn" required="false" size="60" value="#{organizationalStructureBackingBean.unitNameEn}"/>
				<h:message for="nameEn" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.name.card']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitName']}" id="nameCard" required="false" size="42"  maxlength="42" value="#{organizationalStructureBackingBean.unitNameCard}"/>
				<h:message for="nameCard" styleClass="error"/>
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.costCenter']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitCostCenter']}" id="costCenter" size="10" value="#{organizationalStructureBackingBean.unitCostCenter}">
					<fc:regexValidator regex="[0-9]*"/>
				</h:inputText>
				<h:message for="costCenter" styleClass="error"/>
			</h:panelGroup>						
			
			<h:outputText value="<b>#{bundle['message.acronym']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitAcronym']}" size="10" value="#{organizationalStructureBackingBean.unitAcronym}"/>				
			</h:panelGroup>	
			
			<h:outputText value="<b>#{bundle['message.webAddress']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitWebAddress']}" size="30" value="#{organizationalStructureBackingBean.unitWebAddress}"/>				
			</h:panelGroup>									
						
			<h:outputText value="<b>#{bundle['message.initialDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitBeginDate']}" maxlength="10" id="beginDate" required="true" size="10" value="#{organizationalStructureBackingBean.unitBeginDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.endDate']}:</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.unitEndDate']}" maxlength="10" id="endDate" size="10" value="#{organizationalStructureBackingBean.unitEndDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>
				<h:outputText value="#{bundle['date.format']}"/>	
				<h:message for="endDate" styleClass="error"/>			
			</h:panelGroup>
			
			<h:outputText value="<b>#{bundle['message.canBeResponsibleOfSpaces']}</b>" escape="false"/>
			<h:selectBooleanCheckbox value="#{organizationalStructureBackingBean.canBeResponsibleOfSpaces}" />
		
			<h:outputText value="<b>#{bundle['message.classification']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitClassificationName}">
				<f:selectItems value="#{organizationalStructureBackingBean.validUnitClassifications}"/>				
			</fc:selectOneMenu>
				
			<h:outputText value="<b>#{bundle['message.department']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.departmentID}">
				<f:selectItems value="#{organizationalStructureBackingBean.departments}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.administrativeOffice']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.administrativeOfficeID}">
				<f:selectItems value="#{organizationalStructureBackingBean.administrativeOffices}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.degree']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.degreeID}">
				<f:selectItems value="#{organizationalStructureBackingBean.degrees}"/>				
			</fc:selectOneMenu>
			
			<h:outputText value="<b>#{bundle['message.campus']}</b>" escape="false"/>
			<fc:selectOneMenu value="#{organizationalStructureBackingBean.campusID}">
				<f:selectItems value="#{organizationalStructureBackingBean.campuss}"/>				
			</fc:selectOneMenu>
			
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />	
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" action="#{organizationalStructureBackingBean.editUnit}" value="#{bundle['button.submit']}" styleClass="inputbutton"/>				
			<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="backToUnitDetails" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
		</h:panelGrid>				
				
	</h:form>
	
</ft:tilesView>