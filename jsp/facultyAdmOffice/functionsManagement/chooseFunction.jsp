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
	
		<h:panelGrid rendered="#{facultyAdmOfficeFunctionsManagementBackingBean.numberOfFunctions > 0}">	
			<h:outputText value="<br/>" escape="false" />
			<h:panelGrid columns="2" styleClass="infoop">			
				<h:outputText value="<b>#{bundle['label.search.function']}:</b>" escape="false"/>			
				<fc:selectOneMenu value="#{facultyAdmOfficeFunctionsManagementBackingBean.functionID}">
					<f:selectItems value="#{facultyAdmOfficeFunctionsManagementBackingBean.validFunctions}"/>
				</fc:selectOneMenu>
							
				<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="credits" required="true" size="5" maxlength="5" value="#{facultyAdmOfficeFunctionsManagementBackingBean.credits}"/>
					<h:message for="credits" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="<b>#{bundle['label.begin.date']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="beginDate" required="true" size="10" value="#{facultyAdmOfficeFunctionsManagementBackingBean.beginDate}">							
						<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
					</h:inputText>	
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="beginDate" styleClass="error"/>				
				</h:panelGroup>			
							
				<h:outputText value="<b>#{bundle['label.end.date']}</b>" escape="false"/>
				<h:panelGroup>
					<h:inputText id="endDate" required="true" size="10" value="#{facultyAdmOfficeFunctionsManagementBackingBean.endDate}">
						<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
					</h:inputText>				
					<h:outputText value="#{bundle['label.date.format']}"/>
					<h:message for="endDate" styleClass="error"/>
				</h:panelGroup>
			</h:panelGrid>				
			
			<h:outputText value="<br/>" escape="false" />	

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