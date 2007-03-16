<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.createExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

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

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<logic:notEmpty name="externalCurricularCourseEnrolmentBeans">

	<bean:define id="contextInformation" name="contextInformation" />
	<fr:form action="<%= contextInformation.toString() + ".do" %>">
		<html:hidden property="method" value="createExternalEnrolments"/>
		
		<bean:define id="studentId" name="student" property="idInternal" />
		<html:hidden property="studentId" value="<%= studentId.toString() %>"/>
		
		<bean:define id="externalUnitId" name="externalUnit" property="idInternal" />
		<html:hidden property="externalUnitId" value="<%= externalUnitId.toString() %>"/>
		
		<fr:edit id="externalCurricularCourseEnrolmentBeans"
				 name="externalCurricularCourseEnrolmentBeans"
				 schema="ExternalCurricularCourseEnrolmentBean.edit">
				 
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 mtop15" />
			</fr:layout>
			<fr:destination name="invalid" path="<%= contextInformation.toString() + ".do?method=createExternalEnrolmentsInvalid&studentId=" + studentId + "&externalUnitId=" + externalUnitId %>" />
		</fr:edit>
		<html:submit><bean:message key="button.choose" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:cancel onclick="this.form.method.value='chooseExternalCurricularCourses';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		<html:cancel onclick="this.form.method.value='backToMainPage';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</fr:form>
</logic:notEmpty>

</logic:present>
