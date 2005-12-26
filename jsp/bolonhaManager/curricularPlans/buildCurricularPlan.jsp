<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{ScientificCouncilCurricularPlanManagement.dcp.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ScientificCouncilCurricularPlanManagement.dcp.curricularStage.name]})</i>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['buildCurricularPlan']}</h2>" escape="false"/>

	<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
	<h:outputLink value="buildCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['groups']}" />
		<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
	</h:outputLink>
	<h:outputText value=" | " escape="false"/>
	<h:outputLink value="buildCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['curricularYear']}/#{bolonhaBundle['semester']}" />
		<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
	</h:outputLink>

	<h:outputText value="</br></br>* " escape="false"/>
	<h:outputLink value="editCurricularPlanStructure.faces">
		<h:outputText value="#{bolonhaBundle['edit.curricularPlan.structure']}" />
		<f:param name="dcpId" value="#{ScientificCouncilCurricularPlanManagement.dcpId}"/>
	</h:outputLink>

	<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'"/><br/>
	<fc:degreeCurricularPlanRender dcp="#{ScientificCouncilCurricularPlanManagement.dcp}" onlyStructure="false" toEdit="true" />

	<h:outputText value="</br></br>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input id='dcpId' name='dcpId' type='hidden' value='#{ScientificCouncilCurricularPlanManagement.dcpId}'"/><br/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="curricularPlansManagement"/>
	</h:form>
	
</ft:tilesView>
