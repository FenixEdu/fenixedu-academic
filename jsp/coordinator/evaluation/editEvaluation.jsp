<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<style>
.alignright {
text-align: right;
}
.valigntop {
vertical-align: top;
}
</style>
<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'"/>
		<h:outputText escape="false" value="<input id='executionPeriodID' name='executionPeriodID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionPeriodID}'"/>
		<h:outputText escape="false" value="<input id='curricularYearID' name='curricularYearID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.curricularYearID}'"/>
		<h:outputText escape="false" value="<input id='evaluationID' name='evaluationID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationID}'"/>
		<h:outputText escape="false" value="<input id='description' name='description' type='hidden' value='#{CoordinatorEvaluationsBackingBean.description}'"/>
		<h:outputText escape="false" value="<input id='date' name='date' type='hidden' value='#{CoordinatorEvaluationsBackingBean.date}'"/>
		<h:outputText escape="false" value="<input id='beginTime' name='beginTime' type='hidden' value='#{CoordinatorEvaluationsBackingBean.beginTime}'"/>
		<h:outputText escape="false" value="<input id='endTime' name='endTime' type='hidden' value='#{CoordinatorEvaluationsBackingBean.endTime}'"/>
		<h:outputText escape="false" value="<input id='name' name='name' type='hidden' value='#{CoordinatorEvaluationsBackingBean.name}'"/>
		<h:outputText escape="false" value="<input id='begin' name='begin' type='hidden' value='#{CoordinatorEvaluationsBackingBean.begin}'"/>
		<h:outputText escape="false" value="<input id='end' name='end' type='hidden' value='#{CoordinatorEvaluationsBackingBean.end}'"/>

		<h:outputFormat value="<h2>#{bundle['link.edit.evaluation']}</h2/><hr>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty CoordinatorEvaluationsBackingBean.errorMessage}"
				value="#{bundle[CoordinatorEvaluationsBackingBean.errorMessage]}<br/>" escape="false" />

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
			<h:outputText value="#{bundle['label.evaluation.type']}: "/>
			<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.evaluationType}" onchange="this.form.submit()">
				<f:selectItem itemLabel="" itemValue=""/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.type.writtenTest']}" itemValue="net.sourceforge.fenixedu.domain.WrittenTest"/>
				<f:selectItem itemLabel="#{bundle['label.evaluation.type.project']}" itemValue="net.sourceforge.fenixedu.domain.Project"/>
			</h:selectOneMenu>

			<h:outputText value="#{bundle['label.executionCourse']}: "/>
			<h:selectOneMenu value="#{CoordinatorEvaluationsBackingBean.executionCourseID}" onchange="this.form.submit()">
				<f:selectItems value="#{CoordinatorEvaluationsBackingBean.executionCourseSelectItems}"/>
			</h:selectOneMenu>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false" />
	</h:form>

	<h:panelGroup rendered="#{CoordinatorEvaluationsBackingBean.evaluationType == 'net.sourceforge.fenixedu.domain.WrittenTest'}">
		<h:form>
			<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'"/>
			<h:outputText escape="false" value="<input id='executionPeriodID' name='executionPeriodID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionPeriodID}'"/>
			<h:outputText escape="false" value="<input id='curricularYearID' name='curricularYearID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.curricularYearID}'"/>
			<h:outputText escape="false" value="<input id='evaluationType' name='evaluationType' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationType}'"/>
			<h:outputText escape="false" value="<input id='executionCourseID' name='executionCourseID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionCourseID}'"/>
			<h:outputText escape="false" value="<input id='evaluationID' name='evaluationID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationID}'"/>

			<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
				<h:outputText value="#{bundle['label.name']}: "/>
				<h:inputText id="name" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.description}"/>

				<h:outputText value="#{bundle['label.date']}: "/>
				<h:panelGroup>
					<h:inputText id="date" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.date}"/>
					<h:outputText value="#{bundle['label.date.pattern']}: "/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.beginning']}: " style="font-weight: bold" escape="false"/>
				<h:panelGroup>
					<h:inputText id="beginTime" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.beginTime}"/>
					<h:outputText value="#{bundle['label.hour.pattern']}: "/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.end']}: " style="font-weight: bold" escape="false"/>
				<h:panelGroup>
					<h:inputText id="endTime" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.endTime}"/>
					<h:outputText value="#{bundle['label.hour.pattern']}: "/>
				</h:panelGroup>
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			<h:commandButton action="#{CoordinatorEvaluationsBackingBean.editWrittenTest}" styleClass="inputbutton" value="#{bundle['button.edit']}"/>
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
	</h:panelGroup>

	<h:panelGroup rendered="#{CoordinatorEvaluationsBackingBean.evaluationType == 'net.sourceforge.fenixedu.domain.Project'}">
		<h:form>
			<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'"/>
			<h:outputText escape="false" value="<input id='executionPeriodID' name='executionPeriodID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionPeriodID}'"/>
			<h:outputText escape="false" value="<input id='curricularYearID' name='curricularYearID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.curricularYearID}'"/>
			<h:outputText escape="false" value="<input id='evaluationType' name='evaluationType' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationType}'"/>
			<h:outputText escape="false" value="<input id='executionCourseID' name='executionCourseID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionCourseID}'"/>
			<h:outputText escape="false" value="<input id='evaluationID' name='evaluationID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationID}'"/>

			<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
				<h:outputText value="#{bundle['label.name']}: "/>
				<h:inputText id="name" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.name}"/>

				<h:outputText value="#{bundle['label.publish.date']}: "/>
				<h:panelGroup>
					<h:inputText id="begin" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.begin}"/>
					<h:outputText value="#{bundle['label.date.hour.pattern']}: "/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.delivery.date']}: "/>
				<h:panelGroup>
					<h:inputText id="end" required="true" maxlength="100" size="20" value="#{CoordinatorEvaluationsBackingBean.end}"/>
					<h:outputText value="#{bundle['label.date.hour.pattern']}: "/>
				</h:panelGroup>

				<h:outputText value="#{bundle['label.description']}: "/>
				<h:inputTextarea rows="4" cols="40" value="#{CoordinatorEvaluationsBackingBean.description}"/>
			</h:panelGrid>

			<h:outputText value="<br/>" escape="false" />
			<h:commandButton action="#{CoordinatorEvaluationsBackingBean.editProject}" styleClass="inputbutton" value="#{bundle['button.edit']}"/>
			<h:commandButton immediate="true" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
		</h:form>
	</h:panelGroup>

</ft:tilesView>