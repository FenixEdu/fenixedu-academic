<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<ft:tilesView definition="departmentAdmOffice.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/DepartmentAdmOfficeResources" var="bundle"/>
	
	<h:form>

		<h:inputHidden binding="#{functionsManagementBackingBean.personIDHidden}"/>
	   	<h:inputHidden binding="#{functionsManagementBackingBean.personFunctionIDHidden}"/>
	
		<h:outputText value="<h2>#{bundle['label.confirmation']}</h2>" escape="false"/>	
		<h:outputText value="<br/>" escape="false" />
		
		<h:outputText styleClass="error" rendered="#{!empty functionsManagementBackingBean.errorMessage}"
					value="#{bundle[functionsManagementBackingBean.errorMessage]}"/>
		
		<h:panelGrid columns="2">
			<h:outputText value="<b>#{bundle['label.name']}</b>: " escape="false"/>		
			<h:outputText value="#{functionsManagementBackingBean.person.name}"/>		
		</h:panelGrid>	
		
		<h:outputText value="<br/>" escape="false" />
		
		<h:panelGrid columns="2" columnClasses="valigntop">		
			<h:outputText value="<b>#{bundle['label.function']}</b>" escape="false"/>	
			<h:outputText value="#{functionsManagementBackingBean.personFunction.function.name}"/>
					
			<h:outputText value="<b>#{bundle['label.search.unit']}:</b>" escape="false"/>				
			<h:outputText value="#{functionsManagementBackingBean.personFunction.unit.presentationNameWithParentsAndBreakLine}" escape="false"/>
			
			<h:outputText value="<b>#{bundle['label.credits']}</b>" escape="false"/>	
			<h:outputText value="#{functionsManagementBackingBean.personFunction.credits}"/>
		
			<h:outputText value="<b>#{bundle['label.valid']}</b>" escape="false"/>	
			<h:panelGroup>
				<h:outputText value="#{functionsManagementBackingBean.personFunction.beginDate}" escape="false"/>	
				<h:outputText value="<b>&nbsp;#{bundle['label.to']}&nbsp;</b>" escape="false"/>	
				<h:outputText value="#{functionsManagementBackingBean.personFunction.endDate}" escape="false"/>		
			</h:panelGroup>
		</h:panelGrid>
			
		<h:outputText value="<br/>" escape="false" />
		<h:panelGrid columns="2">
			<h:commandButton alt="#{htmlAltBundle['button.delete']}" action="#{functionsManagementBackingBean.deletePersonFunction}" value="#{bundle['button.delete']}" styleClass="inputbutton"/>							
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" action="success" immediate="true" value="#{bundle['button.cancel']}" styleClass="inputbutton"/>								
		</h:panelGrid>
	
	</h:form>

</ft:tilesView>