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
	<h:form>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'/>"/>

		<h:panelGrid styleClass="simpleblock4" columns="2" border="0">
			<h:outputText value="#{scouncilBundle['curricularStage']}: " />
			<h:panelGroup>
				<h:selectOneMenu id="curricularStage" value="#{ScientificCouncilCurricularPlanManagement.curricularStage}">
					<f:selectItems value="#{ScientificCouncilCurricularPlanManagement.curricularStages}" />
				</h:selectOneMenu>
				<h:message for="curricularStage" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
			</h:panelGroup>
		</h:panelGrid>

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