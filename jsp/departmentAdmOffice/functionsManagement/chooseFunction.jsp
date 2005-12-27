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
		<h:inputHidden binding="#{functionsManagementBackingBean.executionPeriodHidden}"/>
		<h:inputHidden binding="#{functionsManagementBackingBean.executionYearIDHidden}"/>
		<h:inputHidden binding="#{functionsManagementBackingBean.durationHidden}"/>
		<h:inputHidden binding="#{functionsManagementBackingBean.disabledVarHidden}"/>
	
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid styleClass="infoop" columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{functionsManagementBackingBean.unit.name}"/>	
				<h:outputText value=" - " rendered="#{!empty functionsManagementBackingBean.unit.topUnits}"/>	
				<fc:dataRepeater value="#{functionsManagementBackingBean.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>
			</h:panelGroup>					
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
				value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
	
		<h:outputText value="<br/>" escape="false" rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}"/>

		<h:panelGrid rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}" 
			columns="2" styleClass="infoop">	
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{functionsManagementBackingBean.functionID}">
				<f:selectItems value="#{functionsManagementBackingBean.validFunctions}"/>
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="credits" size="5" maxlength="5" value="#{functionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>		
		
		<h:outputText value="<br/>" escape="false" rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">							
			
			<h:outputText value="<b>#{bundle['label.begin']}</b>" escape="false"/>																
			<h:panelGroup>
				<h:outputText value="<b>#{bundle['label.executionYear']}</b>&nbsp;" escape="false"/>
				<fc:selectOneMenu readonly="#{functionsManagementBackingBean.disabledVarHidden.value == 1}" onchange="this.form.submit();" value="#{functionsManagementBackingBean.executionYearID}">
					<f:selectItems value="#{functionsManagementBackingBean.executionYears}"/>
				</fc:selectOneMenu>						
				<h:outputText value="&nbsp;&nbsp;&nbsp;<b>#{bundle['label.property.executionPeriod']}</b>&nbsp;" escape="false"/>
				<fc:selectOneMenu readonly="#{functionsManagementBackingBean.disabledVarHidden.value == 1}" onchange="this.form.submit();" value="#{functionsManagementBackingBean.executionPeriod}">
					<f:selectItems value="#{functionsManagementBackingBean.executionPeriods}"/>
				</fc:selectOneMenu>
			</h:panelGroup>
			<h:outputText value=""/>
			
			<h:outputText value="<b>#{bundle['label.duration']}</b>" escape="false"/>
			<h:selectOneRadio onchange="this.form.submit();" disabled="#{functionsManagementBackingBean.executionPeriod == 0 
																		|| functionsManagementBackingBean.executionPeriodHidden.value == 0}" 
							  readonly="#{functionsManagementBackingBean.disabledVarHidden.value == 1 }" 
							  value="#{functionsManagementBackingBean.duration}">
				<f:selectItems value="#{functionsManagementBackingBean.durationList}" />
			</h:selectOneRadio>									
			<fc:commandButton action="" value="#{bundle['link.functions.management.edit']}" rendered="#{functionsManagementBackingBean.disabledVarHidden.value == 1 }"
				styleClass="inputbutton">
				<f:param id="disabledVar1" name="disabledVar" value="0"/>
			</fc:commandButton> 
		</h:panelGrid>				
								
		<h:outputText value="<br/>" escape="false" rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">							
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText readonly="#{functionsManagementBackingBean.disabledVarHidden.value == 0}" id="beginDate"  size="10" value="#{functionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>
			<h:outputText value=""/>									
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText readonly="#{functionsManagementBackingBean.disabledVarHidden.value == 0}" id="endDate" size="10" value="#{functionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
			<fc:commandButton styleClass="inputbutton" action="" value="#{bundle['link.functions.management.edit']}"
				 rendered="#{functionsManagementBackingBean.disabledVarHidden.value == 0
				 			 && functionsManagementBackingBean.numberOfFunctions > 0}">
				<f:param id="disabledVar2" name="disabledVar" value="1"/>
			</fc:commandButton> 
		</h:panelGrid> 
		
		<h:outputText value="<br/><br/>" escape="false" />	
		
		<h:panelGrid columns="3" rendered="#{functionsManagementBackingBean.numberOfFunctions > 0}">							
			<h:panelGroup>
				<h:commandButton action="#{functionsManagementBackingBean.verifyFunction}" value="#{bundle['label.associate1']}" styleClass="inputbutton"/>							
				<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
				<h:commandButton action="cancel" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>				
			</h:panelGroup>
		</h:panelGrid>	
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