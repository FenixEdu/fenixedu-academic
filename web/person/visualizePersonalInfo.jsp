<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present role="PERSON">

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.person.title.personalConsult"/></h2>

<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="float: right; border: 1px solid #aaa; padding: 3px;"/>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>

<!-- Contactos -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactAndAuthorization" /></strong></td>
	</tr>
</table>

<bean:define id="person" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person"/>
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
        <th><bean:message key="label.contact.visible.to.teachers" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.employees" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th><bean:message key="label.contact.visible.to.management" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
        <th></th>
    </tr>
    <!-- <tr>
        <td><bean:message key="label.partyContacts.Picture" /></td>
        <td>-</td>
        <td class="acenter">
            <logic:equal name="person" property="availablePhoto" value="true">
                <img src="<%= request.getContextPath() %>/images/accept.gif"/>
            </logic:equal>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr> -->
<bean:define id="phones" name="person" property="phones" />
<bean:size id="size" name="phones" />
<logic:notEmpty name="phones">
	<logic:iterate id="contact" name="phones">
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
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToPublic" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToStudents" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToStudents" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
            </td>
			<td class="tdclear">
				<html:link action="/partyContacts.do?method=prepareCreatePhone">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link>,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.clear" />
				</html:link>
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
		<td class="tdclear">
			<html:link action="/partyContacts.do?method=prepareCreatePhone">
				<bean:message key="label.add" />
			</html:link>
		</td>
	</tr>
</logic:empty>

<bean:define id="mobilePhones" name="person" property="mobilePhones" />
<bean:size id="size" name="mobilePhones" />
<logic:notEmpty name="mobilePhones">
	<logic:iterate id="contact" name="mobilePhones">
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
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToPublic" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToStudents" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToStudents" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
            </td>
			<td class="tdclear">
				<html:link action="/partyContacts.do?method=prepareCreateMobilePhone">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link>,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.clear" />
				</html:link>
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
		<td class="tdclear">
			<html:link action="/partyContacts.do?method=prepareCreateMobilePhone">
				<bean:message key="label.add" />
			</html:link>
		</td>
	</tr>
</logic:empty>

<bean:define id="emailAddresses" name="person" property="emailAddresses" />
<bean:size id="size" name="emailAddresses" />
<logic:notEmpty name="emailAddresses">
	<logic:iterate id="contact" name="emailAddresses">
		<tr>
			<td><bean:message key="label.partyContacts.EmailAddress" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</td>
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
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToPublic" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToStudents" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToStudents" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
            </td>
			<td class="tdclear">
    			<html:link action="/partyContacts.do?method=prepareCreateEmailAddress">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link><logic:notEqual name="contact" property="type.name" value="INSTITUTIONAL">,
					<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
						<bean:message key="label.clear" />
					</html:link>
				</logic:notEqual>
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
		<tr>
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
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToPublic" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToStudents" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToStudents" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToTeachers" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToTeachers" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                <logic:equal name="contact" property="visibleToEmployees" value="true">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
                </logic:equal>
                <logic:equal name="contact" property="visibleToEmployees" value="false">-</logic:equal>
            </td>
            <td class="acenter">
                    <img src="<%= request.getContextPath() %>/images/accept.gif"/>
            </td>
			<td class="tdclear">
				<html:link action="/partyContacts.do?method=prepareCreateWebAddress">
					<bean:message key="label.add" />
				</html:link>,
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link>,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.clear" />
				</html:link>
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
		<td class="tdclear">
			<html:link action="/partyContacts.do?method=prepareCreateWebAddress">
				<bean:message key="label.add" />
			</html:link>
		</td>
	</tr>
</logic:empty>
</table>
</fr:form>

<fr:form action="/visualizePersonalInfo.do">
<table class="tstyle2 thlight thleft">
    <tr>
        <td><bean:message key="label.person.availablePhoto" /></td>
        <td>
        <fr:edit layout="option-select-postback" name="person" slot="availablePhoto">
        </fr:edit>
        </td>
        <td class="switchNone">
            <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
        <bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
    </html:submit>
    </td>
    </tr>
</table>
</fr:form>

<!-- Dados Pessoais -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
	</tr>
</table>
<fr:form action="/visualizePersonalInfo.do">
	<p class="mtop15">
		<bean:message key="label.homepage.name.instructions" bundle="HOMEPAGE_RESOURCES"/>
	</p>
	<fr:edit id="nickname" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" schema="net.sourceforge.fenixedu.domain.Person.nickname">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight mbottom05 thwhite"/>
			<fr:property name="columnClasses" value=",,tdclear "/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>

<fr:view name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" schema="net.sourceforge.fenixedu.domain.Person.personal.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight mtop15 thwhite"/>
	</fr:layout>	
</fr:view>


<!-- Informação de Utilizador -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
		<td class="infoop"><strong><bean:message key="label.person.login.info" /></strong></td>
	</tr>
</table>
<fr:view name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" schema="net.sourceforge.fenixedu.domain.Person.user.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
	</fr:layout>	
</fr:view>


<!-- Filiação -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
	</tr>
</table>
<fr:view name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" schema="net.sourceforge.fenixedu.domain.Person.family">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
	</fr:layout>	
</fr:view>


<!-- Residência -->
<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.addressInfo" /></strong></td>
	</tr>
</table>
<logic:iterate id="address" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.physicalAddresses">
	<fr:view name="address" schema="contacts.PhysicalAddress.view-for-student">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thwhite"/>
		</fr:layout>	
	</fr:view>
</logic:iterate>

</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>