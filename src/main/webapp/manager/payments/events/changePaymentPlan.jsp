<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.change.paymentPlan" bundle="APPLICATION_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"
			bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<bean:define id="eventId" name="event" property="externalId" />
<bean:define id="personId" name="event" property="person.externalId" />

<p class="mtop15 mbottom05">
<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.event" /></strong></p>
<fr:edit name="event" schema="ChangePaymentPlan">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="success" path="<%="/paymentsManagement.do?method=backToShowEvents&personId=" + personId.toString() %>" />
	<fr:destination name="invalid" path="<%="/paymentsManagement.do?method=prepareChangePaymentPlan&eventId=" + eventId.toString() %>" />
	<fr:destination name="cancel" path="<%="/paymentsManagement.do?method=backToShowEvents&personId=" + personId.toString() %>" />
</fr:edit>
