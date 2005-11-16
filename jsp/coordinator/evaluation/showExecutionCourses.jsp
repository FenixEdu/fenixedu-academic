<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.curricularYearIdHidden}"/>
		
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.yearHidden}"/>
		
		<h:outputText value="<h2>#{bundle['title.choose.discipline']}</h2><br/>" escape="false" />
		<h:selectOneMenu value="#{coordinatorWrittenTestsManagementBackingBean.executionCourseID}" >
			<f:selectItems value="#{coordinatorWrittenTestsManagementBackingBean.executionCoursesLabels}" />
		</h:selectOneMenu>
		<h:outputText value="<br/><br/>" escape="false" />
		<h:commandButton action="#{coordinatorWrittenTestsManagementBackingBean.selectExecutionCourse}" value="#{bundle['label.choose']}" styleClass="inputbutton"/>
		
	</h:form>
</ft:tilesView>