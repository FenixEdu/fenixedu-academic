<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumBundle"/>
		
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
	
</f:view>	