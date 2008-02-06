<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<html:xhtml/>

<logic:present name="cardGenerationBatch">
	<td>
		<bean:write name="cardGenerationBatch" property="executionYear.year"/>
	</td>
	<td>
		<logic:present name="cardGenerationBatch" property="created">
    		<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="cardGenerationBatch" property="created.millis"/></dt:format>
		</logic:present>
	</td>
	<td>
		<bean:write name="cardGenerationBatch" property="description"/>
	</td>
   	<td>
		<logic:present name="cardGenerationBatch" property="updated">
   			<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="cardGenerationBatch" property="updated.millis"/></dt:format>
		</logic:present>
	</td>
   	<td>
		<logic:present name="cardGenerationBatch" property="peopleForEntryCreation">
			<logic:notEmpty name="cardGenerationBatch" property="peopleForEntryCreation">
				<bean:define id="stateTitle" type="java.lang.String"><bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.creating.description"/></bean:define>
				<font color="orange" title="<%= stateTitle %>">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.creating"/>
				</font>
			</logic:notEmpty>
		</logic:present>
		<logic:notPresent name="cardGenerationBatch" property="peopleForEntryCreation">
			<logic:notEmpty name="cardGenerationBatch" property="cardGenerationProblems">
				<font color="red">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.contains.problems"/>
				</font>
			</logic:notEmpty>
		</logic:notPresent>
		<logic:present name="cardGenerationBatch" property="sent">
			<font color="green">
				<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.sent"/>
			</font>
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
   	<td>
		<bean:size id="numberProblems" name="cardGenerationBatch" property="cardGenerationProblems"/>
   		<bean:write name="numberProblems"/>
	</td>
</logic:present>
