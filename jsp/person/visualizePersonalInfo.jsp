<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present role="PERSON">

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.person.title.personalConsult" /></h2>

<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="float: right; border: 1px solid #aaa; padding: 3px;"/>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>


<table class="mtop15" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">1</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.contactInfo" /></strong></td>
	</tr>
</table>

<bean:define id="person" name="UserView" property="person"/>
<logic:messagesPresent message="true" property="contacts">
	<ul class="nobullet list6">
		<html:messages id="messages" property="contacts" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<table class="tstyle4 thlight thleft">

<bean:define id="phones" name="person" property="phones" />
<bean:size id="size" name="phones" />
<logic:notEmpty name="phones">
	<logic:iterate id="contact" name="phones">
		<tr>
			<th><bean:message key="label.partyContacts.Phone" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</th>
			<td>
				<bean:write name="contact" property="number" />
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
					</logic:notEqual>
					
				</logic:equal>
			</td>
			<td>
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link>
				,
				<html:link action="/partyContacts.do?method=prepareCreatePhone" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.add" />
				</html:link>
				,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.clear" />
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="phones">
	<tr>
		<th><bean:message key="label.partyContacts.Phone" />:</th>
		<td class="acenter">-</td>
		<td>
			<html:link action="/partyContacts.do?method=prepareCreatePhone" paramId="contactId" paramName="contact" paramProperty="idInternal">
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
			<th><bean:message key="label.partyContacts.MobilePhone" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</th>
			<td>
				<bean:write name="contact" property="number" />
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
					</logic:notEqual>
					
				</logic:equal>
			</td>
			<td>
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link>
				,
				<html:link action="/partyContacts.do?method=prepareCreateMobilePhone">
					<bean:message key="label.add" />
				</html:link>
				,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.clear" />
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="mobilePhones">
	<tr>
		<th><bean:message key="label.partyContacts.MobilePhone" />:</th>
		<td class="acenter">-</td>
		<td>
			<html:link action="/partyContacts.do?method=prepareCreateMobilePhone" paramId="contactId" paramName="contact" paramProperty="idInternal">
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
			<th><bean:message key="label.partyContacts.EmailAddress" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</th>
			<td>
				<bean:write name="contact" property="value" />
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
					</logic:notEqual>
					
				</logic:equal>
			</td>
			<td>
				<logic:notEqual name="contact" property="type.name" value="INSTITUTIONAL">
					<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
						<bean:message key="label.edit" />
					</html:link>
					,
					<html:link action="/partyContacts.do?method=prepareCreateEmailAddress">
						<bean:message key="label.add" />
					</html:link>
					,
					<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
						<bean:message key="label.clear" />
					</html:link>
				</logic:notEqual>
				<logic:equal name="contact" property="type.name" value="INSTITUTIONAL">
					<html:link action="/partyContacts.do?method=prepareCreateEmailAddress">
						<bean:message key="label.add" />
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="emailAddresses">
	<tr>
		<th><bean:message key="label.partyContacts.EmailAddress" />:</th>
		<td class="acenter">-</td>
		<td>
			<html:link action="/partyContacts.do?method=prepareCreateEmailAddress" paramId="contactId" paramName="contact" paramProperty="idInternal">
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
			<th><bean:message key="label.partyContacts.WebAddress" /> (<bean:message name="contact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />):</th>
			<td>
				<bean:write name="contact" property="url" />
				<logic:equal name="contact" property="defaultContact" value="true">
					<logic:notEqual name="size" value="1">
						 (<bean:message key="label.partyContacts.defaultContact" />)
					</logic:notEqual>
					
				</logic:equal>
			</td>
			<td>
				<html:link action="/partyContacts.do?method=prepareEditPartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.edit" />
				</html:link>
				,
				<html:link action="/partyContacts.do?method=prepareCreateWebAddress">
					<bean:message key="label.add" />
				</html:link>
				,
				<html:link action="/partyContacts.do?method=deletePartyContact" paramId="contactId" paramName="contact" paramProperty="idInternal">
					<bean:message key="label.clear" />
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</logic:notEmpty>
<logic:empty name="webAddresses">
	<tr>
		<th><bean:message key="label.partyContacts.WebAddress" />:</th>
		<td class="acenter">-</td>
		<td>
			<html:link action="/partyContacts.do?method=prepareCreateWebAddress" paramId="contactId" paramName="contact" paramProperty="idInternal">
				<bean:message key="label.add" />
			</html:link>
		</td>
	</tr>
</logic:empty>
</table>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">2</span></td>
		<td class="infoop"><strong><bean:message key="label.person.information.to.publish" /></strong></td>
	</tr>
</table>
<br />
<fr:form action="/visualizePersonalInfo.do">
	<fr:edit id="photo" name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.information.to.publish">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.personal.info" /></strong></td>
	</tr>
</table>
<fr:form action="/visualizePersonalInfo.do">
	<br/>
	<bean:message key="label.homepage.name.instructions" bundle="HOMEPAGE_RESOURCES"/>
	<fr:edit id="nickname" name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.nickname">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight"/>
			<fr:property name="columnClasses" value=",,tdclear "/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="mvert05">
		<bean:message key="person.homepage.update" bundle="HOMEPAGE_RESOURCES"/>
	</html:submit>
</fr:form>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.personal.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
		<td class="infoop"><strong><bean:message key="label.person.login.info" /></strong></td>
	</tr>
</table>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.user.info">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.filiation" /></strong></td>
	</tr>
</table>
<fr:view name="UserView" property="person" schema="net.sourceforge.fenixedu.domain.Person.family">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight"/>
	</fr:layout>	
</fr:view>

<br/>
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop" width="25"><span class="emphasis-box">6</span></td>
		<td class="infoop"><strong><bean:message key="label.person.title.addressInfo" /></strong></td>
	</tr>
</table>
<logic:iterate id="address" name="UserView" property="person.physicalAddresses">
	<fr:view name="address" schema="contacts.PhysicalAddress.view-for-student">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thleft thlight"/>
		</fr:layout>	
	</fr:view>
</logic:iterate>

</logic:present>