<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch" %>

<style>
.santanderTitle {
	margin-top: 20px;
	margin-bottom: 0px;
}

.santanderSubtitle {
	margin-top: 5px;
	margin-bottom: 30px;
}

a {border-bottom: none !important;}

.santanderButtonIcon {
	position: relative;
	top: 2px;
	padding-right: 2px;
	width: 15px;
}
</style>

<html:xhtml />

<em>Cartões de Identificação</em>
<h2 class="santanderTitle">Cartões Santander</h2>
<p class="santanderSubtitle"><strong>Geração de ficheiros TUI</strong></p>

	
<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<fr:form action="/manageSantander.do?method=selectExecutionYearPostback">
	<fr:edit id="santanderBean" name="santanderBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.identificationCardManager.ManageSantanderCardGenerationBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="executionYear" bundle="CARD_GENERATION_RESOURCES" key="label.execution.year" layout="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<logic:present name="santanderBean" property="executionYear">
	<logic:equal name="santanderBean" property="allowNewCreation" value="true">
		<bean:define id="executionYearEid" name="santanderBean" property="executionYear.externalId"></bean:define>
		<a href='<%= request.getContextPath() + "/identificationCardManager/manageSantander.do?method=createBatch&amp;executionYearEid=" + executionYearEid %>'>
			<button type="button">
				<img class="santanderButtonIcon" src="<%=request.getContextPath()%>/images/santander_add.png"/> <strong>Criar novo lote</strong>
			</button>
		</a>
	</logic:equal>
</logic:present>

<logic:present name="santanderBean" property="santanderBatches">
	<logic:empty name="santanderBean" property="santanderBatches">
		<p><em>Não existem lotes criados neste ano.</em></p>
	</logic:empty>
	<logic:notEmpty name="santanderBean" property="santanderBatches">
	
		<table class="tstyle4 thlight tdcenter mtop05">
			<tr>
				<jsp:include page="santanderBatchListHeader.jsp"></jsp:include>
				<th></th>
			</tr>
			<logic:iterate id="batch" name="santanderBean" property="santanderBatches">
				<bean:define id="batch" name="batch" toScope="request"/>
				<tr>
					<jsp:include page="santanderBatchListRow.jsp"></jsp:include>
			   		<td>
			   			<% 	SantanderBatch santanderBatch = ((SantanderBatch) batch);
				   			if (santanderBatch.getGenerated() != null && santanderBatch.getSent() == null && santanderBatch.getSantanderProblemsCount() == 0) { %>
				   				<bean:define id="urlPreview" type="java.lang.String">/manageSantander.do?method=previewBatch&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlPreview %>">
									<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.preview"/>
								</html:link>
								|
								<bean:define id="urlSend" type="java.lang.String">/manageSantander.do?method=previewBatch&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlSend %>">
									<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.send"/>
								</html:link>
				   		<%	} else { %>
					   			<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.preview"/>
					   			|
					   			<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.send"/>
				   		<%	} %>
						<logic:present role="MANAGER">
							<% 	if (santanderBatch.getSent() == null) { %>
								| 
								<bean:define id="urlDelete" type="java.lang.String">/manageSantander.do?method=deleteBatch&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlDelete %>" >
									<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.delete"/>
								</html:link>
							<% } %>
						</logic:present>
					</td>
				</tr>
			</logic:iterate>
		</table>
	
		
		
		<%-- 
		<fr:view name="santanderBean" property="santanderBatches">
			<fr:layout name="tabular">
		
				<fr:property name="linkGroupSeparator" value="&nbsp&nbsp|&nbsp&nbsp" />
		
				<fr:property name="linkFormat(edit)" value="<%="/manageSantander.do?method=selectExecutionYearPostback" %>" />
				<fr:property name="order(edit)" value="2" />
				<fr:property name="key(edit)" value="label.santander.send" />
				<fr:property name="bundle(edit)" value="CARD_GENERATION_RESOURCES" />		
			
				<fr:property name="classes" value="tstyle1 thleft" />
				<fr:property name="columnClasses" value=",,,tdclear tderror1" />
		
			</fr:layout>
		
			<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionCourse" bundle="CARD_GENERATION_RESOURCES">
				<fr:slot name="created" key="label.santander.creationDate" />
				<fr:slot name="requester.person.name" key="label.santander.requester" />
			</fr:schema>
		</fr:view>
		--%>
		
		
	</logic:notEmpty>
</logic:present>
