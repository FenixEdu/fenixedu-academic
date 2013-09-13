<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="thesisOid" name="thesis" property="externalId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>
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
