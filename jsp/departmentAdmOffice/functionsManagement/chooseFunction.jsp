<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{functionsManagementBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
	
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid styleClass="infoop" columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{functionsManagementBackingBean.unit.name}"/>	
				<h:outputText value=" - <i>#{functionsManagementBackingBean.unit.topUnit.name}</i>" 
					rendered="#{functionsManagementBackingBean.unit.topUnit != null}" escape="false"/>
			</h:panelGroup>					
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
				value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
	
		<h:panelGrid rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}">	
			<h:outputText value="<br/>" escape="false" />
			<h:panelGrid columns="2" styleClass="infoop">			
				<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
				<fc:selectOneMenu value="#{functionsManagementBackingBean.functionID}">
					<f:selectItems value="#{functionsManagementBackingBean.validFunctions}"/>
				</fc:selectOneMenu>
							
				<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="credits" required="true" size="5" maxlength="5" value="#{functionsManagementBackingBean.credits}">
						<fc:regexValidator regex="[0-9]+"/>
					</h:inputText>
					<h:message for="credits" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="beginDate" required="true" size="10" value="#{functionsManagementBackingBean.beginDate}">							
						<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
					</h:inputText>	
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="beginDate" styleClass="error"/>				
				</h:panelGroup>			
							
				<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="endDate" required="true" size="10" value="#{functionsManagementBackingBean.endDate}">
						<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
					</h:inputText>				
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="endDate" styleClass="error"/>
				</h:panelGroup>
			</h:panelGrid>				
			
			<h:outputText value="<br/>" escape="false" />	

			<h:panelGroup>
				<h:commandButton action="#{functionsManagementBackingBean.verifyFunction}" value="#{bundle['label.associate1']}" styleClass="inputbutton"/>							
				<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
			</h:panelGroup>
		</h:panelGrid>	

		<h:panelGrid columns="1">
			<h:outputText value="<br/><br/>#{bundle['error.noFunction.in.unit']}" styleClass="error" 
				escape="false" rendered="#{functionsManagementBackingBean.numberOfFunctions <= 0}"/>					
			<h:outputText value="</br></br><br/>" escape="false" />
			<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"
				rendered="#{functionsManagementBackingBean.numberOfFunctions <= 0}"/>						
		</h:panelGrid>
	</h:form>

</ft:tilesView>