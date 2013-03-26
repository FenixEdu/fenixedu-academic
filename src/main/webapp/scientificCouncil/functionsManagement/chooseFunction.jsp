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
	
		<h:outputText value="<h2>#{bundle['label.search.function']}</h2>" escape="false"/>		


		<h:outputText value="<table class='tstyle2 thlight thright'>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{bundle['label.name']}:" escape="false"/>	
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.person.name}" escape="false"/>									
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="<tr>" escape="false"/>	
			<h:outputText value="<th>" escape="false"/>
				<h:outputText value="#{bundle['label.search.unit']}:" escape="false"/>
			<h:outputText value="</th>" escape="false"/>
			<h:outputText value="<td>" escape="false"/>
				<h:outputText value="#{scientificCouncilFunctionsManagementBackingBean.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>
			<h:outputText value="</td>" escape="false"/>
		<h:outputText value="</tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>


		<h:outputText styleClass="error" rendered="#{!empty scientificCouncilFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[scientificCouncilFunctionsManagementBackingBean.errorMessage]}"/>
	
		<h:outputText value="<br/>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>

		<h:panelGroup rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}">
			<h:outputText value="<table class='tstyle5 thlight thright thmiddle'>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.search.function']}:" escape="false"/>			
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<fc:selectOneMenu value="#{scientificCouncilFunctionsManagementBackingBean.functionID}">
						<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.validFunctions}"/>
					</fc:selectOneMenu>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>	
				<h:outputText value="<th>" escape="false"/>			
				<h:outputText value="#{bundle['label.credits']}" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.credits']}" id="credits" size="5" maxlength="5" value="#{scientificCouncilFunctionsManagementBackingBean.credits}"/>
					<h:message for="credits" styleClass="error"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="</table>" escape="false"/>
		</h:panelGroup>		
		
		<h:outputText value="<p class='mtop15'>#{bundle['message.duration.definition']}</p>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		<h:outputText value="<p>#{bundle['message.duration']}</p>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		

		<h:panelGroup rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}">
			<h:outputText value="<table class='tstyle5 thlight thright thmiddle'>" escape="false"/>
				<h:outputText value="<tr>" escape="false"/>
					<h:outputText value="<th>#{bundle['label.begin']}</th>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
						<fc:selectOneMenu disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}" onchange="this.form.submit();" value="#{scientificCouncilFunctionsManagementBackingBean.executionPeriod}">
							<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.executionPeriods}"/>
						</fc:selectOneMenu>
						<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
					<h:outputText value="</td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
	
				<h:outputText value="<tr>" escape="false"/>
					<h:outputText value="<th>#{bundle['label.duration']}</thd>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
						<h:selectOneRadio onchange="this.form.submit();" disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}" 
										  value="#{scientificCouncilFunctionsManagementBackingBean.duration}">
							<f:selectItems value="#{scientificCouncilFunctionsManagementBackingBean.durationList}" />
						</h:selectOneRadio>
						<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
					<h:outputText value="</td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>

				<h:outputText value="<tr>" escape="false"/>
					<h:outputText value="<th>" escape="false"/>
					<h:outputText value="</th>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
						<fc:commandLink action="" value="#{bundle['link.functions.management.edit']}" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}"> 
							<f:param id="disabledVar1" name="disabledVar" value="0"/>
						</fc:commandLink>
						<h:outputText value="<span style=\"color: #777;\">#{bundle['link.functions.management.edit']}</span>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}"/>
					<h:outputText value="</td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="</table>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
			<h:panelGroup rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions > 0}">
			<h:outputText value="<table class='tstyle5 thlight thright thmiddle'>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.begin.date']}" escape="false"/>	
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginDate']}" maxlength="10" disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}" id="beginDate"  size="10" value="#{scientificCouncilFunctionsManagementBackingBean.beginDate}">							
						<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
					</h:inputText>	
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="beginDate" styleClass="error"/>								
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>	
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.end.date']}" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endDate']}" maxlength="10" disabled="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}" id="endDate" size="10" value="#{scientificCouncilFunctionsManagementBackingBean.endDate}">
						<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
					</h:inputText>				
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="endDate" styleClass="error"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>	
				<h:outputText value="<th>" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<fc:commandLink action="" value="#{bundle['link.functions.management.edit']}" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 0}">
						<f:param id="disabledVar2" name="disabledVar" value="1"/>
					</fc:commandLink>
					<h:outputText value="<span style=\"color: #777;\">#{bundle['link.functions.management.edit']}</span>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.disabledVar == 1}"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			<h:outputText value="</table>" escape="false"/>
		</h:panelGroup>	
								


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
			<h:outputText value="<br/><br/><br/>" escape="false" rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions <= 0}" />
			<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"
				rendered="#{scientificCouncilFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>						
		</h:panelGrid>
	</h:form>

</ft:tilesView>