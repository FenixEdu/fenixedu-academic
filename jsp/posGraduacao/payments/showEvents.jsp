<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<html:form action="/payments.do">
<html:hidden property="method" />

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.events"/></h2>
<br/>
	

<fr:view name="paymentsManagementDTO" property="person"
	schema="person.view-with-name-address-and-fiscalCode">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
	</fr:layout>
</fr:view>


<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO" visible="false" />

<br/>
<fr:edit id="payment-entries"
		name="paymentsManagementDTO" 
		property="entryDTOs" 
		schema="entryDTO.edit">
	<fr:layout name="tabular-editable" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>

<html:submit styleClass="inputbutton" onclick="this.form.method.value='preparePrintGuide';"><bean:message key="button.masterDegree.administrativeOffice.payments.guide"/></html:submit>
<html:submit styleClass="inputbutton" onclick="this.form.method.value='doPayment';"><bean:message key="button.masterDegree.administrativeOffice.payments.pay"/></html:submit>
	
</html:form>