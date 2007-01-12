<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<bean:define id="receiptYear" name="createCreditNoteBean" property="receipt.year"/>
	<bean:define id="receiptNumber" name="createCreditNoteBean" property="receipt.number"/>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.creditNotes" arg0="<%=receiptNumber.toString()%>" arg1="<%=receiptYear.toString()%>" /></h2>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<fr:hasMessages for="create-credit-note-entries" type="conversion">
		<ul>
			<fr:messages>
				<li><span class="error0"><fr:message/></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>

	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong>
	<fr:view name="createCreditNoteBean" property="receipt.person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		</fr:layout>
	</fr:view>


	<fr:form action="/payments.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
		<fr:edit id="receipt" name="createCreditNoteBean" property="receipt" visible="false" nested="true"/>
		<logic:notEmpty name="createCreditNoteBean" property="creditNoteEntryDTOs">
				<fr:edit id="create-credit-note" name="createCreditNoteBean" visible="false" nested="true" />
				<p class="mtop15">
					<strong><bean:message  key="label.payments.creditNotes.entries.with.reimbursement.appliable" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
				</p>
				<fr:edit id="create-credit-note-entries" name="createCreditNoteBean" property="creditNoteEntryDTOs" schema="CreditNoteEntryDTO.edit">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle4 thlight thright tdcenter mtop05" />
					</fr:layout>
				</fr:edit>
				
				<p>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createCreditNote';">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create"/>
					</html:submit>
					
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='showCreditNotes';">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.cancel"/>
					</html:submit>
				</p>
			
		</logic:notEmpty>
		<logic:empty name="createCreditNoteBean" property="creditNoteEntryDTOs">
			<span class="error0">
				<bean:message  key="label.payments.creditNotes.no.reimbursable.entries" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</span>
			<br/><br/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='showCreditNotes';">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/>
			</html:submit>
		</logic:empty>
	</fr:form>

</logic:present>
