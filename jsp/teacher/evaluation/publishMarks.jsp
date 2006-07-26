<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.manage.publishMarks']}</h2>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.executionCourse.nome}" />
	</h:outputFormat>

	<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
		value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>

	<h:panelGrid width="100%" columns="1">
		<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.onlineTests.OnlineTest'}">
			<h:outputText value="<b>#{bundle['lable.test']}:</b> " escape="false"/>
			<h:outputText value="#{evaluationManagementBackingBean.evaluation.distributedTest.title}, "/>
			<h:outputText value="#{bundle['label.day']}" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginDateDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.distributedTest.beginHourDate}"/>
			</h:outputFormat>
		</h:panelGroup>

		<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
			<h:outputText value="<b>#{bundle['label.written.test']}:</b> " escape="false"/>
			<h:outputText value="#{evaluationManagementBackingBean.evaluation.description}, "/>
			<h:outputText value="#{bundle['label.day']}" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
			</h:outputFormat>
			<h:outputText value=" #{bundle['label.at']}" />
			<h:outputFormat value="{0, date, HH:mm}">
				<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
			</h:outputFormat>
		</h:panelGroup>

		<h:panelGroup rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}">
			<h:outputText value="<b>#{bundle['label.exam']}:</b> " escape="false"/>
			<h:outputText value="#{evaluationManagementBackingBean.evaluation.season}, "/>
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

		<h:outputText value="<br/>#{bundle['message.publishmentMessage']}" escape="false"/>
		<h:outputText value="(" /><h:outputText value="#{bundle['message.optional']}"/><h:outputText value="): <br/>" escape="false"/>
		<h:inputTextarea cols="45" value="#{evaluationManagementBackingBean.publishMarksMessage}"/>
		<h:outputText value="<br/><br/>#{bundle['message.sendSMS']}: <br/>" escape="false"/>
		<h:selectBooleanCheckbox disabled="true" value="#{evaluationManagementBackingBean.sendSMS}"/>
		<h:outputText styleClass="error" value="#{bundle['message.sms.unavailable']}"/>
		<h:outputText value="<br/><br/>" escape="false"/>

		<h:commandButton alt="#{htmlAltBundle['commandButton.insert']}" styleClass="inputbutton" action="#{evaluationManagementBackingBean.publishMarks}" value="#{bundle['button.post']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
	</h:form>

</ft:tilesView>
