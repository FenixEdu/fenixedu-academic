<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.registration.addNewSCP" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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

<logic:present name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:view>
</logic:notPresent>


<bean:define id="registrationID" name="registration" property="externalId" />
<h3 class="mbottom05"><bean:message key="label.registration.addNewSCP" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:edit name="studentCurricularPlanCreator" schema="studentCurricularPlan.create" action="/addNewStudentCurricularPlan.do?method=createSCP">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%="/student.do?method=visualizeRegistration&registrationID=" + registrationID %>" />	
</fr:edit>

