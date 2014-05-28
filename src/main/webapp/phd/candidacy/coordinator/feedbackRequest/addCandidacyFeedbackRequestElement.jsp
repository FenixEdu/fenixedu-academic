<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElementBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator.feedbackRequest.PhdCandidacyFeedbackRequestDA"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>
<%@page import="net.sourceforge.fenixedu.domain.person.PersonName"%>

<logic:notEmpty name="elementBean">
	
<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdCandidacyFeedbackRequest.do?processId=" + processId %>">	
	<html:hidden property="method" value="addCandidacyFeedbackRequestElement"/>
	
	<fr:edit id="elementBean" name="elementBean" visible="false" />
	
	<strong><bean:message key="label.phd.candidacy.feedback.add.element" bundle="PHD_RESOURCES" /></strong>
	<fr:edit id="elementBean-participant-info" name="elementBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/phdCandidacyFeedbackRequest.do?method=addCandidacyFeedbackRequestElementInvalid&processId=" + processId %>" />
		<fr:destination name="postBack" path="<%= "/phdCandidacyFeedbackRequest.do?method=addCandidacyFeedbackRequestElementPostBack&processId=" + processId %>" />

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
						<fr:slot name="name" required="true">
							<fr:property name="size" value="40" />
						</fr:slot>
						<fr:slot name="title" required="true" />
						<fr:slot name="email" required="true">
							<fr:validator name="<%= EmailValidator.class.getName() %>" />
							<fr:property name="size" value="40" />
						</fr:slot>
					</logic:equal>
					
					<%-- INTERNAL jury type slots --%>
	
					<logic:equal name="elementBean" property="participantType.name" value="INTERNAL">
						<fr:slot name="personName" layout="autoComplete" required="true">
							<fr:property name="size" value="50"/>
							<fr:property name="labelField" value="name"/>
							<fr:property name="indicatorShown" value="true"/>		
							<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchInternalPersonsByNameHavingTeacherOrIsResearcher"/>
							<fr:property name="args" value="size=50"/>
							<fr:property name="className" value="<%= PersonName.class.getName() %>"/>
							<fr:property name="minChars" value="4"/>				
						</fr:slot>
						<fr:slot name="title" />
					</logic:equal>
					
				</logic:notEmpty>
			
			</logic:equal>
			
		</fr:schema>
		
	</fr:edit>

	<br/>

	<strong><bean:message key="label.phd.email.to.send" bundle="PHD_RESOURCES" />:</strong>
	<fr:edit id="elementBean-mail-information" name="elementBean">
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>

		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdCandidacyFeedbackRequestElementBean.class.getName() %>">
			<fr:slot name="mailSubject" required="true">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="mailBody" layout="longText" required="true">
				<fr:property name="rows" value="15" />
				<fr:property name="columns" value="100" />
			</fr:slot>
		</fr:schema>
		
		<fr:destination name="invalid" path="<%= "/phdCandidacyFeedbackRequest.do?method=addCandidacyFeedbackRequestElementInvalid&processId=" + processId %>" />

	</fr:edit>

	<p>
		<strong>Notas:</strong> 
		Além do texto referido, irá ser acrescentada a informação de acesso, consoante o perfil do novo elemento:
	</p>
	<ul>
		<li>Coordenador do programa doutoral do aluno - <bean:message key="message.phd.candidacy.feedback.coordinator.access" bundle="PHD_RESOURCES" /></li>
		<li>Docente - <bean:message key="message.phd.candidacy.feedback.teacher.access" bundle="PHD_RESOURCES" /></li>
		<li>Elemento externo - vai ser colocado um link de accesso, com nome de utilizador e password</li>
	</ul>

	<html:submit><bean:message key="label.add" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='manageFeedbackRequest';return true;" ><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>

</logic:notEmpty>

