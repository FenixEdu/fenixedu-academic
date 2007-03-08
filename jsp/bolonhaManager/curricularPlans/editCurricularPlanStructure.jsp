<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['edit.curricularPlan.structure']}</h2>" escape="false"/>
	
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.root.childContexts}">
		<h:outputText value="<ul>" escape="false"/>
		<h:outputText value="<li>" escape="false"/>
		<h:outputText value="<span class='highlight3'>#{bolonhaBundle['manage.groups']}</span>" rendered="#{CurricularCourseManagement.toOrder == 'false'}" escape="false"/>
		<h:outputLink value="editCurricularPlanStructure.faces" rendered="#{CurricularCourseManagement.toOrder == 'true'}">
			<h:outputText value="#{bolonhaBundle['manage.groups']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="toOrder" value="false"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="build"/>
		</h:outputLink>
		<h:outputText value=" , " escape="false"/>
		<h:outputText value="<span class='highlight3'>#{bolonhaBundle['order.groups']}</span>" rendered="#{CurricularCourseManagement.toOrder == 'true'}" escape="false"/>
		<h:outputLink value="editCurricularPlanStructure.faces" rendered="#{CurricularCourseManagement.toOrder == 'false'}">
			<h:outputText value="#{bolonhaBundle['order.groups']}" escape="false"/>
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="toOrder" value="true"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>			
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="build"/>
		</h:outputLink>
		<h:outputText value="</li>" escape="false"/>
		<h:outputText value="</ul>" escape="false"/>
	</h:panelGroup>
 
	<h:outputText value="<p>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:outputText value="</p>" escape="false"/>
	
	<fc:degreeCurricularPlanRender 
		dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
		onlyStructure="true" 
		toEdit="true" 
		toOrder="<%=request.getParameter("toOrder")%>"/>
	
	<h:outputText value="<br/><p>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CurricularCourseManagement.toOrder}'/>"/>
		
		<h:commandButton alt="#{htmlAltBundle['commandButton.return']}" styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="buildCurricularPlan"/>
	</h:form>
	<h:outputText value="</p>" escape="false"/>
</ft:tilesView>
