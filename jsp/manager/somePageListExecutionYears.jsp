<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
		<h:outputText value="Execution Periods: "/>
		<br />
		<h:dataTable value="#{executionPeriods.executionYears}" var="executionYear">
			<h:column>
				<h:outputText value="#{executionYear.year}"/>
			</h:column>
		</h:dataTable>
</ft:tilesView>