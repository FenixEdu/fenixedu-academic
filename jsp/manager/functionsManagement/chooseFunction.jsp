<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="df.page.functionsManagement" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.personIDHidden}"/>
	
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid styleClass="infoop" columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{managerFunctionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{managerFunctionsManagementBackingBean.unit.name}"/>	
				<h:outputText value=" - " rendered="#{!empty managerFunctionsManagementBackingBean.unit.topUnits}"/>	
				<fc:dataRepeater value="#{managerFunctionsManagementBackingBean.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>
			</h:panelGroup>					
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[managerFunctionsManagementBackingBean.errorMessage]}"/>
	
		<h:panelGrid rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}">	
			<h:outputText value="<br/>" escape="false" />
			<h:panelGrid columns="2" styleClass="infoop">			
				<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
				<fc:selectOneMenu value="#{managerFunctionsManagementBackingBean.functionID}">
					<f:selectItems value="#{managerFunctionsManagementBackingBean.validFunctions}"/>
				</fc:selectOneMenu>
							
				<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="credits" required="true" size="5" maxlength="5" value="#{managerFunctionsManagementBackingBean.credits}">
						<fc:regexValidator regex="[0-9]+"/>
					</h:inputText>
					<h:message for="credits" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="beginDate" required="true" size="10" value="#{managerFunctionsManagementBackingBean.beginDate}">							
						<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
					</h:inputText>	
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="beginDate" styleClass="error"/>				
				</h:panelGroup>			
							
				<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="endDate" required="true" size="10" value="#{managerFunctionsManagementBackingBean.endDate}">
						<fc:regexValidator regex="([1-9]|0[1-9]|[12][0-9]|3[01])[/]([1-9]|0[1-9]|1[012])[/](19|20)\d\d"/>
					</h:inputText>				
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="endDate" styleClass="error"/>
				</h:panelGroup>
			</h:panelGrid>				
			
			<h:outputText value="<br/>" escape="false" />	

			<h:panelGroup>
				<h:commandButton action="#{managerFunctionsManagementBackingBean.verifyFunction}" value="#{bundle['label.associate1']}" styleClass="inputbutton"/>							
				<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
			</h:panelGroup>
		</h:panelGrid>	

		<h:panelGrid columns="1">
			<h:outputText value="<br/><br/>#{bundle['error.noFunction.in.unit']}" styleClass="error" 
				escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>					
			<h:outputText value="</br></br><br/>" escape="false" />
			<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"
				rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>						
		</h:panelGrid>
	</h:form>

</ft:tilesView>