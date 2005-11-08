<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="facultyAdmOffice.masterPage" attributeName="body-inline">

	<f:loadBundle basename="ServidorApresentacao/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.unitIDHidden}"/>
	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.personIDHidden}"/>
	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.creditsHidden}"/>
	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.beginDateHidden}"/>
	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.endDateHidden}"/>
	<h:inputHidden binding="#{managerFunctionsManagementBackingBean.functionIDHidden}"/>

	<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
	<h:outputText value="<br/>" escape="false" />
	
	<h:outputText styleClass="error" rendered="#{!empty managerFunctionsManagementBackingBean.errorMessage}"
				value="#{bundle[managerFunctionsManagementBackingBean.errorMessage]}"/>
	
	<h:panelGrid styleClass="infoop" columns="2">
		<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
		<h:outputText value="#{managerFunctionsManagementBackingBean.person.nome}"/>		
	</h:panelGrid>	
	
	<h:outputText value="</br></br><br/>" escape="false" />
	
	<h:panelGrid styleClass="infoop" columns="2">		
		<h:outputText value="<b>#{bundle['label.new.function']}</b>" escape="false"/>	
		<h:outputText value="#{managerFunctionsManagementBackingBean.function.name}"/>
				
		<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>
		<h:panelGroup>
			<h:outputText value="#{managerFunctionsManagementBackingBean.unit.name}"/>	
			<h:outputText value=" - <i>#{managerFunctionsManagementBackingBean.unit.topUnit.name}</i>"
				rendered="#{managerFunctionsManagementBackingBean.unit.topUnit != null}" escape="false"/>	
		</h:panelGroup>	
	
		<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>	
		<h:outputText value="#{managerFunctionsManagementBackingBean.credits}"/>
	
		<h:outputText value="<b>#{bundle['label.valid']}</b>" escape="false"/>	
		<h:panelGroup>
			<h:outputText value="#{managerFunctionsManagementBackingBean.beginDate}" escape="false"/>	
			<h:outputText value="<b>&nbsp;#{bundle['label.to']}</b>" escape="false"/>	
			<h:outputText value="#{managerFunctionsManagementBackingBean.endDate}" escape="false"/>		
		</h:panelGroup>
	</h:panelGrid>
		
	<h:outputText value="<br/>" escape="false" />
	<h:panelGrid columns="3">
		<h:commandButton action="#{managerFunctionsManagementBackingBean.associateNewFunction}" value="#{bundle['label.confirme']}" styleClass="inputbutton"/>			
		<h:commandButton action="alterUnit" immediate="true" value="#{bundle['alter.unit.button']}" styleClass="inputbutton"/>						
		<h:commandButton action="alterFunction" immediate="true" value="#{bundle['alter.function.button']}" styleClass="inputbutton"/>								
	</h:panelGrid>
	
	</h:form>

</ft:tilesView>