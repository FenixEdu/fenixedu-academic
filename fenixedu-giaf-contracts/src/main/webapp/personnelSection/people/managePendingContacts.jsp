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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<logic:present name="person">
<html:xhtml/>
<bean:define id="person" name="person" type="org.fenixedu.academic.domain.Person"/>
<fr:form  action="/partyContacts.do">
<table class="tstyle2 thlight thleft">
    <tr>
        <th></th>
        <th></th>
        <th><bean:message key="label.contact.visible.to.public" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
        <th><bean:message key="label.contact.visible.to.students" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.staff" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.management" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th></th>
    </tr>
<bean:define id="pendingPhysicalAddresses" name="person" property="pendingPhysicalAddresses" />
<bean:size id="size" name="pendingPhysicalAddresses" />
<logic:notEmpty name="pendingPhysicalAddresses">
	<logic:iterate id="contact" name="pendingPhysicalAddresses" type="org.fenixedu.academic.domain.contacts.PhysicalAddress">
			<tr>
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="<%="/partyContacts.do?method=deletePartyContact&contactId="+contact.getExternalId()%>" paramId="personID" paramName="person" paramProperty="externalId">
						<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
    
<bean:define id="pendingPhones" name="person" property="pendingPhones" />
<bean:size id="size" name="pendingPhones" />
<logic:notEmpty name="pendingPhones">
	<logic:iterate id="contact" name="pendingPhones" type="org.fenixedu.academic.domain.contacts.Phone">
			<tr>
			<td><bean:message key="label.partyContacts.Phone" bundle="APPLICATION_RESOURCES"/> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="number" />
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="<%="/partyContacts.do?method=deletePartyContact&contactId="+contact.getExternalId()%>" paramId="personID" paramName="person" paramProperty="externalId">
						<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>

<bean:define id="pendingMobilePhones" name="person" property="pendingMobilePhones" />
<bean:size id="size" name="pendingMobilePhones" />
<logic:notEmpty name="pendingMobilePhones">
	<logic:iterate id="contact" name="pendingMobilePhones" type="org.fenixedu.academic.domain.contacts.MobilePhone">
			<tr>
			<td><bean:message key="label.partyContacts.MobilePhone" bundle="APPLICATION_RESOURCES"/> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
			<td>
				<bean:write name="contact" property="number" />
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="<%="/partyContacts.do?method=deletePartyContact&contactId="+contact.getExternalId()%>" paramId="personID" paramName="person" paramProperty="externalId">
						<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>

<bean:define id="pendingEmailAddresses" name="person" property="pendingEmailAddresses" />
<bean:size id="size" name="pendingEmailAddresses" />
<logic:notEmpty name="pendingEmailAddresses">
	<logic:iterate id="contact" name="pendingEmailAddresses" type="org.fenixedu.academic.domain.contacts.EmailAddress">
		<tr>
			<td>
				<bean:message key="label.partyContacts.EmailAddress" bundle="APPLICATION_RESOURCES"/> 
					(<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):
			</td> 	
			<td>
				<bean:write name="contact" property="value" />
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="<%="/partyContacts.do?method=deletePartyContact&contactId="+contact.getExternalId()%>" paramId="personID" paramName="person" paramProperty="externalId">
						<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>


</table>
</fr:form>
</logic:present>