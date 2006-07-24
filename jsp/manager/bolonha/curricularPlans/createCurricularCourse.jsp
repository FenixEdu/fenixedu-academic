<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<style>
		table.nospace label {
			width: auto;
		}
	</style>	
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>	
	<h:outputText value="<h2>#{bolonhaBundle['createCurricularCourse']}</h2>" escape="false"/>
	
	<h:messages infoClass="success0" errorClass="error0" globalOnly="true"/>

	<h:outputText value="<div class='simpleblock4'>" escape="false"/>
	<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCompetenceCourse']}</h4>" escape="false"/>
	<h:form>
		<fc:viewState binding="#{CurricularCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{CurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearID' id='curricularYearID' name='curricularYearID' type='hidden' value='#{CurricularCourseManagement.curricularYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularSemesterID' id='curricularSemesterID' name='curricularSemesterID' type='hidden' value='#{CurricularCourseManagement.curricularSemesterID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{CurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{CurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{CurricularCourseManagement.action}'/>"/>
		
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['type']}:</label>" escape="false"/>
		<h:selectOneRadio value="#{CurricularCourseManagement.selectedCurricularCourseType}" styleClass="nospace" onchange="this.form.submit();">
			<f:selectItem itemValue="NORMAL_COURSE" itemLabel="#{bolonhaBundle['NORMAL_COURSE']}" />
			<f:selectItem itemValue="OPTIONAL_COURSE" itemLabel="#{bolonhaBundle['OPTIONAL_COURSE']}" />
		</h:selectOneRadio>
		<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		<h:outputText value="</p>" escape="false"/>
		
		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
					valueChangeListener="#{CurricularCourseManagement.resetCompetenceCourse}">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>			
			<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
				<f:selectItems value="#{CurricularCourseManagement.competenceCourses}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>		
			<h:panelGroup rendered="#{(!empty CurricularCourseManagement.competenceCourseID) && (CurricularCourseManagement.competenceCourseID != 0) }">
				<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
				<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
					<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
					<f:param name="competenceCourseID" value="#{CurricularCourseManagement.competenceCourseID}"/>
				</h:outputLink>
				<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" size="40" maxlength="40" value="#{CurricularCourseManagement.name}"/>
			<h:message for="name" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" size="40" maxlength="40" value="#{CurricularCourseManagement.nameEn}"/>
			<h:message for="nameEn" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>		
		</h:panelGroup>
		
		<h:outputText value="</fieldset></div>" escape="false"/>

		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularInformation']}</h4>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weight" maxlength="5" size="5" value="#{CurricularCourseManagement.weight}" />
			<h:message for="weight" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesPt']}:</label>" escape="false"/>
			<h:inputTextarea id="prerequisites" cols="55" rows="5" value="#{CurricularCourseManagement.prerequisites}"/>
			<h:outputText value="</p>" escape="false"/>
	
			<h:outputText value="" escape="false"/>
			<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
			<h:inputTextarea id="prerequisitesEn" cols="55" rows="5" value="#{CurricularCourseManagement.prerequisitesEn}"/>
			<h:outputText value="</p></fieldset></div>" escape="false"/>
		</h:panelGroup>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['context']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{CurricularCourseManagement.courseGroups}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularYears}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
			
		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{CurricularCourseManagement.curricularSemesters}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.beginExecutionPeriodID}">
			<f:selectItems value="#{CurricularCourseManagement.beginExecutionPeriodItems}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{CurricularCourseManagement.endExecutionPeriodID}">
			<f:selectItems value="#{CurricularCourseManagement.endExecutionPeriodItems}" />
		</fc:selectOneMenu>

		<h:outputText value="</p></fieldset></div>" escape="false"/>		

		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}"
			action="#{CurricularCourseManagement.createCurricularCourse}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>