<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<fr:form action='<%="/caseHandling" + processName + ".do?method=executeChangePaymentCheckedState&processId=" + processId.toString() %>' >
 	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false"/>
 	
 	<bean:message key="label.payment.checked" bundle="CANDIDATE_RESOURCES"/>:
 	<fr:edit id="individualCandidacyProcessBean.paymentChecked" name="individualCandidacyProcessBean" slot="paymentChecked"/>
 	
 	<p/>
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
</fr:form>
 	