<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
		<h:form>
			<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
			<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.executionPeriodIdHidden}"/>
			<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.curricularYearIdHidden}"/>
			<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.executionCourseIdHidden}" />
			<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.evaluationIdHidden}" />
	
			<h:outputFormat value="<h2>#{bundle['link.edit.project']}</h2/><hr/>" escape="false"/>
			
			<%-- ERROR MESSAGE --%>
			<h:outputText styleClass="error" rendered="#{!empty coordinatorProjectsManagementBackingBean.errorMessage}"
				value="#{bundle[coordinatorProjectsManagementBackingBean.errorMessage]}<br/>" escape="false" />
			
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.name']}: " />
				</h:panelGroup>	
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" maxlength="100" size="20" value="#{coordinatorProjectsManagementBackingBean.name}" />			
					<h:message for="name" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>				
					<h:outputText value="#{bundle['label.publish.date']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.beginDate']}" id="beginDate" required="true" maxlength="10" size="10" value="#{coordinatorProjectsManagementBackingBean.beginDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false"/>
					<h:message for="beginDate" styleClass="error"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" id="beginHour" required="true" maxlength="5" size="5" value="#{coordinatorProjectsManagementBackingBean.beginHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="beginHour" styleClass="error"/>					
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>					
					<h:outputText value="#{bundle['label.delivery.date']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.endDate']}" id="endDate" required="true" maxlength="10" size="10" value="#{coordinatorProjectsManagementBackingBean.endDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false" />
					<h:message for="endDate" styleClass="error"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" id="endHour" required="true" maxlength="5" size="5" value="#{coordinatorProjectsManagementBackingBean.endHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />					
					<h:message for="endHour" styleClass="error"/>
				</h:panelGroup>				
				<h:outputText value="#{bundle['label.description']}: " />
				<h:inputTextarea rows="4" cols="40" value="#{coordinatorProjectsManagementBackingBean.description}" />
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.neededFields']}"/>
				</h:panelGroup>
			</h:panelGrid>			
			<h:outputText value="<br/>" escape="false" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" action="#{coordinatorProjectsManagementBackingBean.editProject}"
				styleClass="inputbutton" value="#{bundle['button.submit']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{coordinatorProjectsManagementBackingBean.showProjectsForExecutionCourses}"
				styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
</ft:tilesView>