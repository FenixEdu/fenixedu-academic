<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksList']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>

		<h:panelGrid width="100%" columns="1" cellspacing="8" cellpadding="8">
			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
				<h:outputText value="#{bundle['label.exam']}: #{evaluationManagementBackingBean.evaluation.season}"/>
				<h:outputFormat value="{0, date, yyyy/MM/dd}" >
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, time, HH:mm}" >
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" - "/>
				<h:outputFormat value="{0, time, HH:mm}" >
					<f:param value="#{evaluationManagementBackingBean.evaluation.endDate}" />
				</h:outputFormat>
			</h:panelGroup>

			<h:outputText value="#{bundle['label.marksOnline.instructions']}" escape="false"/>

			<h:commandLink action="enterLoadMarks">
				<f:param name="evaluationIDHidden" value="#{evaluationManagementBackingBean.evaluation.idInternal}" />
				<f:param name="executionCourseIDHidden" value="#{evaluationManagementBackingBean.executionCourse.idInternal}" />
				<f:param name="evaluationID" value="#{evaluationManagementBackingBean.evaluation.idInternal}" />
				<f:param name="executionCourseID" value="#{evaluationManagementBackingBean.executionCourse.idInternal}" />
				<h:outputFormat value="#{bundle['label.load.marks']}" />
			</h:commandLink>

			<h:dataTable value="#{evaluationManagementBackingBean.executionCourse.attends}" var="attends">
				<h:column>
					<h:outputText value="#{attends.aluno.number}" />
				</h:column>
				<h:column>
					<h:outputText value="#{attends.aluno.person.nome}" />
				</h:column>
				<h:column>
					<h:outputText rendered="#{attends.enrolment == null}" value="#{bundle['message.notEnroled']}"/>
					<h:outputText rendered="#{attends.enrolment != null}" value="#{attends.enrolment.enrolmentEvaluationType}"/>
				</h:column>
				<h:column>
					<h:inputText value="#{evaluationManagementBackingBean.marks[attends.idInternal]}"/>
				</h:column>
			</h:dataTable>

			<h:commandButton action="#{evaluationManagementBackingBean.editMarks}" value="#{bundle['button.save']}"/>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
