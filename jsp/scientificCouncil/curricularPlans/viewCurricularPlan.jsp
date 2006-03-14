<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{ScientificCouncilCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]})</em>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['view.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>
	
	<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.groupMembersLabels}">
	<h:outputText value="<br/><b id='members' class='highlight1'>#{scouncilBundle['groupMembers']}</b> (#{scouncilBundle['groupMembersExplanation']}):<br/>" escape="false" />
	<h:dataTable value="#{CurricularPlansMembersManagementBackingBean.groupMembersLabels}" var="memberLabel">
		<h:column>
			<h:outputText value="#{memberLabel}" escape="false"/>
		</h:column>
	</h:dataTable>
	</h:panelGroup>
	<h:panelGroup rendered="#{empty CurricularPlansMembersManagementBackingBean.groupMembersLabels}">
		<h:outputText value="<br/><i>#{scouncilBundle['label.empty.curricularPlanGroup.members']}</i><br/>" escape="false" />
	</h:panelGroup>

	<h:outputText value="<br/>" escape="false"/>
	
	<h:outputText value="<ul><li>" escape="false"/>
	<h:outputLink value="viewCurricularPlanStructure.faces" rendered="#{!empty ScientificCouncilCurricularPlanManagement.dcp.root.childContexts}">
		<h:outputFormat value="#{scouncilBundle['view.param']}" escape="false">
			<f:param value="#{scouncilBundle['curricularPlan.structure']}"/>
		</h:outputFormat>
		<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
		<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
		<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
		<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>		
		<f:param name="action" value="#{CurricularCourseManagement.action}"/>
	</h:outputLink>
	<h:outputText value="</li></ul>" escape="false"/>


	<h:outputText value="<p class='mtop2 mbottom0'>" escape="false"/>
	<h:panelGroup rendered="#{!empty ScientificCouncilCurricularPlanManagement.dcp.degreeStructure.childs}">
		<h:outputText value="#{scouncilBundle['view.structure.organized.by']}: " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
			<h:outputText value="#{scouncilBundle['groups']}" />
			<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
			<f:param name="organizeBy" value="groups"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		</h:outputLink>
		<h:outputText value="<span class='highlight3'>#{scouncilBundle['groups']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}" escape="false"/>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{scouncilBundle['year']}/#{scouncilBundle['semester']}" />
			<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
			<f:param name="organizeBy" value="years"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{CurricularCourseManagement.action}"/>
		</h:outputLink>
		<h:outputText value="<span class='highlight3'>#{scouncilBundle['year']}/#{scouncilBundle['semester']}</span>" rendered="#{CurricularCourseManagement.organizeBy == 'years'}" escape="false"/>
	</h:panelGroup>
	<h:outputText value="</p>" escape="false"/>

	<fc:degreeCurricularPlanRender dcp="#{ScientificCouncilCurricularPlanManagement.dcp}" onlyStructure="false" toEdit="false" organizeBy="<%=request.getParameter("organizeBy")%>"/>

	<h:outputText value="<br/><p>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>

		<h:panelGroup rendered="#{!empty ScientificCouncilCurricularPlanManagement.action && ScientificCouncilCurricularPlanManagement.action == 'view'}">
			<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['return']}" action="curricularPlansManagement"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'close'}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{scouncilBundle['close']}" />
		</h:panelGroup>
	</h:form>
	<h:outputText value="</p>" escape="false"/>
</ft:tilesView>
