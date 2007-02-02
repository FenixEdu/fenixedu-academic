<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.create.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop2">
<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="dismissalBean" property="studentCurricularPlan.student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<p class="breadcumbs">
	<span><bean:message key="label.studentDismissal.step.one" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>

<fr:form action="/studentDismissals.do">
	<html:hidden property="method" value="createDismissals"/>
	
	<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>

	<bean:define id="dismissalType" name="dismissalBean" property="dismissalType.name"/>

	<fr:edit id="dismissalBean-information" name="dismissalBean" schema="<%= "DismissalBean.DismissalType." + dismissalType %>">
		<fr:layout>
			<fr:property name="classes" value="tstyle4 thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/studentDismissals.do?method=stepThree"/>
	</fr:edit>

	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	<logic:notEmpty name="dismissalBean" property="selectedEnrolments">
	<fr:view name="dismissalBean" property="selectedEnrolments">
		<fr:layout name="list">
			<fr:property name="classes" value="tstyle4 thlight" />
			<fr:property name="eachLayout" value="values" />
			<fr:property name="eachSchema" value="equivalence.view" />
		</fr:layout>
	</fr:view>
	</logic:notEmpty>
	
	<logic:empty name="dismissalBean" property="selectedEnrolments">
		<em><bean:message key="label.studentDismissal.no.selected.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</logic:empty>

	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.equivalents" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<logic:notEmpty name="dismissalBean" property="courseGroup">
		<fr:view name="dismissalBean" property="courseGroup" schema="DismissalBean.CourseGroup">
			<fr:layout name="values">
				<fr:property name="classes" value="tstyle4 thlight mtop05" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:notEmpty name="dismissalBean" property="dismissals">
		<fr:view name="dismissalBean" property="dismissals">
			<fr:layout name="list">
				<fr:property name="classes" value="tstyle4 thlight" />
				<fr:property name="eachLayout" value="values" />
				<fr:property name="eachSchema" value="DismissalBean.SelectedCurricularCourse" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<p class="mtop15">
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:cancel onclick="this.form.method.value='stepTwo'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		<html:cancel onclick="this.form.method.value='back'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</p>
</fr:form>