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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<logic:present name="person">

	<h2><bean:message key="label.person.title.personalConsult" /></h2>
	<bean:define id="personID" name="person" property="externalId" />
	<p><span class="error0"><!-- Error messages go here --><html:errors /></span>
	</p>

	<script type="text/javascript"
		src="<%=request.getContextPath()%>/CSS/scripts/checkall.js"></script>

	<logic:notEmpty name="person" property="employee">
	   <p> 
	   	<html:link page="<%= "/professionalInformation.do?method=showProfessioanlData&personId="+ personID%>" titleKey="link.title.professionalInformation"  bundle="CONTRACTS_RESOURCES">
	   		<bean:message key="link.title.professionalInformation" bundle="CONTRACTS_RESOURCES"/>
	   	</html:link>
	   	</p>
   	</logic:notEmpty>
   	  <p> 
	   	<html:link page="<%= "/qualification.do?method=showQualifications&personID="+ personID%>" >
	   		<bean:message key="link.title.qualification" bundle="MANAGER_RESOURCES"/>
	   	</html:link>
	   	<html:link page="<%= "/qualification.do?method=viewStudentLog&personID="+ personID%>" >
	   		<bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/>
	   	</html:link>
	  </p>
	<logic:messagesPresent message="true" property="contacts">
		<ul class="nobullet list6">
			<html:messages id="messages" property="contacts" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<!-- Photo -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.photo" /></strong></td>
		</tr>
	</table>

	<table class="mvert1 tdtop">
		<tbody>
			<tr>
				<td>
                    <bean:define id="personIDForPhoto" name="person" property="username"/>
					<html:img align="middle"
					src="<%=request.getContextPath() + "/user/photo/" + personIDForPhoto.toString() %>"
					altKey="personPhoto" bundle="IMAGE_RESOURCES"
					style="border: 1px solid #aaa; padding: 3px;" /> 
				</td> 	
				<td>
				<div style="padding: 0 2em;">
				<div class="infoop2"><bean:message
					key="label.person.photo.info" /></div>
				</td>
			</tr>
		</tbody>
	</table>
	
		<p class="mvert05">
			<logic:notEmpty name="person" property="personalPhotoEvenIfRejected">
			<logic:equal name="person" property="personalPhotoEvenIfRejected.state" value="PENDING">
			<p>
				<em><bean:message key="label.person.photo.pending.info" bundle="APPLICATION_RESOURCES" /></em>
				<html:link action="/uploadPhoto.do?method=cancelSubmission">
				<bean:message key="link.person.photo.cancel.submission" bundle="APPLICATION_RESOURCES" />
				</html:link>
			</p>
			</logic:equal>
			</logic:notEmpty>
		</p>
	
	</div>

	<!-- Contactos -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.contactAndAuthorization" /></strong></td>
		</tr>
	</table>
	<logic:equal name="person" property="canValidateContacts" value="false">
	<fr:view name="person">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.domain.Person">
			<fr:slot name="numberOfValidationRequests" key="label.Person.numberOfValidationRequests"/>
			<fr:slot name="lastValidationRequestDate" key="label.Person.lastValidationRequestDate"/>
		</fr:schema>
		<fr:layout name="tabular">
        	<fr:property name="classes" value="tstyle2 thleft thlight mtop15 thwhite"/>
            <fr:property name="columnClasses" value=""/>
        </fr:layout>
	</fr:view>
	<html:link page="/partyContacts.do?method=resetValidationRequests" paramId="personID" paramName="personID">Fazer reset ao número de validações</html:link>
	</logic:equal>
	



	<fr:form action="/partyContacts.do">
		<table class="tstyle2 thlight thleft">
			<tr>
				<th></th>
				<th></th>
				<th><bean:message key="label.contact.visible.to.public"
					bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th><bean:message key="label.contact.visible.to.students"
					bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th><bean:message key="label.contact.visible.to.staff"
					bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th><bean:message key="label.contact.visible.to.management"
					bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
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
			<td><bean:message key="label.address" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="presentationValue" />
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
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
				<html:link action="<%="/partyContacts.do?method=prepareCreatePhysicalAddress&personID=" + personID %>">
					<bean:message key="label.add" />
				</html:link>,
				<logic:equal name="contact" property="valid" value="true">
					<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
						<bean:message key="label.edit" />
					</html:link>,
				</logic:equal>
				<html:link action="<%="/partyContacts.do?method=deletePartyContact&personID=" + personID%>" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" />
				</html:link>
				<logic:equal name="contact" property="valid" value="false" >
					,<html:link action="<%="/partyContacts.do?method=prepareValidate&personID=" + personID%>" paramId="partyContact" paramName="contact" paramProperty="externalId">
						<bean:message key="label.validate" />
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="physicalAddresses">
	<tr>
		<td><bean:message key="label.address" />:</td>
        <td>-</td>
        <td class="acenter">-</td>
        <td class="acenter">-</td>
        <td class="acenter">-</td>
        <td class="acenter">-</td>
		<td class="tdclear">
			<html:link action="/partyContacts.do?method=prepareCreatePhysicalAddress">
				<bean:message key="label.add" />
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
						<td><bean:message key="label.partyContacts.Phone" /> (<bean:message
							name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="number" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
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
							action="<%="/partyContacts.do?method=prepareCreatePhone&personID=" + personID%>">
							<bean:message key="label.add" />
						</html:link>, <html:link
							action="<%="/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" />
						</html:link>, <html:link
							action="<%="/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.clear" />
						</html:link></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="phones">
				<tr>
					<td><bean:message key="label.partyContacts.Phone" />:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/partyContacts.do?method=prepareCreatePhone&personID=" + personID%>">
						<bean:message key="label.add" />
					</html:link></td>
				</tr>
			</logic:empty>


			<bean:define id="mobilePhones" name="person" property="mobilePhones" />
			<bean:size id="size" name="mobilePhones" />
			<logic:notEmpty name="mobilePhones">
				<logic:iterate id="contact" name="mobilePhones">
					<bean:define id="contactId" name="contact" property="externalId" />
					<tr>
						<td><bean:message key="label.partyContacts.MobilePhone" /> (<bean:message
							name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="number" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
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
							action="<%="/partyContacts.do?method=prepareCreateMobilePhone&personID=" + personID%>">
							<bean:message key="label.add" />
						</html:link>, <html:link
							action="<%="/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" />
						</html:link>, <html:link
							action="<%="/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.clear" />
						</html:link></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="mobilePhones">
				<tr>
					<td><bean:message key="label.partyContacts.MobilePhone" />:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/partyContacts.do?method=prepareCreateMobilePhone&personID=" + personID%>">
						<bean:message key="label.add" />
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
						<td><bean:message key="label.partyContacts.EmailAddress" />
						(<bean:message name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="value" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
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
							action="<%="/partyContacts.do?method=prepareCreateEmailAddress&personID=" + personID%>">
							<bean:message key="label.add" />
						</html:link>, <html:link
							action="<%="/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" />
						</html:link> <logic:notEqual name="contact" property="type.name"
							value="INSTITUTIONAL">,
					<html:link
								action="<%="/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
								<bean:message key="label.clear" />
							</html:link>
						</logic:notEqual></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="emailAddresses">
				<tr>
					<td><bean:message key="label.partyContacts.EmailAddress" />:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/partyContacts.do?method=prepareCreateEmailAddress&personID=" + personID%>">
						<bean:message key="label.add" />
					</html:link></td>
				</tr>
			</logic:empty>

			<bean:define id="webAddresses" name="person" property="webAddresses" />
			<bean:size id="size" name="webAddresses" />
			<logic:notEmpty name="webAddresses">
				<logic:iterate id="contact" name="webAddresses">
					<bean:define id="contactId" name="contact" property="externalId" />
					<tr>
						<td><bean:message key="label.partyContacts.WebAddress" /> (<bean:message
							name="contact" property="type.qualifiedName"
							bundle="ENUMERATION_RESOURCES" />):</td>
						<td><bean:write name="contact" property="url" /> <logic:equal
							name="contact" property="defaultContact" value="true">
							<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
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
							action="<%="/partyContacts.do?method=prepareCreateWebAddress&personID=" + personID%>">
							<bean:message key="label.add" />
						</html:link>, <html:link
							action="<%="/partyContacts.do?method=prepareEditPartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.edit" />
						</html:link> <html:link
							action="<%="/partyContacts.do?method=deletePartyContact&personID=" + personID + "&contactId="+contactId %>">
							<bean:message key="label.clear" />
						</html:link></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="webAddresses">
				<tr>
					<td><bean:message key="label.partyContacts.WebAddress" />:</td>
					<td>-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="acenter">-</td>
					<td class="tdclear"><html:link
						action="<%="/partyContacts.do?method=prepareCreateWebAddress&personID=" + personID%>">
						<bean:message key="label.add" />
					</html:link></td>
				</tr>
			</logic:empty>
		</table>
	</fr:form>

<logic:equal name="person" property="optOutAvailable" value="true">
	<fr:form action="<%= "/partyContacts.do?method=requestOptOut&personID=" + personID %>">
		<fr:edit name="person" id="edit-person">
			<fr:layout name="tabular-editable">
			</fr:layout>
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.domain.Person">
				<fr:slot name="disableSendEmails" bundle="APPLICATION_RESOURCES" key="person.disable.send.emails" layout="option-select-postback"></fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>
</logic:equal>


<logic:notEmpty name="person" property="allPendingPartyContacts">
<!--  Contactos Pendentes -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2.1</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactAndAuthorization.pending" /></strong></td>
	</tr>
</table>
	<jsp:include page="managePendingContacts.jsp"></jsp:include>
</logic:notEmpty>

	<!-- Dados Pessoais -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.personal.info" /></strong></td>
		</tr>
	</table>

	<logic:present parameter="editPersonalInfo">
		<logic:messagesPresent message="true">
			<ul class="nobullet list6">
				<html:messages id="messages" message="true">
					<li><span class="error0"><bean:write name="messages" /></span></li>
				</html:messages>
			</ul>
		</logic:messagesPresent>
		<fr:edit name="person"
			action="<%="/findPerson.do?method=viewPerson&personID=" + personID %>"
			schema="org.fenixedu.academic.domain.Person.personal.info.withPartitionedNames">
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thleft thlight mtop15 thwhite" />
			</fr:layout>
		</fr:edit>

	</logic:present>

	<logic:notPresent parameter="editPersonalInfo">

		<fr:view name="person"
			schema="org.fenixedu.academic.domain.Person.personal.info.withPartitionedNames">
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thleft thlight mtop15 thwhite" />
			</fr:layout>
		</fr:view>

		<html:link
			action="<%="/findPerson.do?method=viewPerson&personID=" + personID +"&editPersonalInfo=1" %>">
			<bean:message key="label.edit" />
		</html:link>

	</logic:notPresent>



	<!-- Informaï¿½ï¿½o de Utilizador -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.login.info" /></strong></td>
		</tr>
	</table>
	<logic:present parameter="editUserInfo">
		<fr:edit name="person"
			action="<%="/findPerson.do?method=viewPerson&personID=" + personID %>">
			<fr:schema bundle="APPLICATION_RESOURCES" type="org.fenixedu.academic.domain.Person">
				<fr:slot readOnly="true" name="user.username" key="label.person.username"/>
				<fr:slot name="eidentifier" key="label.eidentifier" layout="null-as-value"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thleft thlight mtop15 thwhite" />
			</fr:layout>
		</fr:edit>
	</logic:present>
	<logic:notPresent parameter="editUserInfo">
		<fr:view name="person">
			<fr:schema bundle="APPLICATION_RESOURCES" type="org.fenixedu.academic.domain.Person">
				<fr:slot name="user.username" key="label.person.username"/>
				<fr:slot name="eidentifier" key="label.eidentifier" layout="null-as-value"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
			</fr:layout>
		</fr:view>
		<html:link
			action="<%="/findPerson.do?method=viewPerson&personID=" + personID +"&editUserInfo=1" %>">
			<bean:message key="label.edit" />
		</html:link>
	</logic:notPresent>
	



	<!-- Filiaï¿½ï¿½o -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.filiation" /></strong></td>
		</tr>
	</table>

	<logic:present parameter="editFiliationInfo">

		<fr:edit name="person"
			action="<%="/findPerson.do?method=viewPerson&personID=" + personID %>"
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
			action="<%="/findPerson.do?method=viewPerson&personID=" + personID +"&editFiliationInfo=1" %>">
			<bean:message key="label.edit" />
		</html:link>

	</logic:notPresent>

	<!-- Residï¿½ncia -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">6</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.addressInfo" /></strong></td>
		</tr>
	</table>
	<logic:iterate id="address" name="person" property="physicalAddresses">

		<bean:define id="addressID" name="address" property="externalId" />
		
		<fr:view name="address"
			schema="contacts.PhysicalAddress.view-for-student">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
			</fr:layout>
		</fr:view>

		<html:link
			action="<%="/partyContacts.do?method=prepareCreatePhysicalAddress&personID=" + personID %>">
			<bean:message key="label.add" />
		</html:link>
		<html:link
			action="<%="/partyContacts.do?method=prepareEditPartyContact&personID=" + personID +"&contactId=" + addressID %>">
			<bean:message key="label.edit" />
		</html:link>
		<html:link
			action="<%="/partyContacts.do?method=deletePartyContact&personID=" + personID +"&contactId=" + addressID %>">
			<bean:message key="label.remove" />
		</html:link>
	</logic:iterate>
	
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
