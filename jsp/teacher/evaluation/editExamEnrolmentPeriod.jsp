<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.enrollment.period']}</h2>" escape="false">
		<f:param value="#{bundle['label.exam']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:panelGrid width="100%" columns="1" cellspacing="8" cellpadding="8">
			<h:outputText styleClass="infoop" value="#{bundle['label.period.information']}" escape="false"/>
	
			<h:panelGrid styleClass="infotable" width="50%" columns="1">
				<h:outputText value="<b>#{bundle['label.exam']}:</b> #{evaluationManagementBackingBean.evaluation.season.season}" escape="false"/>
				<h:outputText value="<br/>" escape="false"/>
		
				<h:outputText value="<b>#{bundle['label.day']}:</b> " escape="false"/>
				<h:outputFormat value="{0, date, MM/dd/yyyy}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.dayDate}"/>
				</h:outputFormat>
	
				<h:outputText styleClass="bottomborder" value="<b>#{bundle['label.at']}:</b> " escape="false"/>
				<h:outputFormat value="{0, date, hh:mm}">
					<f:param value="#{evaluationManagementBackingBean.evaluation.beginningDate}"/>
				</h:outputFormat>
	
				<h:outputFormat value="<b>#{bundle['label.evaluation.enrollment.period']}</b>" escape="false">
					<f:param value="#{bundle['label.exam']}" />
				</h:outputFormat>
				
				<h:form>
					<h:outputText value="<br/>#{bundle['label.exam.enrollment.begin.day']} " escape="false"/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginDay}"/>
					<h:outputText value=" / "/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMonth}"/>
					<h:outputText value=" / "/>
					<h:inputText maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentBeginYear}"/>
					<h:outputText value=" <i>#{bundle['label.at']}</i> " escape="false"/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginHour}"/>
					<h:outputText value=" : "/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentBeginMinute}"/>
					<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
	
					<h:outputText value="<br/><br/>#{bundle['label.exam.enrollment.end.day']} " escape="false"/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndDay}"/>
					<h:outputText value=" / "/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMonth}"/>
					<h:outputText value=" / "/>
					<h:inputText maxlength="4" size="4" value="#{evaluationManagementBackingBean.enrolmentEndYear}"/>
					<h:outputText value=" <i>#{bundle['label.at']}</i> " escape="false"/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndHour}"/>
					<h:outputText value=" : "/>
					<h:inputText maxlength="2" size="2" value="#{evaluationManagementBackingBean.enrolmentEndMinute}"/>
					<h:outputText value=" <i>#{bundle['label.date.instructions']}</i>" escape="false"/>
	
					<h:outputText value="<br/><br/>" escape="false"/>
					<h:commandButton action="#{evaluationManagementBackingBean.editEvaluationEnrolmentPeriod}" styleClass="inputbutton" value="#{bundle['button.save']}"/>
				</h:form>
		</h:panelGrid>
	
		</h:panelGrid>
	
	</h:form>

</ft:tilesView>
