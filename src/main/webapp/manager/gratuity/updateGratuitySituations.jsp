<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<h:form>
		<h:panelGrid columns="2">
			<h:selectOneMenu value="#{updateGratuitySituations.executionYear}">
				<f:selectItems value="#{updateGratuitySituations.executionYears}"/>
			</h:selectOneMenu>
			<h:commandButton alt="#{htmlAltBundle['commandButton.Actualizar']}" actionListener="#{updateGratuitySituations.update}" value="Actualizar" />
		</h:panelGrid>	
	</h:form>
</ft:tilesView>