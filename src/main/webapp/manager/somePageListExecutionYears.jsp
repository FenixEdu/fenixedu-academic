<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
		<h:outputText value="Execution Periods: "/>
		<br />
		<h:dataTable value="#{executionPeriods.executionYears}" var="executionYear">
			<h:column>
				<h:outputText value="#{executionYear.year}"/>
			</h:column>
		</h:dataTable>
</ft:tilesView>