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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<f:view>

	<h:outputText value="#{projectManagementBackingBean.hackToStoreExecutionCourse}" />
	<jsp:include page="/teacher/evaluation/evaluationMenu.jsp" />

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<h2>#{bundle['link.projects']}</h2>" escape="false" />
	<h:form>
		<h:inputHidden binding="#{projectManagementBackingBean.executionCourseIdHidden}" />
		
		<%-- ERROR MESSAGE --%>
		<h:outputText styleClass="error" rendered="#{!empty projectManagementBackingBean.errorMessage}"
			value="#{bundle[projectManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:outputText value="<ul class='mvert15'><li>" escape="false"/>
			<h:commandLink action="enterCreateProject">
				<h:outputFormat value="#{bundle['link.create.project']}" escape="false"/>
			</h:commandLink>
		<h:outputText value="</li></ul>" escape="false"/>
		
		<h:panelGroup rendered="#{empty projectManagementBackingBean.associatedProjects}" >
			<h:outputText value="<p><em>" escape="false"/>
			<h:outputText value="#{bundle['message.projects.not.scheduled']}" />
			<h:outputText value="</em></p>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty projectManagementBackingBean.associatedProjects}">
			<fc:dataRepeater  value="#{projectManagementBackingBean.associatedProjects}" var="project">
					<%--
					<h:outputText value="#{bundle['label.project']}: " escape="false" />
					--%>
					<h:outputText value="<div class='mtop15 mbottom2'>" escape="false" />
					<h:outputText value="<b>#{project.name}</b>, " escape="false" />
					
					<h:outputText value="<span class='color888'>#{bundle['label.net.sourceforge.fenixedu.domain.Project.projectBeginDateTime']}:</span> " escape="false" />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{project.begin}" />
					</h:outputFormat>
					<h:outputText value=", <span class='color888'>#{bundle['label.net.sourceforge.fenixedu.domain.Project.projectEndDateTime']}:</span> " escape="false" />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{project.end}" />
					</h:outputFormat>
					
					<h:outputText value=" <p> " escape="false"/>

					<h:outputText value="<a href='#{projectManagementBackingBean.contextPath}/teacher/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&amp;executionCourseID=#{projectManagementBackingBean.executionCourseID}&amp;projectID=#{project.externalId}'>#{bundle['link.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissions']}</a>" escape="false"/>
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="enterEditProject">
						<f:param id="projectIDToEdit" name="projectID" value="#{project.externalId}" />
						<h:outputFormat value="#{bundle['link.edit']}"/>
					</h:commandLink>
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="#{projectManagementBackingBean.deleteProject}">
						<f:param id="projectIDToDelete" name="projectID" value="#{project.externalId}" />
						<h:outputFormat value="#{bundle['link.delete']}" />
					</h:commandLink>
					
					<h:outputText value=" </p> " escape="false"/>
					
					<h:outputText value="#{bundle['label.description']}: " escape="false" rendered="#{!empty project.description}"/>
					<h:outputText value="#{project.description}" />

					<h:outputText value="</div>" escape="false" />
					
			</fc:dataRepeater>
		</h:panelGroup>		
	</h:form>
</f:view>
</div>
</div>
