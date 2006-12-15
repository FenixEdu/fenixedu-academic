<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>


	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<h:form>
	
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['label.students.enrolled']}</h2>" escape="false" />
		<h:panelGrid columns="1" styleClass="tableClass">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.exam']}: " styleClass="boldFontClass" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
				<h:outputText value="#{bundle['label.written.test']}: " styleClass="boldFontClass" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.day']}: " styleClass="boldFontClass"/>
				<h:outputFormat value="{0, date, dd/MM/yy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']}: " styleClass="boldFontClass"/>
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.number.students.enrolled']}: " styleClass="boldFontClass"/>	
				<h:outputText value="#{evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolmentsCount}" />
			</h:panelGroup>						
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />		
		<h:panelGrid rendered="#{!empty evaluationManagementBackingBean.writtenEvaluationEnrolments}">
			<h:dataTable value="#{evaluationManagementBackingBean.writtenEvaluationEnrolments}" var="writtenEvaluationEnrolment" headerClass="listClasses-header" columnClasses="listClasses">
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.number']}"/></f:facet>
					<h:outputText value="#{writtenEvaluationEnrolment.student.number}" />
				</h:column>			
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.name']}"/></f:facet>
					<h:outputText value="#{writtenEvaluationEnrolment.student.person.nome}" />
				</h:column>
				<h:column>
					<f:facet name="header"><h:outputText value="#{bundle['label.room']}"/></f:facet>
					<h:outputText value="#{(!empty writtenEvaluationEnrolment.room) ? writtenEvaluationEnrolment.room.nome : \" \"}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.goBack']}" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['link.goBack']}" styleClass="inputButton"  />
	</h:form>
</ft:tilesView>