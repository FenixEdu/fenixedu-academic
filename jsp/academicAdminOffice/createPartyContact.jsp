<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/student.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createPartyContact"/>

	<bean:define id="studentID" type="java.lang.Integer" name="student" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID" value="<%= studentID.toString() %>"/>

	<bean:define id="person" name="student" property="person" />
	<bean:define id="partyContactName" name="partyContactName" />
	
	<h3 class="mbottom025"><bean:message key="label.partyContacts.addPhysicalAddress" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:create schema="<%= "student.contacts." + partyContactName + ".manage" %>" type="<%= "net.sourceforge.fenixedu.domain.contacts." + partyContactName  %>" >
		<fr:layout name="tabular-editable" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
		<fr:hidden slot="party" name="person" />
	</fr:create>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='prepareEditPersonalData';"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
</fr:form>
