<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</i>" escape="false"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['view.param']}</h2>" escape="false">
		<f:param value="#{bolonhaBundle['curricularPlan']}"/>
	</h:outputFormat>

	<h:panelGroup rendered="#{!empty CurricularPlansMembersManagementBackingBean.groupMembersLabels}">
		<h:outputText value="<br/><b>#{bolonhaBundle['groupMembers']}</b> (#{bolonhaBundle['groupMembersExplanation']}):<br/>" escape="false" />
		<h:dataTable value="#{CurricularPlansMembersManagementBackingBean.groupMembersLabels}" var="memberLabel">
			<h:column>
				<h:outputText value="#{memberLabel}" escape="false"/>
			</h:column>
		</h:dataTable>
	</h:panelGroup>
	<h:panelGroup rendered="#{empty CurricularPlansMembersManagementBackingBean.groupMembersLabels}">
		<h:outputText value="<br/><i>#{bolonhaBundle['label.empty.curricularPlanGroup.members']}</i><br/>" escape="false" />
	</h:panelGroup>

	<h:outputLink value="viewCurricularPlanStructure.faces" rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeModule.courseGroupContexts}">
		<h:outputFormat value="<br/><br/>#{bolonhaBundle['view.param']}" escape="false">
			<f:param value="#{bolonhaBundle['curricularPlan.structure']}"/>
		</h:outputFormat>
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
		<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
		<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>		
		<f:param name="action" value="view"/>
	</h:outputLink>

	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeStructure.childs}">
		<h:outputText value="<br/>#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'years'}">
			<h:outputText value="#{bolonhaBundle['groups']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="groups"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="view"/>
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['groups']}" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}"/>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.organizeBy == 'groups'}">
			<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="years"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="view"/>
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['year']}/#{bolonhaBundle['semester']}" rendered="#{CurricularCourseManagement.organizeBy == 'years'}"/>
	</h:panelGroup>
	
	<h:panelGroup rendered="#{!empty CurricularCourseManagement.degreeCurricularPlan.degreeModule.courseGroupContexts}">	
		<h:outputText value="<br/>#{bolonhaBundle['curricularRules']}: " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.showRules == 'false'}">
			<h:outputText value="#{bolonhaBundle['show']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="true"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="view"/>		
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['show']}" rendered="#{CurricularCourseManagement.showRules == 'true'}"/>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.showRules == 'true'}">
			<h:outputText value="#{bolonhaBundle['hide']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="false"/>
			<f:param name="hideCourses" value="#{CurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="view"/>		
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['hide']}" rendered="#{CurricularCourseManagement.showRules == 'false'}"/>
	</h:panelGroup>

	<h:panelGroup rendered="#{CurricularCourseManagement.showRules == 'true' && CurricularCourseManagement.organizeBy == 'groups'}">
		<h:outputText value="<br/>#{bolonhaBundle['curricularCourses']}: " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.hideCourses == 'true'}">
			<h:outputText value="#{bolonhaBundle['show']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="false"/>
			<f:param name="action" value="view"/>
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['show']}" rendered="#{CurricularCourseManagement.hideCourses == 'false'}"/>
		<h:outputText value=" , " escape="false"/>
		<h:outputLink value="viewCurricularPlan.faces" rendered="#{CurricularCourseManagement.hideCourses == 'false'}">
			<h:outputText value="#{bolonhaBundle['hide']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{CurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="true"/>
			<f:param name="action" value="view"/>		
		</h:outputLink>
		<h:outputText value="#{bolonhaBundle['hide']}" rendered="#{CurricularCourseManagement.hideCourses == 'true'}"/>
	</h:panelGroup>

	<h:outputText value="<br/><br/>" escape="false"/>
	<fc:degreeCurricularPlanRender 
		dcp="#{CurricularCourseManagement.degreeCurricularPlan}" 
		organizeBy="<%=request.getParameter("organizeBy")%>"
		showRules="<%=request.getParameter("showRules")%>"
		hideCourses="<%=request.getParameter("hideCourses")%>"/>

	<h:outputText value="<p>" escape="false"/>
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
	<h:outputText value="</p>" escape="false"/>

</ft:tilesView>
