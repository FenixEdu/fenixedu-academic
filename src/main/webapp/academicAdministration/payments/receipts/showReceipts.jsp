<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="personId" name="person" property="externalId" />
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
	key="label.payments.management" /></h2>


<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><strong><bean:message
	bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="person" property="receipts">
	<fr:view name="person" property="receipts" schema="receipt.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="sortBy" value="year=desc,numberWithSeries=desc" />

			<fr:property name="linkFormat(view)"
				value="<%="/paymentsManagement.do?method=showReceipt&amp;receiptId=${externalId}&amp;personId=" + personId %>" />
			<fr:property name="key(view)" value="label.view" />
			<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />

			<fr:property name="linkFormat(annul)"
				value="<%="/paymentsManagement.do?method=annulReceipt&amp;receiptId=${externalId}&amp;personId=" + personId %>" />
			<fr:property name="key(annul)" value="label.annul" />
			<fr:property name="bundle(annul)" value="APPLICATION_RESOURCES" />
			<fr:property name="visibleIf(annul)" value="active" />
			<fr:property name="confirmationKey(annul)" value="label.payments.receipts.confirmAnnul" />
			<fr:property name="confirmationBundle(annul)" value="ACADEMIC_OFFICE_RESOURCES" />

		</fr:layout>
	</fr:view>		
</logic:notEmpty>
<logic:empty name="person" property="receipts">
	<bean:message  key="label.payments.noReceipts" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</logic:empty>

<br/><br/>

<fr:form
	action="<%="/paymentsManagement.do?method=showOperations&amp;personId=" + personId%>">
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:cancel>
</fr:form>
