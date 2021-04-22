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
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>

	<h:outputText value="#{projectManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
		<h:form>
			<h:inputHidden binding="#{projectManagementBackingBean.executionCourseIdHidden}" />

			<h:outputFormat value="<h2>#{bundle['link.edit.project']}</h2/>" escape="false"/>
			<%-- ERROR MESSAGE --%>
			<h:outputText styleClass="error" rendered="#{!empty projectManagementBackingBean.errorMessage}"
				value="#{bundle[projectManagementBackingBean.errorMessage]}<br/>" escape="false" />
			
			<h:panelGrid columns="2" styleClass="tstyle5" columnClasses="aright,,"  rowClasses=",,,valigntop">
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.name']}: " />
				</h:panelGroup>	
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" maxlength="100" size="35" value="#{projectManagementBackingBean.name}" />			
					<h:message for="name" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>				
					<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.projectBeginDateTime']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.beginProjectDate']}" id="beginDate" required="true" maxlength="10" size="10" value="#{projectManagementBackingBean.beginProjectDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false"/>
					<h:message for="beginDate" styleClass="error"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginProjectHour']}" id="beginHour" required="true" maxlength="5" size="5" value="#{projectManagementBackingBean.beginProjectHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="beginHour" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>					
					<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.projectEndDateTime']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.endProjectDate']}" id="endDate" required="true" maxlength="10" size="10" value="#{projectManagementBackingBean.endProjectDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false" />
					<h:message for="endDate" styleClass="error"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endProjectHour']}" id="endHour" required="true" maxlength="5" size="5" value="#{projectManagementBackingBean.endProjectHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="endHour" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="#{bundle['label.gradeScale']}: " />
				<h:selectOneMenu value="#{projectManagementBackingBean.gradeScale}">
					<f:selectItems value="#{projectManagementBackingBean.gradeScaleOptions}"/>
				</h:selectOneMenu>
						
				<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.description']}: " />
				<h:inputTextarea rows="4" cols="40" value="#{projectManagementBackingBean.description}" />
				
				<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.onlineSubmissionsAllowed']}:" />
				<h:panelGroup>
					<h:selectBooleanCheckbox id="onlineSubmissionsAllowed" required="true" value="#{projectManagementBackingBean.onlineSubmissionsAllowed}" />
					<h:message for="onlineSubmissionsAllowed" styleClass="error"/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.maxSubmissionsToKeep']}:" />				
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.maxSubmissionsToKeep']}" id="maxSubmissionsToKeep" required="false" value="#{projectManagementBackingBean.maxSubmissionsToKeep}" maxlength="2" size="2"/>
					<h:message for="maxSubmissionsToKeep" styleClass="error"/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.visible']}:" />
				<h:panelGroup>
					<h:selectBooleanCheckbox id="visible" required="true" value="#{projectManagementBackingBean.visible}" />
					<h:message for="visible" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.grouping.name']}:" />
				<h:panelGroup>
					<h:selectOneMenu id="groupingID" value="#{projectManagementBackingBean.groupingID}">
						<f:selectItems value="#{projectManagementBackingBean.executionCourseGroupings}"/>
					</h:selectOneMenu>
					<h:message for="groupingID" styleClass="error"/>
				</h:panelGroup>
				<h:outputText value="#{bundle['label.org.fenixedu.academic.domain.Project.deparments']}:" />
				<h:panelGroup>
					<h:selectManyCheckbox id="departments" value="#{projectManagementBackingBean.selectedDepartments}" layout="pageDirection">
						<f:selectItems value="#{projectManagementBackingBean.departments}"/>
					</h:selectManyCheckbox>
				</h:panelGroup>			
			</h:panelGrid>			

			<h:outputText value="* " style="color: red" escape="false"/>
			<h:outputText value="#{bundle['label.neededFields']}"/>

			<h:outputText value="<br/><br/>" escape="false" />			
			<h:inputHidden id="projectID" value="#{projectManagementBackingBean.projectID}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}"  action="#{projectManagementBackingBean.editProject}"
				styleClass="inputbutton" value="#{bundle['button.submit']}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="projectsIndex"
				styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
</f:view>
</div>
</div>
