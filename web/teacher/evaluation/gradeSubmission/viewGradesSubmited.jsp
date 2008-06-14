<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

	
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