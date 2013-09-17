<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.enrolment.period.manage"/></h2>

<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.enrolment.period.edit"/></h3>

<bean:define id="semester" name="configuration" property="semester.externalId" />
<fr:edit id="configuration" name="configuration" action="/manageEnrolementPeriods.do?method=changePeriodValues">
	<fr:schema bundle="MANAGER_RESOURCES"
		type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.ManageEnrolementPeriodsDA$EnrolmentPeriodConfigurationForEdit">
		<fr:slot name="start" key="label.enrolment.period.startDate" required="true" />
		<fr:slot name="end" key="label.enrolment.period.endDate" required="true" />
		<fr:slot name="scope" layout="option-select" key="label.enrolment.period.scope">
			<fr:property name="from" value="possibleScope" />
			<fr:property name="eachSchema" value="alumni.gep.degree" />
			<fr:property name="eachLayout" value="values" />
			<fr:property name="selectAllShown" value="true" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thtop" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="cancel" path='<%= "/manageEnrolementPeriods.do?method=prepare&semester=" + semester %>'/>
</fr:edit>