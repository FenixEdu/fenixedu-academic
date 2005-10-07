<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.enrollment.period']}</h2><br/>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.evaluationType}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText styleClass="infoop" value="#{bundle['label.period.information']}" escape="false"/>
	
		<h:outputText value="<br/><br/><br/><b>#{evaluationManagementBackingBean.evaluationType}:</b> #{evaluationManagementBackingBean.evaluationDescription} - " escape="false"/>
		<h:outputFormat value="{0, date, dd/MM/yyyy}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
		</h:outputFormat>
		<h:outputText value=" #{bundle['label.at']} " escape="false"/>
		<h:outputFormat value="{0, date, HH:mm}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
		</h:outputFormat>
		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>

		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.exam.enrollment.begin.day']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginDay}"/>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMonth}"/>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentBeginYear}"/>
				<h:outputText value=" #{bundle['label.at']} " escape="false"/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginHour}"/>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMinute}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.exam.enrollment.end.day']} " escape="false"/>				
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndDay}"/>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMonth}"/>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentEndYear}"/>
				<h:outputText value=" #{bundle['label.at']} " escape="false"/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndHour}"/>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMinute}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton action="#{evaluationManagementBackingBean.editEvaluationEnrolmentPeriod}" styleClass="inputbutton" value="#{bundle['button.define']}"/>
		<h:commandButton immediate="true" action="#{evaluationManagementBackingBean.redirectToCorrectIndex}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
	</h:form>
</ft:tilesView>
