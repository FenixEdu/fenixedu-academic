<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.receipts" /></h2>

<br/>
<table class="tstyle4">
	<logic:iterate id="receipt" name="receiptsFromParty">
		<tr>
			<bean:define id="receiptId" name="receipt" property="idInternal"/>
			<td><bean:message key="label.masterDegree.administrativeOffice.payments.number" />:&nbsp;<bean:write name="receipt" property="number"/></td>
			<td><bean:message key="label.masterDegree.administrativeOffice.payments.year" />:&nbsp;<bean:write name="receipt" property="year"/></td>
			<td>
				<html:link action="<%="/payments.do?method=prepareShowReceipt&receiptID=" + receiptId %>">
					<bean:message key="label.masterDegree.administrativeOffice.payments.show" />
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>