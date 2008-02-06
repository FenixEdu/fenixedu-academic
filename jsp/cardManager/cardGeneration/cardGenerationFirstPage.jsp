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

<html:link page="/manageCardGeneration.do?method=showCategoryCodes">
	<bean:message key="link.manage.card.generation.consult.category.codes" />
</html:link>
<logic:equal name="categoryCondesProblem" value="true">
	<font color="red">
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.manage.card.generation.category.codes.have.problems" />
	</font>
</logic:equal>

<br/>
<br/>
<fr:form action="/manageCardGeneration.do?method=firstPage">
	<fr:edit id="cardGenerationContext"
			 name="cardGenerationContext"
			 schema="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.CardGenerationContext.selectExecutionYear"
		 	type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.CardGenerationContext">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop1"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<bean:define id="url" type="java.lang.String">/manageCardGeneration.do?method=createCardGenerationBatch&amp;executionYearID=<bean:write name="cardGenerationContext" property="executionYear.idInternal"/></bean:define>
<logic:notEqual name="categoryCondesProblem" value="true">
	<html:link page="<%= url %>">
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.create.batch" />
	</html:link>
</logic:notEqual>

<bean:define id="deleteConfirm">return confirm('<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.confirm.delete"/>')</bean:define>

<table class="tstyle4 thlight mtop05">
	<tr>
		<jsp:include page="cardGenerationBatchHeader.jsp"></jsp:include>
		<th></th>
	</tr>
	<logic:iterate id="cardGenerationBatch" name="cardGenerationContext" property="executionYear.cardGenerationBatches">
		<bean:define id="cardGenerationBatch" name="cardGenerationBatch" toScope="request"/>
		<tr>
			<jsp:include page="cardGenerationBatchRow.jsp"></jsp:include>
	   		<td>
				<bean:define id="urlManage" type="java.lang.String">/manageCardGeneration.do?method=manageCardGenerationBatch&amp;cardGenerationBatchID=<bean:write name="cardGenerationBatch" property="idInternal"/></bean:define>
				<html:link page="<%= urlManage %>">
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.manage"/>
				</html:link>
				<bean:define id="urlDelete" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationBatch&amp;cardGenerationBatchID=<bean:write name="cardGenerationBatch" property="idInternal"/>&amp;executionYearID=<bean:write name="cardGenerationContext" property="executionYear.idInternal"/></bean:define>
				<html:link page="<%= urlDelete %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
					<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.delete"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
