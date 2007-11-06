<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.directiveCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>

	<h:form>	
		<h:outputText value="<h2>#{bundle['title.send.mail']}</h2><br/>" escape="false"/>

		<h:outputText rendered="#{SendMailBackingBean.sent}" value="<p>E-mail enviado com sucesso.</p><br/>" escape="false"/>

		<h:panelGrid columns="2" styleClass="infoop" columnClasses="aright,,"  rowClasses=",,,valigntop">
			<h:outputText value="From: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.from']}" id="from" value="#{SendMailBackingBean.from}"/>

			<h:outputText value="To: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.to']}" id="to" value="#{SendMailBackingBean.to}"/>

			<h:outputText value="Cc: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.ccs']}" id="ccs" value="#{SendMailBackingBean.ccs}"/>

			<h:outputText value="Bc: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.bccs']}" id="bccs" value="#{SendMailBackingBean.bccs}"/>

			<h:outputText value="Subject: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.subject']}" id="subject" value="#{SendMailBackingBean.subject}"/>

		</h:panelGrid>

		<h:outputText value="<br/><br/>" escape="false"/>

		<h:outputText value="Message:<br/>" escape="false"/>
		<h:inputTextarea rows="10" cols="80" id="message" value="#{SendMailBackingBean.message}"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.teachers}"/>
		<h:outputText value="Incluir todos os docentes em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.executionCourseResponsibles}"/>
		<h:outputText value="Incluir todos os docentes responsáveis por disciplinas em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.employees}"/>
		<h:outputText value="Incluir todos os funcionários não docentes em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.students}"/>
		<h:outputText value="Incluir todos os alunos em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaAdvancedFormationDiplomaStudents}"/>
		<h:outputText value="Incluir todos os alunos de diploma de formação avançada em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaDegreeStudents}"/>
		<h:outputText value="Incluir todos os alunos de licenciatura (de bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaIntegratedMasterDegreeStudents}"/>
		<h:outputText value="Incluir todos os alunos de mestrado integrado em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaMasterDegreeStudents}"/>
		<h:outputText value="Incluir todos os alunos de mestrado (de bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaPhdProgramStudents}"/>
		<h:outputText value="Incluir todos os alunos de programas doutorais em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaSpecializationDegreeStudents}"/>
		<h:outputText value="Incluir todos os alunos de especialização em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.degreeStudents}"/>
		<h:outputText value="Incluir todos os alunos de licenciatura (pré-bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.masterDegreeStudents}"/>
		<h:outputText value="Incluir todos os alunos de mestrado (pré-bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaAdvancedFormationDiplomaCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de diplomas de formação avançada em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaDegreeCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de licenciaturas (de bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaIntegratedMasterDegreeCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de mestrados integrados em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaMasterDegreeCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de mestrado (de bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaPhdProgramCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de programas doutorais em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaSpecializationDegreeCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de especialização em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.degreeCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de licenciaturas (pré-bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.masterDegreeCoordinators}"/>
		<h:outputText value="Incluir todos os coordenadores de mestrados (pré-bolonha) em destinatários (bcc)." escape="false"/>

		<h:outputText value="<br/><br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.send']}" action="#{SendMailBackingBean.send}"
				styleClass="inputbutton" value="#{bundle['button.send']}"/>

	</h:form>
</ft:tilesView>
