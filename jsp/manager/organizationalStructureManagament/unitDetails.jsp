<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	
	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>
		
	<h:form>	
		<h:inputHidden binding="#{organizationalStructureBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.listingTypeValueToFunctionsHidden}"/>
		<h:inputHidden binding="#{organizationalStructureBackingBean.listingTypeValueToUnitsHidden}"/>
					
		<h:outputText styleClass="error" rendered="#{!empty organizationalStructureBackingBean.errorMessage}"
				value="#{bundle[organizationalStructureBackingBean.errorMessage]}<br/>" escape="false"/>
		
		<h:outputText value="<h2>#{bundle['title.unitDetails']}</h2>" escape="false" />
		
		<h:dataTable value="#{organizationalStructureBackingBean.unit}" var="unit"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.unit}">
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
					<h:outputText value="#{bundle['message.unitAcronym']}" />
				</f:facet>				
				<h:outputText value="#{unit.acronym}" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitWebAddress']}" />
				</f:facet>				
				<h:outputText value="#{unit.webAddress}" escape="false"/>
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
					<f:param value="#{organizationalStructureBackingBean.unit.beginDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitEndDate']}" />
				</f:facet>								
				<h:outputFormat value="{0, date, dd/MM/yyyy}" rendered="#{!empty organizationalStructureBackingBean.unit.endDate}">
					<f:param value="#{organizationalStructureBackingBean.unit.endDate}"/>
				</h:outputFormat>					
			</h:column>	
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="prepareEditUnit" value="#{bundle['link.edit']}">
					<f:param id="chooseUnitID1" name="chooseUnitID" value="#{unit.idInternal}"/>
				</fc:commandLink>				
			</h:column>	
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="#{organizationalStructureBackingBean.deleteUnit}" value="#{bundle['message.manager.delete']}">
					<f:param id="chooseUnitID3" name="chooseUnitID" value="#{unit.idInternal}"/>
				</fc:commandLink>				
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="prepareChooseParentUnit" value="#{bundle['message.subUnit']}"/>					
			</h:column>
			<h:column rendered="#{!empty organizationalStructureBackingBean.unit.parentUnits}">	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="prepareChooseParentUnitToRemove" value="#{bundle['message.topUnit']}">
					<f:param id="isToRemoveParentUnit" name="isToRemoveParentUnit" value="true"/>
				</fc:commandLink> 				
			</h:column>					
		</h:dataTable>									
					
		<h:outputText value="<br/><h3>#{bundle['message.subUnits']}</h3>" escape="false" />
		<fc:commandLink action="prepareCreateNewSubUnit" value="#{bundle['link.new.unit3']}"/>		
		<h:outputText value="<br/><br/>" escape="false"/>		
			
		<h:outputText value="<b>#{bundle['message.subUnitListingType']}</b>" escape="false"/>
		<fc:selectOneMenu value="#{organizationalStructureBackingBean.listingTypeValueToUnits}" 
			onchange="this.form.submit();">
			<f:selectItems value="#{organizationalStructureBackingBean.listingTypeToUnits}"/>				
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		<h:outputText value="<br/><br/>" escape="false"/>		
			
		<h:dataTable value="#{organizationalStructureBackingBean.allSubUnits}" var="unit"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.allSubUnits}">
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
					<h:outputText value="#{bundle['message.unitAcronym']}" />
				</f:facet>				
				<h:outputText value="#{unit.acronym}" escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.unitWebAddress']}" />
				</f:facet>				
				<h:outputText value="#{unit.webAddress}" escape="false"/>
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
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['title.relation.type']}" />
				</f:facet>								
				<h:outputText value="#{organizationalStructureBackingBean.unitRelationsAccountabilityTypes[unit.idInternal]}" escape="false"/>									
			</h:column>	
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="prepareEditUnit" value="#{bundle['link.edit']}">
					<f:param id="chooseUnitID2" name="chooseUnitID" value="#{unit.idInternal}"/>
				</fc:commandLink>				
			</h:column>	
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="#{organizationalStructureBackingBean.deleteSubUnit}" value="#{bundle['message.manager.delete']}">
					<f:param id="chooseUnitID3" name="chooseUnitID" value="#{unit.idInternal}"/>
				</fc:commandLink>				
			</h:column>				
		</h:dataTable>
		
		<h:outputText value="#{bundle['unit.withoutSubUnits']}<br/>" rendered="#{empty organizationalStructureBackingBean.allSubUnits}" 
				styleClass="error" escape="false"/>
		
		<h:outputText value="<br/><h3>#{bundle['title.Functions']}</h3>" escape="false" />		
		<fc:commandLink action="prepareCreateNewFunction" value="#{bundle['link.new.function']}"/>		
		<h:outputText value="<br/><br/>" escape="false"/>
		
		<h:outputText value="<b>#{bundle['message.functionListingType']}</b>" escape="false"/>
		<fc:selectOneMenu value="#{organizationalStructureBackingBean.listingTypeValueToFunctions}" 
			onchange="this.form.submit();">
			<f:selectItems value="#{organizationalStructureBackingBean.listingTypeToFunctions}"/>				
		</fc:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		<h:outputText value="<br/><br/>" escape="false"/>
		
		<h:dataTable value="#{organizationalStructureBackingBean.allNonInherentFunctions}" var="function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.allNonInherentFunctions}">
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
				<h:outputText value="#{bundleEnum[function.functionType.name]}" escape="false"/>
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
				<fc:commandLink action="prepareEditFunction" value="#{bundle['link.edit']}">
					<f:param id="functionID" name="functionID" value="#{function.idInternal}"/>
					<f:param id="listingTypeToFunctions" name="listingTypeToFunctions" value="#{organizationalStructureBackingBean.listingTypeValueToFunctions}"/>										
				</fc:commandLink>				
			</h:column>	
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="#{organizationalStructureBackingBean.deleteFunction}" value="#{bundle['message.manager.delete']}">
					<f:param id="functionID2" name="functionID" value="#{function.idInternal}"/>
				</fc:commandLink>				
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="#{organizationalStructureBackingBean.prepareAssociateInherentParentFunction}" value="#{bundle['message.inherentTo2']}">
					<f:param id="functionID3" name="functionID" value="#{function.idInternal}"/>					
				</fc:commandLink>				
			</h:column>
		</h:dataTable>					
				
		<h:outputText value="#{bundle['unit.withoutNonInherentFunctions']}<br/>" rendered="#{empty organizationalStructureBackingBean.allNonInherentFunctions}" 
				styleClass="error" escape="false"/>
				
		<h:outputText value="<br/><h3>#{bundle['title.inherentFunctions']}</h3>" escape="false" />				
		
		<h:dataTable value="#{organizationalStructureBackingBean.allInherentFunctions}" var="function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty organizationalStructureBackingBean.allInherentFunctions}">
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
				<h:outputText value="#{bundleEnum[function.functionType.name]}" escape="false"/>
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
					<h:outputText value="#{bundle['message.inherentTo']}" />
				</f:facet>				
				<h:outputText value="#{function.parentInherentFunction.name}" escape="false"/>	
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['message.belongsTo']}" />
				</f:facet>				
				<h:outputText value="#{function.parentInherentFunction.unit.name}" escape="false"/>	
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="prepareEditFunction" value="#{bundle['link.edit']}">
					<f:param id="functionID" name="functionID" value="#{function.idInternal}"/>
				</fc:commandLink>				
			</h:column>	
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="#{organizationalStructureBackingBean.deleteFunction}" value="#{bundle['message.manager.delete']}">
					<f:param id="functionID2" name="functionID" value="#{function.idInternal}"/>
				</fc:commandLink>				
			</h:column>
			<h:column>	
				<f:facet name="header">
					<h:outputText value="#{bundle['message.action']}" />
				</f:facet>								
				<fc:commandLink action="#{organizationalStructureBackingBean.disassociateInherentFunction}" value="#{bundle['message.inherentTo3']}">
					<f:param id="functionID3" name="functionID" value="#{function.idInternal}"/>
				</fc:commandLink>				
			</h:column>
		</h:dataTable>			
		
		<h:outputText value="#{bundle['unit.withoutInherentFunctions']}<br/>" rendered="#{empty organizationalStructureBackingBean.allInherentFunctions}" 
				styleClass="error" escape="false"/>		
		
		<h:outputText value="<br/>" escape="false" />	
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" action="listAllUnits" immediate="true" value="#{bundle['label.return']}" styleClass="inputbutton"/>								
				
	</h:form>
	
</ft:tilesView>