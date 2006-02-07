<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>


<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumBundle"/>
		
	<h:outputText value="<i>#{CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.name} (#{enumBundle[CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.curricularStage]})</i><br/>" escape="false" />
	<h:outputText value="<h2>#{scouncilBundle['accessGroupManagement']}</h2><br/>" escape="false" />

	<h:form>
		<fc:viewState binding="#{CurricularPlansMembersManagementBackingBean.viewState}"/>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{CurricularPlansMembersManagementBackingBean.selectedCurricularPlanID}'/>"/><br/>
				
		<h:messages styleClass="error"/>
				
		<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.groupMembers}">		
			<h:outputText value="<b>#{scouncilBundle['groupMembers']}</b> (#{scouncilBundle['groupMembersExplanation']})<br/>" escape="false" />		
			<h:selectManyCheckbox value="#{CurricularPlansMembersManagementBackingBean.selectedPersonsIDsToRemove}" layout="pageDirection">
				<f:selectItems value="#{CurricularPlansMembersManagementBackingBean.groupMembers}"  />
			</h:selectManyCheckbox>
			<h:commandLink value="#{scouncilBundle['removeMembers']}" actionListener="#{CurricularPlansMembersManagementBackingBean.removeMembers}" />
			<h:outputText value="<br/><br/><br/>" escape="false" />
		</h:panelGroup>

		<h:outputText value="<b>#{scouncilBundle['addNewMembers']}</b><br/>" escape="false" />		
		
		<h:panelGrid columns="2" style="infocell" columnClasses="infocell">
			<h:outputText value="#{scouncilBundle['department']}:" />
			<h:selectOneMenu value="#{CurricularPlansMembersManagementBackingBean.selectedDepartmentID}" onchange="submit()">
				<f:selectItems value="#{CurricularPlansMembersManagementBackingBean.departments}"  />
			</h:selectOneMenu>		
		</h:panelGrid>

		<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.departmentEmployees}">
			<h:outputText value="#{scouncilBundle['departmentMembersList']}:" escape="false"/>
			<h:commandLink value="<br/><br/>#{scouncilBundle['addPersons']}<br/><br/>" actionListener="#{CurricularPlansMembersManagementBackingBean.addMembers}" />
			<h:selectManyCheckbox value="#{CurricularPlansMembersManagementBackingBean.selectedPersonsIDsToAdd}" layout="pageDirection">
				<f:selectItems value="#{CurricularPlansMembersManagementBackingBean.departmentEmployees}"  />
			</h:selectManyCheckbox>
		</h:panelGroup>
				
		<h:commandLink value="#{scouncilBundle['addPersons']}" actionListener="#{CurricularPlansMembersManagementBackingBean.addMembers}" />

		<h:outputText value="<br/><br/><br/><hr/>" escape="false" />
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['return']}"
			action="curricularPlansManagement"/>

	</h:form>
	
</ft:tilesView>	