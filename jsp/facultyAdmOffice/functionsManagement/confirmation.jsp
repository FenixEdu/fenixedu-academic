<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="facultyAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.unitIDHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.personIDHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.creditsHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.beginDateHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.endDateHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.functionIDHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.executionPeriodHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.durationHidden}"/>
	<h:inputHidden binding="#{facultyAdmOfficeFunctionsManagementBackingBean.disabledVarHidden}"/>

	<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
	<h:outputText value="<br/>" escape="false" />
	
	<h:outputText styleClass="error" rendered="#{!empty facultyAdmOfficeFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[facultyAdmOfficeFunctionsManagementBackingBean.errorMessage]}"/>
	
	<h:panelGrid columns="2">
		<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
		<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.person.nome}"/>		
	</h:panelGrid>	
	
	<h:outputText value="</br></br><br/>" escape="false" />
	
	<h:panelGrid columns="2">		
		<h:outputText value="<b>#{bundle['label.new.function']}</b>" escape="false"/>	
		<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.function.name}"/>
				
		<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>
		<h:panelGroup>
			<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.unit.name}"/>	
			<h:outputText value=" - " rendered="#{!empty facultyAdmOfficeFunctionsManagementBackingBean.unit.topUnits}"/>	
			<fc:dataRepeater value="#{facultyAdmOfficeFunctionsManagementBackingBean.unit.topUnits}" var="topUnit">
				<h:outputText value="#{topUnit.name}<br/>" escape="false" />
			</fc:dataRepeater>
		</h:panelGroup>	
	
		<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>	
		<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.credits}"/>
	
		<h:outputText value="<b>#{bundle['label.valid']}</b>" escape="false"/>	
		<h:panelGroup>
			<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.beginDate}" escape="false"/>	
			<h:outputText value="<b>&nbsp;#{bundle['label.to']}&nbsp;</b>" escape="false"/>	
			<h:outputText value="#{facultyAdmOfficeFunctionsManagementBackingBean.endDate}" escape="false"/>		
		</h:panelGroup>
	</h:panelGrid>
		
	<h:outputText value="<br/>" escape="false" />
	<h:panelGrid columns="4">
		<h:commandButton alt="#{htmlAltBundle['commandButton.confirme']}" action="#{facultyAdmOfficeFunctionsManagementBackingBean.associateNewFunction}" value="#{bundle['label.confirme']}" styleClass="inputbutton"/>			
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterUnit" immediate="true" value="#{bundle['alter.unit.button']}" styleClass="inputbutton"/>						
		<h:commandButton alt="#{htmlAltBundle['commandButton.button']}" action="alterFunction" immediate="true" value="#{bundle['alter.function.button']}" styleClass="inputbutton"/>								
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>
	</h:panelGrid>
	
	</h:form>

</ft:tilesView>