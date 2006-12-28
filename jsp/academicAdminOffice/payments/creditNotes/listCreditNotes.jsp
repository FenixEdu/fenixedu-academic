<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="receiptYear" name="receipt" property="year"/>
<bean:define id="receiptNumber" name="receipt" property="number"/>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.creditNotes" arg0="<%=receiptNumber.toString()%>" arg1="<%=receiptYear.toString()%>" /></h2>
<hr/>
<br/>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/>:</strong>
<fr:view name="receipt" property="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<bean:define id="personId" name="receipt" property="person.idInternal"/>
<logic:notEmpty name="receipt" property="creditNotes">
	<fr:view name="receipt" property="creditNotes" schema="CreditNote.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			<fr:property name="linkFormat(view)" value="<%="/payments.do?creditNoteId=${idInternal}&amp;method=showCreditNote"%>"/>
			<fr:property name="key(view)" value="label.payments.show"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="sortBy" value="year=desc,number=desc"/>
		</fr:layout>
	</fr:view>
	
</logic:notEmpty>
<logic:empty name="receipt" property="creditNotes">
		<span class="error0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.noCreditNotes"/></span>
</logic:empty>

<br/>
<br/>

<bean:define id="receiptId" name="receipt" property="idInternal" />
<html:form action="<%="/payments.do?personId=" + personId + "&amp;receiptID=" + receiptId%>">
	<fr:edit id="receipt" name="receipt" visible="false" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='prepareCreateCreditNote';">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create" />
	</html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='prepareShowReceipt';">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/>
	</html:submit>
</html:form>	


</logic:present>
