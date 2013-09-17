<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

	
<h2><bean:message key="label.submited.marks"/></h2>

<fr:view name="marksSubmited"
		 schema="markSheet.teacher.gradeSubmission.view.submited.marks">
		<fr:layout name="tabular">
			<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
			<fr:property name="classes" value="tstyle4"/>
		    <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
</fr:view>
<br/>