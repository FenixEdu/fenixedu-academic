<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['buildCurricularPlan']}</h2>" escape="false"/>

	<h:outputText value="<ul><li>" escape="false"/>
	<h:outputLink value="editCurricularPlanStructure.faces">
		<h:outputText value="#{bolonhaBundle['edit.curricularPlan.structure']}" escape="false" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
	</h:outputLink>
	<h:outputText value="</li>" escape="false"/>
	
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeModule.courseGroupContexts}">
		<h:outputText value="<li>" escape="false"/>
			<h:outputLink value="setCurricularRules.faces" >
			<h:outputText value="#{bolonhaBundle['setCurricularRules']}" escape="false"/>
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>			
		</h:outputLink>
	<h:outputText value="</li>" escape="false"/>
	</h:panelGroup>
	
	<h:outputText value="</ul><br/>" escape="false"/>
	
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
		<h:outputText value="<br/>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
		<h:outputLink value="buildCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
			<h:outputText value="#{bolonhaBundle['groups']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="groups"/>
			<f:param name="action" value="view"/>
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['groups']}" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}"/>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="buildCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="years"/>
			<f:param name="action" value="view"/>
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" rendered="#{CurricularCourseManagement.organizeBy == 'years'}"/>
	</h:panelGroup>

	<h:outputText value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>" escape="false" />
	<h:outputText value="<div class='mvert1'>" escape="false"/>
	<h:messages styleClass="error0" infoClass="success0" layout="table" globalOnly="true"/>
	<h:outputText value="</div>" escape="false"/>
	
	<fc:degreeCurricularPlanRender 
		dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
		toEdit="true" 
		organizeBy="<%=request.getParameter("organizeBy")%>"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>

		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="curricularPlansManagement"/>
		<h:outputText value="</p>" escape="false"/>
	</h:form>

</ft:tilesView>
