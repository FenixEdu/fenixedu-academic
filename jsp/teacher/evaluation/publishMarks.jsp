<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.publishMarks']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:outputText styleClass="info" value="#{bundle[label.publish.information]}"/>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
		<h:outputText value="<br />" escape="false"/>
		<h:outputText value="#{bundle[message.publishmentMessage]}: "/>
		<h:inputText value="#{evaluationManagementBackingBean.publishMarksMessage}"/>
		<h:outputText value="<br />" escape="false"/>
		<h:selectBooleanCheckbox title="#{bundle[message.sendSMS]}" value="#{evaluationManagementBackingBean.sendSMS}"/>
		<h:outputText styleClass="error" value="#{bundle[message.sms.unavailable]}"/>
		<h:outputText value="<br />" escape="false"/>

		<h:commandButton action="#{evaluationManagementBackingBean.publishMarks}" value="#{bundle['label.publish.marks']}"/>
	</h:form>
</ft:tilesView>
