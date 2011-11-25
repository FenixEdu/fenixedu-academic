<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="thesisOid" name="thesis" property="externalId"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.edit" bundle="APPLICATION_RESOURCES"/></h2>

<p class="mtop15 mbottom05">
	<strong><bean:message key="title.coordinator.thesis.changeInformation" bundle="APPLICATION_RESOURCES"/></strong>
</p>

<fr:edit name="thesis"
         action="<%= String.format("/scientificCouncilManageThesis.do?method=editProposalWithDocs&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"
         schema="thesis.jury.proposal.information.edit">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/scientificCouncilManageThesis.do?method=viewThesis&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
 </fr:edit>
