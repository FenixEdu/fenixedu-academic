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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present name="person">
	<bean:define id="person" name="person" type="org.fenixedu.academic.domain.Person"/>

	<h2><bean:message key="label.person.title.personalConsult" bundle="APPLICATION_RESOURCES"/></h2>
	<bean:define id="personID" name="person" property="externalId" />
	<p><span class="error0"><!-- Error messages go here --><html:errors /></span>
	</p>

	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages"/></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
		
	<!-- Photo -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.photo" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>

	<table class="mvert1 tdtop">
		<tbody>
			<tr>
				<td>
					<logic:present name="person" property="username">
                    <bean:define id="personIDForPhoto" name="person" property="username"/>
					<html:img align="middle"
					src="<%=request.getContextPath() + "/user/photo/" + personIDForPhoto.toString() %>"
					altKey="personPhoto" bundle="IMAGE_RESOURCES"
					style="border: 1px solid #aaa; padding: 3px;" />
					</logic:present>
				</td> 	
				<td>
				<div style="padding: 0 2em;">
				<div class="infoop2"><bean:message
					key="label.person.photo.info" bundle="APPLICATION_RESOURCES"/></div>
				</div>
				</td>
			</tr>
		</tbody>
	</table>

	<!-- Contactos -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.contactAndAuthorization" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>

	<fr:form action="/accounts/partyContacts.do">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th></th>
				<th></th>
				<th><bean:message key="label.contact.visible.to.public"
					bundle="ACADEMIC_ADMIN_OFFICE" /></th>
				<th><bean:message key="label.contact.visible.to.students"
					bundle="ACADEMIC_ADMIN_OFFICE" /></th>
				<th><bean:message key="label.contact.visible.to.staff"
					bundle="ACADEMIC_ADMIN_OFFICE" /></th>
				<th><bean:message key="label.contact.visible.to.management"
					bundle="ACADEMIC_ADMIN_OFFICE" /></th>
				<th></th>
			</tr>
<bean:define id="physicalAddresses" name="person" property="physicalAddresses" />
<bean:size id="size" name="physicalAddresses" />
<logic:notEmpty name="physicalAddresses">
	<logic:iterate id="contact" name="physicalAddresses">
		<logic:equal name="contact" property="valid" value="false" >
			<tr style="font-style:italic;">
		</logic:equal>
		<logic:equal name="contact" property="valid" value="true" >
			<tr>
		</logic:equal>
			<td><bean:message key="label.address" bundle="APPLICATION_RESOURCES"/> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="presentationValue" />
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" bundle="APPLICATION_RESOURCES"/>)
					</logic:notEqual>
					
				</logic:equal>
			</td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToPublic" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToPublic" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToStudents" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToStudents" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToStaff" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToStaff" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
            </td>
			<td class="tdclear">
				<html:link action="<%="/accounts/partyContacts.do?method=prepareCreatePhysicalAddress&personID=" + personID%>">
					<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
				</html:link>,
				<logic:equal name="contact" property="valid" value="true">
					<html:link action="/accounts/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
						<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
					</html:link>,
				</logic:equal>
				<html:link action="<%="/accounts/partyContacts.do?method=deletePartyContact&personID=" + personID%>" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				<logic:equal name="contact" property="valid" value="false" >
					,<html:link action="<%="/accounts/partyContacts.do?method=prepareValidate&personID=" + personID%>" paramId="partyContact" paramName="contact" paramProperty="externalId">
						<bean:message key="label.validate" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="physicalAddresses">
	<tr>
		<td><bean:message key="label.address" bundle="APPLICATION_RESOURCES"/>:</td>
        <td>-</td>
        <td class="acenter">-</td>
        <td class="acenter">-</td>
        <td class="acenter">-</td>
        <td class="acenter">-</td>
		<td class="tdclear">
			<html:link action="<%="/accounts/partyContacts.do?method=prepareCreatePhysicalAddress&personID=" + personID%>">
				<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</td>
	</tr>
