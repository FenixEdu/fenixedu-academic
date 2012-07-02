<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>


<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumBundle"/>
		
	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
	<h:outputText value="<h2>#{scouncilBundle['accessGroupManagement']}</h2>" escape="false" />

	<h:outputText value="<p class='mtop15'>#{CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.name} (#{enumBundle[CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.curricularStage]})</p>" escape="false" />

	<h:outputText value="<div class='infoop2 mbottom15'>#{scouncilBundle['accessGroupManagement.instructions']}</div>" escape="false" />	

	<h:form>
		<fc:viewState binding="#{CurricularPlansMembersManagementBackingBean.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.dcpId' id='dcpId' name='dcpId' type='hidden' value='#{CurricularPlansMembersManagementBackingBean.selectedCurricularPlanID}'/>"/>
				
		<h:messages styleClass="error"/>
		

		<h:outputText value="<p><b id='members' class='highlight1'>#{scouncilBundle['groupMembers']}</b> (#{scouncilBundle['groupMembersExplanation']}):</p>" escape="false" />		
		<h:outputText value="<p><em>#{scouncilBundle['groupMembers.empty']}</em></p>" rendered="#{empty CurricularPlansMembersManagementBackingBean.groupMembers}" escape="false" />					

		<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.groupMembers}">		
			<h:selectManyCheckbox value="#{CurricularPlansMembersManagementBackingBean.selectedPersonsIDsToRemove}" layout="pageDirection">
				<f:selectItems value="#{CurricularPlansMembersManagementBackingBean.groupMembers}"  />
			</h:selectManyCheckbox>
			<h:outputText value="<p>" escape="false" />	
			<h:commandLink value="#{scouncilBundle['removeMembers']}" actionListener="#{CurricularPlansMembersManagementBackingBean.removeMembers}" />
			<h:outputText value="</p>" escape="false" />	
		</h:panelGroup>
		
		<h:outputText value="<br/>" escape="false" />
			
	
		<h:outputText value="<p><b>#{scouncilBundle['addNewMembers']}</b>:<p/>" escape="false" />		
		
		<h:panelGrid columns="3" style="infocell" columnClasses="infocell">
			<h:outputText value="#{scouncilBundle['label.istid']}:" />
			
			<h:inputText id="xpto" value="#{CurricularPlansMembersManagementBackingBean.istIdToAdd}" />

			<h:commandLink value="#{scouncilBundle['addPerson']}" actionListener="#{CurricularPlansMembersManagementBackingBean.addMembers}" />
		</h:panelGrid>

		<h:outputText value="<p class='mtop2'>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false" />

	</h:form>
	
</ft:tilesView>	