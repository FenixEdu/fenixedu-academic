<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
		
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
						<h:panelGroup rendered="#{!empty exam.associatedRooms}">
							<h:outputText value=" #{exam.associatedRoomsAsStringList}" escape="false"/>
						</h:panelGroup>
						
						<h:outputText value=" | " escape="false"/>
						<h:commandLink action="enterEditExam">
							<f:param name="evaluationID" value="#{exam.externalId}" />
							<h:outputFormat value="#{bundle['link.edit']}" />
						</h:commandLink>
		
						<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.teacher.evaluation.enrolment.management']}: " escape="false"/>
						<h:panelGroup rendered="#{!exam.specialSeason}">
							<h:commandLink action="enterEditEnrolmentPeriod">
								<f:param name="evaluationID" value="#{exam.externalId}" />
								<h:outputFormat value="#{bundle['link.evaluation.enrollment.period']}"/>
							</h:commandLink>
						</h:panelGroup>
						<h:panelGroup rendered="#{exam.specialSeason}">
							<h:outputFormat value="#{bundle['link.evaluation.enrollment.period']}"/>
						</h:panelGroup>
			
						<h:outputText value=" | " escape="false"/>
						
						<h:panelGroup rendered="#{!exam.specialSeason}">
							<h:commandLink action="enterShowStudentsEnroled">
								<f:param name="evaluationID" value="#{exam.externalId}" />
								<h:outputFormat value="#{bundle['link.students.enrolled.inExam']}" />
							</h:commandLink>
						</h:panelGroup>
						<h:panelGroup rendered="#{exam.specialSeason}">
							<h:outputFormat value="#{bundle['link.students.enrolled.inExam']}"/>
						</h:panelGroup>
		
						<h:outputText value=" | " escape="false"/>
						
						<h:panelGroup rendered="#{!exam.specialSeason}">
							<h:commandLink action="#{evaluationManagementBackingBean.checkIfCanDistributeStudentsByRooms}">
								<f:param name="evaluationID" value="#{exam.externalId}" />
								<h:outputFormat value="#{bundle['link.students.distribution']}" />
							</h:commandLink>
						</h:panelGroup>
						<h:panelGroup rendered="#{exam.specialSeason}">
							<h:outputFormat value="#{bundle['link.students.distribution']}"/>
						</h:panelGroup>
						
						<h:outputText value="</li>" escape="false"/>
						<h:outputText value="</p>" escape="false"/>
					
						<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.students.listMarks']}: " escape="false"/>
						<h:commandLink action="enterShowMarksListOptions">
							<f:param name="evaluationID" value="#{exam.externalId}" />
							<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
						</h:commandLink>

						<h:outputText value=" | " escape="false"/>
						<h:commandLink action="enterPublishMarks">
							<f:param name="evaluationID" value="#{exam.externalId}" />
							<h:outputFormat value="#{bundle['link.publishMarks']}" />
						</h:commandLink>


					<h:outputText escape="false" value="</p><p class='indent1 mvert05'>#{bundle['label.vigilancies']}: "/>
					<h:outputLink value="#{evaluationManagementBackingBean.contextPath}/teacher/evaluation/vigilancy/vigilantsForEvaluation.do?method=viewVigilants&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}&evaluationOID=#{exam.externalId}"><h:outputText value="#{bundle['label.showVigilants']}"/></h:outputLink>
					<h:outputText value=" | " escape="false"/>
					<h:outputLink value="#{evaluationManagementBackingBean.contextPath}/teacher/evaluation/vigilancy/vigilantsForEvaluation.do?method=editReport&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}&evaluationOID=#{exam.externalId}"><h:outputText value="#{bundle['label.editReport']}"/></h:outputLink>					
					<h:outputText value="</li></ul>" escape="false"/>
					<h:outputText value="<br/>" escape="false"/>
					</h:panelGroup>
					<h:panelGroup rendered="#{!exam.isExamsMapPublished}">
						<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
						<h:outputText value="#{exam.season}, "/>
						<h:outputText value="#{bundle['label.examMap.unpublished']}"/>
					</h:panelGroup>

					<fc:dataRepeater value="#{true}" var="firstCurricularCourse"/>
	
					<h:outputText value="<p class='indent1 mvert05 disabledLink'>#{bundle['label.teacher.evaluation.associated.curricular.courses']}: " escape="false"/>
					<fc:dataRepeater value="#{exam.associatedExecutionCourses}" var="associatedExecutionCourse">
						<fc:dataRepeater value="#{exam.degreeModuleScopes}" var="degreeModuleScope">
							<fc:dataRepeater value="#{associatedExecutionCourse.associatedCurricularCoursesSet}" var ="curricularCourse">
								<h:outputText rendered="#{(degreeModuleScope.curricularCourse == curricularCourse) && !firstCurricularCourse}" value=" | " escape="false"/>
								<h:outputLink rendered="#{(degreeModuleScope.curricularCourse == curricularCourse)}" value="../../publico/executionCourse.do">
									<h:outputText value="#{curricularCourse.degreeCurricularPlan.degree.sigla}" escape="false"/>
									<f:param name="method" value="firstPage"/>
									<f:param name="executionCourseID" value="#{associatedExecutionCourse.externalId}"/>
								</h:outputLink>
								<fc:dataRepeater rendered="#{(degreeModuleScope.curricularCourse == curricularCourse) && firstCurricularCourse}" value="#{false}" var="firstCurricularCourse"/>
							</fc:dataRepeater>
						</fc:dataRepeater>
					</fc:dataRepeater>
					<h:outputText value="<br/>" escape="false"/>
					<h:outputText value="</p>" escape="false"/>
				</h:column>
			</h:dataTable>
			<h:outputText value="<br/>" escape="false"/>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
