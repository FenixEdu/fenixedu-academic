<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.guide"/></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong></p>
<fr:view name="paymentsManagementDTO" property="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments"/></strong></p>
<logic:notEmpty name="paymentsManagementDTO" property="selectedEntries">

	<fr:view name="paymentsManagementDTO" property="selectedEntries" schema="entryDTO.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05 mbottom0" />
			<fr:property name="columnClasses" value="width30em acenter,width15em aright"/>
		</fr:layout>
	</fr:view>

	<table class="tstyle4 mtop0">
		<tr>
			<td class="width30em"></td>
			<td class="width15em aright" style="background-color: #fdfbdd;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount"/>: <bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol"/></td>
		</tr>
	</table>


	<html:form action="/guides.do" target="_blank">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" />
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='printGuide';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.print"/></html:submit>
	</html:form>
	
</logic:notEmpty>


<logic:empty name="paymentsManagementDTO" property="selectedEntries">
	<span class="error"><!-- Error messages go here --><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.not.found"/></span>
</logic:empty>

</logic:present>
