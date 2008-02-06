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
		<th></th>
	</tr>
	<tr>
		<jsp:include page="cardGenerationBatchRow.jsp"></jsp:include>
   		<td>
			<logic:notPresent name="cardGenerationBatch" property="peopleForEntryCreation">
				<logic:notEmpty name="cardGenerationBatch" property="cardGenerationProblems">
					<bean:define id="urlResolveProblems" type="java.lang.String">/manageCardGeneration.do?method=manageCardGenerationBatchProblems&amp;cardGenerationBatchID=<bean:write name="cardGenerationBatch" property="idInternal"/></bean:define>
					<html:link page="<%= urlResolveProblems %>">
						<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.resolve.problems"/>
					</html:link>
				</logic:notEmpty>
			</logic:notPresent>
		</td>
	</tr>
</table>

<br/>
<bean:define id="urlDownloadBatchFile" type="java.lang.String">/manageCardGeneration.do?method=downloadCardGenerationBatch&amp;cardGenerationBatchID=<bean:write name="cardGenerationBatch" property="idInternal"/></bean:define>
<html:link page="<%= urlDownloadBatchFile %>">
	<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.download"/>
</html:link>
