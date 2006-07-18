<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>
	<h:outputText value="<strong>#{bundle['label.coordinator.executionCourse']}: #{coordinatorWrittenTestsManagementBackingBean.executionCourse.nome}</strong><br/><br/>" escape="false"/>
	
	<h:form>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionCourseIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorWrittenTestsManagementBackingBean.errorMessage]}"/>
		
		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:outputText value="#{bundle['label.date']}: " style="font-weight: bold" escape="false"/>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.day']}" id="day" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.day}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:message for="day" errorClass="error" />
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.month']}" id="month" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.month}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:message for="month" errorClass="error" />
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.year']}" id="year" required="true" maxlength="4" size="4" value="#{coordinatorWrittenTestsManagementBackingBean.year}"/>				
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
				<h:message for="year" errorClass="error" />
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" id="beginHour" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.beginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:message for="beginHour" errorClass="error" />
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" id="beginMinute" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.beginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:message for="beginMinute" errorClass="error" />
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" id="endHour" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.endHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:message for="endHour" errorClass="error" />
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" id="endMinute" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.endMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:message for="endMinute" errorClass="error" />
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.name']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.description']}" id="description" required="true" maxlength="120" size="15" value="#{coordinatorWrittenTestsManagementBackingBean.description}"/>
				<h:message for="description" errorClass="error" />
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{coordinatorWrittenTestsManagementBackingBean.createWrittenTest}" styleClass="inputbutton" value="#{bundle['button.create']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{coordinatorWrittenTestsManagementBackingBean.showWrittenTestsForExecutionCourses}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
	
</ft:tilesView>