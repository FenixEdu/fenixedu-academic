<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%! 
	private static String f(String format, Object ... args) {
    	return String.format(format, args);
	}
%>

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

<bean:define id="documentRequestId" name="documentRequest" property="externalId" />

<logic:equal name="studentCurriculumValidationAllowed" value="true">
	<p><bean:message key="message.curriculum.validation.not.documentCustomization" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>
	
	<fr:form action="<%= f("/curriculumValidationDocumentRequestsManagement.do?method=printDocument&amp;documentRequestId=%s&amp;studentCurricularPlanId=%s", documentRequestId, studentCurricularPlanId) %>">
		<fr:edit
			id="document.fields.customization" 
			name="documentFieldsCustomization" 
			schema="DocumentFieldsCustomization.edit">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle5 thlight thright"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>		
		</fr:edit>
		
		<html:submit><bean:message key="button.payments.print" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</fr:form>
</logic:equal>
