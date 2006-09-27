<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<fr:form action="/createStudent.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareShowCreateStudentConfirmation"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />	
	
	<h2><strong><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	<fr:edit id="personData" name="personBean" schema="student.personalData-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	<fr:edit id="personFiliation" name="personBean" schema="student.filiation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.addressInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	<fr:edit id="personAddress" name="personBean" schema="student.address-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	<fr:edit id="personContacts" name="personBean" schema="student.contacts-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	<fr:edit name="precedentDegreeInformationBean" id="precedentDegreeInformation" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean" schema="student.precedentDegreeInformation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>	
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>	
</fr:form>