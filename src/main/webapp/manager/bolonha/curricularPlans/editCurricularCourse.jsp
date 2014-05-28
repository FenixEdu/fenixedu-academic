<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{ManagerCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[ManagerCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['editCurricularCourse']}</h2>" escape="false"/>		
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:form>
		<fc:viewState binding="#{ManagerCurricularCourseManagement.viewState}" />
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{ManagerCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularCourseID' id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{ManagerCurricularCourseManagement.curricularCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{ManagerCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{ManagerCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{ManagerCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{ManagerCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{ManagerCurricularCourseManagement.action}'/>"/>
		
		<h:panelGroup rendered="#{ManagerCurricularCourseManagement.degreeCurricularPlan.bolonhaDegree}">
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}</h4>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			
			<h:panelGroup rendered="#{ManagerCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
						valueChangeListener="#{ManagerCurricularCourseManagement.resetCompetenceCourse}">
					<f:selectItems value="#{ManagerCurricularCourseManagement.departmentUnits}"/>
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
				<h:outputText value="</p>" escape="false"/>			
				<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
					<f:selectItems value="#{ManagerCurricularCourseManagement.competenceCourses}"/>
				</fc:selectOneMenu>
				<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
				<h:outputText value="</p>" escape="false"/>		
				<h:panelGroup rendered="#{(!empty ManagerCurricularCourseManagement.competenceCourseID) && (ManagerCurricularCourseManagement.competenceCourseID != 0) }">
					<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
					<h:outputLink value="../competenceCourses/showCompetenceCourse.faces" target="_blank">
						<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
						<f:param name="competenceCourseID" value="#{ManagerCurricularCourseManagement.competenceCourseID}"/>
					</h:outputLink>
					<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
					<h:outputText value="</p><br/>" escape="false"/>
				</h:panelGroup>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{ManagerCurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" size="60" maxlength="200" required="true" value="#{ManagerCurricularCourseManagement.name}"/>
				<h:message for="name" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" size="60" maxlength="200" required="true" value="#{ManagerCurricularCourseManagement.nameEn}"/>
				<h:message for="nameEn" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>	
			</h:panelGroup>
	
			<h:panelGroup rendered="#{ManagerCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<h4 class='first mtop1'>#{bolonhaBundle['curricularCourseInformation']}</h4>" escape="false"/>
	
				<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weight" maxlength="5" size="5" value="#{ManagerCurricularCourseManagement.weight}" />
				<h:message for="weight" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>
	
				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesPt']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisites" cols="55" rows="5" value="#{ManagerCurricularCourseManagement.prerequisites}"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisitesEn" cols="55" rows="5" value="#{ManagerCurricularCourseManagement.prerequisitesEn}"/>
				<h:outputText value="</p>" escape="false"/>
				
				<h:outputText value="<p>" escape="false" />
				<h:outputLink value="#{facesContext.externalContext.requestContextPath}/manager/viewAllCurriculumLinesOfCurricularCourse.do" target="_blank">
					<h:outputText value="#{bolonhaBundle['view.enrolments']}" />
					<f:param name="curricularCourseId" value="#{ManagerCurricularCourseManagement.curricularCourse.externalId}" />
					<f:param name="method" value="view" />
				</h:outputLink>
				<h:outputText value="</p>" escape="false" />	
			</h:panelGroup>
			
			<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
			<fc:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}" action="#{ManagerCurricularCourseManagement.editCurricularCourse}"/>
			<h:outputText value="</p></fieldset></div>" escape="false"/>
		</h:panelGroup>
		
		<%-- Pre-Bolonha DCP --%>
		<h:panelGroup rendered="#{!ManagerCurricularCourseManagement.degreeCurricularPlan.bolonhaDegree}">
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat4'>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="namePB" size="80" maxlength="80" value="#{ManagerCurricularCourseManagement.name}"/>
			<h:message for="namePB" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEnPB" size="80" maxlength="80" value="#{ManagerCurricularCourseManagement.nameEn}"/>
			<h:message for="nameEnPB" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['code']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.code']}" id="code" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.code}"/>
			<h:message for="code" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['acronym']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.acronym']}" id="acronym" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.acronym}"/>
			<h:message for="acronym" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.minIncrementNac.abbr']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.minIncrementNac']}" id="minIncrementNac" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.minimumValueForAcumulatedEnrollments}"/>
			<h:message for="minIncrementNac" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.maxIncrementNac.abbr']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.maxIncrementNac']}" id="maxIncrementNac" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.maximumValueForAcumulatedEnrollments}"/>
			<h:message for="maxIncrementNac" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.weight']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weightPB" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.weight}"/>
			<h:message for="weightPB" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.enrollmentWeigth']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.enrollmentWeigth']}" id="enrollmentWeigth" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.enrollmentWeigth}"/>
			<h:message for="enrollmentWeigth" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.credits']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.credits']}" id="credits" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.credits}"/>
			<h:message for="credits" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.curricular.course.ectsCredits']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.ectsCredits']}" id="ectsCredits" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.ectsCredits}"/>
			<h:message for="ectsCredits" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.theoreticalHours']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.theoreticalHours']}" id="theoreticalHours" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.theoreticalHours}"/>
			<h:message for="theoreticalHours" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.labHours']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.labHours']}" id="labHours" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.labHours}"/>
			<h:message for="labHours" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.praticalHours']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.praticalHours']}" id="praticalHours" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.praticalHours}"/>
			<h:message for="praticalHours" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.theoPratHours']}</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.theoPratHours']}" id="theoPratHours" size="10" maxlength="10" value="#{ManagerCurricularCourseManagement.theoPratHours}"/>
			<h:message for="theoPratHours" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{managerBundle['message.manager.gradeScale']}</label>" escape="false"/>
			<h:selectOneMenu value="#{ManagerCurricularCourseManagement.gradeScaleString}" id="gradeScale">
				<f:selectItems value="#{ManagerCurricularCourseManagement.gradeScales}"/>
			</h:selectOneMenu>
			<h:outputText value="(#{managerBundle['message.manager.by.default']} #{ManagerCurricularCourseManagement.degreeCurricularPlan.gradeScaleChain.description})" />
			<h:message for="gradeScale" styleClass="error0"/>
			<h:outputText value="</p>" escape="false" />
			
			<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
			<fc:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}" action="#{ManagerCurricularCourseManagement.editOldCurricularCourse}"/>
			<h:outputText value="</p></fieldset></div>" escape="false"/>
		</h:panelGroup>
		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['contexts']}:</h4>" escape="false"/>		
		
		<p>
		<h:outputLink value="#{facesContext.externalContext.requestContextPath}/manager/bolonha/curricularPlans/editCurricularCourse.faces">
			<h:outputText value="#{bolonhaBundle['newContext']}" />
			<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}" />
			<f:param name="curricularCourseID" value="#{ManagerCurricularCourseManagement.curricularCourseID}" />
			<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
			<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
		</h:outputLink>
		</p>

		<h:panelGroup rendered="#{empty ManagerCurricularCourseManagement.contextID}">
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.courseGroupID}">
				<f:selectItems value="#{ManagerCurricularCourseManagement.courseGroups}" />
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
		
			<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularYearID}">
				<f:selectItems value="#{ManagerCurricularCourseManagement.curricularYears}" />
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
		
			<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularSemesterID}">
				<f:selectItems value="#{ManagerCurricularCourseManagement.curricularSemesters}" />
			</fc:selectOneMenu>
			
			<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
			<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.beginExecutionPeriodID}">
				<f:selectItems value="#{ManagerCurricularCourseManagement.beginExecutionPeriodItems}" />
			</fc:selectOneMenu>
			<h:outputText value="</p>" escape="false"/>
	
			<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
			<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.endExecutionPeriodID}">
				<f:selectItems value="#{ManagerCurricularCourseManagement.endExecutionPeriodItems}" />
			</fc:selectOneMenu>
			
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
			<fc:commandButton alt="#{htmlAltBundle['commandButton.add']}" styleClass="inputbutton" value="#{bolonhaBundle['add']}"
				action="" actionListener="#{ManagerCurricularCourseManagement.addContext}"/>
			<h:outputText value=" " escape="false"/>							
			<fc:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" immediate="true"
				action="#{ManagerCurricularCourseManagement.cancel}"/>
			<h:outputText value="</p></fieldset>" escape="false"/>
			<h:outputText value="<br/>" escape="false"/>
		</h:panelGroup>

		<h:dataTable value="#{ManagerCurricularCourseManagement.curricularCourse.parentContexts}" var="context">
			<h:column>
				<h:panelGroup rendered="#{context.externalId != ManagerCurricularCourseManagement.contextID}">
					<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
					<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['curricularPeriod']}:</label>" escape="false"/>
					<h:outputText value="#{context.curricularPeriod.fullLabel}</p>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
					<h:outputText value="#{context.beginExecutionPeriod.qualifiedName}</p>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
					<h:outputText value="#{context.endExecutionPeriod.qualifiedName}</p>" escape="false" rendered="#{!empty context.endExecutionPeriod}"/>
					<h:outputText value="#{bolonhaBundle['opened']}</p>" escape="false" rendered="#{empty context.endExecutionPeriod}"/>
					
					<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
					<h:outputLink value="#{facesContext.externalContext.requestContextPath}/manager/bolonha/curricularPlans/editCurricularCourse.faces">
						<h:outputText value="#{bolonhaBundle['edit']}" />
						<f:param name="degreeCurricularPlanID" value="#{ManagerCurricularCourseManagement.degreeCurricularPlanID}" />
						<f:param name="courseGroupID" value="#{context.parentCourseGroup.externalId}" />
						<f:param name="contextID" value="#{context.externalId}" />
						<f:param name="curricularCourseID" value="#{ManagerCurricularCourseManagement.curricularCourseID}" />
						<f:param name="executionYearID" value="#{ManagerCurricularCourseManagement.executionYearID}"/>
						<f:param name="organizeBy" value="#{ManagerCurricularCourseManagement.organizeBy}"/>
						<f:param name="showRules" value="#{ManagerCurricularCourseManagement.showRules}"/>
						<f:param name="hideCourses" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
						<f:param name="action" value="#{ManagerCurricularCourseManagement.hideCourses}"/>
					</h:outputLink>
					<h:outputText value=", " escape="false"/>
					<fc:commandLink value="#{bolonhaBundle['delete']}" action="#{ManagerCurricularCourseManagement.editCurricularCourseReturnPath}"
							actionListener="#{ManagerCurricularCourseManagement.tryDeleteContext}">
						<f:param name="contextIDToDelete" value="#{context.externalId}"/>
					</fc:commandLink>
					<h:outputText value="</p></fieldset>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{context.externalId == ManagerCurricularCourseManagement.contextID}">
					<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
					<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.courseGroupID}">
						<f:selectItems value="#{ManagerCurricularCourseManagement.courseGroups}" />
					</fc:selectOneMenu>
					<h:outputText value="</p>" escape="false"/>
				
					<h:outputText value="<p><label>#{bolonhaBundle['curricularYear']}:</label>" escape="false"/>
					<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularYearID}">
						<f:selectItems value="#{ManagerCurricularCourseManagement.curricularYears}" />
					</fc:selectOneMenu>
					<h:outputText value="</p>" escape="false"/>
				
					<h:outputText value="<p><label>#{bolonhaBundle['semester']}:</label>" escape="false"/>
					<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.curricularSemesterID}">
						<f:selectItems value="#{ManagerCurricularCourseManagement.curricularSemesters}" />
					</fc:selectOneMenu>
					
					<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
					<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.beginExecutionPeriodID}">
						<f:selectItems value="#{ManagerCurricularCourseManagement.beginExecutionPeriodItems}" />
					</fc:selectOneMenu>
					<h:outputText value="</p>" escape="false"/>
			
					<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
					<fc:selectOneMenu value="#{ManagerCurricularCourseManagement.endExecutionPeriodID}">
						<f:selectItems value="#{ManagerCurricularCourseManagement.endExecutionPeriodItems}" />
					</fc:selectOneMenu>
			
					<h:outputText value="</p>" escape="false"/>
					
					<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
					<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{context.externalId}'/>"/>
					<fc:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}"
							action="#{ManagerCurricularCourseManagement.editContext}"/>
					<h:outputText value=" " escape="false"/>							
					<fc:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" immediate="true"
							action="#{ManagerCurricularCourseManagement.cancel}"/>
					<h:outputText value="</p></fieldset>" escape="false"/>
				</h:panelGroup>
			</h:column>
		</h:dataTable>		
		<h:outputText value="</div>" escape="false"/>

		<fc:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</f:view>