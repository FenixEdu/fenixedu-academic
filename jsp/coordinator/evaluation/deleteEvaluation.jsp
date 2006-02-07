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
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.degreeCurricularPlanID}'/>"/>
		<h:outputText escape="false" value="<input id='executionPeriodID' name='executionPeriodID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionPeriodID}'/>"/>
		<h:outputText escape="false" value="<input id='curricularYearID' name='curricularYearID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.curricularYearID}'/>"/>
		<h:outputText escape="false" value="<input id='evaluationType' name='evaluationType' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationType}'/>"/>
		<h:outputText escape="false" value="<input id='executionCourseID' name='executionCourseID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.executionCourseID}'/>"/>
		<h:outputText escape="false" value="<input id='evaluationID' name='evaluationID' type='hidden' value='#{CoordinatorEvaluationsBackingBean.evaluationID}'/>"/>
		<h:outputText escape="false" value="<input id='description' name='description' type='hidden' value='#{CoordinatorEvaluationsBackingBean.description}'/>"/>
		<h:outputText escape="false" value="<input id='date' name='date' type='hidden' value='#{CoordinatorEvaluationsBackingBean.date}'/>"/>
		<h:outputText escape="false" value="<input id='beginTime' name='beginTime' type='hidden' value='#{CoordinatorEvaluationsBackingBean.beginTime}'/>"/>
		<h:outputText escape="false" value="<input id='endTime' name='endTime' type='hidden' value='#{CoordinatorEvaluationsBackingBean.endTime}'/>"/>
		<h:outputText escape="false" value="<input id='name' name='name' type='hidden' value='#{CoordinatorEvaluationsBackingBean.name}'/>"/>
		<h:outputText escape="false" value="<input id='begin' name='begin' type='hidden' value='#{CoordinatorEvaluationsBackingBean.begin}'/>"/>
		<h:outputText escape="false" value="<input id='end' name='end' type='hidden' value='#{CoordinatorEvaluationsBackingBean.end}'/>"/>

		<h:outputFormat value="<h2>#{bundle['link.edit.evaluation']}</h2/><hr>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty CoordinatorEvaluationsBackingBean.errorMessage}"
				value="#{bundle[CoordinatorEvaluationsBackingBean.errorMessage]}<br/>" escape="false" />

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
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
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
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
			<h:panelGrid columns="2" styleClass="infoop" columnClasses="alignright,,"  rowClasses=",,,valigntop">
				<h:outputText value="#{bundle['label.name']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.name}"/>

				<h:outputText value="#{bundle['label.publish.date']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.begin}"/>

				<h:outputText value="#{bundle['label.delivery.date']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.end}"/>

				<h:outputText value="#{bundle['label.description']}: "/>
				<h:outputText value="#{CoordinatorEvaluationsBackingBean.description}"/>
			</h:panelGrid>
		</h:panelGroup>

		<h:outputText value="<br/>" escape="false" />
		<h:commandButton action="#{CoordinatorEvaluationsBackingBean.deleteEvaluation}" styleClass="inputbutton" value="#{bundle['button.delete']}"/>
		<h:commandButton immediate="true" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</ft:tilesView>