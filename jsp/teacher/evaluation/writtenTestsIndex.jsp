<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['title.evaluation.WrittenTests']}</h2>" escape="false" />

		<h:outputFormat value="<ul class='mvert15'><li>" escape="false"/>
			<h:commandLink action="enterCreateWrittenTest">
				<h:outputFormat value="#{bundle['link.create.written.test']}" escape="false"/>
			</h:commandLink>
		<h:outputFormat value="</li></ul>" escape="false"/>
		
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />

		<h:panelGrid rendered="#{empty evaluationManagementBackingBean.writtenTestList}" >
			<h:outputText value="#{bundle['message.writtenTests.not.scheduled']}" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty evaluationManagementBackingBean.writtenTestList}" >
			<h:dataTable value="#{evaluationManagementBackingBean.writtenTestList}" var="writtenTest">
				<h:column>
					<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false"/>
					<h:outputText value="#{writtenTest.description}, "/>
					<h:outputText value="#{bundle['label.day']}" />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{writtenTest.dayDate}"/>
					</h:outputFormat>
					<h:outputText value=" #{bundle['label.at']}" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{writtenTest.beginningDate}"/>
					</h:outputFormat>
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="enterEditWrittenTest">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.edit']}" />
					</h:commandLink>

		
					<h:outputText value="<br/><ul class=\"links\"><li><b>#{bundle['label.teacher.evaluation.enrolment.management']}:</b> " escape="false"/>
					<h:commandLink action="enterEditEnrolmentPeriod">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.evaluation.enrollment.period']}"/>
					</h:commandLink>
			
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="enterShowStudentsEnroled">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.students.enrolled.inExam']}" />
					</h:commandLink>
		
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="#{evaluationManagementBackingBean.checkIfCanDistributeStudentsByRooms}">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.students.distribution']}" />
					</h:commandLink>
					<h:outputText value="</li>" escape="false"/>

					<h:outputText value="<li><b>#{bundle['label.students.listMarks']}:</b> " escape="false"/>
					<h:commandLink action="enterShowMarksListOptions">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
					</h:commandLink>

					<h:outputText value="<b> | </b>" escape="false"/>
					<h:commandLink action="enterPublishMarks">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.publishMarks']}" />
					</h:commandLink>
					
					<h:outputText escape="false" value="</li><li><b>#{bundle['label.vigilancies']}:</b> "/>
					<h:outputLink value="#{evaluationManagementBackingBean.contextPath}/teacher/evaluation/vigilancy/vigilantsForEvaluation.do?method=viewVigilants&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}&evaluationOID=#{writtenTest.idInternal}"><h:outputText value="#{bundle['label.showVigilants']}"/></h:outputLink>
					<h:outputText value="<b> | </b>" escape="false"/>
					<h:outputText rendered="#{writtenTest.isAfterCurrentDate}" value="#{bundle['label.editReport']}"/>					
					<h:outputLink rendered="#{!writtenTest.isAfterCurrentDate}" value="#{evaluationManagementBackingBean.contextPath}/teacher/evaluation/vigilancy/vigilantsForEvaluation.do?method=editReport&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}&evaluationOID=#{writtenTest.idInternal}"><h:outputText value="#{bundle['label.editReport']}"/></h:outputLink>					


					<h:outputText value="</li></ul>" escape="false"/>
					<h:outputText value="<br/>" escape="false"/>

				</h:column>
			</h:dataTable>
		</h:panelGrid>	
	</h:form>

</ft:tilesView>