</logic:empty>
						
			<bean:define id="phones" name="person" property="phones" />

			<bean:size id="size" name="phones" />
			<logic:notEmpty name="phones">
				<logic:iterate id="contact" name="phones">
					<bean:define id="contactId" name="contact" property="externalId" />
					<tr>
						<td><bean:message key="label.partyContacts.Phone" bundle="APPLICATION_RESOURCES"/> (<bean:message
							name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="number" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" bundle="APPLICATION_RESOURCES"/>)
					</logic:notEqual>

						</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToPublic" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToPublic"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStudents" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStudents"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStaff" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStaff"
							value="false">-</logic:equal></td>
						<td class="acenter"><img
							src="<%=request.getContextPath()%>/images/accept.gif" /></td>
						<td class="tdclear"><html:link
							action="<%="/accounts/partyContacts.do?method=prepareCreatePhone&personID=" + personID%>">
							<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
						</html:link>, <html:link
							action="<%="/accounts/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
						</html:link>, <html:link
							action="<%="/accounts/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.clear" bundle="APPLICATION_RESOURCES"/>
						</html:link></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="phones">
				<tr>
					<td><bean:message key="label.partyContacts.Phone" bundle="APPLICATION_RESOURCES"/>:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/accounts/partyContacts.do?method=prepareCreatePhone&personID=" + personID%>">
						<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
					</html:link></td>
				</tr>
			</logic:empty>


			<bean:define id="mobilePhones" name="person" property="mobilePhones" />
			<bean:size id="size" name="mobilePhones" />
			<logic:notEmpty name="mobilePhones">
				<logic:iterate id="contact" name="mobilePhones">
					<bean:define id="contactId" name="contact" property="externalId" />
					<tr>
						<td><bean:message key="label.partyContacts.MobilePhone" bundle="APPLICATION_RESOURCES"/> (<bean:message
							name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="number" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" bundle="APPLICATION_RESOURCES"/>)
					</logic:notEqual>

						</logic:equal></td>

						<td class="acenter"><logic:equal name="contact"
							property="visibleToPublic" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToPublic"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStudents" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStudents"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStaff" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStaff"
							value="false">-</logic:equal></td>
						<td class="acenter"><img
							src="<%=request.getContextPath()%>/images/accept.gif" /></td>
						<td class="tdclear"><html:link
							action="<%="/accounts/partyContacts.do?method=prepareCreateMobilePhone&personID=" + personID%>">
							<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
						</html:link>, <html:link
							action="<%="/accounts/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
						</html:link>, <html:link
							action="<%="/accounts/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.clear" bundle="APPLICATION_RESOURCES"/>
						</html:link></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="mobilePhones">
				<tr>
					<td><bean:message key="label.partyContacts.MobilePhone" bundle="APPLICATION_RESOURCES"/>:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/accounts/partyContacts.do?method=prepareCreateMobilePhone&personID=" + personID%>">
						<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
					</html:link></td>
				</tr>
			</logic:empty>

			<bean:define id="emailAddresses" name="person"
				property="emailAddresses" />
			<bean:size id="size" name="emailAddresses" />
			<logic:notEmpty name="emailAddresses">
				<logic:iterate id="contact" name="emailAddresses">
					<bean:define id="contactId" name="contact" property="externalId" />
					<tr>
						<td><bean:message key="label.partyContacts.EmailAddress" bundle="APPLICATION_RESOURCES"/>
						(<bean:message name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="value" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" bundle="APPLICATION_RESOURCES"/>)
					</logic:notEqual>

						</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToPublic" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToPublic"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStudents" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStudents"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStaff" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStaff"
							value="false">-</logic:equal></td>
						<td class="acenter"><img
							src="<%=request.getContextPath()%>/images/accept.gif" /></td>
						<td class="tdclear"><html:link
							action="<%="/accounts/partyContacts.do?method=prepareCreateEmailAddress&personID=" + personID%>">
							<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
						</html:link>, <html:link
							action="<%="/accounts/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
						</html:link> <logic:notEqual name="contact" property="type.name"
							value="INSTITUTIONAL">,
					<html:link
								action="<%="/accounts/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
								<bean:message key="label.clear" bundle="APPLICATION_RESOURCES"/>
							</html:link>
						</logic:notEqual></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="emailAddresses">
				<tr>
					<td><bean:message key="label.partyContacts.EmailAddress" bundle="APPLICATION_RESOURCES"/>:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/accounts/partyContacts.do?method=prepareCreateEmailAddress&personID=" + personID%>">
						<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
					</html:link></td>
				</tr>
			</logic:empty>

			<bean:define id="webAddresses" name="person" property="webAddresses" />
			<bean:size id="size" name="webAddresses" />
			<logic:notEmpty name="webAddresses">
				<logic:iterate id="contact" name="webAddresses">
					<bean:define id="contactId" name="contact" property="externalId" />
					<tr>
						<td><bean:message key="label.partyContacts.WebAddress" bundle="APPLICATION_RESOURCES"/> (<bean:message
							name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="url" bundle="APPLICATION_RESOURCES"/> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" bundle="APPLICATION_RESOURCES"/>)
					</logic:notEqual>

						</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToPublic" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToPublic"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStudents" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStudents"
							value="false">-</logic:equal></td>
						<td class="acenter"><logic:equal name="contact"
							property="visibleToStaff" value="true">
							<img src="<%=request.getContextPath()%>/images/accept.gif" />
						</logic:equal> <logic:equal name="contact" property="visibleToStaff"
							value="false">-</logic:equal></td>
						<td class="acenter"><img
							src="<%=request.getContextPath()%>/images/accept.gif" /></td>
						<td class="tdclear"><html:link
							action="<%="/accounts/partyContacts.do?method=prepareCreateWebAddress&personID=" + personID%>">
							<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
						</html:link>, <html:link
							action="<%="/accounts/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
						</html:link> <html:link
							action="<%="/accounts/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.clear" bundle="APPLICATION_RESOURCES"/>
						</html:link></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="webAddresses">
				<tr>
					<td><bean:message key="label.partyContacts.WebAddress" bundle="APPLICATION_RESOURCES"/>:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/accounts/partyContacts.do?method=prepareCreateWebAddress&personID=" + personID%>">
						<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
					</html:link></td>
				</tr>
			</logic:empty>
		</table>
	</fr:form>

<logic:notEmpty name="person" property="allPendingPartyContacts">
<!--  Contactos Pendentes -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2.1</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactAndAuthorization.pending"  bundle="APPLICATION_RESOURCES"/></strong></td>
	</tr>
</table>
	<jsp:include page="managePendingContacts.jsp"></jsp:include>
</logic:notEmpty>

<logic:equal name="person" property="optOutAvailable" value="true">
	<fr:form >
		<fr:edit name="person" id="edit-person">
			<fr:layout name="tabular-editable">
			</fr:layout>
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.domain.Person">
				<fr:slot name="disableSendEmails" bundle="APPLICATION_RESOURCES" key="person.disable.send.emails" layout="option-select-postback"></fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>
</logic:equal>

	<!-- Dados Pessoais -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.personal.info" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>

	<logic:present parameter="editPersonalInfo">
		<fr:edit name="person"
			action="<%="/accounts/manageAccounts.do?method=viewPerson&personId=" + personID %>">
			<fr:schema type="org.fenixedu.academic.domain.Person" bundle="APPLICATION_RESOURCES">
				<fr:slot name="givenNames" key="label.givenNames">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="familyNames" key="label.familyNames">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="gender" key="label.person.sex" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="idDocumentType" key="label.person.identificationDocumentType">
					<fr:property name="excludedValues" value="CITIZEN_CARD" />
				</fr:slot>
				<fr:slot name="documentIdNumber" key="label.person.identificationDocumentNumber"/>
				<fr:slot name="identificationDocumentSeriesNumber" key="label.person.identificationDocumentSeriesNumber">
					<fr:property name="readOnly" value="${person.idDocumentType.name ne 'IDENTITY_CARD'}" />
				</fr:slot>
				<fr:slot name="emissionLocationOfDocumentId" key="label.person.identificationDocumentIssuePlace"/>
				<fr:slot name="emissionDateOfDocumentIdYearMonthDay" key="label.person.identificationDocumentIssueDate">
					<validator class="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				</fr:slot>
				<fr:slot name="expirationDateOfDocumentIdYearMonthDay" key="label.person.identificationDocumentExpirationDate">
					<validator class="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				</fr:slot>
				<fr:slot name="socialSecurityNumber" key="label.person.contributorNumber"/>
				<fr:slot name="profession" key="label.person.occupation"/>
				<fr:slot name="maritalStatus" key="label.person.maritalStatus"/>
				<fr:slot name="dateOfBirthYearMonthDay" key="label.person.birth"/>
				<fr:slot name="country" layout="menu-select" key="label.person.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
					<fr:property name="format" value="\${countryNationality}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
				<fr:slot name="countryOfBirth" layout="menu-select" key="label.person.countryOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="format" value="\${name}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
				<fr:slot name="parishOfBirth" key="label.person.birthPlaceParish"/>
				<fr:slot name="districtSubdivisionOfBirth" key="label.person.birthPlaceMunicipality"/>
				<fr:slot name="districtOfBirth" key="label.person.birthPlaceDistrict"/>
				<%--<setter signature="setIdentificationAndNames(documentIdNumber,idDocumentType,givenNames,familyNames)"/>--%>
			</fr:schema>

			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thleft thlight mtop15 thwhite" />
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>

	</logic:present>

	<logic:notPresent parameter="editPersonalInfo">

		<fr:view name="person">
			<fr:schema type="org.fenixedu.academic.domain.Person" bundle="APPLICATION_RESOURCES">
				<fr:slot name="givenNames" key="label.givenNames">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="familyNames" key="label.familyNames">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="gender" key="label.person.sex" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="idDocumentType" key="label.person.identificationDocumentType">
					<fr:property name="excludedValues" value="CITIZEN_CARD" />
				</fr:slot>
				<fr:slot name="documentIdNumber" key="label.person.identificationDocumentNumber"/>
				<logic:equal name="person" property="idDocumentType" value="IDENTITY_CARD">
					<fr:slot name="identificationDocumentSeriesNumber" key="label.person.identificationDocumentSeriesNumber" />
				</logic:equal>
				<fr:slot name="emissionLocationOfDocumentId" key="label.person.identificationDocumentIssuePlace"/>
				<fr:slot name="emissionDateOfDocumentIdYearMonthDay" key="label.person.identificationDocumentIssueDate">
					<validator class="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				</fr:slot>
				<fr:slot name="expirationDateOfDocumentIdYearMonthDay" key="label.person.identificationDocumentExpirationDate">
					<validator class="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				</fr:slot>
				<fr:slot name="socialSecurityNumber" key="label.person.contributorNumber"/>
				<fr:slot name="profession" key="label.person.occupation"/>
				<fr:slot name="maritalStatus" key="label.person.maritalStatus"/>
				<fr:slot name="dateOfBirthYearMonthDay" key="label.person.birth"/>
				<fr:slot name="country" layout="menu-select" key="label.person.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
					<fr:property name="format" value="${countryNationality}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
				<fr:slot name="countryOfBirth" layout="menu-select" key="label.person.countryOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="format" value="${name}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
				<fr:slot name="parishOfBirth" key="label.person.birthPlaceParish"/>
				<fr:slot name="districtSubdivisionOfBirth" key="label.person.birthPlaceMunicipality"/>
				<fr:slot name="districtOfBirth" key="label.person.birthPlaceDistrict"/>
				<%--<fr:setter signature="setIdentificationAndNames(documentIdNumber,idDocumentType,givenNames,familyNames)"/>--%>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thleft thlight mtop15 thwhite" />
			</fr:layout>
		</fr:view>

		<html:link
			action="<%="/accounts/manageAccounts.do?method=viewPerson&personId=" + personID +"&editPersonalInfo=1" %>">
			<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
		</html:link>

	</logic:notPresent>



	<!-- Informa��o de Utilizador -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.login.info" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>
	<fr:view name="person"
		schema="org.fenixedu.academic.domain.Person.user.info">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
		</fr:layout>
	</fr:view>



	<!-- Filia��o -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.filiation" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>

	<logic:present parameter="editFiliationInfo">

		<fr:edit name="person"
			action="<%="/accounts/manageAccounts.do?method=viewPerson&personId=" + personID %>"
			schema="org.fenixedu.academic.domain.Person.family">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
			</fr:layout>
		</fr:edit>

	</logic:present>


	<logic:notPresent parameter="editFiliationInfo">

		<fr:view name="person"
			schema="org.fenixedu.academic.domain.Person.family">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
			</fr:layout>
		</fr:view>

		<html:link
			action="<%="/accounts/manageAccounts.do?method=viewPerson&personId=" + personID +"&editFiliationInfo=1" %>">
			<bean:message key="label.edit"  bundle="APPLICATION_RESOURCES"/>
		</html:link>

	</logic:notPresent>

</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>