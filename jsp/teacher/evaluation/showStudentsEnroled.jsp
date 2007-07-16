<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>


	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:form>
	
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
		
		<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
		<h:outputText value="<h2>#{bundle['label.students.enrolled']}</h2>" escape="false" />

		<h:outputText value="<table class='tstyle2 thlight thright'>" escape="false" />
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.exam']}: " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
					<h:outputText value="#{bundle['label.written.test']}: " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputText value="<strong>" escape="false" />
						<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
						<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
					<h:outputText value="</strong>" escape="false" />
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.day']}: "/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputFormat value="{0, date, dd/MM/yy}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
					</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.beginning']}: "/>
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputFormat value="{0, date, HH:mm}">
						<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
					</h:outputFormat>
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
			<h:outputText value="<tr>" escape="false" />
				<h:outputText value="<th>" escape="false" />
					<h:outputText value="#{bundle['label.number.students.enrolled']}: "/>	
				<h:outputText value="</th>" escape="false" />
				<h:outputText value="<td>" escape="false" />
					<h:outputText value="#{evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolmentsCount}" />
				<h:outputText value="</td>" escape="false" />
			<h:outputText value="</tr>" escape="false" />
			
		<h:outputText value="</table>" escape="false" />
		
		
		<h:panelGroup rendered="#{!empty evaluationManagementBackingBean.writtenEvaluationEnrolments}">

			<h:outputText value="<table class='tstyle4'>" escape="false" />
				<h:outputText value="<tr>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.number']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.name']}"/>
					<h:outputText value="</th>" escape="false" />
					<h:outputText value="<th>" escape="false" />
						<h:outputText value="#{bundle['label.room']}"/>
					<h:outputText value="</th>" escape="false" />
				<h:outputText value="</tr>" escape="false" />
						
				<fc:dataRepeater value="#{evaluationManagementBackingBean.writtenEvaluationEnrolments}" var="writtenEvaluationEnrolment">
					<h:outputText value="<tr>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{writtenEvaluationEnrolment.student.number}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{writtenEvaluationEnrolment.student.person.name}" />
						<h:outputText value="</td>" escape="false" />
						<h:outputText value="<td>" escape="false" />
							<h:outputText value="#{(!empty writtenEvaluationEnrolment.room) ? writtenEvaluationEnrolment.room.nome : \" \"}" />
						<h:outputText value="</td>" escape="false" />
					<h:outputText value="</tr>" escape="false" />
				</fc:dataRepeater>
				
			<h:outputText value="</table>" escape="false" />
			
		</h:panelGroup>

		<h:outputText value="<p>" escape="false" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.goBack']}" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['link.goBack']}" styleClass="inputButton"  />
		<h:outputText value="</p>" escape="false" />
	</h:form>
</ft:tilesView>