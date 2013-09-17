<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<logic:present name="person">
<html:xhtml/>
<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
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
<bean:define id="pendingPhysicalAddresses" name="person" property="pendingPhysicalAddresses" />
<bean:size id="size" name="pendingPhysicalAddresses" />
<logic:notEmpty name="pendingPhysicalAddresses">
	<logic:iterate id="contact" name="pendingPhysicalAddresses" type="net.sourceforge.fenixedu.domain.contacts.PhysicalAddress">
			<tr>
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.contacts.request.validation.token" bundle="MANAGER_RESOURCES"/>
					</html:link>,
					<html:link action="/partyContacts.do?method=validate" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.validate" bundle="MANAGER_RESOURCES" />
					</html:link>,
					<bean:define id="deleteURL">
						<a href="<%= request.getContextPath() %>/manager/partyContacts.do?method=deletePartyContact&contactId=<%= contact.getExternalId()%>&personID=<%= person.getExternalId() %>">
							<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES" />
						</a>
					</bean:define>
					<%= deleteURL %>				
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
    
<bean:define id="pendingPhones" name="person" property="pendingPhones" />
<bean:size id="size" name="pendingPhones" />
<logic:notEmpty name="pendingPhones">
	<logic:iterate id="contact" name="pendingPhones" type="net.sourceforge.fenixedu.domain.contacts.Phone">
			<tr>
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.contacts.request.validation.token" bundle="MANAGER_RESOURCES"/>
					</html:link>,
					<html:link action="/partyContacts.do?method=validate" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.validate" bundle="MANAGER_RESOURCES" />
					</html:link>,
					<bean:define id="deleteURL">
						<a href="<%= request.getContextPath() %>/manager/partyContacts.do?method=deletePartyContact&contactId=<%= contact.getExternalId()%>&personID=<%= person.getExternalId() %>">
							<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES" />
						</a>
					</bean:define>
					<%= deleteURL %>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>

<bean:define id="pendingMobilePhones" name="person" property="pendingMobilePhones" />
<bean:size id="size" name="pendingMobilePhones" />
<logic:notEmpty name="pendingMobilePhones">
	<logic:iterate id="contact" name="pendingMobilePhones" type="net.sourceforge.fenixedu.domain.contacts.MobilePhone">
			<tr>
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.contacts.request.validation.token" bundle="MANAGER_RESOURCES"/>
					</html:link>,
					<html:link action="/partyContacts.do?method=validate" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.validate" bundle="MANAGER_RESOURCES" />
					</html:link>,
					<bean:define id="deleteURL">
						<a href="<%= request.getContextPath() %>/manager/partyContacts.do?method=deletePartyContact&contactId=<%= contact.getExternalId()%>&personID=<%= person.getExternalId() %>">
							<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES" />
						</a>
					</bean:define>
					<%= deleteURL %>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>

<bean:define id="pendingEmailAddresses" name="person" property="pendingEmailAddresses" />
<bean:size id="size" name="pendingEmailAddresses" />
<logic:notEmpty name="pendingEmailAddresses">
	<logic:iterate id="contact" name="pendingEmailAddresses" type="net.sourceforge.fenixedu.domain.contacts.EmailAddress">
		<tr>
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
				<logic:equal name="contact" property="valid" value="false" >
					<html:link action="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.contacts.request.validation.token" bundle="MANAGER_RESOURCES"/>
					</html:link>,
					<html:link action="/partyContacts.do?method=validate" paramId="partyContactValidation" paramName="contact" paramProperty="partyContactValidation.externalId">
						<bean:message key="label.validate" bundle="MANAGER_RESOURCES" />
					</html:link>,
					<bean:define id="deleteURL">
						<a href="<%= request.getContextPath() %>/manager/partyContacts.do?method=deletePartyContact&contactId=<%= contact.getExternalId()%>&personID=<%= person.getExternalId() %>">
							<bean:message key="label.contact.validation.cancel.request" bundle="ACADEMIC_OFFICE_RESOURCES" />
						</a>
					</bean:define>
					<%= deleteURL %>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>


</table>
</fr:form>
</logic:present>