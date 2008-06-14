<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:form>
		<h:outputText escape="false" value="<input alt='input.degreeCurricularPlanID' id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionPeriodID' id='executionPeriodID' name='executionPeriodID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionPeriodID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearID' id='curricularYearID' name='curricularYearID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.curricularYearID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.evaluationType' id='evaluationType' name='evaluationType' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationType}'/>"/>
		<h:outputText escape="false" value="<input alt='input.executionCourseID' id='executionCourseID' name='executionCourseID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionCourseID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.evaluationID' id='evaluationID' name='evaluationID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.description' id='description' name='description' type='hidden' value='#{CoordinatorEvaluationsBackingBean.description}'/>"/>
		<h:outputText escape="false" value="<input alt='input.date' id='date' name='date' type='hidden' value='#{CoordinatorEvaluationsBackingBean.date}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginTime' id='beginTime' name='beginTime' type='hidden' value='#{CoordinatorEvaluationsBackingBean.beginTime}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endTime' id='endTime' name='endTime' type='hidden' value='#{CoordinatorEvaluationsBackingBean.endTime}'/>"/>
		<h:outputText escape="false" value="<input alt='input.name' id='name' name='name' type='hidden' value='#{CoordinatorEvaluationsBackingBean.name}'/>"/>
		<h:outputText escape="false" value="<input alt='input.begin' id='begin' name='begin' type='hidden' value='#{CoordinatorEvaluationsBackingBean.begin}'/>"/>
		<h:outputText escape="false" value="<input alt='input.end' id='end' name='end' type='hidden' value='#{CoordinatorEvaluationsBackingBean.end}'/>"/>

		<h:outputFormat value="<h2>#{bundle['link.edit.evaluation']}</h2/><hr/>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty CoordinatorEvaluationsBackingBean.errorMessage}"
				value="#{bundle[CoordinatorEvaluationsBackingBean.errorMessage]}<br/>" escape="false" />

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
			<h:outputText value="#{bundle['label.evaluation.type']}: "/>
			<h:outputText value="#{bundle['label.evaluation.type.writtenTest']}"
					 rendered="#{CoordinatorEvaluationsBackingBean.evaluationType == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
			<h:outputText value="#{bundle['label.evaluation.type.project']}"
					 rendered="#{CoordinatorEvaluationsBackingBean.evaluationType == 'net.sourceforge.fenixedu.domain.Project'}"/>

			<h:outputText value="#{bundle['label.executionCourse']}: "/>
			<h:outputText value="#{CoordinatorEvaluationsBackingBean.executionCourse.nome}"/>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />

		<h:panelGroup rendered="#{CoordinatorEvaluationsBackingBean.evaluationType == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
				<h:outputText value="#{bundle['label.name']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.description}"/>

				<h:outputText value="#{bundle['label.date']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.date}"/>

				<h:outputText value="#{bundle['label.beginning']}: " style="font-weight: bold" escape="false"/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.beginTime}"/>

				<h:outputText value="#{bundle['label.end']}: " style="font-weight: bold" escape="false"/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.endTime}"/>
			</h:panelGrid>
		</h:panelGroup>

		<h:panelGroup rendered="#{CoordinatorEvaluationsBackingBean.evaluationType == 'net.sourceforge.fenixedu.domain.Project'}">
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.name']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.name}"/>

				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.projectBeginDateTime']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.begin}"/>

				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.projectEndDateTime']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.end}"/>

				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.description']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.description}"/>
				
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.onlineSubmissionsAllowed']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.onlineSubmissionsAllowed ? bundle['label.yes'] : bundle['label.no']}"/>
				
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.maxSubmissionsToKeep']}: " rendered="#{CoordinatorEvaluationsBackingBean.onlineSubmissionsAllowed}"/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.maxSubmissionsToKeep}" rendered="#{CoordinatorEvaluationsBackingBean.onlineSubmissionsAllowed}"/>
				
				<h:outputText value="#{bundle['label.net.sourceforge.fenixedu.domain.Project.grouping.name']}: " rendered="#{CoordinatorEvaluationsBackingBean.onlineSubmissionsAllowed}"/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.grouping.name}" rendered="#{CoordinatorEvaluationsBackingBean.onlineSubmissionsAllowed}"/>
				
			</h:panelGrid>
		</h:panelGroup>

		<h:outputText value="<br/>" escape="false" />
		<h:commandButton alt="#{htmlAltBundle['commandButton.delete']}" action="#{CoordinatorEvaluationsBackingBean.deleteEvaluation}" styleClass="inputbutton" value="#{bundle['button.delete']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" styleClass="inputbutton" value="#{bundle['button.cancel']}" action="#{CoordinatorEvaluationsBackingBean.returnToCalendar}"/>
	</h:form>
</ft:tilesView>