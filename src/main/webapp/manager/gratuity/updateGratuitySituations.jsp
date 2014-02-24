<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications$UpdateGratuitySituations" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<h:form>
		<h:panelGrid columns="2">
			<h:selectOneMenu value="#{updateGratuitySituations.executionYear}">
				<f:selectItems value="#{updateGratuitySituations.executionYears}"/>
			</h:selectOneMenu>
			<h:commandButton alt="#{htmlAltBundle['commandButton.Actualizar']}" actionListener="#{updateGratuitySituations.update}" value="Actualizar" />
		</h:panelGrid>	
	</h:form>
</f:view>