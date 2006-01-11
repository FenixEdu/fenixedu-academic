<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{ScientificCouncilCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>
	<br/>
	
	<h:form>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'"/>

		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:outputText value="#{scouncilBundle['curricularStage']}: " />
			<h:panelGroup>
				<h:selectOneMenu id="curricularStage" value="#{ScientificCouncilCurricularPlanManagement.curricularStage}">
					<f:selectItems value="#{ScientificCouncilCurricularPlanManagement.curricularStages}" />
				</h:selectOneMenu>
				<h:message for="curricularStage" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
			</h:panelGroup>
		</h:panelGrid>

		<h:outputText value="<br/><b>#{scouncilBundle['curricularPlan.data']}:</b><br/>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="<br/><br/>#{!empty ScientificCouncilCurricularPlanManagement.errorMessage}"
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
			
		</h:panelGrid>

		<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.curricularPlanMembersGroup}">
			<h:outputText value="<br/><b>#{scouncilBundle['groupMembers']}</b> (#{scouncilBundle['groupMembersExplanation']}):<br/>" escape="false" />
			<h:dataTable value="#{CurricularPlansMembersManagementBackingBean.groupMembersLabels}" var="memberLabel">
				<h:column>
					<h:outputText value="#{memberLabel}" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:panelGroup>

		<br/><hr/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['edit']}"
			action="#{ScientificCouncilCurricularPlanManagement.editCurricularPlan}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>
</ft:tilesView>