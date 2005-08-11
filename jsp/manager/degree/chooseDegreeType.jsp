<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">

	<h:form>
		<h:panelGrid columns="1">
			<h:outputText value="Escolha o tipo de Graduação:" />
			<h:selectOneMenu value="#{createExecutionDegrees.chosenDegreeType}" >
				<f:selectItems value="#{createExecutionDegrees.degreeTypes}" />			
			</h:selectOneMenu>
			<h:commandButton action="choose" value="Continuar"/>		
		</h:panelGrid>
	</h:form>

</ft:tilesView>



