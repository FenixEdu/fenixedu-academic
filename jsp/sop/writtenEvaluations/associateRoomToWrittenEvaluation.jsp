<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<h2>#{bundleSOP['written.evaluation.associate.rooms']}</h2><br/>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
 		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}"/>

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.originPage}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedBegin}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedEnd}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginMinuteHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endMinuteHidden}"/>
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText escape="false" value="<input alt='input.year' id='year' name='year' type='hidden' value='#{SOPEvaluationManagementBackingBean.year}'/>"/>
		<h:outputText escape="false" value="<input alt='input.month' id='month' name='month' type='hidden' value='#{SOPEvaluationManagementBackingBean.month}'/>"/>
		<h:outputText escape="false" value="<input alt='input.day' id='day' name='day' type='hidden' value='#{SOPEvaluationManagementBackingBean.day}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginHour' id='beginHour' name='beginHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginMinute' id='beginMinute' name='beginMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginMinute}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endHour' id='endHour' name='endHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.endHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endMinute' id='endMinute' name='endMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.endMinute}'/>"/>
	
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
			<h:selectOneRadio onchange="this.form.submit();" value="#{SOPEvaluationManagementBackingBean.orderCriteria}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.orderByCriteriaItems}" />
			</h:selectOneRadio>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.chosenRoomsIDs}" layout="pageDirection" >
			<f:selectItems value="#{SOPEvaluationManagementBackingBean.roomsSelectItems}" />	
		</h:selectManyCheckbox>

		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.choose']}" action="#{SOPEvaluationManagementBackingBean.associateRoomToWrittenEvaluation}" styleClass="inputbutton" value="#{bundleSOP['button.choose']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{SOPEvaluationManagementBackingBean.returnToCreateOrEdit}" styleClass="inputbutton" value="#{bundleSOP['button.cancel']}"/>
	</h:form>
</ft:tilesView>
