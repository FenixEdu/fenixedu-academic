<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<logic:present role="MANAGER">

	<h2><bean:message key="label.curricular.course.from.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES" /> <bean:write name="curricularCourse" property="name" /></h2>
	
	<h3><bean:message key="label.student.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	
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

</logic:present>