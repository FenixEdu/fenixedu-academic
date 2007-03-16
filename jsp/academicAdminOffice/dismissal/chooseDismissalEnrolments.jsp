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
	<span class="actual"><bean:message key="label.studentDismissal.step.one" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>

<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.idInternal" />
<fr:form action="<%= "/studentDismissals.do?scpID=" + scpID %>">
	<html:hidden property="method" value="chooseEquivalents"/>
	
	<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>

	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

	<logic:notEmpty name="dismissalBean" property="externalEnrolments">	
		<fr:edit id="externalEnrolments" name="dismissalBean" property="externalEnrolments" schema="student.Dismissal.choose.external.enrolments">
			<fr:layout name="tabular-editable">
				<fr:property name="sortBy" value="externalEnrolment.name"/>
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:notEmpty>	

	<logic:empty name="dismissalBean" property="externalEnrolments">
		<p>			
			<em><bean:message key="label.studentDismissal.externalEnrolments.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>				
	</logic:empty>
	
	
	<p class="mtop15"><strong><bean:message key="label.studentDismissal.internalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<logic:notEmpty name="dismissalBean" property="enrolments">
		<fr:edit id="internalEnrolments" name="dismissalBean" property="enrolments" schema="student.Dismissal.choose.internal.enrolments">
			<fr:layout name="tabular-editable">
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.startDate,enrolment.executionPeriod,enrolment.name"/>
				<fr:property name="classes" value="tstyle4"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	
	<logic:empty name="dismissalBean" property="enrolments">
		<p>
			<em><bean:message key="label.studentDismissal.internalEnrolments.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>
	
	<p class="mtop2">
		<html:submit onclick="this.form.method.value='chooseEquivalents'; return true;"><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
		<html:submit onclick="this.form.method.value='manage'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</p>
</fr:form>