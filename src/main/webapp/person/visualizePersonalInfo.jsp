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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html:xhtml/>

<bean:define id="person" name="LOGGED_USER_ATTRIBUTE" property="person"/>

<h2>
	<bean:message key="label.person.title.personalConsult"/>
	<small class="mtop5 pull-right">		
		<html:link action="/partyContacts.do?method=viewStudentLog">
			<bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</small>
</h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
		<p class="mtop1 mvert05">
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<p>
    <span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>
		
    <!-- Photo -->
	<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
			<td class="infoop"><strong><bean:message key="label.person.title.photo" /></strong></td>
		</tr>
	</table>

	<table class="mvert1 tdtop">
		<tbody>
			<tr>
				<td align="center">
					<div class="items-container" data-toggle="tooltip" data-placement="right" title="${fr:message('resources.ApplicationResources', 'link.operator.submitPhoto')}">
						<img src="${fr:checksum('/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto')}" width="100" height="100" align="middle" style="border: 2px #eee solid"/>
						<html:link page="/uploadPhoto.do?method=prepare" styleClass="play">
							<div class="play-bg"></div>
							<span class="glyphicon glyphicon-camera"></span>
						</html:link>
					</div>
					<span class="badge" style="margin-top: 5px" data-toggle="tooltip" data-placement="bottom" title="${fr:message('resources.ApplicationResources', 'label.available.for.'.concat(person.photoAvailable ? 'public' : 'user'))}">
						${fr:message('resources.ApplicationResources', 'title.'.concat(person.photoAvailable ? 'public' : 'private'))}
					</span>
			    </td>
				<td>
                    <div style="padding: 0 0 0 2em;">
                    <div class="alert well well-sm" style="margin-top: 0; font-weight: initial">
                        <bean:message key="label.person.photo.info" />
                    </div>
                    </div>
                </td>
            </tr>
            <tr>
				<td></td>
				<td style="padding: 0 2em;">
					<span>
						<html:link page="/uploadPhoto.do?method=togglePhotoAvailability&available=${!person.photoAvailable}">
							${fr:message('resources.ApplicationResources', 'label.make.photo.'.concat(person.photoAvailable ? 'unavailable' : 'available'))}
						</html:link>
					</span>
					<span class="pleft05">
						<html:link page="/photoHistory.do?method=userHistory">
							<bean:message key="link.person.photo.history" bundle="APPLICATION_RESOURCES" />
						</html:link>
					</span>
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
<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactAndAuthorization" /></strong></td>
	</tr>
</table>

<logic:messagesPresent message="true" property="contacts">
	<ul class="nobullet list6">
		<html:messages id="messages" property="contacts" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<div class="row">
	<div class="col-sm-10">
<table class="tstyle2 thlight thleft table table-condensed">
    <tr>
        <th></th>
        <th></th>
        <th><bean:message key="label.contact.visible.to.public" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
        <th><bean:message key="label.contact.visible.to.students" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.staff" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.management" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
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
						 (<bean:message key="label.partyContacts.defaultContact" />)
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
				<html:link action="/partyContacts.do?method=prepareCreatePhysicalAddress">
					<bean:message key="label.add" />
				</html:link>,
				<logic:equal name="contact" property="valid" value="true">
					<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
						<bean:message key="label.edit" />
					</html:link>,
				</logic:equal>
				<logic:notEqual name="contact" property="fiscalAddress" value="true">
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" />
				</html:link>
				</logic:notEqual>
				<logic:equal name="contact" property="valid" value="false" >
					,<html:link action="/partyContacts.do?method=prepareValidate" paramId="partyContact" paramName="contact" paramProperty="externalId">
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
		<logic:equal name="contact" property="valid" value="false" >
			<tr style="font-style:italic;">
		</logic:equal>
		<logic:equal name="contact" property="valid" value="true" >
			<tr>
		</logic:equal>
			<td><bean:message key="label.partyContacts.Phone" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="number" />
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
				<logic:equal name="person" property="canValidateContacts" value="true">
				<html:link action="/partyContacts.do?method=prepareCreatePhone">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.edit" />
				</html:link>,
				</logic:equal>
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" />
				</html:link>
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=prepareValidate" paramId="partyContact" paramName="contact" paramProperty="externalId">
						<bean:message key="label.validate" />
					</html:link>
				</logic:equal>
			</td>
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
		<td class="tdclear">
		<logic:equal name="person" property="canValidateContacts" value="true">
			<html:link action="/partyContacts.do?method=prepareCreatePhone">
				<bean:message key="label.add" />
			</html:link>
		</logic:equal>
		</td>
	</tr>
</logic:empty>

