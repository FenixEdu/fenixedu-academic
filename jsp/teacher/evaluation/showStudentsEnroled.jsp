<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<style>
.tableClass {
	background: #EBECED;
	border: 1px solid #ccc;
	text-align: left;
	height: 30px;
}
.boldFontClass { 
	font-weight: bold
}
</style>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	<h:form>
	
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
		
		<h:outputText value="<h2>#{bundle['label.students.enrolled']}</h2>" escape="false" />
		<h:panelGrid columns="1" styleClass="tableClass">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.exam']}: " styleClass="boldFontClass"/>
				<h:outputText value="#{evaluationManagementBackingBean.evaluationDescription}"/>
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
		<h:panelGrid rendered="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolments}">
			<h:dataTable value="#{evaluationManagementBackingBean.evaluation.writtenEvaluationEnrolments}" var="writtenEvaluationEnrolment" headerClass="listClasses-header" columnClasses="listClasses">
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
		<h:commandButton styleClass="inputButton" action="back" value="#{bundle['link.goBack']}"></h:commandButton>
	</h:form>
</ft:tilesView>