<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.enrollment.period']}</h2><br/>" escape="false">
		<f:param value="#{evaluationManagementBackingBean.evaluationType}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText styleClass="infoop" value="#{bundle['label.period.information']}" escape="false"/>
	
		<h:outputText value="<br/><br/><br/><b>#{bundle['label.exam']}:</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
		<h:outputText value="<br/><br/><br/><b>#{bundle['label.written.test']}:</b> " escape="false" rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>
		<h:outputText value="#{evaluationManagementBackingBean.evaluation.season} - " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.Exam'}"/>
		<h:outputText value="#{evaluationManagementBackingBean.evaluation.description} - " rendered="#{evaluationManagementBackingBean.evaluation.class.name == 'net.sourceforge.fenixedu.domain.WrittenTest'}"/>

		<h:outputFormat value="{0, date, dd/MM/yyyy}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
		</h:outputFormat>
		<h:outputText value=" #{bundle['label.at']} " escape="false"/>
		<h:outputFormat value="{0, date, HH:mm}">
			<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
		</h:outputFormat>
		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>
		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.exam.enrollment.begin.day']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginDay}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMonth}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentBeginYear}"/>
				<h:outputText value=" #{bundle['label.at']} " escape="false"/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.exam.enrollment.end.day']} " escape="false"/>				
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndDay}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMonth}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentEndYear}"/>
				<h:outputText value=" #{bundle['label.at']} " escape="false"/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton action="#{evaluationManagementBackingBean.editEvaluationEnrolmentPeriod}" styleClass="inputbutton" value="#{bundle['button.define']}"/>
		<h:commandButton immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</ft:tilesView>
