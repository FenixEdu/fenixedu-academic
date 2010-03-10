<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElementBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator.feedbackRequest.PhdCandidacyFeedbackRequestDA"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>
<%@page import="net.sourceforge.fenixedu.domain.person.PersonName"%>

<logic:notEmpty name="elementBean">
	<bean:define id="processId" name="process" property="externalId" />

	<br/>
	<br/>
	<fr:edit id="elementBean" name="elementBean" action="<%= "/phdCandidacyFeedbackRequest.do?method=addCandidacyFeedbackRequestElement&processId=" + processId %>">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/phdCandidacyFeedbackRequest.do?method=addCandidacyFeedbackRequestElementInvalid&processId=" + processId %>" />
		<fr:destination name="postBack" path="<%= "/phdCandidacyFeedbackRequest.do?method=addCandidacyFeedbackRequestElementPostBack&processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdCandidacyFeedbackRequest.do?method=manageFeedbackRequest&processId=" + processId %>" />


		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdCandidacyFeedbackRequestElementBean.class.getName() %>">
		
			<%-- select type: NEW or EXISTING --%>
			
			<fr:slot name="participantSelectType" layout="radio-postback" required="true">
				<%--<fr:property name="classes" value="liinline nobullet"/> --%>
				<fr:property name="classes" value="nobullet noindent"/>
				<fr:property name="bundle" value="PHD_RESOURCES" />
			</fr:slot>
		
			<%-- show existing participants to be selected --%>
			
			<logic:equal name="elementBean" property="participantSelectType.name" value="EXISTING">
				<fr:slot name="participants" layout="option-select" required="true" >
					<fr:property name="classes" value="nobullet noindent" />
					
					<fr:property name="providerClass" value="<%= PhdCandidacyFeedbackRequestDA.ExistingPhdParticipantsNotInCandidacyFeedbackRequestProcess.class.getName() %>" />
					
					<fr:property name="eachLayout" value="values" />
					<fr:property name="eachSchema" value="PhdCandidacyFeedbackRequestElement.PhdProgramGuiding.view" />

					<fr:property name="sortBy" value="name" />
					<fr:property name="selectAllShown" value="true" />
					
					<fr:property name="emptyMessageKey" value="label.phd.candidacy.feedback.no.elements" />
					<fr:property name="emptyMessageBundle" value="PHD_RESOURCES" />
					<fr:property name="emptyMessageClasses" value="italic error0" />
				</fr:slot>
			</logic:equal>
			
			<%-- create new partipant by searching teacher or by introducing new information --%>
			
			<logic:equal name="elementBean" property="participantSelectType.name" value="NEW">
			
				<%-- select internal or external element type --%>
				
				<fr:slot name="participantType" layout="radio-postback" required="true">
					<fr:property name="classes" value="liinline nobullet"/>
					<fr:property name="bundle" value="PHD_RESOURCES" />
				</fr:slot>					

				<logic:notEmpty name="elementBean" property="participantType">
				
					<%-- EXTERNAL element --%>
					
					<logic:equal name="elementBean" property="participantType.name" value="EXTERNAL">
						<fr:slot name="name" required="true" />
						<fr:slot name="title" required="true" />
						<fr:slot name="email" required="true">
							<fr:validator name="<%= EmailValidator.class.getName() %>" />
						</fr:slot>
					</logic:equal>
					
					<%-- INTERNAL jury type slots --%>
	
					<logic:equal name="elementBean" property="participantType.name" value="INTERNAL">
						<fr:slot name="personName" layout="autoComplete" required="true">
							<fr:property name="size" value="50"/>
							<fr:property name="labelField" value="name"/>
							<fr:property name="indicatorShown" value="true"/>		
							<fr:property name="serviceName" value="SearchInternalPersonsByNameHavingTeacher"/>
							<fr:property name="serviceArgs" value="size=50"/>
							<fr:property name="className" value="<%= PersonName.class.getName() %>"/>
							<fr:property name="minChars" value="4"/>				
						</fr:slot>
					</logic:equal>
					
				</logic:notEmpty>
		
			</logic:equal>
			
			<fr:slot name="mailSubject" required="true">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="mailBody" layout="longText" required="true">
				<fr:property name="rows" value="15" />
				<fr:property name="columns" value="100" />
			</fr:slot>
		
		</fr:schema>
	</fr:edit>
	
	<p>
		<strong>Notas:</strong> 
		Além do texto referido, irá ser acrescentada a informação de acesso, consoante o perfil do novo elemento:
	</p>
	<ul>
		<li>Coordenador do programa doutoral - <bean:message key="message.phd.candidacy.feedback.coordinator.access" bundle="PHD_RESOURCES" /></li>
		<li>Docente - <bean:message key="message.phd.candidacy.feedback.teacher.access" bundle="PHD_RESOURCES" /></li>
		<li>Elemento externo - vai ser colocado um link de accesso, com nome de utilizador e password</li>
	</ul>

</logic:notEmpty>

