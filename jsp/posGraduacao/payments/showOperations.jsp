<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.operations.title"/></h2>

<bean:define id="personId" name="person" property="idInternal" />
<html:link action="<%="/payments.do?method=showEvents&personId=" + personId%>">
	<bean:message
		key="link.masterDegree.administrativeOffice.payments.operations.events" />
</html:link>
<br/>
<html:link action="<%="/payments.do?method=showPerformedPayments&personId=" + personId%>">
	<bean:message
		key="link.masterDegree.administrativeOffice.payments.operations.performedPayments" />
</html:link>
<br/>
<html:link action="<%="/payments.do?method=showReceipts&personId=" + personId%>">
	<bean:message
		key="link.masterDegree.administrativeOffice.payments.operations.receipts" />
</html:link>
