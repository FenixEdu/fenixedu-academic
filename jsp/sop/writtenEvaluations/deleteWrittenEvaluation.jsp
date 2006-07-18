<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResourcesSOP" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.remove']}</h2><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>
	<h:outputFormat value="<h2>#{bundle['title.remove']}</h2><br/>" rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}" escape="false">
		<f:param value="#{bundle['label.exam']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}" />
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.originPage}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedBegin}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedEnd}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:panelGrid styleClass="infoselected">
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: #{SOPEvaluationManagementBackingBean.executionPeriodLabel}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.degree']}: #{SOPEvaluationManagementBackingBean.executionDegreeLabel}" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.aula.disciplina']}: #{SOPEvaluationManagementBackingBean.executionCourse.nome}" escape="false"/>
		</h:panelGrid>
		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundle[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

		<h:panelGrid columnClasses="infocell" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.day}"/>
				<h:outputText value=" / "/>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.month}"/>
				<h:outputText value=" / "/>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.beginHour}"/>
				<h:outputText value=" : "/>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.beginMinute}"/>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.endHour}"/>
				<h:outputText value=" : "/>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.endMinute}"/>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.description']}" escape="false"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{bundle['property.exam.season']}" escape="false"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.description}"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
				<h:outputText value="#{SOPEvaluationManagementBackingBean.season}"
						rendered="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname == 'net.sourceforge.fenixedu.domain.Exam'}"/>
			</h:panelGroup>
		</h:panelGrid>

		<h:outputText value="<br/>" escape="false"/>
 		<h:commandButton alt="#{htmlAltBundle['commandButton.remove']}" 	immediate="true" styleClass="inputbutton"
							value="#{bundle['button.remove']}" 
							action="#{SOPEvaluationManagementBackingBean.deleteWrittenTest}" 
							title="#{bundle['button.remove']}" 
							onclick="return confirm('#{bundle['message.confirm.written.test']}')"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="writtenEvaluationCalendar" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</ft:tilesView>
