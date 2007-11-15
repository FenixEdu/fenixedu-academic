<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.serviceRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<div style="float: right;">
	<bean:define id="personID" name="academicServiceRequestCreateBean" property="registration.student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="academicServiceRequestCreateBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<logic:present name="academicServiceRequestCreateBean" property="registration">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="academicServiceRequestCreateBean" property="registration" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<bean:define id="registrationID" name="academicServiceRequestCreateBean" property="registration.idInternal" />

<fr:form action="<%= "/academicServiceRequestsManagement.do?registrationID=" + registrationID.toString() %>">
	<html:hidden property="method" value="createServiceRequest" />
	
	<fr:edit id="academicServiceRequestCreateBean" name="academicServiceRequestCreateBean" visible="false" />
	
	<bean:define id="schema">RegistrationAcademicServiceRequestCreateBean.<bean:write name="academicServiceRequestCreateBean" property="academicServiceRequestType.name"/></bean:define>	

	<h3 class="mbottom05"><strong><bean:message key="message.confirm.create.service" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></h3>
	<fr:view name="academicServiceRequestCreateBean" schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",tdhl1,,,,,"/>
		</fr:layout>
	</fr:view>
	
	<p class="mtop15">
		<html:submit><bean:message key="button.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:submit onclick="this.form.method.value='chooseServiceRequestTypePostBack'"><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:cancel onclick="this.form.method.value='backToViewRegistration'" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</p>
</fr:form>
