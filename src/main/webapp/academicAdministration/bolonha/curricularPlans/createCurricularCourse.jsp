<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="df.layout.two-column.contents" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>

	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>	
	<h:outputText value="<h2>#{bolonhaBundle['createCurricularCourse']}</h2>" escape="false"/>
	
	<h:messages infoClass="success0" errorClass="error0" globalOnly="true"/>

	<h:form>
		<fc:viewState binding="#{AcademicAdministrationCurricularCourseManagement.viewState}"/>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearID' id='curricularYearID' name='curricularYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.curricularYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularSemesterID' id='curricularSemesterID' name='curricularSemesterID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.curricularSemesterID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>
		
		<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.bolonhaDegree}">
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['chooseCompetenceCourse']}</h4>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['type']}:</label>" escape="false"/>
			<h:selectOneRadio value="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType}" styleClass="nospace" onchange="this.form.submit();">
				<f:selectItem itemValue="NORMAL_COURSE" itemLabel="#{bolonhaBundle['NORMAL_COURSE']}" />
				<f:selectItem itemValue="OPTIONAL_COURSE" itemLabel="#{bolonhaBundle['OPTIONAL_COURSE']}" />
			</h:selectOneRadio>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
						valueChangeListener="#{AcademicAdministrationCurricularCourseManagement.resetCompetenceCourse}">
					<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.departmentUnits}"/>
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
				<h:outputText value="</p>" escape="false"/>			
				<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
					<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.competenceCourses}"/>
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
				<h:outputText value="</p>" escape="false"/>		
				<h:panelGroup rendered="#{(!empty AcademicAdministrationCurricularCourseManagement.competenceCourseID) && (AcademicAdministrationCurricularCourseManagement.competenceCourseID != 0) }">
					<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
					<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
						<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
						<f:param name="competenceCourseID" value="#{AcademicAdministrationCurricularCourseManagement.competenceCourseID}"/>
					</h:outputLink>
					<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
					<h:outputText value="</p>" escape="false"/>
				</h:panelGroup>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" size="50" maxlength="200" value="#{AcademicAdministrationCurricularCourseManagement.name}"/>
				<h:message for="name" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" size="50" maxlength="200" value="#{AcademicAdministrationCurricularCourseManagement.nameEn}"/>
				<h:message for="nameEn" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>		
			</h:panelGroup>
			
			<h:outputText value="</fieldset></div>" escape="false"/>

			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<div class='simpleblock4'>" escape="false"/>
				<h:outputText value="<h4 class='first'>#{bolonhaBundle['curricularInformation']}</h4>" escape="false"/>
				<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weight" maxlength="5" size="5" value="#{AcademicAdministrationCurricularCourseManagement.weight}" />
				<h:message for="weight" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesPt']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisites" cols="55" rows="5" value="#{AcademicAdministrationCurricularCourseManagement.prerequisites}"/>
				<h:outputText value="</p>" escape="false"/>
		
				<h:outputText value="" escape="false"/>
				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisitesEn" cols="55" rows="5" value="#{AcademicAdministrationCurricularCourseManagement.prerequisitesEn}"/>
				<h:outputText value="</p></fieldset></div>" escape="false"/>
			</h:panelGroup>
		
		</h:panelGroup>
		
		<%-- Pre-Bolonha DCP --%>
		<h:panelGroup rendered="#{!AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.bolonhaDegree}">
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="namePB" size="40" maxlength="100" value="#{AcademicAdministrationCurricularCourseManagement.name}"/>
			<h:message for="namePB" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEnPB" size="40" maxlength="100" value="#{AcademicAdministrationCurricularCourseManagement.nameEn}"/>
			<h:message for="nameEnPB" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['code']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.code']}" id="code" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.code}"/>
			<h:message for="code" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['acronym']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" id="acronym" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.acronym}"/>
			<h:message for="acronym" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.minIncrementNac.abbr']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.minIncrementNac']}" id="minIncrementNac" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.minimumValueForAcumulatedEnrollments}"/>
			<h:message for="minIncrementNac" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.maxIncrementNac.abbr']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.maxIncrementNac']}" id="maxIncrementNac" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.maximumValueForAcumulatedEnrollments}"/>
			<h:message for="maxIncrementNac" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.weight']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weightPB" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.weight}"/>
			<h:message for="weightPB" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.enrollmentWeigth']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.enrollmentWeigth']}" id="enrollmentWeigth" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.enrollmentWeigth}"/>
			<h:message for="enrollmentWeigth" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.credits']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.credits']}" id="credits" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.credits}"/>
			<h:message for="credits" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.ectsCredits']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.ectsCredits']}" id="ectsCredits" size="10" maxlength="10" value="#{AcademicAdministrationCurricularCourseManagement.ectsCredits}"/>
			<h:message for="ectsCredits" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{managerBundle['message.manager.gradeScale']}</label>" escape="false"/>
			<h:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.gradeScaleString}" id="gradeScale">
				<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.gradeScales}"/>
			</h:selectOneMenu>
			<h:outputText value="(#{managerBundle['message.manager.by.default']} #{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.gradeScaleChain.description})" />
			<h:message for="gradeScale" styleClass="error0"/>
			<h:outputText value="</p>" escape="false" />
			
			<h:outputText value="</fieldset></div>" escape="false"/>
		</h:panelGroup>

		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['context']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.courseGroupID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.courseGroups}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularYearID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularYears}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
			
		<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.curricularSemesterID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.curricularSemesters}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>
		
		<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.beginExecutionPeriodID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.beginExecutionPeriodItems}" />
		</fc:selectOneMenu>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
		<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.endExecutionPeriodID}">
			<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.endExecutionPeriodItems}" />
		</fc:selectOneMenu>

		<h:outputText value="</p></fieldset></div>" escape="false"/>		

		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}" 
			action="#{AcademicAdministrationCurricularCourseManagement.createCurricularCourse}" rendered="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.bolonhaDegree}" />
			
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" styleClass="inputbutton" value="#{bolonhaBundle['create']}" 
			action="#{AcademicAdministrationCurricularCourseManagement.createOldCurricularCourse}" rendered="#{!AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.bolonhaDegree}" />

		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}"
			action="buildCurricularPlan"/>
	</h:form>
</ft:tilesView>