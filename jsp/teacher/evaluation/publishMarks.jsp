<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.publishMarks']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
		value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>

	<h:panelGrid width="100%" columns="1">
		<h:panelGroup>
			<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
			<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
			<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}, " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
			<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}, " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
			<h:outputText value="#{bundle['label.day']}" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
			</h:outputFormat>
		</h:panelGroup>
		<h:outputText value="<br/>" escape="false"/>

		<h:panelGrid styleClass="infoop" width="100%" columns="1">
			<h:outputText value="#{bundle['label.publish.information']}" escape="false"/>
		</h:panelGrid>
		
	</h:panelGrid>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:outputText value="<br/>#{bundle['message.publishmentMessage']}: <br/>" escape="false"/>
		<h:inputTextarea cols="45" value="#{evaluationManagementBackingBean.publishMarksMessage}"/>
		<h:outputText value="<br/><br/>#{bundle['message.sendSMS']}: <br/>" escape="false"/>
		<h:selectBooleanCheckbox disabled="true" value="#{evaluationManagementBackingBean.sendSMS}"/>
		<h:outputText styleClass="error" value="#{bundle['message.sms.unavailable']}"/>
		<h:outputText value="<br/><br/>" escape="false"/>

		<h:commandButton styleClass="inputbutton" action="#{evaluationManagementBackingBean.publishMarks}" value="#{bundle['button.insert']}"/>
		<h:commandButton immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
	</h:form>

</ft:tilesView>