<bean:define id="mobilePhones" name="person" property="mobilePhones" />
<bean:size id="size" name="mobilePhones" />
<logic:notEmpty name="mobilePhones">
	<logic:iterate id="contact" name="mobilePhones">
		<logic:equal name="contact" property="valid" value="false" >
			<tr style="font-style:italic;">
		</logic:equal>
		<logic:equal name="contact" property="valid" value="true" >
			<tr>
		</logic:equal>
			<td><bean:message key="label.partyContacts.MobilePhone" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="number" />
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
				<logic:equal name="person" property="canValidateContacts" value="true">
				<html:link action="/partyContacts.do?method=prepareCreateMobilePhone">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.edit" />
				</html:link>,
				</logic:equal>
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" />
				</html:link>
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=prepareValidate" paramId="partyContact" paramName="contact" paramProperty="externalId">
						<bean:message key="label.validate" />
					</html:link>
				</logic:equal>
			</td>
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
		<td class="tdclear">
		<logic:equal name="person" property="canValidateContacts" value="true">
			<html:link action="/partyContacts.do?method=prepareCreateMobilePhone">
				<bean:message key="label.add" />
			</html:link>
		</logic:equal>
		</td>
	</tr>
</logic:empty>

<bean:define id="emailAddresses" name="person" property="emailAddresses" />
<bean:size id="size" name="emailAddresses" />
<logic:notEmpty name="emailAddresses">
	<logic:iterate id="contact" name="emailAddresses">
		<logic:equal name="contact" property="valid" value="false" >
		<tr style="font-style:italic;">
		</logic:equal>
		<logic:equal name="contact" property="valid" value="true" >
		<tr>
		</logic:equal>
			<td>
				<bean:message key="label.partyContacts.EmailAddress" /> 
					(<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):
			</td>
			<td>
				<bean:write name="contact" property="value" />
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
				<logic:equal value="false" property="valid" name="contact">
					<html:link action="/partyContacts.do?method=prepareCreateEmailAddress">
						<bean:message key="Validar" />
					</html:link>
				</logic:equal>
    			<html:link action="/partyContacts.do?method=prepareCreateEmailAddress">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.edit" />
				</html:link><logic:notEqual name="contact" property="type.name" value="INSTITUTIONAL">,
					<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
						<bean:message key="label.clear" />
					</html:link>
				</logic:notEqual>
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=prepareValidate" paramId="partyContact" paramName="contact" paramProperty="externalId">
						<bean:message key="label.validate" />
					</html:link>
				</logic:equal>
			</td>
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
		<td class="tdclear">
			<html:link action="/partyContacts.do?method=prepareCreateEmailAddress">
				<bean:message key="label.add" />
			</html:link>
		</td>
	</tr>
</logic:empty>

<bean:define id="webAddresses" name="person" property="webAddresses" />
<bean:size id="size" name="webAddresses" />
<logic:notEmpty name="webAddresses">
	<logic:iterate id="contact" name="webAddresses">
		<logic:equal name="contact" property="valid" value="false" >
			<tr style="font-style:italic;">
		</logic:equal>
		<logic:equal name="contact" property="valid" value="true" >
			<tr>
		</logic:equal>
			<td><bean:message key="label.partyContacts.WebAddress" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="url" />
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
				<html:link action="/partyContacts.do?method=prepareCreateWebAddress">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.edit" />
				</html:link>,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" />
				</html:link>
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=prepareValidate" paramId="partyContact" paramName="contact" paramProperty="externalId">
						<bean:message key="label.validate" />
					</html:link>
				</logic:equal>
			</td>
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
		<td class="tdclear">
			<html:link action="/partyContacts.do?method=prepareCreateWebAddress">
				<bean:message key="label.add" />
			</html:link>
		</td>
	</tr>
</logic:empty>
</table>
<logic:notEmpty name="person" property="allPendingPartyContacts">
<!--  Contactos Pendentes -->
<table class="mtop15" width="100%" cellpadding="0" cellspacing="0" id="pendingContacts">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2.1</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactAndAuthorization.pending" /></strong></td>
	</tr>
</table>
	<jsp:include page="pendingContacts.jsp"></jsp:include>
</logic:notEmpty>


<logic:equal name="person" property="optOutAvailable" value="true">
	<fr:form action="/partyContacts.do?method=requestOptOut">
		<fr:edit name="person" id="edit-person">
			<fr:layout name="tabular-editable">
			</fr:layout>
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.domain.Person">
				<fr:slot name="disableSendEmails" bundle="APPLICATION_RESOURCES" key="person.disable.send.emails" layout="option-select-postback"></fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>
</logic:equal>

</div>
<div class="col-sm-2">
<!-- Emergency Contact -->
<fr:form action="/updateEmergencyContact.do?method=updateEmergencyContact">
	<p class="mtop15">
		<bean:message key="label.homepage.contact.emergency.instructions" bundle="HOMEPAGE_RESOURCES"/>
	</p>
	<fr:edit id="emergencyContact" name="emergencyContactBean">
		<fr:schema bundle="APPLICATION_RESOURCES" type="org.fenixedu.academic.ui.struts.action.person.UpdateEmergencyContactDA$EmergencyContactBean">
			<fr:slot name="contact" key="label.contact">
				<fr:property name="size" value="50"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight mbottom05 thwhite"/>
			<fr:property name="columnClasses" value=",,tdclear "/>
			<fr:property name="displayLabel" value="false"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>
