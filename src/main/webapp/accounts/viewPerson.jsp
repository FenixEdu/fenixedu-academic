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
<%@page import="org.fenixedu.academic.domain.Country"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page isELIgnored="true"%>

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
				<logic:notEmpty name="contact" property="areaCode">
					<bean:write name="contact" property="areaCode" />
				</logic:notEmpty>				
				<logic:notEmpty name="contact" property="districtSubdivisionOfResidence">
					<bean:write name="contact" property="districtSubdivisionOfResidence" />
				</logic:notEmpty>
				<logic:notEmpty name="contact" property="countryOfResidence">
					<bean:write name="contact" property="countryOfResidence.localizedName.content" />
				</logic:notEmpty>
				
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" bundle="APPLICATION_RESOURCES"/>)
					</logic:notEqual>
				</logic:equal>
				<logic:equal name="contact" property="fiscalAddress" value="true">
						 <em class="highlight1"><bean:message key="label.fiscalAddress" /></em>
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
				<logic:notEqual name="contact" property="fiscalAddress" value="true">
				<html:link action="<%="/accounts/partyContacts.do?method=deletePartyContact&personID=" + personID%>" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" bundle="APPLICATION_RESOURCES"/>
				</html:link>
				</logic:notEqual>
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

	<logic:equal name="editPersonalInfo" value="true">
		<bean:define id="personID" name="person" property="externalId" />
		
		<form id="backToViewForm" action="<%="/accounts/manageAccounts.do?method=viewPerson&personId=" + personID %>" method="post">
		</form>
		
		<fr:form action="<%="/accounts/manageAccounts.do?method=editPersonalData&personId=" + personID %>">
			<bean:define id="personBean" name="personBean" type="org.fenixedu.academic.dto.person.PersonBean" />
			<% 
				if(personBean.getFiscalCountry() != null) {
				    pageContext.setAttribute("countryCode", personBean.getFiscalCountry().getCode());
				} else {
				    pageContext.setAttribute("countryCode", Country.readDefault().getCode());
				}
			%>
	
			<fr:edit id="personBean" name="personBean" visible="false" />
			<fr:edit id="personBean-edit" name="personBean">
				<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="APPLICATION_RESOURCES">
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
				    <fr:slot name="documentIdEmissionLocation" key="label.person.identificationDocumentIssuePlace"/> 
				    <fr:slot name="documentIdEmissionDate" key="label.person.identificationDocumentIssueDate">
				        <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				    </fr:slot>  
				    <fr:slot name="documentIdExpirationDate" key="label.person.identificationDocumentExpirationDate">
				        <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				    </fr:slot>
<%--
					<fr:slot name="fiscalCountry" key="label.fiscalCountry" layout="menu-select-postback">
							<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.CountryProvider" />
							<fr:property name="format" value="${name} (${code})"/>
							<fr:property name="sortBy" value="name"/>
							<fr:property name="destination" value="fiscalCountryPostback" />
					</fr:slot>
				    <fr:slot name="socialSecurityNumber" key="label.person.contributorNumber" >
						<fr:validator name="org.fenixedu.ulisboa.specifications.ui.renderers.validators.FiscalCodeValidator" >
							<fr:property name="countryCode" value="<%= (String) pageContext.getAttribute("countryCode") %>" />
						</fr:validator>
				    </fr:slot>
--%>				    
				    <fr:slot name="profession" key="label.person.occupation"/>
				    <fr:slot name="maritalStatus" key="label.person.maritalStatus"/>
				    <fr:slot name="dateOfBirth" key="label.person.birth"/>
				    <fr:slot name="nationality" layout="menu-select" key="label.person.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" > 
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
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes"
						value="tstyle2 thleft thlight mtop15 thwhite" />
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
	
				<fr:destination name="invalid" path='<%="/accounts/manageAccounts.do?method=editPersonalDataInvalid&personId=" + personID %>'/>
				<fr:destination name="fiscalCountryPostback" path='<%="/accounts/manageAccounts.do?method=editPersonalDataPostback&personId=" + personID %>' />
			</fr:edit>

			<p>
				<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
				<html:cancel onclick="jQuery('#backToViewForm').submit(); return false;" ><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
			</p>
		</fr:form>


	</logic:equal>

	<logic:equal name="editPersonalInfo" value="false">

		<fr:view name="person">
			<fr:schema type="org.fenixedu.academic.domain.Person" bundle="APPLICATION_RESOURCES">
			    <fr:slot name="givenNames" key="label.givenNames">
			        <fr:property name="size" value="50" />
			    </fr:slot>
			    <fr:slot name="familyNames" key="label.familyNames">
			        <fr:property name="size" value="50" />
			    </fr:slot>
				<fr:slot name="displayName" key="label.person.nickname">
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
			    <fr:slot name="profession" key="label.person.occupation"/>
			    <fr:slot name="maritalStatus" key="label.person.maritalStatus"/>
			    <fr:slot name="dateOfBirthYearMonthDay" key="label.person.birth"/>
			    <fr:slot name="country" layout="menu-select" key="label.person.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" > 
					<fr:property name="format" value="${countryNationality.content}"/>
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
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thleft thlight mtop15 thwhite" />
			</fr:layout>
		</fr:view>

		<html:link action="<%="/accounts/manageAccounts.do?method=prepareEditPersonalData&personId=" + personID %>">
			<bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/>
		</html:link>

	</logic:equal>

	<!-- Dados Fiscais -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
			<td class="infoop"><strong><bean:message key="label.person.title.fiscalInformation" bundle="APPLICATION_RESOURCES"/></strong></td>
		</tr>
	</table>

    <fr:view name="person">
        <fr:schema type="org.fenixedu.academic.domain.Person" bundle="APPLICATION_RESOURCES" >
        	<logic:notEmpty name="person" property="fiscalAddress">
        	<logic:notEmpty name="person" property="socialSecurityNumber">
			<fr:slot name="this" layout="format" key="label.socialSecurityNumber" bundle="APPLICATION_RESOURCES">
				<fr:property name="format" value="${fiscalAddress.countryOfResidence.code}  ${socialSecurityNumber}" />
			</fr:slot>
        	</logic:notEmpty>
        	</logic:notEmpty>
        	
        	<logic:empty name="person" property="fiscalAddress">
        	<logic:notEmpty name="person" property="socialSecurityNumber">
			<fr:slot name="this" layout="format" key="label.socialSecurityNumber" bundle="APPLICATION_RESOURCES">
				<fr:property name="format" value="${socialSecurityNumber}" />
			</fr:slot>
        	</logic:notEmpty>
        	</logic:empty>

			
        	<fr:slot name="fiscalAddress">
				<fr:property name="format" value="${address} ${areaCode} ${countryOfResidence.name}" />
        	</fr:slot>
        </fr:schema>
	    <fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle2 thleft thlight mtop15 thwhite" />
	    </fr:layout>
    </fr:view>

    <div class="mbottom2">
		<html:link action="<%="/accounts/manageAccounts.do?method=prepareEditFiscalData&personId=" + personID %>">
			<bean:message key="label.edit" bundle="APPLICATION_RESOURCES" />
		</html:link>
    </div>

	<!-- Informacao de Utilizador -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
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



	<!-- Filiacao -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">6</span></td>
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