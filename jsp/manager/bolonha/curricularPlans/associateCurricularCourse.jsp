<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{ManagerManagerCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ManagerCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['associateCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	<h:form>
		<fc:viewState binding="#{ManagerCurricularCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{ManagerCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{ManagerCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{ManagerCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{ManagerCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{ManagerCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{ManagerCurricularCourseManagement.action}'/>"/>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCurricularCourseAndContext']}:</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	

		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{ManagerCurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{ManagerCurricularCourseManagement.courseGroups}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularCourse']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularCourseID}">
			<f:selectItems value="#{ManagerCurricularCourseManagement.curricularCourses}"/>
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{ManagerCurricularCourseManagement.curricularYears}" />
		</h:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<h:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{ManagerCurricularCourseManagement.curricularSemesters}" />
		</h:selectOneMenu>
		
		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.beginExecutionPeriodID}">
			<f:selectItems value="#{ManagerCurricularCourseManagement.beginExecutionPeriodItems}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.endExecutionPeriodID}">
			<f:selectItems value="#{ManagerCurricularCourseManagement.endExecutionPeriodItems}" />
		</fc:selectOneMenu>
		
		<h:outputText value="</p></fieldset></div>" escape="false"/>
	
		<h:commandButton alt="#{htmlAltBundle['commandButton.associate']}" styleClass="inputbutton" value="#{bolonhaBundle['associate']}"
			action="#{ManagerCurricularCourseManagement.addContext}"/>	
		<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>