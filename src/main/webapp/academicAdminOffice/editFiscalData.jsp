<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>

<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.student.editFiscalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="studentID" type="java.lang.String" name="student" property="externalId"/>
<bean:define id="personBean" name="personBean" type="org.fenixedu.academic.dto.person.PersonBean" />
<bean:define id="person" name="personBean" property="person" />

<fr:view name="person">
	<fr:schema type="org.fenixedu.academic.domain.Person" bundle="ACADEMIC_OFFICE_RESOURCES">
		<fr:slot name="givenNames" >
			<fr:property name="size" value="50" />
		</fr:slot>
		<fr:slot name="familyNames" >
			<fr:property name="size" value="50" />
		</fr:slot>
		<fr:slot name="gender" />

       	<logic:notEmpty name="person" property="fiscalAddress">
		<fr:slot name="this" layout="format" key="label.socialSecurityNumber" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:property name="format" value="${fiscalAddress.countryOfResidence.code} ${socialSecurityNumber}" />
		</fr:slot>
		</logic:notEmpty>
		
       	<logic:empty name="person" property="fiscalAddress">
		<fr:slot name="this" layout="format" key="label.socialSecurityNumber" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:property name="format" value="${socialSecurityNumber}" />
		</fr:slot>
		</logic:empty>
				
       	<fr:slot name="fiscalAddress">
			<fr:property name="format" value="${uiFiscalPresentationValue}" />
       	</fr:slot>
       	
	</fr:schema>
	
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
		<fr:property name="columnClasses" value="width14em,"/>
	</fr:layout>
</fr:view>

<logic:messagesPresent message="true">
    <ul class="nobullet list6">
        <html:messages id="messages" message="true">
            <li><span class="error0"><bean:write name="messages" /></span></li>
        </html:messages>
    </ul>
</logic:messagesPresent>

<p><em class="infoop2"><bean:message key="message.warning.editFiscalData.select.address.equal.fiscalNumber.country" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>

<fr:form action="/student.do">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editFiscalData"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID" value="<%= studentID.toString() %>"/>

	<fr:edit name="personBean" visible="false" />

    <fr:edit id="editPersonBean" name="personBean">
        <fr:schema type="org.fenixedu.academic.domain.Person" bundle="ACADEMIC_OFFICE_RESOURCES" >

			<fr:slot name="socialSecurityNumber" required="true" >
				<% if(personBean.getFiscalAddress() != null) { %>
				<fr:validator name="org.fenixedu.ulisboa.specifications.ui.renderers.validators.FiscalCodeValidator">
					<fr:property name="countryCode" value="<%= personBean.getFiscalAddress().getCountryOfResidence().getCode()  %>" />
				</fr:validator>
				<% } %>
			</fr:slot>
			
        	<fr:slot name="fiscalAddress" layout="menu-select-postback" required="true">
                <fr:property name="from" value="sortedValidAddressesForFiscalData" />
				<fr:property name="format" value="${uiFiscalPresentationValue} (${countryOfResidence.code})" />
				<fr:property name="destination" value="postback" />
        	</fr:slot>
        </fr:schema>
	    <fr:layout name="tabular" >
            <fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
            <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	    </fr:layout>
	    
		<fr:destination name="invalid" path='<%= "/student.do?method=editFiscalDataInvalid&studentID=" + studentID %>' />
		<fr:destination name="postback" path='<%= "/student.do?method=editFiscalDataPostback&studentID=" + studentID %>' />
    </fr:edit>
	
    <p>
        <html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
        <html:cancel onclick="this.form.method.value='prepareEditPersonalData';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
    </p>    
	
</fr:form>