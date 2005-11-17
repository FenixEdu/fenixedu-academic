<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputText value="<h2>#{bundleSOP['written.evaluation.associate.rooms']}</h2><br/>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
 		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}"/>

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.originPage}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginMinuteHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endMinuteHidden}"/>
		<h:outputText escape="false" value="<input id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'"/>
	
		<h:panelGrid styleClass="infotable">
			<h:outputText value="#{bundleSOP['property.aula.disciplina']}: <b>#{SOPEvaluationManagementBackingBean.executionCourse.nome}</b>" escape="false"/>
			<h:outputText value="#{bundleSOP['label.day']}: #{SOPEvaluationManagementBackingBean.day}/#{SOPEvaluationManagementBackingBean.month}/#{SOPEvaluationManagementBackingBean.year}" escape="false"/>
			<h:outputText value="#{bundleSOP['label.hours']}: #{SOPEvaluationManagementBackingBean.beginHour}:#{SOPEvaluationManagementBackingBean.beginMinute} #{bundleSOP['label.at']} #{SOPEvaluationManagementBackingBean.endHour}:#{SOPEvaluationManagementBackingBean.endMinute}" escape="false"/>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

		<h:panelGrid columns="2">
			<h:outputText value="#{bundleSOP['written.evaluation.order.rooms.by']}: " escape="false"/>
			<h:selectOneRadio onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.resetRoomsSelectItems}" value="#{SOPEvaluationManagementBackingBean.orderCriteria}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.orderByCriteriaItems}" />
			</h:selectOneRadio>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.chosenRoomsIDs}" layout="pageDirection" >
			<f:selectItems value="#{SOPEvaluationManagementBackingBean.roomsSelectItems}" />	
		</h:selectManyCheckbox>

		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton action="#{SOPEvaluationManagementBackingBean.associateRoomToWrittenEvaluation}" styleClass="inputbutton" value="#{bundleSOP['button.choose']}"/>
		<h:commandButton immediate="true" action="#{SOPEvaluationManagementBackingBean.returnToCreateOrEdit}" styleClass="inputbutton" value="#{bundleSOP['button.cancel']}"/>
	</h:form>
</ft:tilesView>
