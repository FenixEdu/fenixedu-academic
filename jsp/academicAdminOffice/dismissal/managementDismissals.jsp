<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.studentDismissal.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="dismissalBean" property="studentCurricularPlan.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<ul>
	<li>
		<bean:define id="url1">/studentDismissals.do?method=prepare&amp;scpID=<bean:write name="dismissalBean" property="studentCurricularPlan.idInternal" /></bean:define>
		<html:link action='<%= url1 %>'><bean:message key="label.studentDismissal.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
	<li>
		<bean:define id="url1">/studentDismissals.do?method=chooseExternalCurricularCourse&amp;scpID=<bean:write name="dismissalBean" property="studentCurricularPlan.idInternal" /></bean:define>
		<html:link action='<%= url1 %>'><bean:message key="label.student.create.external.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
</ul>

<fr:form action="/studentDismissals.do">
	<html:hidden property="method" value="deleteCredits"/>
	<fr:edit name="dismissalBean" visible="false"/>
	<logic:notEmpty name="dismissalBean" property="studentCurricularPlan.credits">
		<fr:view name="dismissalBean" property="studentCurricularPlan.credits" schema="student.Dismissal.view.dismissals">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="creditsToDelete" />
				<fr:property name="checkboxValue" value="idInternal" />	
			</fr:layout>
		</fr:view>
		<html:submit><bean:message key="button.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>

	<logic:empty name="dismissalBean" property="studentCurricularPlan.credits">
		<p class="mvert15">
			<em><bean:message key="label.studentDismissal.management.no.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>
	
	<html:cancel onclick="this.form.method.value='backViewRegistration'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>

</fr:form>