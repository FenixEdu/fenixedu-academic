<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
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
</ft:tilesView>