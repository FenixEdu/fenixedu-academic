<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/struts-faces.tld" prefix="s"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<h:form>
		<h:inputHidden value="#{guideEdition.guideID}" />
		
		<h:panelGrid columns="2">
			<h:outputText value="Número" />
			<h:outputText value="#{guideEdition.guideNumber}" />
			<h:outputText value="Ano" />
			<h:outputText value="#{guideEdition.guideYear}" />
			<h:outputText value="Versão" />
			<h:outputText value="#{guideEdition.guideVersion}" />
			<h:outputText value="Total" />
			<h:outputText value="#{guideEdition.guide.total}" />
			<h:outputText value="Nome Aluno" />
			<h:outputText value="#{guideEdition.guide.infoPerson.nome}" />
			<h:outputText value="Curso" />
			<h:panelGroup>		
				<h:selectOneMenu value="#{guideEdition.newDegreeCurricularPlanID}">
					<f:selectItems value="#{guideEdition.degreeCurricularPlans}"/>
				</h:selectOneMenu>
				<h:selectOneMenu value="#{guideEdition.newExecutionYear}">
					<f:selectItems value="#{guideEdition.executionYears}"/>
				</h:selectOneMenu>
				<h:commandButton actionListener="#{guideEdition.editExecutionDegree}" value="Editar" />
			</h:panelGroup>
			<h:commandButton action="#{guideEdition.deleteGuideVersion}" value="Apagar Guia" />			
		</h:panelGrid>


		 
		<h:dataTable binding="#{guideEdition.guideEntriesDataTable}" var="guideEntry" />
		
		<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
		<f:loadBundle basename="ServidorApresentacao/ManagerResources" var="bundleManager"/>
		<h:outputFormat value="#{bundle['label.manager.curricularCourseScope.branch']}" />
		<h:outputFormat value="#{bundleManager['label.manager.degree']}" />		
		
		
		
	</h:form>
</ft:tilesView>