<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.candidacy.enrol.on.modules" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />

<p><strong><bean:message key="message.erasmus.candidacy.enrol.on.modules" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>

<bean:define id="erasmusCandidacy" name="process" property="candidacy" type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication" />
<bean:define id="modulesToEnrolForFirstSemester" name="erasmusCandidacy" property="modulesToEnrolForFirstSemester" />
 
<fr:view name="modulesToEnrolForFirstSemester" >
	<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol">
		<fr:slot name="curriculumGroup.degreeModule.nameI18N" key="label.erasmus.candidacy.group.name" />
		<fr:slot name="context.childDegreeModule.nameI18N" key="label.erasmus.candidacy.child.degree.module.name" />
		<fr:slot name="executionPeriod.name" key="label.erasmus.candidacy.execution.period.name" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<html:link action="<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=executeEnrolOnFirstSemester&processId=" + processId %>">
	<bean:message key="label.eramsus.candidacy.enrol" bundle="ACADEMIC_OFFICE_RESOURCES" />
</html:link>
