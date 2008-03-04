<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<ft:tilesView definition="definition.directiveCouncil.masterPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ManagerResources" var="bundle"/>

	<h:form>
		<h:outputText value="<em>#{bundle['DIRECTIVE_COUNCIL']}</em>" escape="false"/>
		<h:outputText value="<h2>#{bundle['title.send.mail']}</h2>" escape="false"/>

		<h:outputText rendered="#{SendMailBackingBean.sent}" value="<p>E-mail enviado com sucesso.</p><br/>" escape="false"/>

		<h:panelGrid columns="2" styleClass="tstyle5" columnClasses="aright,,"  rowClasses=",,,valigntop">
			<h:outputText value="From: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.from']}" id="from" value="#{SendMailBackingBean.from}" size="60"/>

			<h:outputText value="To: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.to']}" id="to" value="#{SendMailBackingBean.to}" size="60"/>

			<h:outputText value="Cc: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.ccs']}" id="ccs" value="#{SendMailBackingBean.ccs}" size="60"/>

			<h:outputText value="Bc: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.bccs']}" id="bccs" value="#{SendMailBackingBean.bccs}" size="60"/>

			<h:outputText value="Subject: " escape="false"/>
			<h:inputText alt="#{htmlAltBundle['inputText.subject']}" id="subject" value="#{SendMailBackingBean.subject}" size="60"/>
			
		<h:outputText value="Message:<br/>" escape="false"/>
		<h:inputTextarea rows="9" cols="70" id="message" value="#{SendMailBackingBean.message}"/>

		</h:panelGrid>


		<h:selectBooleanCheckbox value="#{SendMailBackingBean.teachers}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> docentes <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.researchers}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> investigadores <span class='color888'>em destinatários (bcc).</span>" escape="false"/>
		
		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.executionCourseResponsibles}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> docentes responsáveis por disciplinas <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.employees}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> funcionários não docentes <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.students}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaAdvancedFormationDiplomaStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de diploma de formação avançada <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaDegreeStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de licenciatura (de bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaIntegratedMasterDegreeStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de mestrado integrado <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaMasterDegreeStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de mestrado (de bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaPhdProgramStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de programas doutorais <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaSpecializationDegreeStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de especialização <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.degreeStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de licenciatura (pré-bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.masterDegreeStudents}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> alunos de mestrado (pré-bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaAdvancedFormationDiplomaCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de diplomas de formação avançada <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaDegreeCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de licenciaturas (de bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaIntegratedMasterDegreeCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de mestrados integrados <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaMasterDegreeCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de mestrado (de bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaPhdProgramCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de programas doutorais <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.bolonhaSpecializationDegreeCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de especialização <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.degreeCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de licenciaturas (pré-bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<br/>" escape="false"/>
		<h:selectBooleanCheckbox value="#{SendMailBackingBean.masterDegreeCoordinators}"/>
		<h:outputText value="<span class='color888'>Incluir todos os</span> coordenadores de mestrados (pré-bolonha) <span class='color888'>em destinatários (bcc).</span>" escape="false"/>

		<h:outputText value="<p class='mtop2'>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.send']}" action="#{SendMailBackingBean.send}"
				styleClass="inputbutton" value="#{bundle['button.send']}"/>
		<h:outputText value="</p>" escape="false"/>

	</h:form>
</ft:tilesView>
