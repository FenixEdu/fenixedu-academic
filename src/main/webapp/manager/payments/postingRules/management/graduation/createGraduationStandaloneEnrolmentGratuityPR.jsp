<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- createGraduationGratuityPR.jsp -->

<h2><bean:message
	key="label.payments.postingRules.createGraduationGratuityPostingRule"
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

<strong><bean:message  key="label.StandaloneEnrolmentGratuityPR.generic.formula" bundle="APPLICATION_RESOURCES"/></strong>

<fr:edit id="createPostingRuleBean" name="createPostingRuleBean"
	schema="CreateStandaloneEnrolmentGratuityPRBean.edit"
	action="/postingRules.do?method=createGraduationStandaloneEnrolmentGratuityPR">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
		<fr:destination name="invalid" path="/postingRules.do?method=prepareCreateGraduationStandaloneEnrolmentGratuityPRInvalid" />
		<fr:destination name="changeExecutionYearPostback" path="/postingRules.do?method=prepareCreateGraduationStandaloneEnrolmentGratuityPRPostback"/>
		<fr:destination name="cancel" path="/postingRules.do?method=manageGraduationRules"/>
	</fr:layout>
</fr:edit>


