<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{ScientificCouncilCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'/>"/>
		<fc:viewState binding="#{ScientificCouncilCurricularPlanManagement.viewState}"/>
		
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{scouncilBundle['curricularPlan.data']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<h:outputText value="<p><label>#{scouncilBundle['curricularStage']}:</label> " escape="false"/>
		<h:selectOneMenu id="curricularStage" value="#{ScientificCouncilCurricularPlanManagement.curricularStage}" onchange="this.form.submit();">
			<f:selectItems value="#{ScientificCouncilCurricularPlanManagement.curricularStages}" />
		</h:selectOneMenu>
		<h:message for="curricularStage" errorClass="error" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{ScientificCouncilCurricularPlanManagement.curricularStage == 'APPROVED'}">
			<h:outputText value="<p><label>#{scouncilBundle['executionYear']}:</label> " escape="false"/>
 			<h:selectOneMenu value="#{ScientificCouncilCurricularPlanManagement.executionYearID}">
				<f:selectItems value="#{ScientificCouncilCurricularPlanManagement.executionYearItems}" />
			</h:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<p><label>#{scouncilBundle['name']}:</label>" escape="false"/>
		<h:inputText id="name" value="#{ScientificCouncilCurricularPlanManagement.name}" required="true" maxlength="100" size="40"/>
		<h:message for="name" errorClass="error0" rendered="#{empty ScientificCouncilCurricularPlanManagement.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>
			
		<h:outputText value="</fieldset></div>" escape="false"/>

		<h:outputText value="<p><b>#{scouncilBundle['groupMembers']}</b> (#{scouncilBundle['groupMembersExplanation']}):<p/>" escape="false" />
		<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.groupMembersLabels}">
			<h:dataTable value="#{CurricularPlansMembersManagementBackingBean.groupMembersLabels}" var="memberLabel">
				<h:column>
					<h:outputText value="#{memberLabel}" escape="false"/>
				</h:column>
			</h:dataTable>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty CurricularPlansMembersManagementBackingBean.groupMembersLabels}">
			<h:outputText value="<p><em>#{scouncilBundle['label.empty.curricularPlanGroup.members']}</em><p/>" escape="false" />
		</h:panelGroup>
		
		<h:outputText value="<p>" escape="false" />
		<h:outputLink value="editCurricularPlanMembersGroup.faces">
			<h:outputText value="#{scouncilBundle['accessGroupManagement']}" />
			<f:param name="dcpId" value="#{CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.idInternal}"/>
		</h:outputLink>
		<h:outputText value="</p>" escape="false" />
		
		<h:outputText value="<br/><p>" escape="false" />
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{ScientificCouncilCurricularPlanManagement.editCurricularPlan}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false" />
	</h:form>
</ft:tilesView>