<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.student.manageExternalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<p class="mvert2">
<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />
<logic:notEmpty name="parameters">
	<bean:define id="parameters">&amp;<bean:write name="parameters"/></bean:define>
</logic:notEmpty>

<ul>
	<li>
		<bean:define id="url1"><bean:write name="contextInformation"/>method=chooseExternalUnit&amp;studentId=<bean:write name="student" property="idInternal" /><bean:write name="parameters"/></bean:define>
		<html:link action='<%= url1 %>'><bean:message key="label.student.create.external.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
</ul>

<bean:define id="studentId" name="student" property="idInternal" />

<fr:form action="<%= contextInformation.toString() + "studentId=" + studentId + parameters.toString()  %>">

	<html:hidden property="method" value="deleteExternalEnrolments"/>
	
	<logic:notEmpty name="student" property="externalEnrolments">
		<fr:view name="student" property="externalEnrolments" schema="ExternalEnrolment.view-externalCurricularCours">
			<fr:layout name="tabular">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight thcenter" />
					<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
					<fr:property name="checkable" value="true" />
					<fr:property name="checkboxName" value="externalEnrolmentsToDelete" />
					<fr:property name="checkboxValue" value="idInternal" />	
				</fr:layout>
			</fr:layout>
		</fr:view>
		<html:submit><bean:message key="button.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>

	<logic:empty name="student" property="externalEnrolments">
		<p class="mvert15">
			<em><bean:message key="label.student.enrollment.no.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
		</p>
	</logic:empty>
	
	<html:cancel onclick="this.form.method.value='cancelExternalEnrolment';"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>

</logic:present>
