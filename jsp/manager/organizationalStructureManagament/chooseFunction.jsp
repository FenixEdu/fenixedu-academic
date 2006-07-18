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
		<h:inputHidden binding="#{organizationalStructureBackingBean.functionIDHidden}"/>
										
		<h:outputText value="<h2>#{bundle['title.chooseFunction']}</h2><br/>" escape="false" />		
		
		<h:panelGrid styleClass="infoselected" columns="2">
			<h:outputText value="<b>#{bundle['message.name']}</b>" escape="false"/>		
			<h:outputText value="#{organizationalStructureBackingBean.chooseUnit.name}" escape="false"/>												
		</h:panelGrid>
		
		<h:outputText value="<br/>" escape="false" />		
		
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
								
		<h:dataTable value="#{organizationalStructureBackingBean.chooseUnit.functions}" var="function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.chooseUnit.functions}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['title.FunctionName']}" />
				</f:facet>				
				<h:outputText value="<strong>#{function.name}</strong>" escape="false"/>				
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitType']}" />
				</f:facet>				
				<h:outputText value="#{bundleEnum[function.type.name]}" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitBeginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty function.beginDate}">
					<f:param value="#{function.beginDate}"/>
				</h:outputFormat>	
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitEndDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty function.endDate}">
					<f:param value="#{function.endDate}"/>
				</h:outputFormat>			
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<h:commandLink action="#{organizationalStructureBackingBean.associateInherentParentFunction}" value="#{bundle['button.choose']}">
					<f:param id="principalFunctionID" name="principalFunctionID" value="#{function.idInternal}"/>
				</h:commandLink>				
			</h:column>
		</h:dataTable>		
		
		<h:outputText value="#{bundle['unit.withoutFunctions']}<br/>" rendered="#{empty organizationalStructureBackingBean.chooseUnit.functions}" 
				styleClass="error" escape="false"/>				
		
		<h:outputText value="<br/>" escape="false" />	
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="listAllUnits" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
				
	</h:form>
</ft:tilesView>