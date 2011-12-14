<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.tutorshipSummary" bundle="APPLICATION_RESOURCES" /></h2>

<html:link page="/tutorshipSummaryPeriod.do?method=prepareCreate">
Definir Período de Preenchimento das Fichas
</html:link>

<br />

<fr:form action="/tutorshipSummary.do?method=exportSummaries">
    <fr:edit id="tutorateBean" name="tutorateBean">
        <fr:schema bundle="PEDAGOGICAL_COUNCIL"
            type="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorSummaryBean">
            <fr:slot name="searchType" key="label.searchType" layout="radio-postback">
                <fr:property name="trueLabel" value="label.department" />
                <fr:property name="falseLabel" value="label.degree" />
                <fr:property name="destination" value="postback" />
                <fr:property name="classes" value="nobullet liinline" />
            </fr:slot>
            <logic:notEmpty name="tutorateBean" property="searchType">
                <logic:equal name="tutorateBean" property="searchType" value="true">
                    <fr:slot name="executionSemester" bundle="APPLICATION_RESOURCES" layout="menu-select-postback"
                        key="label.semestre">
                        <fr:property name="providerClass"
                            value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider" />
                        <fr:property name="format" value="${semester} - ${executionYear.year}" />
                        <fr:property name="destination" value="postback" />
                    </fr:slot>
                    <fr:slot name="department" layout="menu-select-postback" key="label.teacher.department">
                        <fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.DepartmentProvider" />
                        <fr:property name="key" value="true" />
                        <fr:property name="defaultText" value="label.dropDown.all" />
                        <fr:property name="bundle" value="PEDAGOGICAL_COUNCIL" />
                        <fr:property name="format" value="${nameI18n}" />
                        <fr:property name="sortBy" value="name" />
                        <fr:property name="destination" value="postback" />
                    </fr:slot>
                    <fr:slot name="teacher" layout="menu-select-postback" key="label.teacher"
                        validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
                        <fr:property name="providerClass"
                            value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorSearchBean$DepartmentTeachersProvider" />
                        <fr:property name="format" value="${teacherId} - ${employee.person.name}" />
                        <fr:property name="sortBy" value="teacherId" />
                        <fr:property name="destination" value="postback" />
                    </fr:slot>
                </logic:equal>
                <logic:equal name="tutorateBean" property="searchType" value="false">
                    <fr:slot name="executionSemester" bundle="APPLICATION_RESOURCES" layout="menu-select-postback"
                        key="label.semestre">
                        <fr:property name="providerClass"
                            value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider" />
                        <fr:property name="format" value="${semester} - ${executionYear.year}" />
                        <fr:property name="destination" value="postback" />
                    </fr:slot>
                    <fr:slot name="degree" layout="menu-select-postback" key="label.degree">
                        <fr:property name="providerClass"
                            value="net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.TutorSummaryBean$DegreesProvider" />
                        <fr:property name="format" value="${presentationName}" />
                        <fr:property name="destination" value="postback" />
                    </fr:slot>
                </logic:equal>
            </logic:notEmpty>
            <fr:layout>
                <fr:property name="classes" value="tstyle5 thlight thright gluetop mtop0 mbottom05" />
                <fr:property name="columnClasses" value="width125px,,tderror1 tdclear" />
            </fr:layout>
            <fr:destination name="postback" path="/tutorshipSummary.do?method=searchTeacher" />
        </fr:schema>
    </fr:edit>
    <html:submit>Exportar esta listagem (Excel)</html:submit>
</fr:form>

<br />
<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
    <span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
</html:messages>

<p class="mtop2 mbottom1 separator2"><b><bean:message key="label.tutorshipSummary.ableToCreate"
    bundle="APPLICATION_RESOURCES" /></b></p>

<!-- available summaries to create -->
<logic:empty name="tutorateBean" property="availableSummaries">
    <bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
</logic:empty>

<logic:notEmpty name="tutorateBean" property="availableSummaries">
    <ul>
        <logic:iterate id="createSummaryBean" name="tutorateBean" property="availableSummaries">

            <li><logic:equal value="true" name="createSummaryBean" property="persisted">
                <bean:define id="summaryId" name="createSummaryBean" property="externalId" type="java.lang.String" />

                <html:link page="<%= "/tutorshipSummary.do?method=createSummary&summaryId=" + summaryId %>">
                    <bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" />
                    <strong><bean:write name="createSummaryBean" property="executionSemester.semester" /> - <bean:write
                        name="createSummaryBean" property="executionSemester.executionYear.year" /></strong>,
				<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
				<strong><bean:write name="createSummaryBean" property="degree.sigla" /></strong>
                </html:link>
                <!--  (created) -->
            </logic:equal> <logic:equal value="false" name="createSummaryBean" property="persisted">
                <bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" />
                <strong><bean:write name="createSummaryBean" property="executionSemester.semester" /> - <bean:write
                    name="createSummaryBean" property="executionSemester.executionYear.year" /></strong>,
				<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
				<strong><bean:write name="createSummaryBean" property="degree.sigla" /></strong>
				( <bean:message key="message.tutorship.summary.not.filled" bundle="PEDAGOGICAL_COUNCIL" /> )
                <!--  (new) -->
            </logic:equal></li>

        </logic:iterate>
    </ul>
</logic:notEmpty>

<!--  finished summaries -->

<p class="mtop2 mbottom1 separator2"><b><bean:message key="label.tutorshipSummary.past" bundle="APPLICATION_RESOURCES" /></b>
</p>

<logic:empty name="tutorateBean" property="pastSummaries">
    <bean:message key="message.tutorshipSummary.empty" bundle="APPLICATION_RESOURCES" />
</logic:empty>
<logic:notEmpty name="tutorateBean" property="pastSummaries">
    <ul>
        <logic:iterate id="summary" name="tutorateBean" property="pastSummaries">
            <li><bean:define id="summaryId" name="summary" property="externalId" /> <html:link
                page="<%= "/tutorshipSummary.do?method=viewSummary&summaryId=" + summaryId %>">
                <bean:message key="label.curricular.course.semester" bundle="APPLICATION_RESOURCES" />
                <strong><bean:write name="summary" property="semester.semester" /> - <bean:write name="summary"
                    property="semester.executionYear.year" /></strong>,
				<bean:message key="label.degree.name" bundle="APPLICATION_RESOURCES" />:
				<strong><bean:write name="summary" property="degree.sigla" /></strong>
            </html:link></li>
        </logic:iterate>
    </ul>
</logic:notEmpty>


