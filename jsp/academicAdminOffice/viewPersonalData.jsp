<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="link.student.viewPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<br/>

<h2><strong><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
<fr:view name="personBean" schema="student.personalData-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
<fr:view name="personBean" schema="student.filiation-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.addressInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
<fr:view name="personBean" schema="student.address-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
<fr:view name="personBean" schema="student.contacts-edit" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
	
<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="student" paramProperty="idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
