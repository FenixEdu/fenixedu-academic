<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
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
			<fc:dataRepeater value="#{evaluationManagementBackingBean.writtenTestList}" var="writtenTest">

				<h:outputText value="<div class='mtop05 mbottom15'>" escape="false"/>

					<%--
					<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false"/>
					--%>
					<h:outputText value="<b>#{writtenTest.description}</b>, " escape="false"/>
					<h:outputText value="#{bundle['label.day']}" />
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{writtenTest.dayDate}"/>
					</h:outputFormat>
					<h:outputText value=" #{bundle['label.at']}" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{writtenTest.beginningDate}"/>
					</h:outputFormat>
					<h:panelGroup rendered="#{!empty writtenTest.associatedRooms}">
						<h:outputText value=" #{writtenTest.associatedRoomsAsStringList}" escape="false"/>
					</h:panelGroup>
					
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="enterEditWrittenTest">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.edit']}" />
					</h:commandLink>

					<h:panelGroup rendered="#{evaluationManagementBackingBean.canManageRoomsMap[writtenTest.idInternal]}">
						<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.teacher.evaluation.room.management']}: " escape="false"/>
						<h:commandLink action="enterChooseRoom">
							<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
							<h:outputFormat value="#{bundle['link.evaluation.choose.room']}"/>
						</h:commandLink>
					</h:panelGroup>

					<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.teacher.evaluation.enrolment.management']}: " escape="false"/>
					<h:commandLink action="enterEditEnrolmentPeriod">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.evaluation.enrollment.period']}"/>
					</h:commandLink>
			
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="enterShowStudentsEnroled" >
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.students.enrolled.inExam']}" />
					</h:commandLink>
		
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="#{evaluationManagementBackingBean.checkIfCanDistributeStudentsByRooms}">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.students.distribution']}" />
					</h:commandLink>
					<h:outputText value="</p>" escape="false"/>

					<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.students.listMarks']}: " escape="false"/>
					<h:commandLink action="enterShowMarksListOptions">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
					</h:commandLink>

					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="enterPublishMarks">
						<f:param name="evaluationID" value="#{writtenTest.idInternal}" />
						<h:outputFormat value="#{bundle['link.publishMarks']}" />
					</h:commandLink>
					
					<h:outputText escape="false" value="</p><p class='indent1 mvert05'>#{bundle['label.vigilancies']}: "/>
					<h:outputLink value="#{evaluationManagementBackingBean.contextPath}/teacher/evaluation/vigilancy/vigilantsForEvaluation.do?method=viewVigilants&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}&evaluationOID=#{writtenTest.idInternal}"><h:outputText value="#{bundle['label.showVigilants']}"/></h:outputLink>
					<h:outputText value=" | " escape="false"/>
					<h:outputText rendered="#{writtenTest.isAfterCurrentDate}" value="#{bundle['label.editReport']}"/>					
					<h:outputLink rendered="#{!writtenTest.isAfterCurrentDate}" value="#{evaluationManagementBackingBean.contextPath}/teacher/evaluation/vigilancy/vigilantsForEvaluation.do?method=editReport&executionCourseID=#{evaluationManagementBackingBean.executionCourseID}&evaluationOID=#{writtenTest.idInternal}"><h:outputText value="#{bundle['label.editReport']}"/></h:outputLink>					


					<h:outputText value="</p>" escape="false"/>

				<h:outputText value="</div>" escape="false"/>
			</fc:dataRepeater>
		</h:panelGrid>	
	</h:form>

</ft:tilesView>
