<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.curriculum.validation.set.end.stage.date" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId"/>

<p>
	<html:link page="<%= "/curriculumValidation.do?method=prepareCurriculumValidation&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>

<logic:equal name="studentCurriculumValidationAllowed" value="false">
	<bean:message key="message.curriculum.validation.not.allowed" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:equal>

<logic:equal name="studentCurriculumValidationAllowed" value="true">
	<fr:form action="<%= "/curriculumValidation.do?method=editEndStageDate&amp;studentCurricularPlanId=" + studentCurricularPlanId %>">
		 <fr:edit id="student.enrolment.bean" name="bolonhaStudentEnrollmentBean" schema="set.end.stage.date" layout="tabular-editable">
		 	<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>					                	 	
		 	</fr:layout>
		 </fr:edit>
		 
		 <html:submit><bean:message key="label.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
	 </fr:form> 
</logic:equal>
