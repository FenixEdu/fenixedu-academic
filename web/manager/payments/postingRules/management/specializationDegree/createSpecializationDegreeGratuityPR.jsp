<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.createSpecializationDegreeGratuityPR"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"
			bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<bean:define id="degreeCurricularPlanId" name="createSpecializationDegreeGratuityPostingRuleBean" property="degreeCurricularPlan.idInternal" />

<logic:empty name="createSpecializationDegreeGratuityPostingRuleBean" property="rule">		
	<fr:form action="<%="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPRTypeChosen&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>">
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean" name="createSpecializationDegreeGratuityPostingRuleBean" visible="false" />
	
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean.chooseType"
			name="createSpecializationDegreeGratuityPostingRuleBean"
			schema="CreateSpecializationDegreeGratuityPostingRuleBean.choose-type">
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thmiddle thright thlight mtop05" />
			</fr:layout>
			<fr:destination name="invalid" path="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPRInvalid" />
			<fr:destination name="cancel" path="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules" />
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>
	</fr:form>			
</logic:empty>

<logic:notEmpty name="createSpecializationDegreeGratuityPostingRuleBean" property="rule">
	<fr:form action="<%="/postingRules.do?method=createSpecializationDegreeGratuityPR&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>">
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean" name="createSpecializationDegreeGratuityPostingRuleBean" visible="false" />
		
		<bean:define id="postingRuleClassName" name="createSpecializationDegreeGratuityPostingRuleBean" property="rule.simpleName" />
		<fr:edit id="createSpecializationDegreeGratuityPostingRuleBean.editPostingRule"
			name="createSpecializationDegreeGratuityPostingRuleBean"
			schema="<%="CreateSpecializationDegreeGratuityPostingRuleBean.edit-" + postingRuleClassName %>">
	
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thmiddle thright thlight mtop05" />
			</fr:layout>
			
			<fr:destination name="invalid" path="/postingRules.do?method=prepareCreateSpecializationDegreeGratuityPRInvalid" />
			<fr:destination name="cancel" path="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules" />
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>
	</fr:form>
</logic:notEmpty>

