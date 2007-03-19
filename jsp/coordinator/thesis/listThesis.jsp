<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.thesis.list"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="theses">
    <bean:message key="label.coordinator.thesis.list.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <p><bean:message key="ThesisPresentationState.UNEXISTING" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.UNEXISTING.label"/></p>
    <p><bean:message key="ThesisPresentationState.DRAFT" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DRAFT.label"/></p>
    <p><bean:message key="ThesisPresentationState.SUBMITTED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.SUBMITTED.label"/></p>
    <p><bean:message key="ThesisPresentationState.REJECTED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.REJECTED.label"/></p>
    <p><p><bean:message key="ThesisPresentationState.APPROVED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.APPROVED.label"/></p>
    <p><bean:message key="ThesisPresentationState.CONFIRMED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.CONFIRMED.label"/></p>
    <p><bean:message key="ThesisPresentationState.EVALUATED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.EVALUATED.label"/></p>

    <fr:view name="theses" schema="coordinator.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="link(create)" value="<%= "/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(create)" value="link.coordinator.list.create"/>
            <fr:property name="param(create)" value="student.idInternal/studentID"/>
            <fr:property name="order(create)" value="1"/>
            <fr:property name="visibleIf(create)" value="unassigned"/>

            <fr:property name="link(edit)" value="<%= "/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(edit)" value="link.coordinator.list.edit"/>
            <fr:property name="param(edit)" value="thesisId/thesisID"/>
            <fr:property name="order(edit)" value="2"/>
            <fr:property name="visibleIf(edit)" value="draft"/>

            <fr:property name="link(print)" value="<%= "/manageThesis.do?method=viewSubmitted&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(print)" value="link.coordinator.list.print"/>
            <fr:property name="param(print)" value="thesisId/thesisID"/>
            <fr:property name="order(print)" value="3"/>
            <fr:property name="visibleIf(print)" value="submitted"/>

            <fr:property name="link(confirm)" value="<%= "/manageThesis.do?method=viewApproved&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(confirm)" value="link.coordinator.list.confirm"/>
            <fr:property name="param(confirm)" value="thesisId/thesisID"/>
            <fr:property name="order(confirm)" value="4"/>
            <fr:property name="visibleIf(confirm)" value="waitingConfirmation"/>

            <fr:property name="link(revise)" value="<%= "/manageThesis.do?method=viewConfirmed&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(revise)" value="link.coordinator.list.revise"/>
            <fr:property name="param(revise)" value="thesisId/thesisID"/>
            <fr:property name="order(revise)" value="5"/>
            <fr:property name="visibleIf(revise)" value="confirmed"/>

            <fr:property name="link(evaluated)" value="<%= "/manageThesis.do?method=viewEvaluated&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(evaluated)" value="link.coordinator.list.evaluated"/>
            <fr:property name="param(evaluated)" value="thesisId/thesisID"/>
            <fr:property name="order(evaluated)" value="6"/>
            <fr:property name="visibleIf(evaluated)" value="evaluated"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="<%= "/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>