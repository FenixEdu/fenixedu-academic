<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.residenceManagement" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></em>
<h2><bean:message key="title.student.performance.report" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></h2>

<p><strong><bean:message key="title.student.performance.report.create.request" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>


<p><strong><bean:message key="" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>

<fr:view name="studentsListBean" property="students">
	<fr:schema bundle="RESIDENCE_MANAGEMENT_RESOURCES" type="net.sourceforge.fenixedu.domain.student.Student">
		<fr:slot name="number" key="label.student.performance.report.student.number" />
		<fr:slot name="name" key="label.student.performance.report.student.name" /> 
	</fr:schema>
	
	<fr:layout name="tabular">
		
	</fr:layout>
</fr:view>

<p><strong><bean:message key="title.student.performance.report.add.student" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>

<fr:form action="/residenceStudentsPerformance.do?method=addStudent">
	<fr:edit id="execution.semester.bean" name="executionSemesterBean" visible="false" />
	<fr:edit id="students.list.bean" name="studentsListBean" visible="false" />
	
	<fr:edit id="students.list.bean.add.student" name="studentsListBean">
		<fr:schema bundle="RESIDENCE_MANAGEMENT_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.residenceManagement.StudentsPerformanceStudyDA$StudentsListBean" >
			<fr:slot name="studentNumber" key="label.student.performance.report.student.number" required="true">
			</fr:slot>
		</fr:schema>
	</fr:edit>
	
	<fr:destination name="invalid" path="/residenceStudentsPerformance.do?method=addStudentInvalid" />
	
	<html:submit><bean:message key="label.student.performance.report.student.add" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></html:submit>
</fr:form> 

<p><strong><bean:message key="title.student.performance.report.unaccepted.entries" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></strong></p>

<fr:view name="studentsListBean">
	<fr:schema bundle="RESIDENCE_MANAGEMENT_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.residenceManagement.StudentsPerformanceStudyDA$StudentsListBean">
		<fr:slot name="unacceptedEntries" key="label.student.performance.report.student.number" />
	</fr:schema>
	
	<fr:layout name="tabular">
		
	</fr:layout>
</fr:view>

<fr:form action="/residenceStudentsPerformance.do?method=requestStudentsPerformanceStudy">
	<fr:edit id="execution.semester.bean" name="executionSemesterBean" visible="false" />
	<fr:edit id="students.list.bean" name="studentsListBean" visible="false" />
	
	<fr:destination name="invalid" path="/residenceStudentsPerformance.do?method=requestStudentsPerformanceStudyInvalid" />
	
	<html:submit><bean:message key="button.student.performance.report.request" bundle="RESIDENCE_MANAGEMENT_RESOURCES" /></html:submit>
</fr:form>