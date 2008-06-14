<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>


<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<h:outputText value="Hello World :o)"/>
	<br />
	<h:outputText value="#{infoExecutionPeriod.name}"/>
	<br />
	<br />
	<h:form>
		<h:commandButton alt="#{htmlAltBundle['commandButton.List Execution Periods']}" value="List Execution Periods" action="listExecutionPeriods"/>
	</h:form>
</ft:tilesView>