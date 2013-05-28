<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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

<logic:equal name="studentCurriculumValidationAllowed" value="true">
	<p class="mtop15">
		<b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="notDelivered.requests"/></b>
		<bean:define id="concludedAcademicServiceRequests" name="registration" property="toDeliverAcademicServiceRequests"/>
		<logic:notEmpty name="concludedAcademicServiceRequests">
			<fr:view name="concludedAcademicServiceRequests" schema="AcademicServiceRequest.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight mtop0" />
					<fr:property name="columnClasses" value="smalltxt acenter nowrap,smalltxt acenter nowrap,acenter,,acenter,tdhl1 nowrap,,,acenter nowrap,nowrap" />
	
					<fr:property name="linkFormat(print)" value="<%= f("/curriculumValidationDocumentRequestsManagement.do?method=preparePrintDocument&amp;documentRequestId=${externalId}&amp;studentCurricularPlanId=%s", studentCurricularPlanId) %>" />
					<fr:property name="key(print)" value="print"/>
					<fr:property name="visibleIf(print)" value="toPrint"/>
					
					<fr:property name="sortBy" value="requestDate=desc, activeSituation.situationDate=desc, urgentRequest=desc, description=asc"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="concludedAcademicServiceRequests">
			<p>
				<em>
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="no.concluded.academic.service.requests"/>
				</em>
			</p>
		</logic:empty>
	</p>	
</logic:equal>