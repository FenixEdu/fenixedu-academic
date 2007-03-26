<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="TREASURY">

<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts" /></h2>

<strong><bean:write name="administrativeOfficeUnit" property="nameWithAcronym"/></strong>
<br/><br/>

<logic:messagesPresent message="true" property="context">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="context" bundle="TREASURY_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person"/></strong>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<bean:define id="personId" name="person" property="idInternal"/>
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />

<logic:notEmpty name="receiptsForAdministrativeOffice">
	<fr:view name="receiptsForAdministrativeOffice" schema="receipt.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="sortBy" value="year=desc,number=desc"/>
			<fr:property name="linkFormat(view)" value="<%="/receipts.do?method=prepareShowReceipt&amp;receiptID=${idInternal}&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId+ "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId %>"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
			<%--
			<fr:property name="linkFormat(cancel)" value="<%="/payments.do?receiptID=${idInternal}&amp;method=prepareCancelReceipt&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId %>"/>
			<fr:property name="visibleIf(cancel)" value="active" />
			<fr:property name="key(cancel)" value="label.cancel"/>
			<fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES"/>
			--%>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="receiptsForAdministrativeOffice">
		<em><bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts.noReceipts"/></em>.
</logic:empty>

<html:form action='<%= "/receipts.do?method=backToShowOperations&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId %>'>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:submit>
</html:form>

</logic:present>
