<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['create.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['degree']}"/>
	</h:outputFormat>
	<h:form>
		<h:outputText value="<b>#{scouncilBundle['curricularPlan.data']}:</b><br/><br/>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilCurricularPlanManagement.errorMessage}"
			value="#{ScientificCouncilCurricularPlanManagement.errorMessage}<br/>" escape="false"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{scouncilBundle['name']}: " />
			<h:panelGroup>
				<h:inputText id="name" value="#{ScientificCouncilCurricularPlanManagement.name}" required="true" maxlength="100" size="60"/>
				<h:message for="name" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
			</h:panelGroup>
			
			<h:outputText value="#{scouncilBundle['acronym']}: " />
			<h:panelGroup>
				<h:inputText id="acronym" value="#{ScientificCouncilCurricularPlanManagement.acronym}" required="true" maxlength="100" size="10"/>
				<h:message for="acronym" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
			</h:panelGroup>
			
		</h:panelGrid>
		<br/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['create']}"
			action="#{ScientificCouncilCurricularPlanManagement.createDegreeCurricularPlan}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>