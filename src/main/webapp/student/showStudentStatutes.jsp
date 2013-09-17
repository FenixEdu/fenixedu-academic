<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<html:xhtml/>

<h2><bean:message key="label.student.statutes" bundle="STUDENT_RESOURCES"/></h2>
<br>
<logic:empty name="studentStatutes">
	<bean:message key="message.student.statutes.empty" bundle="STUDENT_RESOURCES"/>
</logic:empty>

<logic:notEmpty name="studentStatutes">
	<fr:view name="studentStatutes" schema="student.studentStatutes.show">
		<fr:schema type="net.sourceforge.fenixedu.domain.student.StudentStatute" bundle="STUDENT_RESOURCES">
			<fr:slot name="statuteType" key="label.student.statute.description" />
			<fr:slot name="beginExecutionPeriod.beginDateYearMonthDay" key="label.student.statute.startdate" />
			<fr:slot name="endExecutionPeriod.endDateYearMonthDay" key="label.student.statute.enddate" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mtop05" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
