<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<h2><bean:message bundle="MANAGER_RESOURCES"
		key="label.payments.annulTransaction" /></h2>

	<bean:define id="personId" name="annulAccountingTransactionBean"
		property="person.idInternal" />
	<bean:define id="eventId" name="annulAccountingTransactionBean"
		property="transaction.event.idInternal" />
		
	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<fr:edit id="annulAccountingTransactionBean"
		name="annulAccountingTransactionBean"
		schema="AnnulAccountingTransactionBean.edit"
		action="<%="/payments.do?method=annulTransaction&amp;personId=" + personId%>">

		<fr:layout name="layout">
			<fr:property name="classes"
				value="tstyle2 thmiddle thright thlight mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid"
				path="/payments.do?method=prepareAnnulTransactionInvalid" />
			<fr:destination name="cancel"
				path="<%="/payments.do?method=showPaymentsForEvent&eventId=" + eventId %>" />
		</fr:layout>
	</fr:edit>

</logic:present>
