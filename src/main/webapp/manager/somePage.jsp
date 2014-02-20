<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>


<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<h:outputText value="Hello World :o)"/>
	<br />
	<h:outputText value="#{infoExecutionPeriod.name}"/>
	<br />
	<br />
	<h:form>
		<h:commandButton alt="#{htmlAltBundle['commandButton.List.Execution.Periods']}" value="List Execution Periods" action="listExecutionPeriods"/>
	</h:form>
</f:view>