</div>
</div>

<!-- Dados Pessoais -->
<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
	</tr>
</table>
<div class="row">
	<div class="col-sm-6">
		<fr:view name="LOGGED_USER_ATTRIBUTE" property="person" schema="org.fenixedu.academic.domain.Person.personal.info">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight mtop15"/>
			</fr:layout>	
		</fr:view>
	</div>
	<div class="col-sm-6">
		<fr:form action="/updateNickname.do?method=updateNickname">
			<p class="mtop15">
				<bean:message key="label.homepage.name.instructions" bundle="HOMEPAGE_RESOURCES"/>
			</p>
			<fr:edit id="nickname" name="personBean" schema="org.fenixedu.academic.dto.person.PersonBean.nickname">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thleft thlight mbottom05 thwhite"/>
					<fr:property name="columnClasses" value=",,tdclear "/>
					<fr:property name="displayLabel" value="false"/>
				</fr:layout>
			</fr:edit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
				<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
			</html:submit>
		</fr:form>
	</div>
	<div class="col-sm-6">
        <fr:form action="/updateHealthCardNumber.do?method=updateHealthCardNumber">
            <p class="mtop15">
                <bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/> <bean:message key="label.person.healthCardNumber" bundle="APPLICATION_RESOURCES"/> 
            </p>
            <fr:edit id="healthCardNumber" name="personBean" schema="org.fenixedu.academic.dto.person.PersonBean.healthCardNumber">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle2 thleft thlight mbottom05 thwhite"/>
                    <fr:property name="columnClasses" value=",,tdclear "/>
                    <fr:property name="displayLabel" value="false"/>
                </fr:layout>
            </fr:edit>
            <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
                <bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
            </html:submit>
        </fr:form>
    </div>
</div>

<div class="row">
	<div class="col-sm-12">
<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.fiscalInformation" /></strong></td>
	</tr>
</table>
</div>
</div>
<div class="row">
	<div class="col-sm-12">
	
		<table class="tstyle2 thleft thlight mtop15 table">
		  <tbody>
		    <tr>
		      <th scope="row">
			      	<bean:message key="label.socialSecurityNumber" bundle="APPLICATION_RESOURCES" />
		      </th>
		      <td>

		       	<logic:notEmpty name="person" property="fiscalAddress">
		       		<bean:write name="person" property="fiscalAddress.countryOfResidence.code"/>
		       		<bean:write name="person" property="socialSecurityNumber"/>
		       	</logic:notEmpty>

		       	<logic:empty name="person" property="fiscalAddress">
		       		<bean:write name="person" property="socialSecurityNumber"/>
		       	</logic:empty>
		      </td>
		    </tr>
		    <tr>
		      <th scope="row">
			      	<bean:message key="label.fiscalAddress" bundle="APPLICATION_RESOURCES" />
		      </th>
		      <td>
		       	<logic:notEmpty name="person" property="fiscalAddress">
			      	<bean:write name="person" property="fiscalAddress.address" />
			      	<bean:write name="person" property="fiscalAddress.areaCode" />
			      	<bean:write name="person" property="fiscalAddress.countryOfResidence.localizedName.content" />
		      	</logic:notEmpty>
		      </td>
		    </tr>
		  </tbody>
		</table>
	
   </div>
</div>

<!-- Informação de Utilizador -->
<div class="row">
	<div class="col-sm-6">
		<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
				<td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
			</tr>
		</table>
		<fr:view name="LOGGED_USER_ATTRIBUTE" property="person" schema="org.fenixedu.academic.domain.Person.family">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
			</fr:layout>	
		</fr:view>
	</div>
	<div class="col-sm-4">
		<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
				<td class="infoop"><strong><bean:message key="label.person.login.info" /></strong></td>
			</tr>
		</table>
		<fr:view name="LOGGED_USER_ATTRIBUTE" property="person" schema="org.fenixedu.academic.domain.Person.user.info">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
			</fr:layout>	
		</fr:view>
	</div>
</div>

<style>
.items-container{position: relative;}
.items-container:hover .play, .items-container:hover .play-bg {display: block;}
.play,.play-bg{
	position : absolute;
	text-align: center;
    display:none;
    top:0; 
    width:100%;
    height: 100%;
    margin:0 auto;
}
.play-bg {
    z-index:100;
    opacity:0.1;
    background-color: black;
}
.play>span{
	font-size: 30pt;
	color: white;
    z-index:110;
    top: 30%;
    opacity: 0.9;
}
</style>
<script>
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
</script>