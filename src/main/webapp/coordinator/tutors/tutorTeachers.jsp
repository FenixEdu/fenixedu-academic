<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<jsp:include page="/coordinator/context.jsp" />

<html:xhtml />

<bean:define id="degreeCurricularPlanID" name="master_degree" property="degreeCurricularPlan.externalId" type="java.lang.String" />
<bean:define id="executionDegreeID" name="master_degree" property="externalId" />

<h2><bean:message key="link.coordinator.tutorTeachers" bundle="COORDINATOR_RESOURCES"/></h2>

<fr:form action="/tutorTeachers.do">
    <fr:edit id="yearSelection" name="yearSelection">
        <fr:schema bundle="COORDINATOR_RESOURCES"
            type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.TutorTeachersManagementDispatchAction$YearSelection">
            <fr:slot name="executionYear" layout="menu-select-postback">
                <fr:property name="providerClass"
                    value="net.sourceforge.fenixedu.presentationTier.renderers.providers.FutureAndCurrentAcademicIntervalYears" />
                <fr:property name="format" value="\${pathName}" />
                <fr:property name="nullOptionHidden" value="true" />
                <fr:property name="destination" value="selectYear" />
                <fr:property name="sortBy" value="pathName=desc" />
            </fr:slot>
        </fr:schema>
        <fr:layout name="tabular">
            <fr:destination name="selectYear"
                path="<%= "/tutorTeachers.do?method=selectYear&executionDegreeId=" + executionDegreeID  + "&degreeCurricularPlanID="
     + degreeCurricularPlanID %>" />
        </fr:layout>
    </fr:edit>
</fr:form>

<logic:present name="selector">
    <bean:define id="academicInterval" name="yearSelection" property="executionYear.resumedRepresentationInStringFormat"
        type="java.lang.String" />
    <fr:form
        action="<%= "/tutorTeachers.do?method=saveChanges&executionDegreeId=" + executionDegreeID  + "&degreeCurricularPlanID="
     + degreeCurricularPlanID + "&academicInterval=" + academicInterval %>">
        <fr:edit id="selector" name="selector">
            <fr:schema bundle="COORDINATOR_RESOURCES"
                type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.TutorTeachersManagementDispatchAction$TutorshipIntentionSelector">
                <fr:slot name="department.acronym" key="label.teacher.department" readOnly="true" />
                <fr:slot name="teacher.person.name" key="label.teacher.name" readOnly="true" />
                <fr:slot name="teacher.person.istUsername" key="label.istUsername" readOnly="true" />
                <fr:slot name="intending" key="label.tutorship.intendsTutorship" />
                <fr:slot name="previousParticipations" key="label.tutorship.previousParticipations" readOnly="true" />
            </fr:schema>
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle1" />
                <fr:property name="sortBy" value="department.name=asc,teacher.person.name=asc"></fr:property>
            </fr:layout>
        </fr:edit>
        <html:submit>
            <bean:message bundle="COMMON_RESOURCES" key="button.submit" />
        </html:submit>
    </fr:form>
</logic:present>