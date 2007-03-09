<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.edit"/></h2>

<h3>
    <bean:message key="title.coordinator.thesis.changeInformation"/> 
</h3>

<fr:edit name="thesis"
         action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>"
         schema="thesis.jury.proposal.information.edit">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>"/>
 </fr:edit>
