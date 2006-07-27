<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.personIDHidden}"/>
		<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.executionPeriodHidden}"/>
		<h:inputHidden binding="#{scientificCouncilFunctionsManagementBackingBean.durationHidden}"/>
	
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.unit.name}"/>	
				<h:outputText value=" - " rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.unit.topUnits}"/>	
				<fc:dataRepeater value="#{scientificCouncilFunctionsManagementBackingBean.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>
			</h:panelGroup>					
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[scientificCouncilFunctionsManagementBackingBean.errorMessage]}"/>
	
		<h:outputText value="<br/>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>

		<h:panelGrid rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}" 
			columns="2" styleClass="infoop">	
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{scientificCouncilFunctionsManagementBackingBean.functionID}">
				<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.validFunctions}"/>
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.credits']}" id="credits" size="5" maxlength="5" value="#{scientificCouncilFunctionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>		
		
		<h:outputText value="<br/><p>#{bundle['message.duration.definition']}</p>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		<h:outputText value="<p>#{bundle['message.duration']}</p>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
						
		<h:panelGrid rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">										
			<h:outputText value="<b>#{bundle['label.begin']}</b>" escape="false"/>																
			<fc:selectOneMenu disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}" onchange="this.form.submit();" value="#{scientificCouncilFunctionsManagementBackingBean.executionPeriod}">
				<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.executionPeriods}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			<h:outputText value=""/>
			
			<h:outputText value="<b>#{bundle['label.duration']}</b>" escape="false"/>
			<h:selectOneRadio onchange="this.form.submit();" disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}" 
							  value="#{scientificCouncilFunctionsManagementBackingBean.duration}">
				<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.durationList}" />
			</h:selectOneRadio>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			<h:panelGroup style="padding-left: 28px;">
				<fc:commandLink action="" value="#{bundle['link.functions.management.edit']}" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}"> 
					<f:param id="disabledVar1" name="disabledVar" value="0"/>
				</fc:commandLink> 
				<h:outputText value="<span style=\"color: #777;\">#{bundle['link.functions.management.edit']}</span>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}"/>
			</h:panelGroup>	
		</h:panelGrid>				
								
		<h:outputText value="<br/>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="4" styleClass="infoop">							
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginDate']}" disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}" id="beginDate"  size="10" value="#{scientificCouncilFunctionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>
			<h:outputText value=""/>
			<h:outputText value=""/>									
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endDate']}" disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}" id="endDate" size="10" value="#{scientificCouncilFunctionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
			<h:outputText value=""/>
			<h:panelGroup style="padding-left: 20px;">
				<fc:commandLink action="" value="#{bundle['link.functions.management.edit']}" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}">
					<f:param id="disabledVar2" name="disabledVar" value="1"/>
				</fc:commandLink>
				<h:outputText value="<span style=\"color: #777;\">#{bundle['link.functions.management.edit']}</span>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}"/>
			</h:panelGroup>
		</h:panelGrid>									
		
		<h:outputText value="<br/><br/>" escape="false" />	

		<h:panelGrid columns="3" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}">							
			<h:panelGroup>
				<fc:commandButton action="confirmation" value="#{bundle['button.continue']}" styleClass="inputbutton">
					<f:param id="disabledVar3" name="disabledVar" value="#{scientificCouncilFunctionsManagementBackingBean.disabledVar}"/>
				</fc:commandButton>							
				<h:commandButton alt="#{htmlAltBundle['commandButton.person']}" action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
				<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="cancel" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>				
			</h:panelGroup>
		</h:panelGrid>	

		<h:panelGrid columns="1">
			<h:outputText value="<br/><br/>#{bundle['error.noFunction.in.unit']}" styleClass="error" 
				escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>					
			<h:outputText value="</br></br><br/>" escape="false" />
			<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"
				rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>						
		</h:panelGrid>
	</h:form>

</ft:tilesView>