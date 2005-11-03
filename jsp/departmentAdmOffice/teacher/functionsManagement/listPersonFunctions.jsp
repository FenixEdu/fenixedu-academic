<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{functionsManagementBackingBean.linkHidden}"/>
		<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
	
		<h:outputText value="#{bundle['label.site.orientation6']}" escape="false"/>	
		<h:outputText value="<br/><br/>" escape="false" />
			
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>	

		<h:outputText value="<br/>" escape="false" />	

		<h:panelGroup styleClass="infoop">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>		
		</h:panelGroup>
		
		<h:outputText value="<br/><br/><br/>" escape="false" />
	
		<h:dataTable value="#{functionsManagementBackingBean.person.activeFunctions}" var="person_function"
			 headerClass="listClasses-header" columnClasses="listClasses" rendered="#{!empty functionsManagementBackingBean.person.activeFunctions}">
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
					<h:outputText value="#{bundle['label.action']}" />
				</f:facet>				
				<h:commandLink action="prepareEditFunction">
					<h:outputText value="(#{bundle['link.functions.management.edit']})"/>					
					<f:param name="personFunctionID" id="personFunctionID1" value="#{person_function.idInternal}"/>
					<f:param name="functionID" id="functionID" value="#{person_function.function.idInternal}"/>
				</h:commandLink>				
			</h:column>			
		</h:dataTable>
	
		<h:panelGroup rendered="#{empty functionsManagementBackingBean.person.activeFunctions}">
			<h:outputText value="#{bundle['error.noFunctions.in.person']}" styleClass="error"/>
			<h:outputText value="<br/><br/>" escape="false"/>
			<h:commandButton value="#{bundle['button.choose.new.person']}" action="chooseNewPerson" styleClass="inputbutton"/>
		</h:panelGroup>
	</h:form>

</ft:tilesView>