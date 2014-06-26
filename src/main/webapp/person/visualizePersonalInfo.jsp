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
<%@ page import="org.fenixedu.bennu.core.i18n.BundleUtil" %>
<%@ page import="net.sourceforge.fenixedu.domain.Person" %>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<logic:present role="role(PERSON)">
<bean:define id="person" name="LOGGED_USER_ATTRIBUTE" property="person"/>

<h2><bean:message key="label.person.title.personalConsult"/></h2>

<p>
    <span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>


		
<span class="mtop5 mbottom5">		
	<html:link action="/partyContacts.do?method=viewStudentLog">
		<bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</span>
		
<script type="text/javascript" src="<%=request.getContextPath()%>/CSS/scripts/checkall.js"></script>

    <!-- Photo -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
			<td class="infoop"><strong><bean:message key="label.person.title.photo" /></strong></td>
		</tr>
	</table>

	<table class="mvert1 tdtop">
		<tbody>
			<tr>
				<td>
				    <html:img align="middle"
					src="<%=request.getContextPath() + "/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto"%>"
					altKey="personPhoto" bundle="IMAGE_RESOURCES"
					style="border: 1px solid #aaa; padding: 3px;" />
			    </td>
				<td>
                    <div style="padding: 0 2em;">
                    <div class="infoop2">
                        <bean:message key="label.person.photo.info" />
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
	
	
	<p> 
		<span class="mtop1 mbottom0">
			<html:link page="/uploadPhoto.do?method=prepare">
				<bean:message key="link.person.upload.photo" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</span>
		
		<span class="pleft05">
			<html:link page="/photoHistory.do?method=userHistory">
				<bean:message key="link.person.photo.history" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</span>
	</p>
	
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

	<fr:form  action="/partyContacts.do">
<table class="tstyle2 thlight thleft">
    <tr>
        <th></th>
        <th></th>
        <th><bean:message key="label.contact.visible.to.public" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
        <th><bean:message key="label.contact.visible.to.students" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.alumni" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.teachers" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.employees" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
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
                <logic:equal name="contact" property="visibleToAlumni" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToAlumni" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
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
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="externalId">
					<bean:message key="label.clear" />
				</html:link>
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
                <logic:equal name="contact" property="visibleToAlumni" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToAlumni" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
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
                <logic:equal name="contact" property="visibleToAlumni" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToAlumni" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
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
                <logic:equal name="contact" property="visibleToAlumni" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToAlumni" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
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
                <logic:equal name="contact" property="visibleToAlumni" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToAlumni" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%=request.getContextPath()%>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
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
</fr:form>


<logic:notEmpty name="person" property="allPendingPartyContacts">
<!--  Contactos Pendentes -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0" id="pendingContacts">
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
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.Person">
				<fr:slot name="disableSendEmails" bundle="APPLICATION_RESOURCES" key="person.disable.send.emails" layout="option-select-postback"></fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>
</logic:equal>


<!-- Dados Pessoais -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
	</tr>
</table>
<fr:form action="/visualizePersonalInfo.do">
	<p class="mtop15">
		<bean:message key="label.homepage.name.instructions" bundle="HOMEPAGE_RESOURCES"/>
	</p>
	<fr:edit id="nickname" name="LOGGED_USER_ATTRIBUTE" property="person" schema="net.sourceforge.fenixedu.domain.Person.nickname">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight mbottom05 thwhite"/>
			<fr:property name="columnClasses" value=",,tdclear "/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="button.update.nickname" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:submit>
</fr:form>

<!------------- Partial Names ------------------>
<link rel='stylesheet' href='http://codepen.io/assets/libs/fullpage/jquery-ui.css'>
<script src='http://codepen.io/assets/libs/fullpage/jquery_and_jqueryui.js'></script>
<%
	final Person p = (Person) person;
	final String[] nameParts = p.getName().split(" ");
	final int init_pos = (p.getGivenNames() == null ) ? 0 : p.getGivenNames().split(" ").length - 1;
%>

<style >
#slider .ui-slider-range { 
	background: #009ee3; 
}

#slider .ui-slider-handle { 
	border-color: transparent; 
	background: transparent;
	outline: none;
	
}

#sliderIMG {
    position:absolute;
    margin: -5px 0px 0px -15px;
}

.test {
	width: 42px;
	height:10px;
	color: red;
}

.partialName {
    padding-right: 15px;
    padding-bottom: 5px;
	display:table-cell;
}
</style>

<script>
$(function() {
	var names = [];
	var slider_values = $.makeArray($('.partialName').map(function(i,e){return $(e).position().left + $(e).width() - 10}));
	
	<%
	for(int i = 0; i < nameParts.length; i++) {
	%>
		names.push( "<%= nameParts[i] %>" );
	<%
	}
	%>
    var last_i = -1;
	var slider = $( "#slider" ).slider({
	      range: "min",
	      min: 0,
	      max: $('.partialName').last().position().left + $('.partialName').last().width(),
	      slide: function( event, ui ) {
	        var i = findSelectedNames(ui.value);
			var GNames = names.slice(0,i).toString().replace(/\,/g, ' ');
			var FNames = names.slice(i).toString().replace(/\,/g, ' ');
	        slider.slider('value', slider_values[i-1]);
	        $("#sliderIMG").css('left', $('.ui-slider-handle').position().left);
			if(last_i != i) {
				last_i = i;
				$.post($('#saveNames').attr('href'), { given: GNames, family: FNames  })
					.done(function() {
					        $('#GNames').text(GNames);
					        $('#FNames').text(FNames);							
						});
		    }
	    	return false;
	      }
	});
	function findSelectedNames(value) {
	    var i;
	    for(i = 1; i < slider_values.length; i++) {
	    	if(value < slider_values[i]) {
	        	break;
	        }
	    }
	    return i;
	}
});

