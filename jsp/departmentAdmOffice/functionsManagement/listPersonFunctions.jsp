<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
			
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>	

		<h:outputText value="<br/>" escape="false" />	

		<h:panelGroup styleClass="infoop">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>		
		</h:panelGroup>
		
		<h:outputText value="<br/><br/><br/>" escape="false" />	
		
		<h:commandLink value="#{bundle['label.associate']}" action="associateNewFunction" />
				
		<h:outputText value="<br/><br/><h3>#{bundle['label.active.functions']}<h3/>" escape="false" />
	
		<h:dataTable value="#{functionsManagementBackingBean.activeFunctions}" var="person_function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty functionsManagementBackingBean.activeFunctions}">
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
				<h:outputText value="#{person_function.function.unit.name}"/>
			</h:column>	
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.belongs.to']}" />
				</f:facet>				
				<h:outputText value="#{person_function.function.unit.topUnit.name}"/>
			</h:column>	
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.beginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.beginDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.endDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.endDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['link.group.creditsManagement']}" />
				</f:facet>				
				<h:outputText value="#{person_function.credits}" />
			</h:column>
			<h:column> 			
				<f:facet name="header">
					<h:outputText value="#{bundle['label.action']}" />
				</f:facet>				
				<h:commandLink action="prepareEditFunction">
					<h:outputText value="(#{bundle['link.functions.management.edit']})"/>					
					<f:param name="personFunctionID" id="personFunctionID1" value="#{person_function.idInternal}"/>
					<f:param name="functionID" id="functionID" value="#{person_function.function.idInternal}"/>
				</h:commandLink>				
			</h:column>			
		</h:dataTable>

		<h:outputText value="#{bundle['error.noActiveFunctions.in.person']}<br/>" styleClass="error" 
				rendered="#{empty functionsManagementBackingBean.activeFunctions}" escape="false"/>			
	
		<h:outputText value="<br/><h3>#{bundle['label.inactive.functions']}</h3>" escape="false" />
	
		<h:dataTable value="#{functionsManagementBackingBean.inactiveFunctions}" var="person_function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty functionsManagementBackingBean.inactiveFunctions}">
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
				<h:outputText value="#{person_function.function.unit.name}"/>
			</h:column>	
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.belongs.to']}" />
				</f:facet>				
				<h:outputText value="#{person_function.function.unit.topUnit.name}"/>
			</h:column>			
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.beginDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.beginDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['label.endDate']}" />
				</f:facet>				
				<h:outputFormat value="{0, date, dd/MM/yyyy}">
					<f:param value="#{person_function.endDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="#{bundle['link.group.creditsManagement']}" />
				</f:facet>				
				<h:outputText value="#{person_function.credits}" />
			</h:column>	
		</h:dataTable>
		
		<h:outputText value="#{bundle['error.noInactiveFunctions.in.person']}<br/>" styleClass="error" 
				rendered="#{empty functionsManagementBackingBean.inactiveFunctions}" escape="false"/>			
		
		<h:outputText value="<br/><h3>#{bundle['label.inherent.functions']}</h3>" escape="false" 
			rendered="#{!empty functionsManagementBackingBean.inherentFunctions}"/>
	
		<h:dataTable value="#{functionsManagementBackingBean.inherentFunctions}" var="function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty functionsManagementBackingBean.inherentFunctions}">
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
				<h:outputText value="#{function.unit.topUnit.name}" 
					rendered="#{function.unit.topUnit != null}"/>
			</h:column>
			
		</h:dataTable>
			
		<h:outputText value="<br/><br/>" escape="false"/>				
		<h:commandButton value="#{bundle['button.choose.new.person']}" action="chooseNewPerson" styleClass="inputbutton"/>
					
	</h:form>

</ft:tilesView>