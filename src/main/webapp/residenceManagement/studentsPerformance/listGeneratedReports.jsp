<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="title.student.performance.report" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></h2>

<fr:form action="/residenceStudentsPerformance.do?method=listGeneratedReports">
	<fr:edit visible="false" id="execution.semester.bean" name="executionSemesterBean" />
	
	<fr:edit id="execution.semester.bean" name="executionSemesterBean" >
		<fr:schema bundle="RESIDENCE_MANAGEMENT_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.residenceManagement.StudentsPerformanceStudyDA$ExecutionSemesterBean">
			<fr:slot name="semester" bundle="RESIDENCE_MANAGEMENT_RESOURCES" layout="menu-select-postback"
						key="label.student.performance.report.execution.semester">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider" />
				<fr:property name="destination" value="postback"/>
				<fr:property name="format" value="${name} ${executionYear.name}"/>
				<fr:property name="sortBy" value="executionYear.year=desc, semester=desc"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
		</fr:layout>
		
		<fr:destination name="postback" path="/residenceStudentsPerformance.do?method=listGeneratedReports"/>
	</fr:edit>
	
</fr:form>

<p><strong><bean:message key="title.student.performance.report.generated" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>

<logic:empty name="generatedReports">
	<bean:message key="message.student.performance.report.generated.reports.emtpy" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
</logic:empty>

<logic:notEmpty name="generatedReports">
	<fr:view name="generatedReports">
		<fr:schema	type="net.sourceforge.fenixedu.domain.residence.StudentsPerformanceReport" bundle="RESOURCE_MANAGEMENT_RESOURCES">
			<fr:slot name="requestedDate" key="label.student.performance.report.request.date" />
			<fr:slot name="filename" key="label.student.peformance.report.filename" />
			<fr:slot name="jobStartTime" key="label.student.performance.report.job.start.time" />
			<fr:slot name="jobEndTime" key="label.student.performance.report.job.end.time" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="sortBy" value="requestedDate=desc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p><strong><bean:message key="title.student.performance.report.not.generated" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>


<logic:empty name="otherReports">
	<bean:message key="message.student.performance.report.not.generated.empty" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
</logic:empty>

<logic:notEmpty name="otherReports">
	<fr:view name="otherReports">
		<fr:schema	type="net.sourceforge.fenixedu.domain.residence.StudentsPerformanceReport" bundle="RESOURCE_MANAGEMENT_RESOURCES">
			<fr:slot name="requestedDate" key="label.student.performance.report.request.date" />
			<fr:slot name="filename" key="label.student.peformance.report.filename" />
			<fr:slot name="jobStartTime" key="label.student.performance.report.job.start.time" />
			<fr:slot name="jobEndTime" key="label.student.performance.report.job.end.time" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="sortBy" value="requestedDate=desc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<bean:define id="executionSemesterId" name="executionSemesterBean" property="semester.externalId" />
<p>
	<html:link action="<%= "/residenceStudentsPerformance.do?method=prepareUploadStudentNumbers&executionSemesterId=" + executionSemesterId %>">
		<bean:message key="label.student.performance.report.request.job" bundle="RESIDENCE_MANAGEMENT_RESOURCES" />
	</html:link>
</p>
