<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/student.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editPartyContact"/>
	<bean:define id="studentID" type="java.lang.Integer" name="student" property="idInternal"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID" value="<%= studentID.toString() %>"/>

	<bean:define id="partyContactClass" name="partyContact" property="class.simpleName" />
	<h3 class="mbottom025"><bean:message key="label.person.title.addressesInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit name="partyContact" schema="<%= "student.contacts." + partyContactClass + ".manage" %>">
		<fr:layout name="tabular-editable" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='prepareEditPersonalData';"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
</fr:form>
