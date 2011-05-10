<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree"%>

<html:xhtml />

<bean:define id="infoExecutionDegree" name="<%=PresentationConstants.MASTER_DEGREE%>" type="InfoExecutionDegree" />
<bean:define id="infoDegreeCurricularPlan" name="infoExecutionDegree" property="infoDegreeCurricularPlan" />
<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" type="java.lang.Integer" />
<bean:define id="degreeCurricularPlanOID" name="infoDegreeCurricularPlan" property="externalId" />
<bean:define id="executionDegreeID" name="infoExecutionDegree" property="idInternal" />

<fr:form>
    <fr:edit id="yearSelection" name="yearSelection">
        <fr:schema bundle="COORDINATOR_RESOURCES"
            type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor.TutorTeachersManagementDispatchAction$YearSelection">
            <fr:slot name="executionYear" layout="menu-select-postback">
                <fr:property name="providerClass"
                    value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ActiveAcademicIntervalYears" />
                <fr:property name="format" value="${pathName}" />
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
                type="net.sourceforge.fenixedu.presentationTier.Action.coordinator.tutor.TutorTeachersManagementDispatchAction$TutorshipIntentionSelector">
                <fr:slot name="teacher.person.istUsername" key="label.istUsername" readOnly="true" />
                <fr:slot name="teacher.person.name" key="label.teacher.name" readOnly="true" />
                <fr:slot name="department.name" key="label.teacher.department" readOnly="true" />
                <fr:slot name="intending" key="label.tutorship.intendsTutorship" />
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