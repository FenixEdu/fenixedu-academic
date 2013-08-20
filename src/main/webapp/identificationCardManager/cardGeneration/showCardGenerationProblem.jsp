<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<html:xhtml/>

<em>Cartões de Identificação</em>
<h2><bean:message key="link.manage.card.generation" /></h2>
<p><html:link page="/manageCardGeneration.do?method=manageCardGenerationBatch">« Voltar</html:link></p>

<br/>

<bean:define id="cardGenerationBatch" toScope="request" name="cardGenerationProblem" property="cardGenerationBatch"/>
<bean:define id="executionYear" toScope="request" name="cardGenerationBatch" property="executionYear"/>
<table class="tstyle4 thlight mtop05">
	<tr>
		<jsp:include page="cardGenerationBatchHeader.jsp"></jsp:include>
	</tr>
	<tr>
		<jsp:include page="cardGenerationBatchRow.jsp"></jsp:include>
	</tr>
</table>

<br/>

<bean:define id="deleteConfirm">return confirm('<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.confirm.delete"/>')</bean:define>

<logic:present name="cardGenerationProblems">
	<table class="tstyle4 thlight mtop05">
		<tr>
			<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.problem.description"/></th>
			<th></th>
		</tr>
		<logic:iterate id="cardGenerationProblem" name="cardGenerationProblems" length="100">
			<tr>
				<td>
					<bean:define id="arg" type="java.lang.String" name="cardGenerationProblem" property="arg"/>
					<bean:message bundle="CARD_GENERATION_RESOURCES" name="cardGenerationProblem" property="descriptionKey" arg0="<%= arg %>"/>
				</td>
				<td>
					<logic:present role="MANAGER">
						<bean:define id="urlDeleteProblem" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationProblem&amp;cardGenerationProblemID=<bean:write name="cardGenerationProblem" property="externalId"/></bean:define>
						<html:link page="<%= urlDeleteProblem %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
							<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.problem.delete"/>
						</html:link>
					</logic:present>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:equal name="cardGenerationProblem" property="descriptionKey" value="no.person.found">
	<table class="tstyle4 thlight mtop05">
		<tr>
			<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.problem.description"/></th>
			<th></th>
		</tr>
		<tr>
			<td>
				<bean:define id="arg" type="java.lang.String" name="cardGenerationProblem" property="arg"/>
				<bean:message bundle="CARD_GENERATION_RESOURCES" name="cardGenerationProblem" property="descriptionKey" arg0="<%= arg %>"/>
			</td>
			<td>
				<logic:present role="MANAGER">
					<bean:define id="urlDeleteProblem" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationProblem&amp;cardGenerationProblemID=<bean:write name="cardGenerationProblem" property="externalId"/></bean:define>
					<html:link page="<%= urlDeleteProblem %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
						<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.problem.delete"/>
					</html:link>
				</logic:present>
			</td>
		</tr>
	</table>
	<bean:define id="documentId" type="java.lang.String" name="cardGenerationProblem" property="arg"/>
	<logic:iterate id="cardGenerationEntryX" type="net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry" name="cardGenerationProblem" property="cardGenerationBatch.cardGenerationEntries">
		<logic:equal name="cardGenerationEntryX" property="documentID" value="<%= documentId %>">
			<bean:define id="cardGenerationEntry" name="cardGenerationEntryX" toScope="request"/>
			<jsp:include page="cardGenerationEntry.jsp"/>

			<logic:present role="MANAGER">
				<bean:define id="urlDeleteEntry" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationEntry&amp;cardGenerationProblemID=<bean:write name="cardGenerationProblem" property="externalId"/>&amp;cardGenerationEntryID=<bean:write name="cardGenerationEntryX" property="externalId"/></bean:define>
				<html:link page="<%= urlDeleteEntry %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.entry.delete"/>
				</html:link>

					<bean:define id="url" type="java.lang.String">/manageCardGeneration.do?method=setPersonForCardGenerationEntry&amp;cardGenerationEntryID=<bean:write name="cardGenerationEntryX" property="externalId"/></bean:define>
					<fr:edit id="setPersonForCardGenerationEntryBean" name="setPersonForCardGenerationEntryBean" action="<%= url %>">
						<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.ManageCardGenerationDA$SetPersonForCardGenerationEntryBean" bundle="CARD_GENERATION_RESOURCES">
							<fr:slot name="personName" layout="autoComplete" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator" key="label.person">
								<fr:property name="size" value="30"/>
								<fr:property name="rawSlotName" value="name"/>
								<fr:property name="indicatorShown" value="true"/>
								<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchInternalPersons"/>
								<fr:property name="args" value="slot=name,size=30"/>
								<fr:property name="className" value="net.sourceforge.fenixedu.domain.person.PersonName"/>				
								<fr:property name="minChars" value="4"/>
								<fr:property name="labelField" value="person.name"/>
								<fr:property name="format" value="${text}"/>
							</fr:slot>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="form listInsideClear" />
							<fr:property name="columnClasses" value="width100px,,tderror" />
						</fr:layout>
					</fr:edit>

			</logic:present>

		</logic:equal>
	</logic:iterate>
