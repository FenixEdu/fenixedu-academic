<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="TREASURY">

<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts.confirmCreateReceipt" /></h2>

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

<strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person" /></strong>
<fr:view name="createReceiptBean" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.contributor" /></strong></p>
<fr:view name="createReceiptBean" property="contributorParty" schema="contributor.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments" /></strong></p>
<fr:view name="createReceiptBean" property="selectedEntries" schema="entry.view">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 mtop05 mbottom0 width700px" />
		<fr:property name="columnClasses" value="width8em acenter, width30em acenter,width8em acenter,width15em aright"/>
		<fr:property name="sortBy" value="whenRegistered=desc"/>
	</fr:layout>
</fr:view>

<table class="tstyle4 mtop0" style="width: 700px;">
	<tr>
		<td class="aright" colspan="4"><span style="background-color: #fdfbdd;"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.totalAmount"/>: <bean:define id="totalAmount" name="createReceiptBean" property="totalAmount" type="Money"/>&nbsp;<%= totalAmount.toPlainString() %>&nbsp;<bean:message bundle="APPLICATION_RESOURCES" key="label.currencySymbol"/></span></td>
	</tr>
</table>

<bean:define id="personId" name="createReceiptBean" property="person.idInternal"/>
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
<fr:form action='<%= "/receipts.do?personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>'>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="receiptsForm" property="method" />
	<fr:edit id="createReceiptBeanConfirm" name="createReceiptBean" visible="false" />
	<p>	
		<span class="warning0"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts.confirmCreateReceiptQuestion"/></span>
	</p>
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createReceipt';"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts.createReceipt"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>
	</p>
</fr:form>

</logic:present>