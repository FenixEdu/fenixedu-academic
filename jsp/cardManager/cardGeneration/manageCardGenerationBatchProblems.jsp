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

<table class="tstyle4 thlight mtop05">
	<tr>
		<jsp:include page="cardGenerationBatchHeader.jsp"></jsp:include>
	</tr>
	<tr>
		<jsp:include page="cardGenerationBatchRow.jsp"></jsp:include>
	</tr>
</table>

<br/>

<table class="tstyle4 thlight mtop05">
	<tr>
		<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.problem.description"/></th>
		<th></th>
	</tr>
	<logic:iterate id="cardGenerationBatchProblem" name="cardGenerationBatch" property="cardGenerationProblems" length="100">
		<tr>
			<td>
				<bean:define id="arg" type="java.lang.String" name="cardGenerationBatchProblem" property="arg"/>
				<bean:message bundle="CARD_GENERATION_RESOURCES" name="cardGenerationBatchProblem" property="descriptionKey" arg0="<%= arg %>"/>
			</td>
	   		<td>
				<bean:define id="urlMarkAsResolved" type="java.lang.String">/manageCardGeneration.do?method=showCardGenerationProblem&amp;cardGenerationProblemID=<bean:write name="cardGenerationBatchProblem" property="idInternal"/></bean:define>
				<html:link page="<%= urlMarkAsResolved %>">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.problem.show"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>

<bean:size id="numberProblems" name="cardGenerationBatch" property="cardGenerationProblems"/>
<logic:greaterThan name="numberProblems" value="100">
	<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.contains.more.problems"/>
</logic:greaterThan>
