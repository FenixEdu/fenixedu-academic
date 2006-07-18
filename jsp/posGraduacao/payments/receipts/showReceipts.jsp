<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.receipts" /></h2>
<hr>
<br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person"/>:</strong>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
<br/>
<logic:notEmpty name="person" property="receipts">
	<bean:define id="personId" name="person" property="idInternal"/>
	<fr:view name="person" property="receipts" schema="receipt.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			<fr:property name="linkFormat(view)" value="<%="/payments.do?method=prepareShowReceipt&receiptID=${idInternal}&personId=" + personId %>"/>
			<fr:property name="key(view)" value="link.masterDegree.administrativeOffice.payments.show"/>
			<fr:property name="sortBy" value="year=desc,number=desc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="person" property="receipts">
		<span class="error0"><bean:message key="label.masterDegree.administrativeOffice.payments.noReceipts"/></span>
</logic:empty>

<bean:define id="personId" name="person" property="idInternal"/>
<html:form action='<%= "/payments.do?method=backToShowOperations&personId=" + personId %>'>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.masterDegree.administrativeOffice.payments.back"/></html:submit>
</html:form>
