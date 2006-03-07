<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/ScientificCouncilResources" var="scouncilBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{ScientificCouncilCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['view.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}"/>
	</h:outputFormat>
 	
 	<h:outputText value="<br/>" escape="false"/>
	<h:outputText value="<p>#{scouncilBundle['view.structure.organized.by']}: " escape="false"/>
	<h:outputLink value="viewCurricularPlan.faces">
		<h:outputText value="#{scouncilBundle['groups']}" />
		<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
		<f:param name="organizeBy" value="groups"/>
		<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
		<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>		
		<f:param name="action" value="#{CurricularCourseManagement.action}"/>
	</h:outputLink>
	<h:outputText value=" , " escape="false"/>
	<h:outputLink value="viewCurricularPlan.faces">
		<h:outputText value="#{scouncilBundle['year']}/#{scouncilBundle['semester']}" />
		<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
		<f:param name="organizeBy" value="years"/>
		<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
		<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>		
		<f:param name="action" value="#{CurricularCourseManagement.action}"/>
	</h:outputLink>
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
