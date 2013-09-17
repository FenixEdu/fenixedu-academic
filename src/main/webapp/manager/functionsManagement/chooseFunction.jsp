<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<ft:tilesView definition="df.page.functionsManagement" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.personIDHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.executionPeriodHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.durationHidden}"/>
			
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2" columnClasses="valigntop">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{managerFunctionsManagementBackingBean.person.name}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:outputText value="#{managerFunctionsManagementBackingBean.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>				
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[managerFunctionsManagementBackingBean.errorMessage]}"/>

		<h:outputText value="<br/>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>

		<h:panelGrid rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}" 
			columns="2" styleClass="infoop">	
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{managerFunctionsManagementBackingBean.functionID}">
				<f:selectItems value="#{managerFunctionsManagementBackingBean.validFunctions}"/>
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.credits']}" id="credits" size="5" maxlength="5" value="#{managerFunctionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>		
		
		<h:outputText value="<br/><p>#{bundle['message.duration.definition']}</p>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		<h:outputText value="<p>#{bundle['message.duration']}</p>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
						
		<h:panelGroup rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}">
			<h:outputText value="<table class='infoop mbottom2'>" escape="false"/>
				<h:outputText value="<tr>" escape="false"/>
					<h:outputText value="<td><b>#{bundle['label.begin']}</b></td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
						<fc:selectOneMenu disabled="#{managerFunctionsManagementBackingBean.disabledVar == 1}" onchange="this.form.submit();" value="#{managerFunctionsManagementBackingBean.executionPeriod}">
							<f:selectItems value="#{managerFunctionsManagementBackingBean.executionPeriods}"/>
						</fc:selectOneMenu>
						<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
					<h:outputText value="</td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
	
				<h:outputText value="<tr>" escape="false"/>
					<h:outputText value="<td><b>#{bundle['label.duration']}</b></td>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
						<h:selectOneRadio onchange="this.form.submit();" disabled="#{managerFunctionsManagementBackingBean.disabledVar == 1}" 
										  value="#{managerFunctionsManagementBackingBean.duration}">
							<f:selectItems value="#{managerFunctionsManagementBackingBean.durationList}" />
						</h:selectOneRadio>
						<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
					<h:outputText value="</td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>

				<h:outputText value="<tr>" escape="false"/>
					<h:outputText value="<td>" escape="false"/>
						<fc:commandLink action="" value="#{bundle['link.functions.management.edit']}" rendered="#{managerFunctionsManagementBackingBean.disabledVar == 1}"> 
							<f:param id="disabledVar1" name="disabledVar" value="0"/>
						</fc:commandLink>
						<h:outputText value="<span style=\"color: #777;\">#{bundle['link.functions.management.edit']}</span>" escape="false" rendered="#{managerFunctionsManagementBackingBean.disabledVar == 0}"/>
					<h:outputText value="</td>" escape="false"/>
					<h:outputText value="<td></td>" escape="false"/>
				<h:outputText value="</tr>" escape="false"/>
		</h:panelGroup>			
								
		<h:outputText value="<br/>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="4" styleClass="infoop">							
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginDate']}" maxlength="10" disabled="#{managerFunctionsManagementBackingBean.disabledVar == 0}" id="beginDate"  size="10" value="#{managerFunctionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>
			<h:outputText value=""/>
			<h:outputText value=""/>									
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endDate']}" maxlength="10" disabled="#{managerFunctionsManagementBackingBean.disabledVar == 0}" id="endDate" size="10" value="#{managerFunctionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="true"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
			<h:outputText value=""/>
			<h:panelGroup style="padding-left: 20px;">
				<fc:commandLink action="" value="#{bundle['link.functions.management.edit']}" rendered="#{managerFunctionsManagementBackingBean.disabledVar == 0}">
					<f:param id="disabledVar2" name="disabledVar" value="1"/>
				</fc:commandLink>
				<h:outputText value="<span style=\"color: #777;\">#{bundle['link.functions.management.edit']}</span>" escape="false" rendered="#{managerFunctionsManagementBackingBean.disabledVar == 1}"/>
			</h:panelGroup>
		</h:panelGrid>									
		
		<h:outputText value="<br/>" escape="false" />	

		<h:panelGrid columns="3" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}">							
			<h:panelGroup>
				<fc:commandButton action="confirmation" value="#{bundle['button.continue']}" styleClass="inputbutton">
					<f:param id="disabledVar3" name="disabledVar" value="#{managerFunctionsManagementBackingBean.disabledVar}"/>
				</fc:commandButton>							
				<h:commandButton alt="#{htmlAltBundle['commandButton.person']}" action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
				<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="cancel" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>				
			</h:panelGroup>
		</h:panelGrid>	
	

		<h:panelGrid columns="1">
			<h:outputText value="<br/><br/>#{bundle['error.noFunction.in.unit']}" styleClass="error" 
				escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>					
			<h:outputText value="<br/><br/><br/>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>
			<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"
				rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>						
		</h:panelGrid>
	</h:form>

</ft:tilesView>