<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="personId" name="createReceiptBean" property="person.idInternal"/>
<fr:form action="<%="/receipts.do?personId=" + personId%>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="receiptsForm" property="method" />

	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentsWithoutReceipt" /></h2>
	
	
	<logic:messagesPresent message="true" property="context">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="context" bundle="ACADEMIC_OFFICE_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"  property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong>
	<fr:view name="createReceiptBean" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<logic:notEmpty name="createReceiptBean" property="entries">

		<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.contributor" /></strong></p>
		<fr:edit id="createReceiptBean" name="createReceiptBean" schema="createReceiptBean.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear" />
			</fr:layout>
			<fr:destination name="invalid" path="/receipts.do?method=prepareShowPaymentsWithoutReceiptInvalid"/>
		</fr:edit>

		<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></strong></p>
		<fr:edit id="createReceiptBean-entries-part" name="createReceiptBean"
			property="entries" schema="selectableEntryBean.view-selectable">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value=",,,aright,aright,aright,acenter" />
				<fr:property name="sortBy" value="entry.whenRegistered=desc"/>
			</fr:layout>
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='confirmCreateReceipt';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.continue"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
	</logic:notEmpty>

	<logic:empty name="createReceiptBean" property="entries">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.noPaymentsWithoutReceipt" />.
			</em>
		</p>
		<br/>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
	</logic:empty>
</fr:form>

</logic:present>
