<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>


<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<h:outputText value="Hello World :o)"/>
	<br />
	<h:outputText value="#{infoExecutionPeriod.name}"/>
	<br />
	<br />
	<h:form>
		<h:commandButton alt="#{htmlAltBundle['commandButton.List.Execution.Periods']}" value="List Execution Periods" action="listExecutionPeriods"/>
	</h:form>
</ft:tilesView>