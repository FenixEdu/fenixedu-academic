<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="org.apache.struts.action.ActionMessages"%><html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="title.utl.scholarship.report" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
		

	<p>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>

	<h3 class="mbottom05"><bean:message key="title.utl.scholarship.report.result" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="utlScholarshipBean"  >
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration">
			<fr:slot name="institutionCode" layout="null-as-label" />
			<fr:slot name="institutionName" layout="null-as-label" />
			<fr:slot name="applicationNumber" layout="null-as-label" />
			<fr:slot name="studentNumber" layout="null-as-label" />
			<fr:slot name="studentName" layout="null-as-label" />
			<fr:slot name="idDocumentType" layout="null-as-label" />
			<fr:slot name="idDocumentNumber" layout="null-as-label" />
			<fr:slot name="degreeCode" layout="null-as-label" />
			<fr:slot name="degreeName" layout="null-as-label" />
			<fr:slot name="degreeTypeName" layout="null-as-label" />
			<fr:slot name="numberOfDegreeChanges" layout="null-as-label" />
			<fr:slot name="hasMadeDegreeChangeInThisExecutionYear" layout="null-as-label" />
			<fr:slot name="currentExecutionYearBeginDate" layout="null-as-label" />
			<fr:slot name="regimen" layout="null-as-label" />
			<fr:slot name="code" layout="null-as-label" />
			<fr:slot name="firstExecutionYearInIST" layout="null-as-label" />
			<fr:slot name="numberOfStudyExecutionYearsInCurrentRegistration" layout="null-as-label" />
			<fr:slot name="numberOfCurricularYearsOnCurrentDegreeCurricularPlan" layout="null-as-label" />
			<fr:slot name="lastYearCurricularYear" layout="null-as-label" />
			<fr:slot name="lastYearEnrolledECTS" layout="null-as-label" />
			<fr:slot name="lastYearApprovedECTS" layout="null-as-label" />
			<fr:slot name="wasApprovedOnMostECTS" layout="null-as-label" />
			<fr:slot name="currentYearCurricularYear" layout="null-as-label" />
			<fr:slot name="currentYearEnrolledECTS" layout="null-as-label" />
			<fr:slot name="degreeConcluded" layout="null-as-label" />
			<fr:slot name="finalResult" layout="null-as-label" />
			<fr:slot name="gratuityAmount" layout="null-as-label" />
			<fr:slot name="numberOfMonthsInExecutionYear" layout="null-as-label" />
			<fr:slot name="firstMonthToPay" layout="null-as-label" />
			<fr:slot name="isCETQualificationOwner" layout="null-as-label" />
			<fr:slot name="isDegreeQualificationOwner" layout="null-as-label" />
			<fr:slot name="isMasterDegreeQualificationOwner" layout="null-as-label" />
			<fr:slot name="isPhdQualificationOwner" layout="null-as-label" />
			<fr:slot name="numberOfEnrolmentsYearsSinceRegistrationStart" layout="null-as-label" />
			<fr:slot name="observations" layout="null-as-label" />
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
		</fr:layout>
	</fr:view>

	<p>
		<html:link page="/student/scholarship/report/utlScholarshipReport.do?method=downloadRegistrationResultsSpreadsheet" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message key="link.utl.scholarship.report.download.spreadsheet" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
	
</logic:present>
