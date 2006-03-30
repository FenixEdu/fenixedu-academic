<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.executionDegreeManagement.page.firstPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ManagerResources" var="managerResources"/>

	<h:outputText value="Os cursos de execução foram criados!" style="text-align:center; font:bold;" />
	
	<br/><br/>
	<h:commandButton action="back" immediate="true" value="#{managerResources['label.return']}" styleClass="inputbutton" />
	
</ft:tilesView>



