<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.guide"/></h2>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong>
<fr:view name="createOtherPartyPayment" property="event.person"	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments"/></strong>
<fr:view name="createOtherPartyPayment" schema="CreateOtherPartyPaymentBean.view">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 mtop05 mbottom0" />
		<fr:property name="columnClasses" value="width10em,width100em"/>
	</fr:layout>
</fr:view>

<br/>
<html:form action="/payments.do" target="_blank">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" />
	<fr:edit id="createOtherPartyPayment" name="createOtherPartyPayment" visible="false"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='printGuideForOtherParty';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.print"/></html:submit>
</html:form>
	
</logic:present>
