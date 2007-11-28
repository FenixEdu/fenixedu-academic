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
		<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.execution.year"/></th>
		<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.created"/></th>
		<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.updated"/></th>
		<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.sent"/></th>
		<th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.number.entries"/></th>
	</tr>
  	<tr>
   		<td>
   			<bean:write name="cardGenerationBatch" property="executionYear.year"/>
		</td>
   		<td>
			<logic:present name="cardGenerationBatch" property="created">
    			<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="cardGenerationBatch" property="created.millis"/></dt:format>
			</logic:present>
		</td>
   		<td>
			<logic:present name="cardGenerationBatch" property="updated">
   				<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="cardGenerationBatch" property="updated.millis"/></dt:format>
			</logic:present>
		</td>
   		<td>
			<logic:present name="cardGenerationBatch" property="sent">
   				<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="cardGenerationBatch" property="sent.millis"/></dt:format>
			</logic:present>
		</td>
   		<td>
			<bean:size id="numberEntries" name="cardGenerationBatch" property="cardGenerationEntries"/>
   			<bean:write name="numberEntries"/>
		</td>
	</tr>
</table>
