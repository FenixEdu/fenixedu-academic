<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['title.evaluation.Exam']}</h2>" escape="false" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:panelGrid rendered="#{empty evaluationManagementBackingBean.examList}" >
			<h:outputText value="#{bundle['message.exams.not.scheduled']}" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty evaluationManagementBackingBean.examList}" >
			<h:dataTable value="#{evaluationManagementBackingBean.examList}" var="exam">
				<h:column>
					<h:panelGroup rendered="#{exam.isExamsMapPublished}">
						<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
						<h:outputText value="#{exam.season}, "/>
						<h:outputText value="#{bundle['label.day']}" />
						<h:outputFormat value="{0, date, dd/MM/yyyy}">
							<f:param value="#{exam.dayDate}"/>
						</h:outputFormat>
						<h:outputText value=" #{bundle['label.at']}" />
						<h:outputFormat value="{0, date, HH:mm}">
							<f:param value="#{exam.beginningDate}"/>
						</h:outputFormat>
		
						<h:outputText value="<br/><ul class=\"links\"><li><b>#{bundle['label.teacher.evaluation.enrolment.management']}:</b> " escape="false"/>
						<h:commandLink action="enterEditEnrolmentPeriod">
							<f:param name="evaluationID" value="#{exam.idInternal}" />
							<h:outputFormat value="#{bundle['link.evaluation.enrollment.period']}"/>
						</h:commandLink>
			
						<h:outputText value="<b> | </b>" escape="false"/>
						<h:commandLink action="enterShowStudentsEnroled">
							<f:param name="evaluationID" value="#{exam.idInternal}" />
							<h:outputFormat value="#{bundle['link.students.enrolled.inExam']}" />
						</h:commandLink>
		
						<h:outputText value="<b> | </b>" escape="false"/>
						<h:commandLink action="#{evaluationManagementBackingBean.checkIfCanDistributeStudentsByRooms}">
							<f:param name="evaluationID" value="#{exam.idInternal}" />
							<h:outputFormat value="#{bundle['link.students.distribution']}" />
						</h:commandLink>
						<h:outputText value="</li>" escape="false"/>
					
						<h:outputText value="<li><b>#{bundle['label.students.listMarks']}:</b> " escape="false"/>
						<h:commandLink action="enterShowMarksListOptions">
							<f:param name="evaluationID" value="#{exam.idInternal}" />
							<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
						</h:commandLink>

						<h:outputText value="<b> | </b>" escape="false"/>
						<h:commandLink action="enterPublishMarks">
							<f:param name="evaluationID" value="#{exam.idInternal}" />
							<h:outputFormat value="#{bundle['link.publishMarks']}" />
						</h:commandLink>

						<h:outputText value="</li><ul>" escape="false"/>
						<h:outputText value="<br/>" escape="false"/>
					</h:panelGroup>
					<h:panelGroup rendered="#{!exam.isExamsMapPublished}">
						<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
						<h:outputText value="#{exam.season}, "/>
						<h:outputText value="#{bundle['label.examMap.unpublished']}"/>
					</h:panelGroup>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
