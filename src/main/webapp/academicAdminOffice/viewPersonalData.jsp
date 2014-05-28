<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.student.viewPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p class="mtop2">
	<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="student" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>

<h3 class="mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<fr:view name="personBean" schema="student.personalData-withoutProfessionDetails" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
		<fr:property name="columnClasses" value="width14em,"/>
	</fr:layout>
</fr:view>

<h3 class="mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<fr:view name="personBean" schema="student.documentId-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>
	
<h3 class="mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<fr:view name="personBean" schema="student.filiation-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
		<fr:property name="columnClasses" value="width14em,"/>
	</fr:layout>
</fr:view>

<h3 class="mbottom025"><bean:message key="label.person.title.addressesInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
<logic:notEmpty name="personBean" property="sortedPhysicalAdresses">
	<fr:view name="personBean" property="sortedPhysicalAdresses" schema="contacts.PhysicalAddress.view" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="personBean" property="sortedPhysicalAdresses">
	<p>
		<em><bean:message key="label.partyContacts.no.physicalAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:empty>



<h3 class="mbottom025"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<bean:message key="label.phones" bundle="ACADEMIC_OFFICE_RESOURCES" />
<logic:notEmpty name="personBean" property="sortedPhones">
	<fr:view name="personBean" property="sortedPhones" schema="contacts.Phone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedPhones">
	<p>
		<em><bean:message key="label.partyContacts.no.phones" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:empty>

<bean:message key="label.mobilePhones" bundle="ACADEMIC_OFFICE_RESOURCES" />
<logic:notEmpty name="personBean" property="sortedMobilePhones">
	<fr:view name="personBean" property="sortedMobilePhones" schema="contacts.MobilePhone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="personBean" property="sortedMobilePhones">
	<p>
		<em><bean:message key="label.partyContacts.no.mobilePhones" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:empty>

<bean:message key="label.email" bundle="ACADEMIC_OFFICE_RESOURCES" />
<logic:notEmpty name="personBean" property="sortedEmailAddresses">
	<fr:view name="personBean" property="sortedEmailAddresses" schema="contacts.EmailAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="personBean" property="sortedEmailAddresses">
	<p>
		<em><bean:message key="label.partyContacts.no.emailAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:empty>

<bean:message key="label.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" />
<logic:notEmpty name="personBean" property="sortedWebAddresses">
	<fr:view name="personBean" property="sortedWebAddresses" schema="contacts.WebAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="personBean" property="sortedWebAddresses">
	<p>
		<em><bean:message key="label.partyContacts.no.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:empty>
	
<p class="mtop2">
	<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="student" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>