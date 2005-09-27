<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="definition.student.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/StudentResources" var="bundle"/>
	<h:form>
		<!-- UNENROLED EXAMS -->
		<h:outputText value="<h2>#{bundle['label.examsToEnroll']}</h2>" escape="false" />
		<h:dataTable value="#{displayEvaluationsToEnrol.unenroledExams}" var="unenroledEvaluation" columnClasses="listClasses" headerClass="listClasses-header">
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.course']}"/></f:facet>
				<h:dataTable value="#{unenroledEvaluation.associatedExecutionCourses}" var="executionCourse">
					<h:column>
						<h:outputText value="#{executionCourse.nome}-" /><h:outputText value="#{executionCourse.sigla}" />
					</h:column>
				</h:dataTable>			
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.season']}" /></f:facet>
				<h:outputText value="#{unenroledEvaluation.season.season}" />
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.day']}" /></f:facet>
				<h:outputFormat value="{0, date, MM/dd/yy}">
					<f:param value="#{unenroledEvaluation.dayDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.day']}" /></f:facet>
				<h:outputFormat value="{0, date, hh:mm}">
					<f:param value="#{unenroledEvaluation.beginningDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.enroll']}" /></f:facet>
				<h:commandLink action="success" 
							   actionListener="#{displayEvaluationsToEnrol.enrolStudent}" >
					<h:outputText value="#{bundle['label.enroll']}" />	
					<f:param id="evaluationID" name="evaluationID" value="#{unenroledEvaluation.idInternal}" />
				</h:commandLink>
			</h:column>
		</h:dataTable>

	
	<!-- TODO: ENROLED AN UNENROLED WRITTEN.TESTS -->	
	
	<!-- ENROLED EXAMS -->
		<h:outputText value="<br><h2>#{bundle['label.examsEnrolled']}</h2>" escape="false" />
		<h:dataTable value="#{displayEvaluationsToEnrol.enroledExams}" var="enroledEvaluation" columnClasses="listClasses" headerClass="listClasses-header">
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.course']}"/></f:facet>
				<h:dataTable value="#{enroledEvaluation.associatedExecutionCourses}" var="executionCourse">
					<h:column>
						<h:outputText value="#{executionCourse.nome}-" /><h:outputText value="#{executionCourse.sigla}" />
					</h:column>
				</h:dataTable>			
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.season']}" /></f:facet>
				<h:outputText value="#{enroledEvaluation.season.season}" />
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.day']}" /></f:facet>
				<h:outputFormat value="{0, date, MM/dd/yy}">
					<f:param value="#{enroledEvaluation.dayDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<f:facet name="header"><h:outputText value="#{bundle['label.day']}" /></f:facet>
				<h:outputFormat value="{0, date, hh:mm}">
					<f:param value="#{enroledEvaluation.beginningDate}"/>
				</h:outputFormat>
			</h:column>
			<h:column>
				<h:commandLink  action="success"
								actionListener="#{displayEvaluationsToEnrol.unenrolStudent}">
					<h:outputText value="#{bundle['label.unEnroll']}" />	
					<f:param id="evaluationID" name="evaluationID" value="#{enroledEvaluation.idInternal}" />
				</h:commandLink>
			</h:column>
		</h:dataTable>
	</h:form>
</ft:tilesView>