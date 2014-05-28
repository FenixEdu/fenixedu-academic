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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{DegreeCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[DegreeCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.dcpId' id='dcpId' name='dcpId' type='hidden' value='#{DegreeCurricularPlanManagement.dcpId}'/>"/>
		<fc:viewState binding="#{DegreeCurricularPlanManagement.viewState}"/>
		
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{scouncilBundle['curricularPlan.data']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<h:outputText value="<p><label>#{scouncilBundle['curricularStage']}:</label> " escape="false"/>
		<h:selectOneMenu id="curricularStage" value="#{DegreeCurricularPlanManagement.curricularStage}" onchange="this.form.submit();">
			<f:selectItems value="#{DegreeCurricularPlanManagement.curricularStages}" />
		</h:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:message for="curricularStage" errorClass="error" rendered="#{empty DegreeCurricularPlanManagement.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{scouncilBundle['state']}:</label> " escape="false"/>
		<h:selectOneMenu id="state" value="#{DegreeCurricularPlanManagement.state}" onchange="this.form.submit();">
			<f:selectItems value="#{DegreeCurricularPlanManagement.states}" />
		</h:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		<h:message for="state" errorClass="error" rendered="#{empty DegreeCurricularPlanManagement.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{DegreeCurricularPlanManagement.curricularStage == 'APPROVED'}">
			<h:outputText value="<p><label>#{scouncilBundle['executionYear']}:</label> " escape="false"/>
 			<h:selectOneMenu disabled="#{DegreeCurricularPlanManagement.dcp.curricularStage == 'APPROVED'}" value="#{DegreeCurricularPlanManagement.executionYearID}">
				<f:selectItems value="#{DegreeCurricularPlanManagement.executionYearItems}" />
			</h:selectOneMenu>
			<h:outputText rendered="#{DegreeCurricularPlanManagement.dcp.curricularStage == 'APPROVED'}" value="#{scouncilBundle['curricular.plan.approved']}" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<p><label>#{scouncilBundle['name']}:</label>" escape="false"/>
		<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" disabled="#{DegreeCurricularPlanManagement.curricularStage == 'APPROVED'}" value="#{DegreeCurricularPlanManagement.name}" required="true" maxlength="100" size="40"/>
		<h:message for="name" errorClass="error0" rendered="#{empty DegreeCurricularPlanManagement.errorMessage}"/>
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
		
		<h:outputText value="<br/><p>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{DegreeCurricularPlanManagement.editCurricularPlan}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false" />
	</h:form>
</f:view>