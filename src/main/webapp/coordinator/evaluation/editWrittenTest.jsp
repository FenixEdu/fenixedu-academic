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
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.edit.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>
	<h:outputText value="<strong>#{bundle['label.coordinator.executionCourse']}: #{coordinatorWrittenTestsManagementBackingBean.executionCourse.nome}</strong><br/><br/>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionCourseIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorWrittenTestsManagementBackingBean.errorMessage]}<br/><br/>" escape="false"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"/>

 		<h:outputText styleClass="success0" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}"
			value="#{bundle['message.evaluation.not.editable']}<br/><br/>" escape="false"/>

		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.day']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.day}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.month']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.month}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.year']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="4" size="4" value="#{coordinatorWrittenTestsManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.beginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.beginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.endHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.endMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.name']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.description']}" required="true" maxlength="120" size="15" value="#{coordinatorWrittenTestsManagementBackingBean.description}"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" action="#{coordinatorWrittenTestsManagementBackingBean.editWrittenTest}"
			styleClass="inputbutton" value="#{bundle['label.submit']}"/>		
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{coordinatorWrittenTestsManagementBackingBean.showWrittenTestsForExecutionCourses}"
			styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</f:view>
