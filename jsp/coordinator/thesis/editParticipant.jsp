<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>
<bean:define id="target" name="targetType"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.editParticipant"/></h2>

<h3>
    <bean:message key="title.coordinator.thesis.editParticipant.change"/> 
    <bean:message key="<%= "title.coordinator.selectPerson.select." + target %>"/>
</h3>

<fr:edit name="participant"
         action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>"
         schema="thesis.jury.proposal.participant.edit">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearID=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>"/>
</fr:edit>
