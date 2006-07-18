<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
	<h:form>	

		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.chooseUnitIDHidden}"/>	
		
		<h:messages infoClass="success0" errorClass="error0" globalOnly="true" layout="table"/>
		
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
										
		<h:outputText value="<h2>#{bundle['title.chooseUnit']}</h2><br/>" escape="false" />		
		
		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.unit.name}" escape="false"/>												
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />	
		
		<h:dataTable value="#{organizationalStructureBackingBean.chooseUnit}" var="unit"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.chooseUnit}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitName']}" />
				</f:facet>				
				<h:outputText value="<strong>#{unit.name}</strong>" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitCostCenter']}" />
				</f:facet>				
				<h:outputText value="#{unit.costCenterCode}" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitType']}" />
				</f:facet>				
				<h:outputText value="#{bundleEnum[unit.type.name]}" escape="false"/>
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitBeginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{unit.beginDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitEndDate']}" />
				</f:facet>								
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty unit.endDate}">
					<f:param value="#{unit.endDate}"/>
				</h:outputFormat>					
			</h:column>										
		</h:dataTable>			
				
		<h:outputText value="<br/>" escape="false" />			
		<h:outputText value="<b>#{bundle['title.relation.type']}</b>:" escape="false" />		
		<fc:selectOneMenu value="#{organizationalStructureBackingBean.unitRelationTypeValue}">
			<f:selectItems value="#{organizationalStructureBackingBean.unitRelationTypes}"/>				
		</fc:selectOneMenu>
		<h:outputText value="<br/><br/>" escape="false" />	
						
		<h:outputText value="<br/>" escape="false" />								
		<h:commandButton alt="#{htmlAltBundle['commandButton.choose']}" action="#{organizationalStructureBackingBean.associateParentUnit}" value="#{bundle['button.choose']}" 
			styleClass="inputbutton"/> 		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="listAllUnitsToChooseParentUnit" immediate="true" value="#{bundle['label.return']}"
			 styleClass="inputbutton"/>	
				
	</h:form>
</ft:tilesView>