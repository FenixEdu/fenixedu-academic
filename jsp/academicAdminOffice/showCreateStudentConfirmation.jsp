<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><strong><bean:message key="label.student.registerStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<fr:view name="personBean" schema="student.show.personInformation">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<fr:view name="executionDegreeBean" schema="student.show.registrationInformation" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<fr:view name="ingressionInformationBean" schema="student.show.ingressionInformation" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<fr:form action="/createStudent.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createStudent"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />	
	<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformationBean" visible="false" />
	
	<h2><strong><bean:message key="message.student.registerStudent.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
	
	<html:submit ><bean:message key="button.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit> 	
	<!--<html:submit onclick="document.forms[0].method.value='choosePerson'" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	-->
</fr:form>