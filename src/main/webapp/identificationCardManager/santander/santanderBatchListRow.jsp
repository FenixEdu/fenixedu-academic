<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch" %>
<html:xhtml/>

<logic:present name="batch">
	<bean:define id="thisBatch" name="batch"/>
	<td>
		<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="batch" property="created.millis"/></dt:format>
	</td>
	<td>
		<bean:write name="batch" property="santanderBatchRequester.person.username" />
	</td>
	<td>
   		<logic:present name="batch" property="generated">
   			<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="batch" property="generated.millis"/></dt:format>
   		</logic:present>
   		<logic:notPresent name="batch" property="generated">
   			<span>--</span>
   		</logic:notPresent>		
	</td>
   	<td>
   		<logic:present name="batch" property="sent">
   			<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="batch" property="sent.millis"/></dt:format>
   		</logic:present>
   		<logic:notPresent name="batch" property="sent">
   			<span>--</span>
   		</logic:notPresent>		
	</td>
	<td>
   		<logic:present name="batch" property="santanderBatchSender">
   			<bean:write name="batch" property="santanderBatchSender.person.username" />
   		</logic:present>
   		<logic:notPresent name="batch" property="santanderBatchSender">
   			<span>--</span>
   		</logic:notPresent>		
	</td>
	<td>
   		<logic:present name="batch" property="sequenceNumber">
   			<bean:write name="batch" property="sequenceNumber" />
   		</logic:present>
   		<logic:notPresent name="batch" property="sequenceNumber">
   			<span>--</span>
   		</logic:notPresent>		
	</td>
   	<td>
   		<% 	SantanderBatch santanderBatch = ((SantanderBatch) thisBatch);
   			if (santanderBatch.getSantanderProblemsCount() > 0) { %>
   				<font color="red">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.contains.problems"/>
				</font>
   		<%	} else if (santanderBatch.getGenerated() == null) { %>
   				<font color="orange">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.creating"/>
				</font>
   		<%	} else if (santanderBatch.getSent() == null) { %>
   				<font color="green">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.processed"/>
				</font>
   		<%	} else { %>
   				<font color="blue">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.card.generation.batch.sent"/>
				</font>
   		<%	} %>
	</td>
   	<td>
		<bean:size id="numberLines" name="batch" property="santanderEntries"/>
   		<bean:write name="numberLines"/>
	</td>
   	<td>
		<bean:size id="numberProblems" name="batch" property="santanderProblems"/>
   		<bean:write name="numberProblems"/>
	</td>	
</logic:present>
