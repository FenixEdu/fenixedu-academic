<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.viewPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<ul class="mvert2">
	<li>
		<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="student" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<h3 class="mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<fr:view name="personBean" schema="student.personalData-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="columnClasses" value="width14em,"/>
	</fr:layout>
</fr:view>

<h3 class="mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<fr:view name="personBean" schema="student.documentId-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>
	
<h3 class="mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<fr:view name="personBean" schema="student.filiation-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="columnClasses" value="width14em,"/>
	</fr:layout>
</fr:view>

<h3 class="mbottom025"><bean:message key="label.person.title.addressesInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<logic:notEmpty name="personBean" property="sortedPhysicalAdresses">
	<fr:view name="personBean" property="sortedPhysicalAdresses" schema="contacts.PhysicalAddress.view" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedPhysicalAdresses">
	<br/>
	<em><bean:message key="label.partyContacts.no.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<h3 class="mbottom025"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<strong><bean:message key="label.phones" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
<logic:notEmpty name="personBean" property="sortedPhones">
	<fr:view name="personBean" property="sortedPhones" schema="contacts.Phone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedPhones">
	<br/>
	<em><bean:message key="label.partyContacts.no.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<strong><bean:message key="label.mobilePhones" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
<logic:notEmpty name="personBean" property="sortedMobilePhones">
	<fr:view name="personBean" property="sortedMobilePhones" schema="contacts.MobilePhone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedMobilePhones">
	<br/>
	<em><bean:message key="label.partyContacts.no.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<strong><bean:message key="label.email" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
<logic:notEmpty name="personBean" property="sortedEmailAddresses">
	<fr:view name="personBean" property="sortedEmailAddresses" schema="contacts.EmailAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedEmailAddresses">
	<br/>
	<em><bean:message key="label.partyContacts.no.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>

<strong><bean:message key="label.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
<logic:notEmpty name="personBean" property="sortedWebAddresses">
	<fr:view name="personBean" property="sortedWebAddresses" schema="contacts.WebAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedWebAddresses">
	<br/>
	<em><bean:message key="label.partyContacts.no.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
</logic:empty>
	
<ul>
	<li>
		<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="student" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>
