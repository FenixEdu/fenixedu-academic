<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResourcesSOP" var="bundleSOP"/>

	<h:outputText value="<h2>#{bundleSOP['title.choose.discipline']}</h2><br/>" escape="false" />
	
	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />

		<h:panelGrid columns="1" styleClass="infoselected">
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: <b>#{SOPEvaluationManagementBackingBean.executionPeriodLabel}</b>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.degree']}: <b>#{SOPEvaluationManagementBackingBean.executionDegreeLabel}</b>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: <b>#{SOPEvaluationManagementBackingBean.curricularYear}</b>" escape="false"/>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
		<h:outputText value="<br/>" escape="false"/>
		
		<h:outputText value="<br/>#{bundleSOP['label.choose.written.evaluation.type']}:<br/>" escape="false"/>
		<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}" >
			<f:selectItems value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassnameLabels}" />
		</h:selectOneMenu>

		<h:outputText value="<br/><br/>#{bundleSOP['label.choose.execution.course']}:<br/>" escape="false"/>
		<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionCourseID}" >
			<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesLabels}" />
		</h:selectOneMenu>

		<h:outputText value="<br/><br/>" escape="false" />
		<h:commandButton action="#{SOPEvaluationManagementBackingBean.continueToCreateWrittenEvaluation}" value="#{bundleSOP['button.continue']}" styleClass="inputbutton"/>
		<h:commandButton action="writtenEvaluationCalendar" value="#{bundleSOP['label.back']}" styleClass="inputbutton"/>
	</h:form>

</ft:tilesView>