<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="facultyAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>
	
		<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.unitIDHidden}"/>
		<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.personIDHidden}"/>
		<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.executionPeriodHidden}"/>
		<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.executionYearIDHidden}"/>
		<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.durationHidden}"/>
		<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden}"/>
	
		<h:outputText value="<H2>#{bundle['label.search.function']}</H2>" escape="false"/>		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid styleClass="infoop" columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.person.nome}" escape="false"/>									
				
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.unit.name}"/>	
				<h:outputText value=" - " rendered="#{!empty facultyAdmOfficeFunctionsManagementBackingBean.unit.topUnits}"/>	
				<fc:dataRepeater value="#{facultyAdmOfficeFunctionsManagementBackingBean.unit.topUnits}" var="topUnit">
					<h:outputText value="#{topUnit.name}<br/>" escape="false" />
				</fc:dataRepeater>
			</h:panelGroup>					
		</h:panelGrid>	
						
		<h:outputText styleClass="error" rendered="#{!empty facultyAdmOfficeFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[facultyAdmOfficeFunctionsManagementBackingBean.errorMessage]}"/>
	
		<h:outputText value="<br/>" escape="false" rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}"/>

		<h:panelGrid rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}" 
			columns="2" styleClass="infoop">	
			<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
			<fc:selectOneMenu value="#{facultyAdmOfficeFunctionsManagementBackingBean.functionID}">
				<f:selectItems value="#{facultyAdmOfficeFunctionsManagementBackingBean.validFunctions}"/>
			</fc:selectOneMenu>
						
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText id="credits" size="5" maxlength="5" value="#{facultyAdmOfficeFunctionsManagementBackingBean.credits}"/>
				<h:message for="credits" styleClass="error"/>
			</h:panelGroup>
		</h:panelGrid>		
		
		<h:outputText value="<br/>" escape="false" rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">							
			
			<h:outputText value="<b>#{bundle['label.begin']}</b>" escape="false"/>																
			<h:panelGroup>
				<h:outputText value="<b>#{bundle['label.executionYear']}</b>&nbsp;" escape="false"/>
				<fc:selectOneMenu readonly="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 1}" onchange="this.form.submit();" value="#{facultyAdmOfficeFunctionsManagementBackingBean.executionYearID}">
					<f:selectItems value="#{facultyAdmOfficeFunctionsManagementBackingBean.executionYears}"/>
				</fc:selectOneMenu>						
				<h:outputText value="&nbsp;&nbsp;&nbsp;<b>#{bundle['label.property.executionPeriod']}</b>&nbsp;" escape="false"/>
				<fc:selectOneMenu readonly="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 1}" onchange="this.form.submit();" value="#{facultyAdmOfficeFunctionsManagementBackingBean.executionPeriod}">
					<f:selectItems value="#{facultyAdmOfficeFunctionsManagementBackingBean.executionPeriods}"/>
				</fc:selectOneMenu>
			</h:panelGroup>
			<h:outputText value=""/>
			
			<h:outputText value="<b>#{bundle['label.duration']}</b>" escape="false"/>
			<h:selectOneRadio onchange="this.form.submit();" disabled="#{facultyAdmOfficeFunctionsManagementBackingBean.executionPeriod == 0 
																		|| facultyAdmOfficeFunctionsManagementBackingBean.executionPeriodHidden.value == 0}" 
							  readonly="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 1 }" 
							  value="#{facultyAdmOfficeFunctionsManagementBackingBean.duration}">
				<f:selectItems value="#{facultyAdmOfficeFunctionsManagementBackingBean.durationList}" />
			</h:selectOneRadio>									
			<fc:commandButton action="" value="#{bundle['link.functions.management.edit']}" rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 1 }"
				styleClass="inputbutton">
				<f:param id="disabledVar1" name="disabledVar" value="0"/>
			</fc:commandButton> 
		</h:panelGrid>				
								
		<h:outputText value="<br/>" escape="false" rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}"/>		
		
		<h:panelGrid rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}"
			columns="3" styleClass="infoop">							
			<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText readonly="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 0}" id="beginDate"  size="10" value="#{facultyAdmOfficeFunctionsManagementBackingBean.beginDate}">							
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>	
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="beginDate" styleClass="error"/>				
			</h:panelGroup>
			<h:outputText value=""/>									
						
			<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
			<h:panelGroup>
				<h:inputText readonly="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 0}" id="endDate" size="10" value="#{facultyAdmOfficeFunctionsManagementBackingBean.endDate}">
					<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
				</h:inputText>				
				<h:outputText value="#{bundle['label.date.format']}"/>
				<h:message for="endDate" styleClass="error"/>
			</h:panelGroup>
			<fc:commandButton styleClass="inputbutton" action="" value="#{bundle['link.functions.management.edit']}"
				 rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden.value == 0
				 			 && facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}">
				<f:param id="disabledVar2" name="disabledVar" value="1"/>
			</fc:commandButton> 
		</h:panelGrid>									
					
		<h:outputText value="<br/><br/>" escape="false" />	

		<h:panelGrid columns="3" rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}">							
			<h:panelGroup>
				<h:commandButton action="#{facultyAdmOfficeFunctionsManagementBackingBean.verifyFunction}" value="#{bundle['label.associate1']}" styleClass="inputbutton"/>							
				<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"/>						
				<h:commandButton action="cancel" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>				
			</h:panelGroup>
		</h:panelGrid>	

		<h:panelGrid columns="1">
			<h:outputText value="<br/><br/>#{bundle['error.noFunction.in.unit']}" styleClass="error" 
				escape="false" rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>					
			<h:outputText value="</br></br><br/>" escape="false" />
			<h:commandButton action="alterUnit" immediate="true" value="#{bundle['button.choose.new.person']}" styleClass="inputbutton"
				rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions <= 0}"/>						
		</h:panelGrid>
	</h:form>

</ft:tilesView>