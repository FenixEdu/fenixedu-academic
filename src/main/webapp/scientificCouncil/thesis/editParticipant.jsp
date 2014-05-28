<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="thesisOid" name="thesis" property="externalId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="target" name="targetType"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.editParticipant" bundle="APPLICATION_RESOURCES"/></h2>

<h3>
    <bean:message key="title.coordinator.thesis.editParticipant.change" bundle="APPLICATION_RESOURCES"/> 
    <bean:message key="<%= "title.coordinator.selectPerson.select." + target %>" bundle="APPLICATION_RESOURCES"/>
</h3>

<bean:define id="urlEdit">/scientificCouncilManageThesis.do?method=editProposal&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
<bean:define id="urlCancel">/scientificCouncilManageThesis.do?method=viewThesis&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
<fr:edit name="participant"
         action="<%= String.format(urlEdit + "&amp;thesisID=%s", thesisId) %>"
         schema="thesis.jury.proposal.participant.edit">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format(urlCancel + "&amp;thesisID=%s", thesisId) %>"/>
</fr:edit>