$(document).ready(function(){
	//slider
	$('#slider').width($('.partialName').last().position().left + $('.partialName').last().width());
	$('#slider').slider('value', $('#name<%= init_pos %>').position().left + $('#name<%= init_pos %>').width() - 10);
	//image
	$('#sliderIMG').height($('.ui-slider-handle').height() * 1.5);
	$('#sliderIMG').width($('.ui-slider-handle').width() * 1.5);
    $("#sliderIMG").css('top', $('.ui-slider-handle').position().top);
    $('#sliderIMG').css('left',$('#name<%= init_pos %>').position().left + $('#name<%= init_pos %>').width() - 10);
});

</script>

<br>
<p><bean:message key="label.partialNames" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<table class="tstyle2 thleft thlight mtop15 thwhite">
	<tr>
		<th><bean:message key="label.givenNames"/>:</th>
		<td > <span id="GNames">${person.givenNames}</span> </td>
	</tr>
	<tr>
		<th><bean:message key="label.familyNames" />:</th>
		<td ><span id="FNames">${person.familyNames}</span></td>
	</tr>
</table>

<div class="partialNameSelector">
<%
	int name_size = 0;
	for(int idx = 0; idx < nameParts.length; idx++) {
%>
	<div class="partialName" id="name<%= idx %>"><span><%= nameParts[idx] %></span></div>
<%
}
%>
<div id="slider" style="overflow-y:visible;">
	<img src="<%= request.getContextPath() %>/images/text_slider.png" id="sliderIMG"/>
</div>
</div>
<a class="hide btn btn-primary" href="${pageContext.request.contextPath}/person/partyContacts.do?method=saveNamesDivision" id="saveNames">
	<bean:message key="button.update.partialName" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</a>
	
<!-- End Division of Names -->


<fr:view name="LOGGED_USER_ATTRIBUTE" property="person">
	<fr:schema type="net.sourceforge.fenixedu.domain.Person" bundle="APPLICATION_RESOURCES">
	    <fr:slot name="name" key="label.person.name">
	        <fr:property name="size" value="50"/>
	    </fr:slot> 
	   <fr:slot name="gender" key="label.person.sex" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
	    <fr:slot name="idDocumentType" key="label.person.identificationDocumentType">
	        <fr:property name="excludedValues" value="CITIZEN_CARD" />
	    </fr:slot>
	    <fr:slot name="documentIdNumber" key="label.person.identificationDocumentNumber"/>
	    <fr:slot name="emissionLocationOfDocumentId" key="label.person.identificationDocumentIssuePlace"/> 
	    <fr:slot name="emissionDateOfDocumentIdYearMonthDay" key="label.person.identificationDocumentIssueDate"/>
	    <fr:slot name="expirationDateOfDocumentIdYearMonthDay" key="label.person.identificationDocumentExpirationDate"/>
	    <fr:slot name="socialSecurityNumber" key="label.person.contributorNumber"/>
	    <fr:slot name="profession" key="label.person.occupation"/>
	    <fr:slot name="maritalStatus" key="label.person.maritalStatus"/>
	    <fr:slot name="dateOfBirthYearMonthDay" key="label.person.birth"/>
	    <fr:slot name="country" layout="menu-select" key="label.person.country" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" > 
	        <fr:property name="format" value="${countryNationality}"/>
	        <fr:property name="sortBy" value="name=asc" />
	        <fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
	    </fr:slot> 
	    <fr:slot name="countryOfBirth" layout="menu-select" key="label.person.countryOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
	        <fr:property name="format" value="${name}"/>
	        <fr:property name="sortBy" value="name=asc" />
	        <fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
	    </fr:slot>
	    <fr:slot name="parishOfBirth" key="label.person.birthPlaceParish"/>
	    <fr:slot name="districtSubdivisionOfBirth" key="label.person.birthPlaceMunicipality"/>
	    <fr:slot name="districtOfBirth" key="label.person.birthPlaceDistrict"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight mtop15 thwhite"/>
	</fr:layout>	
</fr:view>


<!-- Informação de Utilizador -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
		<td class="infoop"><strong><bean:message key="label.person.login.info" /></strong></td>
	</tr>
</table>
<fr:view name="LOGGED_USER_ATTRIBUTE" property="person" schema="net.sourceforge.fenixedu.domain.Person.user.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
	</fr:layout>	
</fr:view>


<!-- Filiação -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
	</tr>
</table>
<fr:view name="LOGGED_USER_ATTRIBUTE" property="person" schema="net.sourceforge.fenixedu.domain.Person.family">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
	</fr:layout>	
</fr:view>

</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>