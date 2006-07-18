<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

<style>
.boldFontClass { font-weight: bold; }
</style>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>	

	<h:form>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.executionCourseIdHidden}"/>
		<h:inputHidden binding="#{coordinatorProjectsManagementBackingBean.evaluationIdHidden}" />
		
		<%-- Error Message --%>
		<h:outputText styleClass="error" rendered="#{!empty coordinatorProjectsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorProjectsManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty coordinatorProjectsManagementBackingBean.errorMessage}"/>
		
		<h:outputText value="<br/><strong>#{bundle['label.coordinator.executionCourse']}: </strong>" escape="false" />
		<h:outputText value="#{coordinatorProjectsManagementBackingBean.executionCourse.nome}<br/><br/>" escape="false"/>
		
		<h:panelGrid columns="2" styleClass="infoop">	
			<h:outputText value="#{bundle['label.coordinator.identification']}: " styleClass="boldFontClass" />
			<h:outputText value="#{coordinatorProjectsManagementBackingBean.evaluation.name}" />
			<h:outputText value="#{bundle['label.beginDate']}: " styleClass="boldFontClass" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{coordinatorProjectsManagementBackingBean.evaluation.begin}" />
			</h:outputFormat>			
			<h:outputText value="#{bundle['label.endDate']}: " styleClass="boldFontClass" />
			<h:outputFormat value="{0, date, dd/MM/yyyy}">
				<f:param value="#{coordinatorProjectsManagementBackingBean.evaluation.end}" />
			</h:outputFormat>
		</h:panelGrid>
		
		<h:outputText value="<br/>#{bundle['message.confirm.evaluation']}<br/><br/>" escape="false" styleClass="error"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.yes']}" action="#{coordinatorProjectsManagementBackingBean.deleteProject}"
		   styleClass="inputbutton" value="#{bundle['button.yes']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.no']}" immediate="true" action="#{coordinatorProjectsManagementBackingBean.showProjectsForExecutionCourses}"
		   styleClass="inputbutton" value="#{bundle['button.no']}"/>
					
	</h:form>
</ft:tilesView>