<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	
	<h:form>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.curricularYearIdHidden}"/>
		
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.yearHidden}"/>
		
		<h:inputHidden binding="#{coordinatorEvaluationManagementBackingBean.evaluationTypeHidden}" />
		
		<h:outputText value="<h2>#{bundle['title.choose.discipline']}</h2><br/>" escape="false" />
		<h:selectOneMenu value="#{coordinatorEvaluationManagementBackingBean.executionCourseID}" >
			<f:selectItems value="#{coordinatorEvaluationManagementBackingBean.executionCoursesLabels}" />
		</h:selectOneMenu>
		<h:outputText value="<br/><br/>" escape="false" />		
		<h:commandButton alt="#{htmlAltBundle['commandButton.choose']}" action="#{coordinatorEvaluationManagementBackingBean.selectExecutionCourse}"
		  value="#{bundle['label.choose']}" styleClass="inputbutton"/>
		
	</h:form>
</ft:tilesView>