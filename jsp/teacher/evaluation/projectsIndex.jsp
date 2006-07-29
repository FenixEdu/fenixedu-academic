<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:outputText value="<h2>#{bundle['link.projects']}</h2>" escape="false" />
	<h:form>
		<h:inputHidden binding="#{projectManagementBackingBean.executionCourseIdHidden}" />
		
		<%-- ERROR MESSAGE --%>
		<h:outputText styleClass="error" rendered="#{!empty projectManagementBackingBean.errorMessage}"
			value="#{bundle[projectManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:commandLink action="enterCreateProject">
			<h:outputFormat value="<br/>#{bundle['link.create.project']}<br/><br/>" escape="false"/>
		</h:commandLink>
		<h:outputText value="<hr/>" escape="false" />
		
		<h:panelGroup rendered="#{empty projectManagementBackingBean.associatedProjects}" >
			<h:outputText value="#{bundle['message.projects.not.scheduled']}" />
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty projectManagementBackingBean.associatedProjects}">
			<h:dataTable  value="#{projectManagementBackingBean.associatedProjects}" var="project">
				<h:column>
					<h:outputText value="<strong>#{bundle['label.project']}:</strong> " escape="false" />
					<h:outputText value="#{project.name}, " />
					
					<h:outputText value="#{bundle['label.publish.date']}: " />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{project.begin}" />
					</h:outputFormat>
					<h:outputText value=", #{bundle['label.delivery.date']}: " />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{project.end}" />
					</h:outputFormat>
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:outputLink value="#{projectManagementBackingBean.contextPath}/teacher/projectSubmissionsManagement.do">
						<f:param id="method" name="method" value="viewLastProjectSubmissionForEachGroup" />
						<f:param id="executionCourseID" name="executionCourseID" value="#{projectManagementBackingBean.executionCourseID}" />
						<f:param id="projectID" name="projectID" value="#{project.idInternal}" />
						<h:outputFormat value="#{bundle['link.teacher.executionCourseManagement.evaluation.project.viewProjectSubmissions']}"/>
					</h:outputLink>
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="enterEditProject">
						<f:param id="projectIDToEdit" name="projectID" value="#{project.idInternal}" />
						<h:outputFormat value="#{bundle['link.edit']}"/>
					</h:commandLink>
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="#{projectManagementBackingBean.deleteProject}">
						<f:param id="projectIDToDelete" name="projectID" value="#{project.idInternal}" />
						<h:outputFormat value="#{bundle['link.delete']}" />
					</h:commandLink>
					
					<h:outputText value="<br/>" escape="false"/>
					<h:outputText value="#{bundle['label.description']}: " style="font-weight: bold" />
					<h:outputText value="#{project.description}" />
					
					<h:outputText value="<br/><br/>" escape="false"/>
				</h:column>			
			</h:dataTable>
		</h:panelGroup>		
	</h:form>
</ft:tilesView>
