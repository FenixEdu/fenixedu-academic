<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>

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

<h2><bean:message key="label.student.enrollment.choose.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
<strong><bean:message key="label.student.enrollment.externalUnit" bundle="ACADEMIC_OFFICE_RESOURCES" />:</strong> <bean:write name="externalUnit" property="name" />
<br/>
<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>
<br/>

<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />

<fr:form action="<%= contextInformation.toString() + parameters.toString() %>">
	<html:hidden property="method" value="prepareCreateExternalEnrolments"/>
	
	<bean:define id="studentId" name="student" property="idInternal" />
	<html:hidden property="studentId" value="<%= studentId.toString() %>"/>
	
	<bean:define id="externalUnitId" name="externalUnit" property="idInternal" />
	<html:hidden property="externalUnitId" value="<%= externalUnitId.toString() %>"/>

	<logic:notEmpty name="externalCurricularCourseBeans">	
		<fr:view name="externalCurricularCourseBeans" schema="ExternalCurricularCourseResultBean.view-fullName">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="selectedExternalCurricularCourses" />
				<fr:property name="checkboxValue" value="externalCurricularCourse.idInternal" />	
			</fr:layout>
		</fr:view>
		<html:submit><bean:message key="button.choose" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>
	<logic:empty name="externalCurricularCourseBeans">
		<em><bean:message key="label.student.enrollment.no.externalCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
		<br/>
		<br/>
	</logic:empty>	
	<html:cancel onclick="this.form.method.value='chooseExternalUnit';" ><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	<html:cancel onclick="this.form.method.value='backToMainPage';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>



</logic:present>
