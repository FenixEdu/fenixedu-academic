<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<h:form>
		<h:panelGrid columns="2">
			<h:selectOneMenu value="#{updateGratuitySituations.executionYear}">
				<f:selectItems value="#{updateGratuitySituations.executionYears}"/>
			</h:selectOneMenu>
			<h:commandButton actionListener="#{updateGratuitySituations.update}" value="Actualizar" />
		</h:panelGrid>	
	</h:form>
</ft:tilesView>