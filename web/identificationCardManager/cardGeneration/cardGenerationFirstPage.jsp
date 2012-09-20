<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<html:xhtml/>

<em>Cartões de Identificação</em>
<h2><bean:message key="link.manage.card.generation" /></h2>

<logic:present name="crossReferenceResult">
	<pre><bean:write name="crossReferenceResult"/></pre>
</logic:present>

<ul>
	<li>
		<html:link page="/manageCardGeneration.do?method=showCategoryCodes">
			<bean:message key="link.manage.card.generation.consult.category.codes" />
		</html:link>
	</li>
	<logic:present role="MANAGER">
		<li>
			<html:link page="/manageCardGeneration.do?method=prepareCrossReferenceNewBatch">
				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.croosRefNewBatch" />
			</html:link>
		</li>
		<li>
			<html:link page="/manageCardGeneration.do?method=uploadCardInfo">
				<bean:message key="link.manage.card.generation.upload.card.info" />
			</html:link>
		</li>
	</logic:present>
</ul>

<logic:equal name="categoryCondesProblem" value="true">
	<font color="red">
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.manage.card.generation.category.codes.have.problems" />
	</font>
</logic:equal>

<logic:equal name="professionalCategoryCondesProblem" value="true">
	<font color="red">
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.manage.card.generation.professional.category.codes.have.problems" />
	</font>
</logic:equal>


<fr:form action="/manageCardGeneration.do?method=firstPage">
	<fr:edit id="cardGenerationContext"
			 name="cardGenerationContext"
			 schema="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.CardGenerationContext.selectExecutionYear"
		 	type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.CardGenerationContext">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<bean:define id="url" type="java.lang.String">/manageCardGeneration.do?method=createCardGenerationBatch&amp;executionYearID=<bean:write name="cardGenerationContext" property="executionYear.idInternal"/></bean:define>
<bean:define id="urlEmpty" type="java.lang.String">/manageCardGeneration.do?method=createEmptyCardGenerationBatch&amp;executionYearID=<bean:write name="cardGenerationContext" property="executionYear.idInternal"/></bean:define>
<p class="mvert05">
	<logic:notEqual name="categoryCondesProblem" value="true">
		<logic:notEqual name="professionalCategoryCondesProblem" value="true">
			<html:link page="<%= url %>">
				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.create.batch" />
			</html:link>
			|
			<html:link page="<%= urlEmpty %>">
				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.create.batch.empty" />
			</html:link>
			|
		</logic:notEqual>
	</logic:notEqual>
	<bean:define id="sentButNotIssued" type="java.lang.String">/manageCardGeneration.do?method=downloadCardGenerationBatchSentButNotIssuedByYear&amp;executionYearID=<bean:write name="cardGenerationContext" property="executionYear.idInternal"/></bean:define>
	<html:link page="<%= sentButNotIssued %>">
		<bean:message bundle="CARD_GENERATION_RESOURCES" key="label.card.generation.batch.sent.but.not.issued"/>
	</html:link>
</p>

<bean:define id="deleteConfirm">return confirm('<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.confirm.delete"/>')</bean:define>

<table class="tstyle4 thlight tdcenter mtop05">
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
				<logic:present role="MANAGER">
					| 
					<bean:define id="urlDelete" type="java.lang.String">/manageCardGeneration.do?method=deleteCardGenerationBatch&amp;cardGenerationBatchID=<bean:write name="cardGenerationBatch" property="idInternal"/>&amp;executionYearID=<bean:write name="cardGenerationContext" property="executionYear.idInternal"/></bean:define>
					<html:link page="<%= urlDelete %>" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
						<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.delete"/>
					</html:link>
				</logic:present>
			</td>
		</tr>
	</logic:iterate>
</table>
