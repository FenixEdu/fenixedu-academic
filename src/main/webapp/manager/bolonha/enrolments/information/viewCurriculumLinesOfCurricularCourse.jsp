<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />


<h2><bean:message key="label.curricular.course.from.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:write name="curricularCourse" property="name" /></h2>

<h3><bean:message key="label.student.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<logic:empty name="curricularCourse" property="curriculumModules">
	<bean:message key="message.curricular.course.has.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>

<logic:notEmpty name="curricularCourse" property="curriculumModules">	
	<fr:view name="curricularCourse" property="curriculumModules">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine">
			<fr:slot name="student.number" key="label.studentNumber" />
			<fr:slot name="moduleTypeName" bundle="ENUMERATION_RESOURCES" />
			<fr:slot name="name" />
			<fr:slot name="executionYear.name" key="label.executionYear" />
			<fr:slot name="executionPeriod.name" key="label.executionPeriod"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="sortBy" value="executionYear.name" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
