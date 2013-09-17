<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<ft:tilesView definition="df.layout.two-column.contents" attributeName="body-inline">
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