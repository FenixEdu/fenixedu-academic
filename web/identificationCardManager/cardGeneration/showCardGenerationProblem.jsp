<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<html:xhtml/>

<h2>
	<bean:message key="link.manage.card.generation" />
</h2>

<br/>

<bean:define id="cardGenerationBatch" toScope="request" name="cardGenerationProblem" property="cardGenerationBatch"/>
<bean:define id="person" toScope="request" name="cardGenerationProblem" property="person"/>
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
				<bean:define id="urlDeleteProblem" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationProblem&amp;cardGenerationProblemID=<bean:write name="cardGenerationProblem" property="idInternal"/></bean:define>
				<html:link page="<%= urlDeleteProblem %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.problem.delete"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>

<logic:iterate id="cardGenerationEntry" type="net.sourceforge.fenixedu.domain.cardGeneration.CardGenerationEntry" name="person" property="cardGenerationEntries">
	<br/>
	<strong><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.entry.line"/>:</strong>
	<% if (cardGenerationEntry.getCardGenerationBatch().getExecutionYear() == executionYear) { %>
			<bean:define id="line" type="java.lang.String" name="cardGenerationEntry" property="line"/>
			<br/>
			<pre><%= line.substring(0, 131) %></pre>
			<br/>
			<pre><%= line.substring(131) %></pre>
	<% } %>
	<bean:define id="urlDeleteEntry" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationEntry&amp;cardGenerationProblemID=<bean:write name="cardGenerationProblem" property="idInternal"/>&amp;cardGenerationEntryID=<bean:write name="cardGenerationEntry" property="idInternal"/></bean:define>
	<html:link page="<%= urlDeleteEntry %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.entry.delete"/>
	</html:link>
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
