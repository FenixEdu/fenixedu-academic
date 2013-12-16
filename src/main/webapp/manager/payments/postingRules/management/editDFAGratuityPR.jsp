<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.editPostingRule"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan"
	property="externalId" />
<bean:define id="postingRuleId" name="postingRuleEditor"
	property="dfaGratuityPR.externalId" />

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" >
			<li><span class="error0"><bean:write name="messages"/></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<fr:hasMessages type="conversion" for="postingRuleEditor">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="postingRuleEditorClassName" name="postingRuleEditor" property="class.simpleName" />
<fr:edit id="postingRuleEditor" name="postingRuleEditor" schema="<%=postingRuleEditorClassName + ".edit"%>" 
action="<%= "/postingRules.do?method=editDFAGratuityPR&degreeCurricularPlanId=" + degreeCurricularPlanId %>">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
	</fr:layout>
	<fr:destination name="invalid"
		path="<%="/postingRules.do?method=prepareEditDFAGratuityPRInvalid&postingRuleId=" + postingRuleId + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
	<fr:destination name="cancel"
		path="<%="/postingRules.do?method=showPostGraduationDegreeCurricularPlanPostingRules&degreeCurricularPlanId=" + degreeCurricularPlanId %>" />
</fr:edit>
