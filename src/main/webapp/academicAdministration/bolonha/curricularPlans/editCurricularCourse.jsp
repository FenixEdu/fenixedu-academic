<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication$CurricularPlansManagement" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/ManagerResources" var="managerBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['editCurricularCourse']}</h2>" escape="false"/>		
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:form>
		<fc:viewState binding="#{AcademicAdministrationCurricularCourseManagement.viewState}" />
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularCourseID' id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.curricularCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionYearID' id='executionYearID' name='executionYearID' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.executionYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.organizeBy}'/>"/>
		<h:outputText escape="false" value="<input alt='input.showRules' id='showRules' name='showRules' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.showRules}'/>"/>
		<h:outputText escape="false" value="<input alt='input.hideCourses' id='hideCourses' name='hideCourses' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.hideCourses}'/>"/>
		<h:outputText escape="false" value="<input alt='input.action' id='action' name='action' type='hidden' value='#{AcademicAdministrationCurricularCourseManagement.action}'/>"/>
		
		<h:panelGroup>
			<h:outputText value="<div class='simpleblock4'>" escape="false"/>
			<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}</h4>" escape="false"/>
			<h:outputText value="<fieldset class='lfloat'>" escape="false"/>

			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
						valueChangeListener="#{AcademicAdministrationCurricularCourseManagement.resetCompetenceCourse}">
					<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.departmentUnits}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>			
				<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
				<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
					<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.competenceCourses}"/>
				</fc:selectOneMenu>
				<h:outputText value="</p>" escape="false"/>		
				<h:panelGroup rendered="#{(!empty AcademicAdministrationCurricularCourseManagement.competenceCourseID) && (AcademicAdministrationCurricularCourseManagement.competenceCourseID != 0) }">
					<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
					<h:outputLink value="#{CurricularCourseManagement.contextPath}/bolonhaManager/competenceCourses/showCompetenceCourse.faces" target="_blank">
						<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
						<f:param name="competenceCourseID" value="#{AcademicAdministrationCurricularCourseManagement.competenceCourseID}"/>
					</h:outputLink>
					<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
					<h:outputText value="</p><br/>" escape="false"/>
				</h:panelGroup>
			</h:panelGroup>

			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
				<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" size="60" maxlength="200" required="true" value="#{AcademicAdministrationCurricularCourseManagement.name}"/>
				<h:message for="name" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>

				<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" size="60" maxlength="200" required="true" value="#{AcademicAdministrationCurricularCourseManagement.nameEn}"/>
				<h:message for="nameEn" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>	
			</h:panelGroup>

			<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
				<h:outputText value="<h4 class='first mtop1'>#{bolonhaBundle['curricularCourseInformation']}</h4>" escape="false"/>

				<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
				<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weight" maxlength="5" size="5" value="#{AcademicAdministrationCurricularCourseManagement.weight}" />
				<h:message for="weight" styleClass="error0"/>
				<h:outputText value="</p>" escape="false"/>

				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesPt']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisites" cols="55" rows="5" value="#{AcademicAdministrationCurricularCourseManagement.prerequisites}"/>
				<h:outputText value="</p>" escape="false"/>

				<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
				<h:inputTextarea id="prerequisitesEn" cols="55" rows="5" value="#{AcademicAdministrationCurricularCourseManagement.prerequisitesEn}"/>
				<h:outputText value="</p>" escape="false"/>

				<h:outputText value="<p>" escape="false" />
				<h:outputLink value="#{facesContext.externalContext.requestContextPath}/academicAdministration/viewAllCurriculumLinesOfCurricularCourse.do" target="_blank">
					<h:outputText value="#{bolonhaBundle['view.enrolments']}" />
					<f:param name="curricularCourseId" value="#{AcademicAdministrationCurricularCourseManagement.curricularCourse.externalId}" />
					<f:param name="method" value="view" />
				</h:outputLink>
				<h:outputText value="</p>" escape="false" />	
			</h:panelGroup>
			
			<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
			<fc:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}" action="#{AcademicAdministrationCurricularCourseManagement.editCurricularCourse}"/>
			<h:outputText value="</p></fieldset></div>" escape="false"/>
		</h:panelGroup>

		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['contexts']}:</h4>" escape="false"/>		
		
		<p>
		<h:outputLink value="#{facesContext.externalContext.requestContextPath}/academicAdministration/bolonha/curricularPlans/editCurricularCourse.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.degree.canBeAccessedByUser}">
			<h:outputText value="#{bolonhaBundle['newContext']}" />
			<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}" />
			<f:param name="curricularCourseID" value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseID}" />
			<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
			<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
			<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
			<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
			<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}"/>
			<f:param name="toAddNewContext" value="true"/>
		</h:outputLink>
		</p>

		<h:panelGroup rendered="#{AcademicAdministrationCurricularCourseManagement.toAddNewContext}">
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

			<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.term}">
				<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.terms}" />
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
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
			<fc:commandButton alt="#{htmlAltBundle['commandButton.add']}" styleClass="inputbutton" value="#{bolonhaBundle['add']}"
				action="#{AcademicAdministrationCurricularCourseManagement.addContext}">
				<f:param name="toAddNewContext" value="true"/>
			</fc:commandButton>
			<h:outputText value=" " escape="false"/>							
			<fc:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" immediate="true"
				action="#{AcademicAdministrationCurricularCourseManagement.cancel}">
				<f:param name="toAddNewContext" value="true"/>
			</fc:commandButton>
			<h:outputText value="</p></fieldset>" escape="false"/>
			<h:outputText value="<br/>" escape="false"/>
		</h:panelGroup>

		<h:dataTable value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseParentContexts}" var="context">
			<h:column>
				<h:panelGroup rendered="#{context.externalId != AcademicAdministrationCurricularCourseManagement.contextID}">
					<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['courseGroup']}:</label>" escape="false"/>
					<h:outputText value="#{context.parentCourseGroup.oneFullName}</p>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['curricularPeriod']}:</label>" escape="false"/>
					<h:outputText value="#{context.curricularPeriod.fullLabel}" escape="false"/>
					<h:panelGroup rendered="#{not empty context.fakeTerm}">
						<h:outputText value=", #{bolonhaBundle['term']} " escape="false"/>
						<h:outputText value="#{context.term}" escape="false"/>
					</h:panelGroup>
					<h:outputText value="</p>" escape="false"/>

					<h:outputText value="<p><label>#{bolonhaBundle['beginExecutionPeriod.validity']}:</label> " escape="false"/>
					<h:outputText value="#{context.beginExecutionPeriod.qualifiedName}</p>" escape="false"/>
					
					<h:outputText value="<p><label>#{bolonhaBundle['endExecutionPeriod.validity']}:</label> " escape="false"/>
					<h:outputText value="#{context.endExecutionPeriod.qualifiedName}</p>" escape="false" rendered="#{!empty context.endExecutionPeriod}"/>
					<h:outputText value="#{bolonhaBundle['opened']}</p>" escape="false" rendered="#{empty context.endExecutionPeriod}"/>

					<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
					<h:outputLink value="#{facesContext.externalContext.requestContextPath}/academicAdministration/bolonha/curricularPlans/editCurricularCourse.faces" rendered="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.degree.canBeAccessedByUser}">
						<h:outputText value="#{bolonhaBundle['edit']}" />
						<f:param name="degreeCurricularPlanID" value="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlanID}" />
						<f:param name="courseGroupID" value="#{context.parentCourseGroup.externalId}" />
						<f:param name="contextID" value="#{context.externalId}" />
						<f:param name="curricularCourseID" value="#{AcademicAdministrationCurricularCourseManagement.curricularCourseID}" />
						<f:param name="executionYearID" value="#{AcademicAdministrationCurricularCourseManagement.executionYearID}"/>
						<f:param name="organizeBy" value="#{AcademicAdministrationCurricularCourseManagement.organizeBy}"/>
						<f:param name="showRules" value="#{AcademicAdministrationCurricularCourseManagement.showRules}"/>
						<f:param name="hideCourses" value="#{AcademicAdministrationCurricularCourseManagement.hideCourses}"/>
						<f:param name="action" value="#{AcademicAdministrationCurricularCourseManagement.action}"/>
					</h:outputLink>
					<h:outputText value=", " rendered="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.degree.canBeAccessedByUser}" escape="false"/>
					<fc:commandLink value="#{bolonhaBundle['delete']}" action="#{AcademicAdministrationCurricularCourseManagement.editCurricularCourseReturnPath}"
							actionListener="#{AcademicAdministrationCurricularCourseManagement.tryDeleteContext}"
							rendered="#{AcademicAdministrationCurricularCourseManagement.degreeCurricularPlan.degree.canBeAccessedByUser}">
						<f:param name="contextIDToDelete" value="#{context.externalId}"/>
					</fc:commandLink>
					<h:outputText value="</p></fieldset>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{context.externalId == AcademicAdministrationCurricularCourseManagement.contextID}">
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

					<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
					<fc:selectOneMenu value="#{AcademicAdministrationCurricularCourseManagement.term}">
						<f:selectItems value="#{AcademicAdministrationCurricularCourseManagement.terms}" />
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

					<h:outputText value="</p>" escape="false"/>
					
					<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
					<fc:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}"
							action="#{AcademicAdministrationCurricularCourseManagement.editContext}">
							<f:param name="contextID" value="#{AcademicAdministrationCurricularCourseManagement.contextID}"/>
					</fc:commandButton>
					<h:outputText value=" " escape="false"/>							
					<fc:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" immediate="true"
							action="#{AcademicAdministrationCurricularCourseManagement.cancel}"/>
					<h:outputText value="</p></fieldset>" escape="false"/>
				</h:panelGroup>
			</h:column>
		</h:dataTable>		
		<h:outputText value="</div>" escape="false"/>

		<fc:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</f:view>