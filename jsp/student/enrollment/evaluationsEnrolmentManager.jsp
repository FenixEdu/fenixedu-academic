<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>
.greyBorderClass {
	border-style: solid;
	border-width: 1px;
	border-color: #909090;
	width: 100%
}
.centerClass {
	text-align: center
}
.boldFontClass { 
	font-weight: bold
}
</style>

<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/StudentResources" var="bundle"/>
	<h:form>	
		<h:outputText value="<h2>#{bundle['link.exams.enrolment']}</h2>" escape="false" />
		<h:outputText styleClass="error" rendered="#{!empty displayEvaluationsToEnrol.errorMessage}"
				value="#{bundle[displayEvaluationsToEnrol.errorMessage]}<br/>" escape="false" />
		<%-- UNENROLED EXAMS --%>
		<h:outputText value="<h3>#{bundle['label.examsToEnroll']}</h3>" escape="false" />
		<h:panelGrid rendered="#{empty displayEvaluationsToEnrol.unenroledExams}" >
			<h:outputText value="(#{bundle['message.no.exams.to.enroll']})" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty displayEvaluationsToEnrol.unenroledExams}" >
			<h:dataTable value="#{displayEvaluationsToEnrol.unenroledExams}" var="unenroledEvaluation" styleClass="greyBorderClass" cellspacing="0" >
				<h:column>
					<h:dataTable value="#{displayEvaluationsToEnrol.attendingExecutionCourses[unenroledEvaluation.idInternal]}" var="executionCourse" styleClass="boldFontClass">
						<h:column>
							<h:outputText value="#{executionCourse.nome} - " /><h:outputText value="#{executionCourse.sigla}" />
						</h:column>
					</h:dataTable>
					<h:panelGrid columns="4" columnClasses="listClasses" width="100%">
						<h:panelGroup>
							<h:outputText value="#{unenroledEvaluation.season.season} ª" />
							<h:outputText value="#{bundle['label.season']}" />
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.day']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, dd/MM/yy}<br/>" escape="false">
								<f:param value="#{unenroledEvaluation.dayDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.hour']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, HH:mm} ">
								<f:param value="#{unenroledEvaluation.beginningDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.to']} "/>
							<h:outputFormat value="{0, date, HH:mm}">
								<f:param value="#{unenroledEvaluation.endDate}"/>
							</h:outputFormat>						
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.enrolmentPeriod']}<br/>" escape="false" styleClass="boldFontClass" />
							<h:outputText value="#{bundle['label.enrolmentBegin']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{unenroledEvaluation.enrollmentBeginDayDate}"/>
								<f:param value="#{unenroledEvaluation.enrollmentBeginTimeDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.enrolmentEnd']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{unenroledEvaluation.enrollmentEndDayDate}"/>
								<f:param value="#{unenroledEvaluation.enrollmentEndTimeDate}"/>
							</h:outputFormat>					
						</h:panelGroup>
						<h:panelGroup rendered="#{displayEvaluationsToEnrol.renderUnenrolLinks[unenroledEvaluation.idInternal]}">
							<h:commandLink action="" 
								   		   actionListener="#{displayEvaluationsToEnrol.enrolStudent}" >
								<h:outputText value="#{bundle['label.enroll']}" />	
								<f:param id="evaluationID" name="evaluationID" value="#{unenroledEvaluation.idInternal}" />
							</h:commandLink>
						</h:panelGroup>
					</h:panelGrid>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<%-- END --%>
		<%-- ENROLED EXAMS --%>
		<h:outputText value="<h3>#{bundle['label.examsEnrolled']}</h3>" escape="false" />
		<h:panelGrid rendered="#{empty displayEvaluationsToEnrol.enroledExams}" >
			<h:outputText value="(#{bundle['message.no.exams.enroled']})" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty displayEvaluationsToEnrol.enroledExams}" >
			<h:dataTable value="#{displayEvaluationsToEnrol.enroledExams}" var="enroledEvaluation" styleClass="greyBorderClass" cellspacing="0">
				<h:column>
					<h:dataTable value="#{displayEvaluationsToEnrol.attendingExecutionCourses[enroledEvaluation.idInternal]}" var="executionCourse" styleClass="boldFontClass" width="100%">
						<h:column>
							<h:outputText value="#{executionCourse.nome} - " /><h:outputText value="#{executionCourse.sigla}" />
						</h:column>
					</h:dataTable>
					<h:panelGrid columns="5" columnClasses="listClasses" width="100%">
						<h:panelGroup>
							<h:outputText value="#{enroledEvaluation.season.season} ª" />
							<h:outputText value="#{bundle['label.season']}" />
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.day']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, dd/MM/yy}<br/>" escape="false">
								<f:param value="#{enroledEvaluation.dayDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.hour']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, HH:mm} ">
								<f:param value="#{enroledEvaluation.beginningDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.to']} "/>
							<h:outputFormat value="{0, date, HH:mm}">
								<f:param value="#{enroledEvaluation.endDate}"/>
							</h:outputFormat>						
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.enrolmentPeriod']}<br/>" escape="false" styleClass="boldFontClass" />
							<h:outputText value="#{bundle['label.enrolmentBegin']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{enroledEvaluation.enrollmentBeginDayDate}"/>
								<f:param value="#{enroledEvaluation.enrollmentBeginTimeDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.enrolmentEnd']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{enroledEvaluation.enrollmentEndDayDate}"/>
								<f:param value="#{enroledEvaluation.enrollmentEndTimeDate}"/>
							</h:outputFormat>					
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.lesson.room']} " />
							<h:outputText value="#{displayEvaluationsToEnrol.enroledRooms[enroledEvaluation.idInternal]}" />
						</h:panelGroup>
						<h:panelGroup rendered="#{displayEvaluationsToEnrol.renderUnenrolLinks[enroledEvaluation.idInternal]}">
							<h:commandLink action=""
										   actionListener="#{displayEvaluationsToEnrol.unenrolStudent}">
								<h:outputText value="#{bundle['label.unEnroll']}" />	
								<f:param id="evaluationID" name="evaluationID" value="#{enroledEvaluation.idInternal}" />
							</h:commandLink>
						</h:panelGroup>
					</h:panelGrid>
				</h:column>				
			</h:dataTable>
		</h:panelGrid>
		<%-- END --%>		
		<h:outputText value="<h2>#{bundle['label.writtenTests']}</h2>" escape="false" />
		<%-- UNENROLED WRITTEN.TESTS --%>
		<h:outputText value="<h3>#{bundle['label.writtenTestsToEnroll']}</h3>" escape="false" />
		<h:panelGrid rendered="#{empty displayEvaluationsToEnrol.unenroledWrittenTests}" >
			<h:outputText value="(#{bundle['message.no.writtenTests.to.enroll']})" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty displayEvaluationsToEnrol.unenroledWrittenTests}" >
			<h:dataTable value="#{displayEvaluationsToEnrol.unenroledWrittenTests}" var="unenroledEvaluation" styleClass="greyBorderClass" cellspacing="0" >
				<h:column>
					<h:dataTable value="#{displayEvaluationsToEnrol.attendingExecutionCourses[unenroledEvaluation.idInternal]}" var="executionCourse" styleClass="boldFontClass">
						<h:column>
							<h:outputText value="#{executionCourse.nome} - " /><h:outputText value="#{executionCourse.sigla}" />
						</h:column>
					</h:dataTable>
					<h:panelGrid columns="4" columnClasses="listClasses" width="100%">
						<h:outputText value="#{unenroledEvaluation.description}" />
						<h:panelGroup>
							<h:outputText value="#{bundle['label.day']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, dd/MM/yy}<br/>" escape="false">
								<f:param value="#{unenroledEvaluation.dayDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.hour']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, HH:mm} ">
								<f:param value="#{unenroledEvaluation.beginningDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.to']} "/>
							<h:outputFormat value="{0, date, HH:mm}">
								<f:param value="#{unenroledEvaluation.endDate}"/>
							</h:outputFormat>						
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.enrolmentPeriod']}<br/>" escape="false" styleClass="boldFontClass" />
							<h:outputText value="#{bundle['label.enrolmentBegin']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{unenroledEvaluation.enrollmentBeginDayDate}"/>
								<f:param value="#{unenroledEvaluation.enrollmentBeginTimeDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.enrolmentEnd']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{unenroledEvaluation.enrollmentEndDayDate}"/>
								<f:param value="#{unenroledEvaluation.enrollmentEndTimeDate}"/>
							</h:outputFormat>					
						</h:panelGroup>
						<h:panelGroup rendered="#{displayEvaluationsToEnrol.renderUnenrolLinks[unenroledEvaluation.idInternal]}">
							<h:commandLink action="" 
								   		   actionListener="#{displayEvaluationsToEnrol.enrolStudent}" >
								<h:outputText value="#{bundle['label.enroll']}" />	
								<f:param id="evaluationID" name="evaluationID" value="#{unenroledEvaluation.idInternal}" />
							</h:commandLink>
						</h:panelGroup>
					</h:panelGrid>
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<%-- END --%>		
		<%-- ENROLED WRITTEN.TESTS --%>
		<h:outputText value="<h3>#{bundle['label.writtenTestsEnrolled']}</h3>" escape="false" />
		<h:panelGrid rendered="#{empty displayEvaluationsToEnrol.enroledWrittenTests}" >
			<h:outputText value="(#{bundle['message.no.writtenTests.enroled']})" />
		</h:panelGrid>
		<h:panelGrid rendered="#{!empty displayEvaluationsToEnrol.enroledWrittenTests}" >
			<h:dataTable value="#{displayEvaluationsToEnrol.enroledWrittenTests}" var="enroledEvaluation" styleClass="greyBorderClass" cellspacing="0">
				<h:column>
					<h:dataTable value="#{displayEvaluationsToEnrol.attendingExecutionCourses[enroledEvaluation.idInternal]}" var="executionCourse" styleClass="boldFontClass" width="100%">
						<h:column>
							<h:outputText value="#{executionCourse.nome} - " /><h:outputText value="#{executionCourse.sigla}" />
						</h:column>
					</h:dataTable>
					<h:panelGrid columns="5" columnClasses="listClasses" width="100%">
						<h:outputText value="#{enroledEvaluation.description}" />
						<h:panelGroup>
							<h:outputText value="#{bundle['label.day']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, dd/MM/yy}<br/>" escape="false">
								<f:param value="#{enroledEvaluation.dayDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.hour']}: " styleClass="boldFontClass" />
							<h:outputFormat value="{0, date, HH:mm} ">
								<f:param value="#{enroledEvaluation.beginningDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.to']} "/>
							<h:outputFormat value="{0, date, HH:mm}">
								<f:param value="#{enroledEvaluation.endDate}"/>
							</h:outputFormat>						
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.enrolmentPeriod']}<br/>" escape="false" styleClass="boldFontClass" />
							<h:outputText value="#{bundle['label.enrolmentBegin']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{enroledEvaluation.enrollmentBeginDayDate}"/>
								<f:param value="#{enroledEvaluation.enrollmentBeginTimeDate}"/>
							</h:outputFormat>
							<h:outputText value="#{bundle['label.enrolmentEnd']}: "/>
							<h:outputFormat value="{0, date, dd/MM/yy} - {1, date, HH:mm} <br/>" escape="false">
								<f:param value="#{enroledEvaluation.enrollmentEndDayDate}"/>
								<f:param value="#{enroledEvaluation.enrollmentEndTimeDate}"/>
							</h:outputFormat>					
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText value="#{bundle['label.lesson.room']} " />
							<h:outputText value="#{displayEvaluationsToEnrol.enroledRooms[enroledEvaluation.idInternal]}" />
						</h:panelGroup>
						<h:panelGroup rendered="#{displayEvaluationsToEnrol.renderUnenrolLinks[enroledEvaluation.idInternal]}">
							<h:commandLink action=""
										   actionListener="#{displayEvaluationsToEnrol.unenrolStudent}">
								<h:outputText value="#{bundle['label.unEnroll']}" />	
								<f:param id="evaluationID" name="evaluationID" value="#{enroledEvaluation.idInternal}" />
							</h:commandLink>
						</h:panelGroup>
					</h:panelGrid>
				</h:column>				
			</h:dataTable>
		</h:panelGrid>
		<%-- END --%>		
	</h:form>
</ft:tilesView>