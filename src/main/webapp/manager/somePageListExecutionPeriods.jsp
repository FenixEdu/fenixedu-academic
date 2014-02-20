<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
		<h:outputText value="Execution Periods: "/>
		<br />
		<h:dataTable value="#{executionPeriods.executionPeriods}" var="executionPeriod">
			<h:column>
				<h:outputText value="#{executionPeriod.name}"/>
			</h:column>
			<h:column>
				<h:outputText value="#{executionPeriod.infoExecutionYear.year}"/>
			</h:column>
		</h:dataTable>
</f:view>