<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication$CreateCourseReports" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<h:form>
		<h:panelGrid columns="2">
			<h:selectOneMenu value="#{createCourseReports.executionPeriodID}">
				<f:selectItems value="#{createCourseReports.executionPeriods}"/>
			</h:selectOneMenu>
			<h:commandButton alt="#{htmlAltBundle['commandButton.Criar']}" actionListener="#{createCourseReports.create}" value="#{htmlAltBundle['commandButton.Criar']}" />
		</h:panelGrid>	
	</h:form>
	<br />
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
</f:view>