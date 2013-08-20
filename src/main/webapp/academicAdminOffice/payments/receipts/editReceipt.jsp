<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipt" /></h2>

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

<bean:define id="receipt" name="editReceiptBean" property="receipt" />

<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="receipt" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipt" /></strong></p>
<fr:view name="receipt" schema="receipt.view-with-number-and-year">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",tdhl1" />
	</fr:layout>
</fr:view>

<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.contributor" /></strong></p>
<logic:present name="receipt" property="contributorParty">
	<fr:view name="receipt" property="contributorParty" schema="contributor.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		</fr:layout>
	</fr:view>
</logic:present>
<logic:notPresent name="receipt" property="contributorParty">
	<fr:view name="receipt" schema="receipt.view.contributorName">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		</fr:layout>
	</fr:view>
</logic:notPresent>


<bean:define id="personId" name="editReceiptBean" property="receipt.person.externalId" />
<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.newContributor" /></strong></p>




<bean:define id="receiptId" name="editReceiptBean" property="receipt.externalId" />

<fr:form action="<%="/receipts.do?personId=" + personId.toString() %>">
	<input type="hidden" name="method" value=""/>
	
	<fr:edit id="editReceiptBean" name="editReceiptBean" visible="false" />
		
	<logic:equal name="editReceiptBean" property="usingContributorParty" value="true">
		<fr:edit id="editReceiptBean.edit.contributorParty"
			name="editReceiptBean"
			schema="editReceiptBean.edit.contributorParty">
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight" />
				<fr:property name="columnClasses" value="nowrap," />
				<fr:destination name="usingContributorPartyPostback" path="/receipts.do?method=editUsingContributorPartyPostback" />
				<fr:destination name="invalid" path="/receipts.do?method=prepareEditReceiptInvalid"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>
	<logic:notEqual name="editReceiptBean" property="usingContributorParty" value="true">
		<fr:edit 	id="editReceiptBean.edit.contributorName"
					name="editReceiptBean"
					schema="editReceiptBean.edit.contributorName">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight" />
				<fr:property name="columnClasses" value="nowrap," />
				<fr:destination name="usingContributorPartyPostback" path="/receipts.do?method=editUsingContributorPartyPostback" />
				<fr:destination name="invalid" path="/receipts.do?method=prepareEditReceiptInvalid" />
			</fr:layout>
		</fr:edit>	
	</logic:notEqual>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='editReceipt';"><bean:message bundle="APPLICATION_RESOURCES" key="label.edit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='showReceipts';"><bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/></html:cancel>

</fr:form>


<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></strong></p>
<fr:view name="receipt" property="entries" schema="entry.view">
	<fr:layout name="tabular" >
	<fr:property name="classes" value="tstyle4 mtop05 mbottom0 width700px" />
	<fr:property name="columnClasses" value="width8em acenter, width30em acenter,width8em acenter,width15em aright"/>
		<fr:property name="sortBy" value="whenRegistered=desc"/>
	</fr:layout>
</fr:view>
