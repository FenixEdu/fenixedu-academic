<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="title.student.performance.report" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></h2>

<p><strong><bean:message key="title.student.performance.report.create.request" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>

<bean:define id="executionSemesterName" name="executionSemesterBean" property="semester.name" type="String" />
<bean:define id="executionYearName" name="executionSemesterBean" property="semester.executionYear.name" type="String" />

<p><bean:message key="message.student.performance.report.create.for.execution.semester" bundle="RESIDENCE_MANAGEMENT_RESOURCES" arg0="<%= executionSemesterName + " " +  executionYearName %>"/></p>

<fr:form action="/residenceStudentsPerformance.do?method=uploadStudentNumbers">
	<fr:edit id="execution.semester.bean" name="executionSemesterBean" visible="false" />
	
	<fr:edit id="students.list.bean" name="studentsListBean" >
		<fr:schema bundle="RESIDENCE_MANAGEMENT_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.residenceManagement.StudentsPerformanceStudyDA$StudentsListBean">
			<fr:slot name="stream" key="label.student.performance.report.input.file">
				<fr:property name="fileNameSlot" value="fileName"/>
				<fr:property name="fileSizeSlot" value="fileSize"/>
				<fr:validator 
					name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
					<fr:property name="acceptedTypes" value="application/vnd.ms-excel" />			
				</fr:validator>		
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			
		</fr:layout>
		
		<fr:destination name="cancel" path="/residenceStudentsPerformance.do?method=listGeneratedReports" />
		<fr:destination name="invalid" path="/residenceStudentsPerformance.do?method=uploadStudentNumbersInvalid" />
	</fr:edit>
	
	<html:submit><bean:message key="button.student.performance.report.upload" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></html:submit>
</fr:form>
