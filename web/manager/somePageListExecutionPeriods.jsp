<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

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