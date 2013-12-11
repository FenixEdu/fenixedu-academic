<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<ft:tilesView definition="df.page.functionsManagement" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.personIDHidden}"/>
			
		<h:outputText value="<H2>#{bundle['label.search.functionManagement']}</H2>" escape="false"/>	

		<h:outputText value="<br/>" escape="false" />	

		<h:panelGroup>
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{managerFunctionsManagementBackingBean.person.name}" escape="false"/>		
		</h:panelGroup>
		
		<h:outputText value="<br/><br/>" escape="false" />	
		
		<h:commandLink value="#{bundle['label.associate']}" action="associateNewFunction" />
		
		<h:outputText value="<br/><br/>" escape="false" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"/>	
		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[managerFunctionsManagementBackingBean.errorMessage]}" escape="false"/>		
					
		<h:outputText value="<br/><br/><h3>#{bundle['label.active.functions']}</h3>" escape="false" />
	
		<h:dataTable value="#{managerFunctionsManagementBackingBean.activeFunctions}" var="person_function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty managerFunctionsManagementBackingBean.activeFunctions}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.function']}" />
				</f:facet>				
				<h:outputText value="<strong>#{person_function.function.name}</strong>" escape="false"/>				
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.search.unit']}" />
				</f:facet>				
				<h:outputText value="#{person_function.function.unit.presentationName}"/>
			</h:column>	
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.belongs.to']}" />
				</f:facet>				
				<h:outputText value="#{person_function.function.unit.parentUnitsPresentationName}"/>				
			</h:column>	
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.beginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.beginDateInDateType}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.endDate']}" />
				</f:facet>				
				<h:outputFormat rendered="#{!empty person_function.endDate}" value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.endDateInDateType}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['link.group.creditsManagement']}" />
				</f:facet>				
				<h:outputText value="#{person_function.credits}" />
			</h:column>
			<h:column> 						
				<h:commandLink action="prepareEditFunction">
					<h:outputText value="#{bundle['link.functions.management.edit']}"/>					
					<f:param name="personFunctionID" id="personFunctionID1" value="#{person_function.externalId}"/>
					<f:param name="functionID" id="functionID" value="#{person_function.function.externalId}"/>
				</h:commandLink>				
			</h:column>			
			<h:column> 						
				<h:commandLink action="deletePersonFunction">
					<h:outputText value="#{bundle['link.delete']}"/>					
					<f:param name="personFunctionID" id="personFunctionID2" value="#{person_function.externalId}"/>					
				</h:commandLink>				
			</h:column>		
		</h:dataTable>

		<h:outputText value="#{bundle['error.noActiveFunctions.in.person']}<br/>" styleClass="error" 
				rendered="#{empty managerFunctionsManagementBackingBean.activeFunctions}" escape="false"/>	
				
		<h:outputText value="<br/><h3>#{bundle['label.inherent.functions']}</h3>" escape="false" 
			rendered="#{!empty managerFunctionsManagementBackingBean.inherentFunctions}"/>
	
		<h:dataTable value="#{managerFunctionsManagementBackingBean.inherentFunctions}" var="function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty managerFunctionsManagementBackingBean.inherentFunctions}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.inherentFunction']}" />
				</f:facet>				
				<h:outputText value="<strong>#{function.name}</strong>" escape="false"/>				
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.search.unit']}" />
				</f:facet>				
				<h:outputText value="#{function.unit.name}"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.belongs.to']}" />
				</f:facet>				
				<fc:dataRepeater value="#{function.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>
			</h:column>			
		</h:dataTable>				
	
		<h:outputText value="<br/><h3>#{bundle['label.inactive.functions']}</h3>" escape="false" />
	
		<h:dataTable value="#{managerFunctionsManagementBackingBean.inactiveFunctions}" var="person_function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty managerFunctionsManagementBackingBean.inactiveFunctions}">
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.function']}" />
				</f:facet>				
				<h:outputText value="<strong>#{person_function.function.name}</strong>" escape="false"/>				
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.search.unit']}" />
				</f:facet>				
				<h:outputText value="#{person_function.function.unit.presentationName}"/>
			</h:column>	
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.belongs.to']}" />
				</f:facet>				
				<h:outputText value="#{person_function.function.unit.parentUnitsPresentationName}"/>
				<%--<fc:dataRepeater value="#{person_function.function.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>--%>
			</h:column>			
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.beginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.beginDateInDateType}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.endDate']}" />
				</f:facet>				
				<h:outputFormat rendered="#{!empty person_function.endDate}" value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.endDateInDateType}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['link.group.creditsManagement']}" />
				</f:facet>				
				<h:outputText value="#{person_function.credits}" />
			</h:column>
			<h:column> 											
				<h:commandLink action="prepareEditFunction">
					<h:outputText value="#{bundle['link.functions.management.edit']}"/>					
					<f:param name="personFunctionID" id="personFunctionID1" value="#{person_function.externalId}"/>
					<f:param name="functionID" id="functionID" value="#{person_function.function.externalId}"/>
				</h:commandLink>				
			</h:column>			
		</h:dataTable>
		
		<h:outputText value="#{bundle['error.noInactiveFunctions.in.person']}<br/>" styleClass="error" 
				rendered="#{empty managerFunctionsManagementBackingBean.inactiveFunctions}" escape="false"/>							
			
		<h:outputText value="<br/><br/>" escape="false"/>				
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" value="#{bundle['button.cancel']}" action="chooseNewPerson" styleClass="inputbutton"/>
					
	</h:form>

</ft:tilesView>