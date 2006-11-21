<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.title.RegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
<br/>
<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

<h2><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<table >
	<tr>
		<td>
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
			      	<fr:property name="columnClasses" value="listClasses,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="registration" property="student.person.idInternal"/>
			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>
<br/>

<logic:present name="registration" property="ingressionEnum">
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="registration" property="ingressionEnum">
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>


<br/><br/>
<h2><strong><bean:message key="label.registration.manageState" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:edit name="registrationStateBean" schema="student.manageRegistrationState" action="/manageRegistrationState.do?method=createNewState">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>

<br/><br/>
<h2><strong><bean:message key="label.registration.historic" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" property="registrationStates" schema="student.viewRegistrationStatesHistoric" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
<br/>
