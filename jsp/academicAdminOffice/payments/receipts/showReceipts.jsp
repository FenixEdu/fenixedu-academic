<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipts" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list2">
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong></p>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="receiptsForAdministrativeOffice">
	<bean:define id="personId" name="person" property="idInternal"/>
	<fr:view name="receiptsForAdministrativeOffice" schema="receipt.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop15" />
			<fr:property name="columnClasses" value="acenter,,,acenter,acenter," />
			<fr:property name="sortBy" value="year=desc,number=desc"/>
			<fr:property name="linkFormat(view)" value="<%="/payments.do?method=prepareShowReceipt&amp;receiptID=${idInternal}&amp;personId=" + personId %>"/>
			<fr:property name="key(view)" value="label.payments.show"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<%--
			<fr:property name="linkFormat(cancel)" value="<%="/payments.do?receiptID=${idInternal}&amp;method=prepareCancelReceipt&amp;personId=" + personId %>"/>
			<fr:property name="visibleIf(cancel)" value="active" />
			<fr:property name="key(cancel)" value="label.payments.cancel"/>
			<fr:property name="bundle(cancel)" value="ACADEMIC_OFFICE_RESOURCES"/>
			--%>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="receiptsForAdministrativeOffice">
	<p>
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.noReceipts"/></em>.
	</p>
</logic:empty>

<bean:define id="personId" name="person" property="idInternal"/>
<html:form action='<%= "/payments.do?method=backToShowOperations&amp;personId=" + personId %>'>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:submit>
	</p>
</html:form>

</logic:present>
