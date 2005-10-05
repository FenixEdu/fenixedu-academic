<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.marksList']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:form enctype="multipart/form-data">
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

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

			<h:outputText value="#{bundle['label.fileUpload.information']}" escape="false"/>

			<h:outputText value="<input type=\"file\" name=\"theFile\"/>" escape="false"/>

			<h:commandButton action="#{evaluationManagementBackingBean.loadMarks}" value="#{bundle['button.load']}"/>
		</h:panelGrid>
	</h:form>

</ft:tilesView>
