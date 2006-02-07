<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:outputText value="<h2>#{bundle['link.evaluation']}</h2>" escape="false" />
</ft:tilesView>