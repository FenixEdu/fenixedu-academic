<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<!-- Struts/Renderers Magic -->
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{scouncilBundle['scientificCouncil']}</em>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['edit.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>

	<h:outputText value="<p class='mtop15'>#{DegreeCurricularPlanManagement.dcp.name} (#{enumerationBundle[DegreeCurricularPlanManagement.dcp.curricularStage.name]})</p>" escape="false"/>
	
	<h:form>
		<h:outputText escape="false" value="<input alt='input.dcpId' id='dcpId' name='dcpId' type='hidden' value='#{DegreeCurricularPlanManagement.dcpId}'/>"/>
		<fc:viewState binding="#{DegreeCurricularPlanManagement.viewState}"/>
		
		<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>

		<h:outputText value="<div class='simpleblock4 mtop05'>" escape="false"/>
		<h:outputText value="<p><strong>#{scouncilBundle['curricularPlan.data']}</strong></p>" escape="false"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{scouncilBundle['curricularStage']}:</label> " escape="false"/>
		<h:selectOneMenu id="curricularStage" disabled="#{DegreeCurricularPlanManagement.dcp.curricularStage == 'APPROVED' && !DegreeCurricularPlanManagement.dcp.canModify}" value="#{DegreeCurricularPlanManagement.curricularStage}" onchange="this.form.submit();">
			<f:selectItems value="#{DegreeCurricularPlanManagement.curricularStages}" />
		</h:selectOneMenu>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		<h:message for="curricularStage" errorClass="error" rendered="#{empty DegreeCurricularPlanManagement.errorMessage}"/>
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
		
		<h:outputText value="<p class='mtop15'>" escape="false" />
		<h:outputLink value="editCurricularPlanMembersGroup.faces">
			<h:outputText value="#{scouncilBundle['accessGroupManagement']}" />
			<f:param name="dcpId" value="#{CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.externalId}"/>
		</h:outputLink>
		<h:outputText value="</p>" escape="false" />
		
		<!-- Your ticket out of hell! / Diogo was here. ;)-->
		<h:outputText value="<br/><br/><p><strong>#{scouncilBundle['label.coordinationTeams']}</strong><p/>" escape="false" />
		<h:outputText value="<p>#{scouncilBundle['editCoordinationExplanation']}<p/>" escape="false" />
		<h:outputText value="<p><a href='#{CurricularPlansMembersManagementBackingBean.contextPath}/scientificCouncil/curricularPlans/editExecutionDegreeCoordination.do?method=prepareEditCoordination&degreeCurricularPlanId=#{CurricularPlansMembersManagementBackingBean.degreeCurricularPlan.externalId}'>" escape="false"/>
				<h:outputText value="#{scouncilBundle['accessCoordination']}"/>
		<h:outputText value="</a></p>" escape="false"/>
		<!-- Hack ends here. -->
		
		<h:outputText value="<br/><p>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" styleClass="inputbutton" value="#{scouncilBundle['button.save']}"
			action="#{DegreeCurricularPlanManagement.editCurricularPlan}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false" />
	</h:form>
	
</ft:tilesView>


