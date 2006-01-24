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
		<h:outputText escape="false" value="<input id='degreeId' name='degreeId' type='hidden' value='#{ScientificCouncilDegreeManagement.degreeId}'/>"/>

		<h:messages infoClass="infoMsg" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{scouncilBundle['curricularPlan.data']}:</h4>" escape="false"/>

		<h:outputText value="<br/><fieldset class='lfloat'>" escape="false"/>

		<h:outputText value="<p><label>#{scouncilBundle['name']}:</label>" escape="false"/>
		<h:panelGroup>
			<h:inputText id="name" value="#{ScientificCouncilCurricularPlanManagement.name}" required="true" maxlength="100" size="20"/>
			<h:message for="name" errorClass="error0" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
		</h:panelGroup>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="</fieldset></div>" escape="false"/>

		<br/><hr/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['create']}"
			action="#{ScientificCouncilCurricularPlanManagement.createCurricularPlan}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>

</ft:tilesView>
