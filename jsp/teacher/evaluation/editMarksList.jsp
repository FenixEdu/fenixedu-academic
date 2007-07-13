<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputFormat value="<h2>#{bundle['title.evaluation.loadMarks']}</h2>" escape="false"/>
	
	<h:outputFormat value="<h3>#{bundle['title.evaluation.manage.marksList']}</h3>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:messages layout="table" errorClass="error"/>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>


			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.onlineTests.OnlineTest'}">
				<h:outputText value="<p>" escape="false"/>
					<h:outputText value="#{bundle['lable.test']}: " escape="false"/>
					<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.distributedTest.title}</b>" escape="false"/>
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginDateDate}"/>
					</h:outputFormat>
					<h:outputText value=" #{bundle['label.at']}" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginHourDate}"/>
					</h:outputFormat>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
				<h:outputText value="<p>" escape="false"/>
					<h:outputText value="#{bundle['label.written.test']}: " escape="false"/>
					<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.description}</b> " escape="false"/>
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
					</h:outputFormat>
					<h:outputText value=" #{bundle['label.at']}" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
					</h:outputFormat>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>

			<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
				<h:outputText value="<p>" escape="false"/>
					<h:outputText value="#{bundle['label.exam']}: " escape="false"/>
					<h:outputText value="<b>#{evaluationManagementBackingBean.evaluation.season}</b> " escape="false"/>
					<h:outputFormat value="{0, date, dd/MM/yyyy}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
					</h:outputFormat>
					<h:outputText value=" #{bundle['label.at']}" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
					</h:outputFormat>
				<h:outputText value="</p>" escape="false"/>
			</h:panelGroup>


			<h:outputText value="<div class='infoop2'>" escape="false"/>
				<h:outputText value="<p>" escape="false"/>
					<h:outputText value="#{bundle['label.marksOnline.instructions']}" escape="false"/>
				<h:outputText value="</p>" escape="false"/>

				<h:outputText value="<p>" escape="false"/>
					<h:commandLink action="enterLoadMarks">
						<f:param name="evaluationIDHidden" value="#{evaluationManagementBackingBean.evaluation.idInternal}" />
						<f:param name="executionCourseIDHidden" value="#{evaluationManagementBackingBean.executionCourse.idInternal}" />
						<f:param name="evaluationID" value="#{evaluationManagementBackingBean.evaluation.idInternal}" />
						<f:param name="executionCourseID" value="#{evaluationManagementBackingBean.executionCourse.idInternal}" />
						<h:outputFormat value="#{bundle['label.load.marks']}" />
					</h:commandLink>
				<h:outputText value="</p>" escape="false"/>
			<h:outputText value="</div>" escape="false"/>


			<h:outputText value="<table class='tstyle4'>" escape="false" />
				<h:outputText value="<tr>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.number']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.name']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.enrolmentEvaluationType']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.mark']}"/>
					<h:outputText value="</th>" escape="false" />
				<h:outputText value="</tr>" escape="false" />


				<fc:dataRepeater value="#{evaluationManagementBackingBean.executionCourseAttends}" var="attends">
				
					<h:outputText value="<tr>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{attends.registration.number}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{attends.registration.person.name}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText rendered="#{attends.enrolment == null}" value="#{bundle['message.notEnroled']}"/>
							<h:outputText rendered="#{attends.enrolment != null}" value="#{attends.enrolment.enrolmentEvaluationType}"/>
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:inputText alt="#{htmlAltBundle['inputText.number]']}" size="3" maxlength="4" value="#{evaluationManagementBackingBean.marks[attends.aluno.number]}"/>
						<h:outputText value="</td>" escape="false" />
					<h:outputText value="</tr>" escape="false" />
					
				</fc:dataRepeater>
			
			<h:outputText value="</table>" escape="false" />

		<h:outputText value="<p>" escape="false" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.save']}" action="#{evaluationManagementBackingBean.editMarks}" value="#{bundle['button.save']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['button.cancel']}"/>
		<h:outputText value="</p>" escape="false" />
	</h:form>

</ft:tilesView>
