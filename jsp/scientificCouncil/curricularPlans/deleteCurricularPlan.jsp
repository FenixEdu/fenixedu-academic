<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="scientificCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="scouncilBundle"/>	
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<i>#{ScientificCouncilCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{scouncilBundle['delete.param']}</h2>" escape="false">
		<f:param value="#{scouncilBundle['curricularPlan']}" />
	</h:outputFormat>
	<h:form>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'/>"/>
		
		<h:outputText value="<b>#{scouncilBundle['curricularPlan.data']}:</b><br/><br/>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty ScientificCouncilCurricularPlanManagement.errorMessage}"
			value="#{ScientificCouncilCurricularPlanManagement.errorMessage}<br/><br/>" escape="false"/>
		
		<h:panelGrid styleClass="infoselected" columns="2" border="0">
 			<h:outputText value="<b>#{scouncilBundle['curricularStage']}:</b> " escape="false"/>
			<h:outputText value="#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]}"/>

			<h:outputText value="<b>#{scouncilBundle['name']}</b> " escape="false"/>
			<h:outputText value="#{ScientificCouncilCurricularPlanManagement.name}"/>

		</h:panelGrid>
		<br/><br/><hr/>
		<h:commandButton styleClass="inputbutton" value="#{scouncilBundle['confirm']}"
			action="#{ScientificCouncilCurricularPlanManagement.deleteCurricularPlan}" onclick="return confirm('#{scouncilBundle['confirm.delete.curricularPlan']}')"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{scouncilBundle['cancel']}"
			action="curricularPlansManagement"/>
	</h:form>

</ft:tilesView>