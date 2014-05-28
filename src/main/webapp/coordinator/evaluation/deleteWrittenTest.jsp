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

	<h:form>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionCourseIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.evaluationIdHidden}" />
		
		<%-- Error Message --%>
		<h:outputText styleClass="error" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorWrittenTestsManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"/>
		
		<h:outputText value="<br/><strong>#{bundle['label.coordinator.executionCourse']}: </strong>" escape="false" />
		<h:outputText value="#{coordinatorWrittenTestsManagementBackingBean.executionCourse.nome}<br/><br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">	
			<h:outputText value="#{bundle['label.coordinator.identification']}: " styleClass="bold" />
			<h:outputText value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.description}" />
			<h:outputText value="#{bundle['label.coordinator.evaluationDate']}: " styleClass="bold" escape="false"/>
			<h:panelGroup>
				<h:outputFormat value="{0, date, dd/MM/yyyy} - ">
					<f:param value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.coordinator.to']} " />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.endDate}" />
				</h:outputFormat>				
			</h:panelGroup>
		</h:panelGrid>
		
		<h:outputText value="<br/>#{bundle['message.confirm.written.test']}<br/><br/>" escape="false" styleClass="error"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" action="#{coordinatorWrittenTestsManagementBackingBean.deleteWrittenTest}"
		   styleClass="inputbutton" value="#{bundle['button.yes']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" action="#{coordinatorWrittenTestsManagementBackingBean.showWrittenTestsForExecutionCourses}"
		   styleClass="inputbutton" value="#{bundle['button.no']}"/>
					
	</h:form>
</f:view>