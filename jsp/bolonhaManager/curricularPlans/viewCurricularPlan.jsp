<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ScientificCouncilResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['view.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularPlan']}"/>
	</h:outputFormat>

	<h:outputText value="<br/><br/>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
	<h:outputLink value="viewCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['groups']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="groups"/>
		<f:param name="action" value="view"/>		
	</h:outputLink>
	<h:outputText value=" , " escape="false"/>
	<h:outputLink value="viewCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="years"/>
		<f:param name="action" value="view"/>
	</h:outputLink>

	<h:outputText value="<br/><br/>" escape="false"/>
	<fc:degreeCurricularPlanRender dcp="#{CurricularCourseManagement.degreeCurricularPlan}" onlyStructure="false" toEdit="false" organizeBy="<%=request.getParameter("organizeBy")%>"/>

	<h:outputText value="<br/><br/><hr/>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>

		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'view'}">
			<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['return']}" action="curricularPlansManagement"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'close'}">
			<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{bolonhaBundle['close']}" />
		</h:panelGroup>
	</h:form>
	
</ft:tilesView>
