<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<h:form>
		<h:panelGrid columns="2">
			<h:selectOneMenu value="#{createCourseReports.executionPeriodID}">
				<f:selectItems value="#{createCourseReports.executionPeriods}"/>
			</h:selectOneMenu>
			<h:commandButton alt="#{htmlAltBundle['commandButton.Criar']}" actionListener="#{createCourseReports.create}" value="#{htmlAltBundle['commandButton.Criar']}" />
		</h:panelGrid>	
	</h:form>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
</ft:tilesView>