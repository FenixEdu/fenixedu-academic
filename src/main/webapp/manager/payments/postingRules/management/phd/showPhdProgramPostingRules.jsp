<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="label.payments.postingRules.phdProgram.rulesFor"
	bundle="MANAGER_RESOURCES" /> <bean:write name="phdProgram"
	property="name" /></h2>

<br />

<bean:define id="phdProgramId" name="phdProgram"
	property="externalId" />
	
<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<html:link
	action="<%="/phdPostingRules.do?method=prepareAddGratuityPhdPostingRule&phdProgramId=" + phdProgramId %>">
	Criar Regra de pagamento de doutoramento
</html:link>

<fr:view name="phdProgram" property="serviceAgreementTemplate.postingRules" schema="PostingRule.view-with-eventType">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="sortBy" value="eventType=asc,startDate=desc" />
		
		<fr:link 	name="details" label="label.details,APPLICATION_RESOURCES" 
					link="<%= "/phdPostingRules.do?method=viewPostingRuleDetails&amp;postingRuleId=${externalId}&amp;phdProgramId=" + phdProgramId %>" 
					order="0"/>

		<fr:link	name="edit" label="label.edit,APPLICATION_RESOURCES"
					link="<%= "/phdPostingRules.do?method=prepareEditPhdProgramPostingRule&amp;postingRuleId=${externalId}&amp;phdProgramId=" + phdProgramId %>"
					condition="mostRecent"
					order="1"
					/>
		<fr:link	name="delete" label="label.delete,APPLICATION_RESOURCES"
					link="<%= "/phdPostingRules.do?method=deleteDegreeCurricularPlanPostingRule&amp;postingRuleId=${externalId}&amp;phdProgramId=" + phdProgramId %>"
					condition="mostRecent" 
					order="2" 
					confirmation="label.payments.postingRules.confirmDeletePostingRule,MANAGER_RESOURCES" />
	</fr:layout>	
</fr:view>

<br />
<html:link
	action="<%="/postingRules.do?method=managePostGraduationRules"%>">
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
</html:link>
