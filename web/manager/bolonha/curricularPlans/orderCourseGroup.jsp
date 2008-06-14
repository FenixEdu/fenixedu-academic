<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	
	<h:outputText value="#{CourseGroupManagement.degreeCurricularPlan.name}" style="font-style: italic"/>
	<h:outputFormat value="<h2>#{bolonhaBundle['order.group']} </h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CourseGroupManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.courseGroupID' id='courseGroupID' name='courseGroupID' type='hidden' value='#{CourseGroupManagement.courseGroupID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{CourseGroupManagement.contextID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		<h:outputText escape="false" value="<input alt='input.toOrder' id='toOrder' name='toOrder' type='hidden' value='#{CurricularCourseManagement.toOrder}'/>"/>
		<h:outputText escape="false" value="<input alt='input.pos' id='pos' name='pos' type='hidden' value='#{CourseGroupManagement.position}'/>"/>		

		<h:outputText value="<p>#{bolonhaBundle['group']}: " escape="false"/>
		<h:outputText value="<b>#{CourseGroupManagement.name}</b></p><br/>" escape="false"/>

		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText styleClass="warning0" value="#{bolonhaBundle['confirmMoveMessage']}"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<br/><p>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" styleClass="inputbutton" value="#{bolonhaBundle['yes']}"
			action="#{CourseGroupManagement.orderCourseGroup}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['no']}"
			action="editCurricularPlanStructure"/>
		<h:outputText value="</p>" escape="false"/>	
	</h:form>
</ft:tilesView>