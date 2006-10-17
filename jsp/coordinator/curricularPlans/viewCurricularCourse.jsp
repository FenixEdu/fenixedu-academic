<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView locale="<%=request.getAttribute(org.apache.struts.Globals.LOCALE_KEY).toString()%>" definition="coordinatorDegreeCurricularPlan" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['viewCurricularCourse']}</h2>" escape="false"/>
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.competenceCourse.departmentUnit.name}</p>" escape="false"/>	
		<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
		<h:outputText value="<span class='attention'>#{CurricularCourseManagement.curricularCourse.name}</span></p>" escape="false"/>	
		<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
		<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
			<h:outputText value="(#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']})"/>
			<f:param name="competenceCourseID" value="#{CurricularCourseManagement.curricularCourse.competenceCourse.idInternal}"/>
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>			
		</h:outputLink>
		<h:outputText value="</p></fieldset></div>" escape="false"/>
	</h:panelGroup>

	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularCourseInformation']}</h4>" escape="false"/>
	<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
	<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
		<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.weigth} (#{bolonhaBundle['for.average.grade.calculus']})</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisites']}:</label>" escape="false"/>
		<h:outputText value="<div style='margin-left: 11em;'>#{CurricularCourseManagement.curricularCourse.prerequisites}</div></p>" escape="false" rendered="#{!empty CurricularCourseManagement.curricularCourse.prerequisites}"/>	
		<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i></p>" escape="false" rendered="#{empty CurricularCourseManagement.curricularCourse.prerequisites}"/>
		<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
		<h:outputText value="<div style='margin-left: 11em;'>#{CurricularCourseManagement.curricularCourse.prerequisitesEn}</div></p>" escape="false" rendered="#{!empty CurricularCourseManagement.curricularCourse.prerequisitesEn}"/>	
		<h:outputText value="<i>#{bolonhaBundle['empty.field']}</i></p>" escape="false" rendered="#{empty CurricularCourseManagement.curricularCourse.prerequisitesEn}"/>
	</h:panelGroup>
	<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
		<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.name}</p>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
		<h:outputText value="#{CurricularCourseManagement.curricularCourse.nameEn}</p>" escape="false"/>
	</h:panelGroup>
	<h:outputText value="</fieldset></div>" escape="false"/>
	
	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['contexts']}</h4>" escape="false"/>	
	<fc:dataRepeater value="#{CurricularCourseManagement.curricularCourse.parentContexts}" var="context">
		<h:outputText value="<fieldset class='lfloat mbottom1'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularPeriod']}:</label>" escape="false"/>
		<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
		<h:outputText value="</fieldset>" escape="false"/>
	</fc:dataRepeater>
	<h:outputText value="</div>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.curricularCourse.parentDegreeCurricularPlan.idInternal}'/>"/>
 		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>

		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'view'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="viewCurricularPlan"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty CurricularCourseManagement.action && CurricularCourseManagement.action == 'build'}">
			<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}" action="buildCurricularPlan"/>
		</h:panelGroup>
	</h:form>
</ft:tilesView>