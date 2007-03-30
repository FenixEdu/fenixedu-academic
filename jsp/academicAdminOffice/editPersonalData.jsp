<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/student.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editPersonalData"/>
	<bean:define id="studentID" type="java.lang.Integer" name="student" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID" value="<%= studentID.toString() %>"/>

	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personData" name="personBean" schema="student.personalData-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personDocumentId" name="personBean" schema="student.documentId-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3 class="mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personFiliation" name="personBean" schema="student.filiation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='visualizeStudent';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
	<br/>
	
	<h3 class="mbottom025"><bean:message key="label.person.title.addressesInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:view name="personBean" property="sortedPhysicalAdresses" schema="contacts.PhysicalAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
			
			<fr:property name="linkFormat(default)" value="<%="/student.do?method=changeDefaultPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(default)" value="label.partyContacts.setDefaultContact"/>
			<fr:property name="bundle(default)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(default)" value="defaultContact"/>
			<fr:property name="order(default)" value="1"/>
			
			<fr:property name="linkFormat(edit)" value="<%="/student.do?method=prepareEditPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="2"/>
			
			<fr:property name="linkFormat(delete)" value="<%="/student.do?method=deletePartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="3"/>
		</fr:layout>
	</fr:view>
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/student.do?method=prepareCreatePhysicalAddress" paramId="studentID" paramName="studentID">
		<bean:message key="label.partyContacts.addPhysicalAddress" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
	<br/><br/>
	
	<h3 class="mbottom025"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	
	<strong><bean:message key="label.phones" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
	<fr:view name="personBean" property="sortedPhones" schema="contacts.Phone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />

			<fr:property name="linkFormat(default)" value="<%="/student.do?method=changeDefaultPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(default)" value="label.partyContacts.setDefaultContact"/>
			<fr:property name="bundle(default)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(default)" value="defaultContact"/>
			<fr:property name="order(default)" value="1"/>
			
			<fr:property name="linkFormat(edit)" value="<%="/student.do?method=prepareEditPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="2"/>
			
			<fr:property name="linkFormat(delete)" value="<%="/student.do?method=deletePartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="3"/>
		</fr:layout>
	</fr:view>
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/student.do?method=prepareCreatePhone" paramId="studentID" paramName="studentID">
		<bean:message key="label.partyContacts.addPhone" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
	<br/><br/>
	
	<strong><bean:message key="label.mobilePhones" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
	<fr:view name="personBean" property="sortedMobilePhones" schema="contacts.MobilePhone.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />

			<fr:property name="linkFormat(default)" value="<%="/student.do?method=changeDefaultPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(default)" value="label.partyContacts.setDefaultContact"/>
			<fr:property name="bundle(default)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(default)" value="defaultContact"/>
			<fr:property name="order(default)" value="1"/>

			<fr:property name="linkFormat(edit)" value="<%="/student.do?method=prepareEditPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="2"/>

			<fr:property name="linkFormat(delete)" value="<%="/student.do?method=deletePartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="3"/>
		</fr:layout>
	</fr:view>
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/student.do?method=prepareCreateMobilePhone" paramId="studentID" paramName="studentID">
		<bean:message key="label.partyContacts.addMobilePhone" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
	<br/><br/>

	<strong><bean:message key="label.email" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
	<fr:view name="personBean" property="sortedEmailAddresses" schema="contacts.EmailAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
			
			<fr:property name="linkFormat(default)" value="<%="/student.do?method=changeDefaultPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(default)" value="label.partyContacts.setDefaultContact"/>
			<fr:property name="bundle(default)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(default)" value="defaultContact"/>
			<fr:property name="order(default)" value="1"/>

			<fr:property name="linkFormat(edit)" value="<%="/student.do?method=prepareEditPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(edit)" value="institutionalType"/>
			<fr:property name="order(edit)" value="2"/>

			<fr:property name="linkFormat(delete)" value="<%="/student.do?method=deletePartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(delete)" value="institutionalType"/>
			<fr:property name="order(delete)" value="3"/>
		</fr:layout>
	</fr:view>
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/student.do?method=prepareCreateEmailAddress" paramId="studentID" paramName="studentID">
		<bean:message key="label.partyContacts.addEmailAddress" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
	<br/><br/>
	
	<strong><bean:message key="label.webAddresses" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
	<fr:view name="personBean" property="sortedWebAddresses" schema="contacts.WebAddress.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05" />
			
			<fr:property name="linkFormat(default)" value="<%="/student.do?method=changeDefaultPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(default)" value="label.partyContacts.setDefaultContact"/>
			<fr:property name="bundle(default)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIfNot(default)" value="defaultContact"/>
			<fr:property name="order(default)" value="1"/>
			
			<fr:property name="linkFormat(edit)" value="<%="/student.do?method=prepareEditPartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(edit)" value="label.partyContacts.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(edit)" value="2"/>

			<fr:property name="linkFormat(delete)" value="<%="/student.do?method=deletePartyContact&amp;contactId=${idInternal}&amp;studentID=" + studentID %>"/>
			<fr:property name="key(delete)" value="label.partyContacts.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="order(delete)" value="3"/>
		</fr:layout>
	</fr:view>
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
	<html:link action="/student.do?method=prepareCreateWebAddress" paramId="studentID" paramName="studentID">
		<bean:message key="label.partyContacts.addWebAddress" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
	<br/><br/>

	<p>
		<html:cancel onclick="this.form.method.value='visualizeStudent';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
</fr:form>
