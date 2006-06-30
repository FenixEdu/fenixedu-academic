<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.receipts" /></h2>

<br/>

<logic:iterate id="receipt" name="receiptsFromParty">

	<bean:define id="receiptId" name="receipt" property="idInternal"/>
	<html:link action="<%="/payments.do?method=prepareShowReceipt&receiptId=" + receiptId %>">
		<bean:message key="label.masterDegree.administrativeOffice.payments.number" />:&nbsp;
		<bean:write name="receipt" property="number" />
		<bean:message key="label.masterDegree.administrativeOffice.payments.year" />:&nbsp;
		<bean:write name="receipt" property="number" />
	</html:link>
	
</logic:iterate>