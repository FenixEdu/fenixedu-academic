<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CourseGroupReport.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['course.group.report']} </h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupReport.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='courseGroupID' name='courseGroupID' type='hidden' value='#{CourseGroupReport.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input id='toOrder' name='toOrder' type='hidden' value='#{CurricularCourseManagement.toOrder}'/>"/>
		<h:outputText escape="false" value="<input id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>

		<h:panelGroup rendered="#{!CourseGroupReport.rootWasClicked}">
			<h:outputText value="<p>#{bolonhaBundle['group']}: " escape="false"/>
			<h:outputText value="<b>#{CourseGroupReport.name}</b></p><br/>" escape="false"/>
		</h:panelGroup>

		<p>
		<fc:commandLink value="#{bolonhaBundle['course.group.curricular.structure']}" action="#{CourseGroupReport.exportCourseGroupCurricularStructureToExcel}"/>
		</p>
		<p>
		<fc:commandLink value="#{bolonhaBundle['course.group.studies.plan']}" action="#{CourseGroupReport.exportCourseGroupStudiesPlanToExcel}"/>
		</p>
		
		<p>
		<h:commandButton immediate="true" styleClass="inputbutton" onclick="window.close()" value="#{bolonhaBundle['close']}" />
		</p>
	</h:form>
</ft:tilesView>
