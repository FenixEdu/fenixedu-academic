<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	
	<h:outputText value="<i>#{scouncilBundle['scientificCouncil']}</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['create.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeId' name='degreeId' type='hidden' value='#{ScientificCouncilDegreeManagement.degreeId}'"/><br/>	

		<h:outputText value="<b>#{scouncilBundle['curricularPlan.data']}:</b><br/><br/>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilCurricularPlanManagement.errorMessage}"
			value="#{ScientificCouncilCurricularPlanManagement.errorMessage}<br/>" escape="false"/>
		
		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{scouncilBundle['name']}: " />
			<h:panelGroup>
				<h:inputText id="name" value="#{ScientificCouncilCurricularPlanManagement.name}" required="true" maxlength="100" size="60"/>
				<h:message for="name" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
			</h:panelGroup>

			<h:outputText value="#{scouncilBundle['ectsCredits']}: " />
			<h:panelGroup>
				<h:inputText id="ectsCredits" value="#{ScientificCouncilCurricularPlanManagement.ectsCredits}" required="true" maxlength="100" size="10"/>
				<h:message for="ectsCredits" errorClass="error" rendered="#{empty ScientificCouncilDegreeManagement.errorMessage}"/>
			</h:panelGroup>
			
<%--
 			<h:outputText value="#{scouncilBundle['gradeTypes']}: " />
			<h:panelGroup>
				<h:selectOneMenu id="gradeType" value="#{ScientificCouncilCurricularPlanManagement.gradeType}">
					<f:selectItems value="#{ScientificCouncilCurricularPlanManagement.gradeScales}" />
				</h:selectOneMenu>
				<h:message for="gradeType" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>				
			</h:panelGroup>
--%>

		</h:panelGrid>
		<br/><br/><hr/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['create']}"
			action="#{ScientificCouncilCurricularPlanManagement.createCurricularPlan}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>