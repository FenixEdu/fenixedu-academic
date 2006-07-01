<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.receipts" /></h2>

<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person"/>:</strong>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
<br/>
<logic:notEmpty name="person" property="receipts">
	<fr:view name="person" property="receipts" schema="receipt.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
		    <fr:property name="linkFormat(view)" value="/payments.do?method=prepareShowReceipt&receiptID=${idInternal}"/>
			<fr:property name="key(view)" value="link.masterDegree.administrativeOffice.payments.show"/>
			<fr:property name="sortBy" value="year=desc,number=desc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="person" property="receipts">
		<span class="error"><bean:message key="label.masterDegree.administrativeOffice.payments.noReceipts"/></span>
</logic:empty>
