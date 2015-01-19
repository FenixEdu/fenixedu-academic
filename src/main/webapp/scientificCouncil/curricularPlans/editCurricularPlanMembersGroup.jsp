<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />

${portal.toolkit()}

<f:view>

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumBundle"/>
	
	
	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
	<h:outputText value="<h2>#{scouncilBundle['accessGroupManagement']}</h2>" escape="false" />

	<h:outputText value="<p class='mtop15'>#{DegreeCurricularPlanManagement.dcp.name} (#{enumBundle[DegreeCurricularPlanManagement.dcp.curricularStage]})</p>" escape="false" />

	<h:outputText value="<div class='infoop2 mbottom15'>#{scouncilBundle['curricularPlans.accessGroupManagement.instructions']}</div>" escape="false" />	
	
	<h:form>				
		<fc:viewState binding="#{DegreeCurricularPlanManagement.viewState}"/>

		<h:messages styleClass="error"/>
		
		<h:outputText value="<p class='mtop15 mbottom05'><b id='members' class='highlight1'>#{scouncilBundle['groupMembers']}</b> (#{scouncilBundle['groupMembersExplanation']}):</p>" escape="false" />
		
		
		<h:panelGroup rendered="#{!empty DegreeCurricularPlanManagement.groupMembers}">		
			<h:selectManyCheckbox value="#{DegreeCurricularPlanManagement.selectedGroupMembersToDelete}" layout="pageDirection">
				<f:selectItems value="#{DegreeCurricularPlanManagement.groupMembers}"  />
			</h:selectManyCheckbox>
			<h:outputText value="<p>" escape="false" />	
			<h:commandLink value="#{scouncilBundle['removeMembers']}" actionListener="#{DegreeCurricularPlanManagement.removeUsersFromGroup}" />
			<h:outputText value="</p>" escape="false" />	
		</h:panelGroup>
		
		<h:panelGroup rendered="#{empty DegreeCurricularPlanManagement.groupMembers}">
			<h:outputText value="<br/><i>#{scouncilBundle['label.empty.curricularPlanGroup.members']}</i><br/>" escape="false" />
		</h:panelGroup>
		
		<h:outputText value="<br/>" escape="false" />
		
		
		<h:outputText escape="false" value="<input alt='input.dcpId' id='dcpId' name='dcpId' type='hidden' value='#{DegreeCurricularPlanManagement.dcpId}'/>"/>
		<h:outputText value="<p><b>#{scouncilBundle['addNewMembers']}</b>:<p/>" escape="false" />
		<div class="form-group">
			<h:outputText value="<input bennu-user-autocomplete id='newGroupMember' name='newGroupMember' type='text' size='60' placeHolder='#{htmlAltBundle[\'placeholder.user.autocomplete\']}'/>" escape="false"/>
		</div>
		<h:commandButton alt="#{scouncilBundle['addPerson']}" styleClass="inputbutton" value="#{scouncilBundle['addPerson']}"
			action="#{DegreeCurricularPlanManagement.addUserToGroup}" />
	</h:form>
	
</f:view>