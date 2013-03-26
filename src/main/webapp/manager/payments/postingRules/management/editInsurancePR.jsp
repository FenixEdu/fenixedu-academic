<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message
	key="label.payments.postingRules.editPostingRule"
	bundle="MANAGER_RESOURCES" /></h2>

<br />

<fr:hasMessages type="conversion" for="postingRuleEditor">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label" />:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="postingRuleEditorClassName" name="postingRule"
	property="class.simpleName" />

<bean:define id="postingRuleId" name="postingRule"
	property="idInternal" />

<fr:edit id="postingRuleEditor" name="postingRule"
	schema="<%=postingRuleEditorClassName + ".edit"%>">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
	</fr:layout>
	<fr:destination name="invalid"
		path="<%="/postingRules.do?method=prepareEditInsuracePRInvalid&postingRuleId=" + postingRuleId%>" />
	<fr:destination name="success"
		path="<%="/postingRules.do?method=showInsurancePostingRules"%>" />
	<fr:destination name="cancel"
		path="<%="/postingRules.do?method=showInsurancePostingRules"%>" />
</fr:edit>