</logic:equal>

<logic:present name="cardGenerationProblem" property="person">
	<bean:define id="person" toScope="request" name="cardGenerationProblem" property="person"/>

	<h4>
		<bean:write name="person" property="name"/>
	</h4>
	<ul>
		<logic:iterate id="role" name="person" property="personRoles">
			<li>
				<bean:write name="role" property="roleType.name"/>
			</li>
		</logic:iterate>
	</ul>

	<logic:iterate id="cardGenerationEntryX" type="net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry" name="person" property="cardGenerationEntries">
		<bean:define id="cardGenerationEntry" name="cardGenerationEntryX" toScope="request"/>
		<jsp:include page="cardGenerationEntry.jsp"/>

		<logic:present role="MANAGER">
			<bean:define id="urlDeleteEntry" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationEntry&amp;cardGenerationProblemID=<bean:write name="cardGenerationProblem" property="externalId"/>&amp;cardGenerationEntryID=<bean:write name="cardGenerationEntryX" property="externalId"/></bean:define>
			<html:link page="<%= urlDeleteEntry %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.entry.delete"/>
			</html:link>
		</logic:present>
		<br/>
	</logic:iterate>

	<br/>

	<logic:present name="person" property="student">
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.student.information"/>
		<table class="tstyle4 thlight mtop05">
			<tr>
				<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.registration.start.date"/></th>
				<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.registration.degree.type"/></th>
				<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.registration.degree"/></th>
				<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.registration.current.state"/></th>
				<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.registration.agreemente"/></th>
			</tr>
			<logic:iterate id="registration" name="person" property="student.registrations">
				<tr>
					<td>
						<dt:format pattern="yyyy-MM-dd">
							<bean:write name="registration" property="activeState.stateDate.millis"/>
						</dt:format>
					</td>
					<td>
						<bean:message bundle="ENUMERATION_RESOURCES" name="registration" property="degreeType.name"/>
					</td>
					<td>
						<bean:write name="registration" property="degree.name"/>
					</td>
					<td>
						<bean:define id="registrationStateTypeKey" type="java.lang.String">RegistrationStateType.<bean:write name="registration" property="activeState.stateType.name"/></bean:define>
						<bean:message bundle="ENUMERATION_RESOURCES" key="<%= registrationStateTypeKey %>"/>
					</td>
					<td>
						<bean:define id="registrationAgreementKey" type="java.lang.String">RegistrationAgreement.<bean:write name="registration" property="registrationAgreement.name"/></bean:define>
						<bean:message bundle="ENUMERATION_RESOURCES" key="<%= registrationAgreementKey %>"/>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>

</logic:present>
