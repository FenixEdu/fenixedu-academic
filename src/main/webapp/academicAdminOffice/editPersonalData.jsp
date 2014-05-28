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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/student.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editPersonalData"/>
	<bean:define id="studentID" type="java.lang.String" name="student" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID" value="<%= studentID.toString() %>"/>

	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personData" name="personBean" schema="student.personalData-withoutProfessionDetails" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personDocumentId" name="personBean" schema="student.documentId-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personFiliation" name="personBean" schema="student.filiation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='visualizeStudent';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
	
	<div class="infoop2 mtop2">
		<p>A introdução de contactos no sistema Fénix utiliza um mecanismo de validação automática.</p>
		<p>A secretaria académica ao introduzir uma morada esta ficará automáticamente validada no sistema.</p>
		</p>Qualquer outro contacto (excepto homepages) necessitará de validação por parte do utilizador em questão.</p>
		<p>O mesmo deve fazer login no sistema Fénix e proceder à sua validação.</p> 
	</p>	
</div>
	
	
	<h3 class="mtop2 mbottom025"><bean:message key="label.person.title.addressesInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:view name="personBean" property="sortedPhysicalAdresses" schema="contacts.PhysicalAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
			
			<fr:property name="linkFormat(edit)" value="<%="/partyContacts.do?method=prepareEditPartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="1"/>
			
			<fr:property name="linkFormat(delete)" value="<%="/partyContacts.do?method=deletePartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>

	<div class="mbottom2">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/partyContacts.do?method=prepareCreatePhysicalAddress" paramId="studentID" paramName="studentID">
			<bean:message key="label.partyContacts.addPhysicalAddress" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</div>

	
	<h3 class="mbottom05"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	
	<bean:message key="label.phones" bundle="ACADEMIC_OFFICE_RESOURCES" />
	<fr:view name="personBean" property="sortedPhones" schema="contacts.Phone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />

			<fr:property name="linkFormat(edit)" value="<%="/partyContacts.do?method=prepareEditPartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="1"/>
			
			<fr:property name="linkFormat(delete)" value="<%="/partyContacts.do?method=deletePartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>
	
	<div class="mbottom2">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/partyContacts.do?method=prepareCreatePhone" paramId="studentID" paramName="studentID">
			<bean:message key="label.partyContacts.addPhone" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</div>
	
	<bean:message key="label.mobilePhones" bundle="ACADEMIC_OFFICE_RESOURCES" />
	<fr:view name="personBean" property="sortedMobilePhones" schema="contacts.MobilePhone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />

			<fr:property name="linkFormat(edit)" value="<%="/partyContacts.do?method=prepareEditPartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="1"/>

			<fr:property name="linkFormat(delete)" value="<%="/partyContacts.do?method=deletePartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>
	
	<div class="mbottom2">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/partyContacts.do?method=prepareCreateMobilePhone" paramId="studentID" paramName="studentID">
			<bean:message key="label.partyContacts.addMobilePhone" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</div>


	<bean:message key="label.email" bundle="ACADEMIC_OFFICE_RESOURCES" />
	<fr:view name="personBean" property="sortedEmailAddresses" schema="contacts.EmailAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />

			<fr:property name="linkFormat(edit)" value="<%="/partyContacts.do?method=prepareEditPartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(edit)" value="institutionalType"/>
			<fr:property name="order(edit)" value="1"/>

			<fr:property name="linkFormat(delete)" value="<%="/partyContacts.do?method=deletePartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(delete)" value="institutionalType"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>
	
	<div class="mbottom2">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/partyContacts.do?method=prepareCreateEmailAddress" paramId="studentID" paramName="studentID">
			<bean:message key="label.partyContacts.addEmailAddress" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</div>

	
	<bean:message key="label.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" />
	<fr:view name="personBean" property="sortedWebAddresses" schema="contacts.WebAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		
			<fr:property name="linkFormat(edit)" value="<%="/partyContacts.do?method=prepareEditPartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="1"/>

			<fr:property name="linkFormat(delete)" value="<%="/partyContacts.do?method=deletePartyContact&amp;contactId=${externalId}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>

	<div class="mbottom2">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/partyContacts.do?method=prepareCreateWebAddress" paramId="studentID" paramName="studentID">
			<bean:message key="label.partyContacts.addWebAddress" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</div>


	<p>
		<html:cancel onclick="this.form.method.value='visualizeStudent';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
</fr:form>
