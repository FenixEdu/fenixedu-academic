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
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.executionPeriodHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.executionYearIDHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.durationHidden}"/>
		<h:inputHidden binding="#{managerFunctionsManagementBackingBean.disabledVarHidden}"/>
			
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

		<h:outputText value="<br/>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>

		<h:panelGrid rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}" 
			columns="2" styleClass="infoop">	
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{managerFunctionsManagementBackingBean.functionID}">
				<f:selectItems value="#{managerFunctionsManagementBackingBean.validFunctions}"/>
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="credits" size="5" maxlength="5" value="#{managerFunctionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>		
		
		<h:outputText value="<br/>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">							
			
			<h:outputText value="<b>#{bundle['label.begin']}</b>" escape="false"/>																
			<h:panelGroup>
				<h:outputText value="<b>#{bundle['label.executionYear']}</b>&nbsp;" escape="false"/>
				<fc:selectOneMenu readonly="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 1}" onchange="this.form.submit();" value="#{managerFunctionsManagementBackingBean.executionYearID}">
					<f:selectItems value="#{managerFunctionsManagementBackingBean.executionYears}"/>
				</fc:selectOneMenu>						
				<h:outputText value="&nbsp;&nbsp;&nbsp;<b>#{bundle['label.property.executionPeriod']}</b>&nbsp;" escape="false"/>
				<fc:selectOneMenu readonly="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 1}" onchange="this.form.submit();" value="#{managerFunctionsManagementBackingBean.executionPeriod}">
					<f:selectItems value="#{managerFunctionsManagementBackingBean.executionPeriods}"/>
				</fc:selectOneMenu>
			</h:panelGroup>
			<h:outputText value=""/>
			
			<h:outputText value="<b>#{bundle['label.duration']}</b>" escape="false"/>
			<h:selectOneRadio onchange="this.form.submit();" disabled="#{managerFunctionsManagementBackingBean.executionPeriod == 0 
																		|| managerFunctionsManagementBackingBean.executionPeriodHidden.value == 0}" 
							  readonly="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 1 }" 
							  value="#{managerFunctionsManagementBackingBean.duration}">
				<f:selectItems value="#{managerFunctionsManagementBackingBean.durationList}" />
			</h:selectOneRadio>									
			<fc:commandButton action="" value="#{bundle['link.functions.management.edit']}" rendered="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 1 }"
				styleClass="inputbutton">
				<f:param id="disabledVar1" name="disabledVar" value="0"/>
			</fc:commandButton> 
		</h:panelGrid>				
								
		<h:outputText value="<br/>" escape="false" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">							
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText readonly="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 0}" id="beginDate"  size="10" value="#{managerFunctionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>
			<h:outputText value=""/>									
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText readonly="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 0}" id="endDate" size="10" value="#{managerFunctionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
			<fc:commandButton styleClass="inputbutton" action="" value="#{bundle['link.functions.management.edit']}"
				 rendered="#{managerFunctionsManagementBackingBean.disabledVarHidden.value == 0
				 			 && managerFunctionsManagementBackingBean.numberOfFunctions > 0}">
				<f:param id="disabledVar2" name="disabledVar" value="1"/>
			</fc:commandButton> 
		</h:panelGrid>						
		
		<h:outputText value="<br/><br/>" escape="false" />	
		
		<h:panelGrid columns="3" rendered="#{managerFunctionsManagementBackingBean.numberOfFunctions > 0}">							
			<h:panelGroup>
				<h:commandButton action="#{managerFunctionsManagementBackingBean.verifyFunction}" value="#{bundle['label.associate1']}" styleClass="inputbutton"/>							
				<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
				<h:commandButton action="cancel" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>				
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