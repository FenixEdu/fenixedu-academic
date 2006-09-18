<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>	

	<h:form>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionCourseIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.evaluationIdHidden}" />
		
		<%-- Error Message --%>
		<h:outputText styleClass="error" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorWrittenTestsManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"/>
		
		<h:outputText value="<br/><strong>#{bundle['label.coordinator.executionCourse']}: </strong>" escape="false" />
		<h:outputText value="#{coordinatorWrittenTestsManagementBackingBean.executionCourse.nome}<br/><br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">	
			<h:outputText value="#{bundle['label.coordinator.identification']}: " styleClass="bold" />
			<h:outputText value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.description}" />
			<h:outputText value="#{bundle['label.coordinator.evaluationDate']}: " styleClass="bold" escape="false"/>
			<h:panelGroup>
				<h:outputFormat value="{0, date, dd/MM/yyyy} - ">
					<f:param value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.dayDate}" />
				</h:outputFormat>
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.beginningDate}" />
				</h:outputFormat>
				<h:outputText value=" #{bundle['label.coordinator.to']} " />
				<h:outputFormat value="{0, date, HH:mm}">
					<f:param value="#{coordinatorWrittenTestsManagementBackingBean.evaluation.endDate}" />
				</h:outputFormat>				
			</h:panelGroup>
		</h:panelGrid>
		
		<h:outputText value="<br/>#{bundle['message.confirm.written.test']}<br/><br/>" escape="false" styleClass="error"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" action="#{coordinatorWrittenTestsManagementBackingBean.deleteWrittenTest}"
		   styleClass="inputbutton" value="#{bundle['button.yes']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" action="#{coordinatorWrittenTestsManagementBackingBean.showWrittenTestsForExecutionCourses}"
		   styleClass="inputbutton" value="#{bundle['button.no']}"/>
					
	</h:form>
</ft:tilesView>