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
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.executionCourseIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.evaluationIdHidden}" />
		
		<%-- Error Message --%>
		<h:outputText styleClass="error" rendered="#{!empty coordinatorProjectsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorProjectsManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty coordinatorProjectsManagementBackingBean.errorMessage}"/>
		
		<h:outputText value="<br/><strong>#{bundle['label.coordinator.executionCourse']}: </strong>" escape="false" />
		<h:outputText value="#{coordinatorProjectsManagementBackingBean.executionCourse.nome}<br/><br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">	
			<h:outputText value="#{bundle['label.coordinator.identification']}: " styleClass="bold" />
			<h:outputText value="#{coordinatorProjectsManagementBackingBean.evaluation.name}" />
			<h:outputText value="#{bundle['label.beginDate']}: " styleClass="bold" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{coordinatorProjectsManagementBackingBean.evaluation.begin}" />
			</h:outputFormat>			
			<h:outputText value="#{bundle['label.endDate']}: " styleClass="bold" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{coordinatorProjectsManagementBackingBean.evaluation.end}" />
			</h:outputFormat>
		</h:panelGrid>
		
		<h:outputText value="<br/>#{bundle['message.confirm.evaluation']}<br/><br/>" escape="false" styleClass="error"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" action="#{coordinatorProjectsManagementBackingBean.deleteProject}"
		   styleClass="inputbutton" value="#{bundle['button.yes']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" action="#{coordinatorProjectsManagementBackingBean.showProjectsForExecutionCourses}"
		   styleClass="inputbutton" value="#{bundle['button.no']}"/>
					
	</h:form>
</f:view>