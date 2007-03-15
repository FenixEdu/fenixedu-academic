<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.viewStudent"/></h2>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<strong><bean:message key="title.coordinator.viewStudent.subTitle"/></strong>

<fr:form action="<%= "/manageThesis.do?method=selectStudent&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="student" name="bean" schema="thesis.bean.student">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>

    <html:submit styleClass="mtop05">
        <bean:message key="button.submit"/>
    </html:submit>
</fr:form>

<logic:present name="proposeStartProcess">
    <bean:message key="label.coordinator.thesis.propose.shortcut"/>
    
    <bean:define id="studentId" name="bean" property="student.idInternal"/>
    <fr:form action="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;studentID=%s", dcpId, studentId) %>">
        <html:submit>
            <bean:message key="button.coordinator.thesis.proposal.create"/>
        </html:submit>
    </fr:form>
</logic:present>

<logic:present name="hasThesis">
    <bean:message key="label.coordinator.thesis.existing"/>

    <bean:define id="thesisId" name="thesis" property="idInternal"/>
    <html:link page="<%= String.format("/manageThesis.do?method=viewThesis&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="label.coordinator.thesis.state.view"/>
    </html:link>
</logic:present>
