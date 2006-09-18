<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
		<h:form>
			<h:inputHidden binding="#{projectManagementBackingBean.executionCourseIdHidden}" />
	
			<h:outputFormat value="<h2>#{bundle['link.edit.project']}</h2/><hr/>" escape="false"/>
			<%-- ERROR MESSAGE --%>
			<h:outputText styleClass="error" rendered="#{!empty projectManagementBackingBean.errorMessage}"
				value="#{bundle[projectManagementBackingBean.errorMessage]}<br/>" escape="false" />
			
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>
					<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.name']}: " />
				</h:panelGroup>	
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.name']}" id="name" required="true" maxlength="100" size="20" value="#{projectManagementBackingBean.name}" />			
					<h:message for="name" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>				
					<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.projectBeginDateTime']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.beginProjectDate']}" id="beginDate" required="true" maxlength="10" size="10" value="#{projectManagementBackingBean.beginProjectDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false"/>
					<h:message for="beginDate" styleClass="error"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginProjectHour']}" id="beginHour" required="true" maxlength="5" size="5" value="#{projectManagementBackingBean.beginProjectHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="beginHour" styleClass="error"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="* " style="color: red"/>					
					<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.projectEndDateTime']}: " />
				</h:panelGroup>
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.endProjectDate']}" id="endDate" required="true" maxlength="10" size="10" value="#{projectManagementBackingBean.endProjectDate}" />
					<h:outputText value=" #{bundle['label.date.instructions.small']} &nbsp&nbsp" escape="false" />
					<h:message for="endDate" styleClass="error"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endProjectHour']}" id="endHour" required="true" maxlength="5" size="5" value="#{projectManagementBackingBean.endProjectHour}" />
					<h:outputText value=" #{bundle['label.hour.instructions']}" />
					<h:message for="endHour" styleClass="error"/>
				</h:panelGroup>				
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.description']}: " />
				<h:inputTextarea rows="4" cols="40" value="#{projectManagementBackingBean.description}" />
				
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.onlineSubmissionsAllowed']}" />
				<h:panelGroup>
					<h:selectBooleanCheckbox id="onlineSubmissionsAllowed" required="true" value="#{projectManagementBackingBean.onlineSubmissionsAllowed}" />
					<h:message for="onlineSubmissionsAllowed" styleClass="error"/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.maxSubmissionsToKeep']}" />				
				<h:panelGroup>
					<h:inputText alt="#{htmlAltBundle['inputText.maxSubmissionsToKeep']}" id="maxSubmissionsToKeep" required="false" value="#{projectManagementBackingBean.maxSubmissionsToKeep}" maxlength="2" size="2"/>
					<h:message for="maxSubmissionsToKeep" styleClass="error"/>
				</h:panelGroup>
				
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.grouping.name']}" />
				<h:panelGroup>
					<h:selectOneMenu id="groupingID" value="#{projectManagementBackingBean.groupingID}">
						<f:selectItems value="#{projectManagementBackingBean.executionCourseGroupings}"/>
					</h:selectOneMenu>
					<h:message for="groupingID" styleClass="error"/>
				</h:panelGroup>			
				
				<h:panelGroup>
					<h:outputText value="<br/><br/>* " style="color: red" escape="false"/>
					<h:outputText value="#{bundle['label.neededFields']}"/>
				</h:panelGroup>
			</h:panelGrid>			
			<h:outputText value="<br/>" escape="false" />			
			<h:inputHidden id="projectID" value="#{projectManagementBackingBean.projectID}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}"  action="#{projectManagementBackingBean.editProject}"
				styleClass="inputbutton" value="#{bundle['button.submit']}" />
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="projectsIndex"
				styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
</ft:tilesView>