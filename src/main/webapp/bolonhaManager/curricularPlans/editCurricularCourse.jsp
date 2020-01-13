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

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.BolonhaManager.BolonhaManagerApplication$CurricularPlansManagement"/>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="resources/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<em>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</em>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['editCurricularCourse']}</h2>" escape="false"/>		
	<h:messages infoClass="success0" errorClass="error0" layout="table" globalOnly="true"/>
	
	<h:form>
		<fc:viewState binding="#{CurricularCourseManagement.viewState}" />
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularCourseID' id='curricularCourseID' name='curricularCourseID' type='hidden' value='#{CurricularCourseManagement.curricularCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.organizeBy' id='organizeBy' name='organizeBy' type='hidden' value='#{CurricularCourseManagement.organizeBy}'/>"/>
		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['competenceCourse']}</h4>" escape="false"/>
		<h:outputText value="<fieldset class='lfloat'>" escape="false"/>
		
		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['department']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularCourseManagement.departmentUnitID}" onchange="this.form.submit();"
					valueChangeListener="#{CurricularCourseManagement.resetCompetenceCourse}">
				<f:selectItems value="#{CurricularCourseManagement.departmentUnits}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>			
			<h:outputText value="<p><label>#{bolonhaBundle['course']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularCourseManagement.competenceCourseID}" onchange="this.form.submit();">
				<f:selectItems value="#{CurricularCourseManagement.competenceCourses}"/>
			</fc:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>		
			<h:panelGroup rendered="#{(!empty CurricularCourseManagement.competenceCourseID) && (CurricularCourseManagement.competenceCourseID != 0) }">
				<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
				<h:outputLink value="#{CurricularCourseManagement.contextPath}/bolonhaManager/competenceCourses/showCompetenceCourse.faces" target="_blank">
					<h:outputText value="#{bolonhaBundle['showPage']} #{bolonhaBundle['competenceCourse']}"/>
					<f:param name="competenceCourseID" value="#{CurricularCourseManagement.competenceCourseID}"/>
				</h:outputLink>
				<h:outputText value=" (#{bolonhaBundle['newPage']})" escape="false"/>
				<h:outputText value="</p><br/>" escape="false"/>
			</h:panelGroup>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'OPTIONAL_COURSE'}">
			<h:outputText value="<p><label>#{bolonhaBundle['name']} (pt):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" size="60" maxlength="200" required="true" value="#{CurricularCourseManagement.name}"/>
			<h:message for="name" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['nameEn']} (en):</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.nameEn']}" id="nameEn" size="60" maxlength="200" required="true" value="#{CurricularCourseManagement.nameEn}"/>
			<h:message for="nameEn" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>	
		</h:panelGroup>

		<h:panelGroup rendered="#{CurricularCourseManagement.selectedCurricularCourseType == 'NORMAL_COURSE'}">
			<h:outputText value="<h4 class='first mtop1'>#{bolonhaBundle['curricularCourseInformation']}</h4>" escape="false"/>

			<h:outputText value="<p><label>#{bolonhaBundle['weight']}:</label>" escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.weight']}" id="weight" maxlength="5" size="5" value="#{CurricularCourseManagement.weight}" />
			<h:message for="weight" styleClass="error0"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesPt']}:</label>" escape="false"/>
			<h:inputTextarea id="prerequisites" cols="55" rows="5" value="#{CurricularCourseManagement.prerequisites}"/>
			<h:outputText value="</p>" escape="false"/>
			
			<h:outputText value="<p><label>#{bolonhaBundle['prerequisitesEn']}:</label>" escape="false"/>
			<h:inputTextarea id="prerequisitesEn" cols="55" rows="5" value="#{CurricularCourseManagement.prerequisitesEn}"/>
			<h:outputText value="</p>" escape="false"/>	
		</h:panelGroup>
		<h:outputText value="<p class='mtop1'><label class='lempty'>.</label>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}" action="#{CurricularCourseManagement.editCurricularCourse}"/>
		<h:outputText value="</p></fieldset></div>" escape="false"/>
		
		<h:outputText value="<div class='simpleblock4'>" escape="false"/>
		<h:outputText value="<h4 class='first'>#{bolonhaBundle['contexts']}:</h4>" escape="false"/>		
		
		<p>
		<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/curricularPlans/editCurricularCourse.faces">
			<h:outputText value="#{bolonhaBundle['newContext']}" />
			<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}" />
			<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}" />
			<f:param name="curricularCourseID" value="#{CurricularCourseManagement.curricularCourseID}" />
			<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}" />
		</h:outputLink>
		</p>

		<h:panelGroup rendered="#{empty CurricularCourseManagement.contextID}">
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

			<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
			<fc:selectOneMenu value="#{CurricularCourseManagement.term}">
				<f:selectItems value="#{CurricularCourseManagement.terms}" />
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

			<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.add']}" styleClass="inputbutton" value="#{bolonhaBundle['add']}"
				action="" actionListener="#{CurricularCourseManagement.addContext}"/>
			<h:outputText value=" " escape="false"/>							
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" immediate="true"
				action="#{CurricularCourseManagement.cancel}"/>
			<h:outputText value="</p></fieldset>" escape="false"/>
			<h:outputText value="<br/>" escape="false"/>
		</h:panelGroup>

		<h:dataTable value="#{CurricularCourseManagement.curricularCourseParentContexts}" var="context">
			<h:column>
				<h:panelGroup rendered="#{context.externalId != CurricularCourseManagement.contextID}">
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
					<h:outputLink value="#{facesContext.externalContext.requestContextPath}/bolonhaManager/curricularPlans/editCurricularCourse.faces">
						<h:outputText value="#{bolonhaBundle['edit']}" />
						<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}" />
						<f:param name="executionYearID" value="#{CurricularCourseManagement.executionYearID}" />
						<f:param name="courseGroupID" value="#{context.parentCourseGroup.externalId}" />
						<f:param name="contextID" value="#{context.externalId}" />
						<f:param name="curricularCourseID" value="#{CurricularCourseManagement.curricularCourseID}" />
						<f:param name="organizeBy" value="#{CurricularCourseManagement.organizeBy}" />
					</h:outputLink>
					<h:outputText value=", " escape="false"/>
					<h:commandLink value="#{bolonhaBundle['delete']}" action="#{CurricularCourseManagement.editCurricularCourseReturnPath}"
							actionListener="#{CurricularCourseManagement.tryDeleteContext}">
						<f:param name="contextIDToDelete" value="#{context.externalId}"/>
					</h:commandLink>
					<h:outputText value="</p></fieldset>" escape="false"/>
				</h:panelGroup>
				
				<h:panelGroup rendered="#{context.externalId == CurricularCourseManagement.contextID}">
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

					<h:outputText value="<p><label>#{bolonhaBundle['term']}:</label>" escape="false"/>
					<fc:selectOneMenu value="#{CurricularCourseManagement.term}">
						<f:selectItems value="#{CurricularCourseManagement.terms}" />
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

					<h:outputText value="<p class='mtop05'><label class='lempty'>.</label>" escape="false"/>
					<h:outputText escape="false" value="<input alt='input.contextID' id='contextID' name='contextID' type='hidden' value='#{context.externalId}'/>"/>
					<h:commandButton alt="#{htmlAltBundle['commandButton.update']}" styleClass="inputbutton" value="#{bolonhaBundle['update']}"
							action="#{CurricularCourseManagement.editContext}"/>
					<h:outputText value=" " escape="false"/>							
					<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" styleClass="inputbutton" value="#{bolonhaBundle['cancel']}" immediate="true"
							action="#{CurricularCourseManagement.cancel}"/>
					<h:outputText value="</p></fieldset>" escape="false"/>
				</h:panelGroup>
			</h:column>
		</h:dataTable>
		<h:outputText value="</div>" escape="false"/>

		<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" immediate="true" styleClass="inputbutton" value="#{bolonhaBundle['back']}"
			action="buildCurricularPlan"/>
	</h:form>
</f:view>