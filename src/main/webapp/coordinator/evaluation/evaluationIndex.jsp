<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:outputText value="<h2>#{bundle['link.evaluation']}</h2>" escape="false" />
</f:view>