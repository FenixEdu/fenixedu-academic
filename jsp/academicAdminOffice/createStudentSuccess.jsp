<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><strong><bean:message key="message.student.registerStudent.success" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<br/>

<fr:view name="registration" schema="student.show.personInformationWithIstUsername">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<fr:view name="registration" schema="student.show.registrationInformationWithNumber" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<br/>
<bean:define id="registrationID" name="registration" property="idInternal" />
<html:link action="<%="/createStudent.do?method=printRegistrationDeclarationTemplate&registrationID=" + registrationID%>" target="_blank"><bean:message key="link.student.printRegistrationDeclaration" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>