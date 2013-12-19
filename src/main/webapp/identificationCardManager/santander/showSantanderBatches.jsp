<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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

<em><bean:message bundle="CARD_GENERATION_RESOURCES" key="title.card.generation"/></em>
<h2 class="santanderTitle"><bean:message bundle="CARD_GENERATION_RESOURCES" key="subtitle.santander.cards"/></h2>
<p class="santanderSubtitle"><strong><bean:message bundle="CARD_GENERATION_RESOURCES" key="subtitle.santander.cards.tui.generation"/></strong></p>

	
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
				<img class="santanderButtonIcon" src="<%=request.getContextPath()%>/images/santander_add.png"/> <strong><bean:message bundle="CARD_GENERATION_RESOURCES" key="button.santander.create.new.batch"/></strong>
			</button>
		</a>
	</logic:equal>
</logic:present>

<logic:present name="santanderBean" property="santanderBatches">
	<logic:empty name="santanderBean" property="santanderBatches">
		<p><em><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.santander.no.available.bacthes.for.this.year"/></em></p>
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
			   			<% 	SantanderBatch santanderBatch = ((SantanderBatch) batch); %>
			   			<%	if (santanderBatch.getGenerated() != null) { %>
				   				<bean:define id="urlDownload" type="java.lang.String">/manageSantander.do?method=downloadBatch&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlDownload %>">
									<button type="button">
										<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.download"/>
									</button>
								</html:link>
				   		<%	} else { %>
				   				<button type="button" disabled="disabled" >
					   				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.download"/>
					   			</button>
				   		<%	} %>
				   		<%	if (santanderBatch.getSent() != null) { %>
				   				<bean:define id="urlDDXR" type="java.lang.String">/manageSantander.do?method=downloadDDXR&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlDDXR %>">
									<button type="button">
										<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.ddxr"/>
									</button>
								</html:link>
				   		<%	} else { %>
				   				<button type="button" disabled="disabled" >
					   				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.ddxr"/>
					   			</button>
				   		<%	} %>
				   		<%	if (santanderBatch.getGenerated() != null && santanderBatch.getSent() == null && santanderBatch.getSantanderProblemsCount() == 0) { %>
								<bean:define id="urlSend" type="java.lang.String">/manageSantander.do?method=sendBatch&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlSend %>">
									<button type="button">
										<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.send"/>
									</button>
								</html:link>
				   		<%	} else { %>
				   				<button type="button" disabled="disabled" >
					   				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.santanderBatch.send"/>
					   			</button>
				   		<%	} %>
						<logic:present role="role(MANAGER)">
							<% 	if (santanderBatch.getSent() == null) { %>
								<bean:define id="urlDelete" type="java.lang.String">/manageSantander.do?method=deleteBatch&amp;executionYearEid=<bean:write name="santanderBean" property="executionYear.externalId"/>&amp;santanderBatchEid=<bean:write name="batch" property="externalId"/></bean:define>
								<html:link page="<%= urlDelete %>" >
									<button type="button">
										<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.delete"/>
									</button>
								</html:link>
							<%	} else { %>
								<button type="button" disabled="disabled" >
					   				<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.batch.delete"/>
					   			</button>
				   			<%	} %>
						</logic:present>
					</td>
				</tr>
			</logic:iterate>
		</table>		
	</logic:notEmpty>
</logic:present>
