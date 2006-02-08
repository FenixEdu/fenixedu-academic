<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</i>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['setCurricularRules']}</h2>" escape="false"/>

	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
		<h:outputText value="<br/>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
		<h:outputLink value="../curricularPlans/setCurricularRules.faces">
			<h:outputText value="#{bolonhaBundle['groups']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="groups"/>
		</h:outputLink>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="../curricularPlans/setCurricularRules.faces">
			<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="years"/>		
		</h:outputLink>
	</h:panelGroup>

	<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
	<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
	<fc:degreeCurricularPlanRender 
		dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
		showRules="true" 
		onlyStructure="false" 
		toEdit="true" 
		organizeBy="<%=request.getParameter("organizeBy")%>"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>

		<h:outputText value="<br/><hr/>" escape="false"/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="buildCurricularPlan"/>
	</h:form>

</ft:tilesView>
