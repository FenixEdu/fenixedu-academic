<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page language="java" %>
<html:xhtml />

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	<ul class="mtop2 list5">
		<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		</li>
	</ul>
	
	<div style="float: right;">
		<bean:define id="personID" name="registration" property="student.person.idInternal"/>
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>

	<logic:present name="registration" property="ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="registration" property="ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>

	<h3 class="mbottom05"><bean:message key="label.registrationAttends" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

	<logic:present name="attendsMap">
		<logic:iterate id="attendsEntry" name="attendsMap">
			<bean:define id="executionPeriod" name="attendsEntry" property="key"/>
			<bean:define id="attendsSet" name="attendsEntry" property="value"/>
			<bean:write name="executionPeriod" property="semester"/> - <bean:write name="executionPeriod" property="executionYear.name"/>
			<br/>
			<logic:iterate id="attends" name="attendsSet">
				<bean:define id="executionCourse" name="attends" property="executionCourse"/>
				<bean:write name="executionCourse" property="nome"/>
				<br/>
			</logic:iterate>
		</logic:iterate>
	</logic:present>

</logic:present>
