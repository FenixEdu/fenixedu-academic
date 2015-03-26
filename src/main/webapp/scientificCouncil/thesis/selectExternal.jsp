<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

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

<html:xhtml/>

<bean:define id="target" name="bean" property="targetType"/>

<h2><bean:message key="title.coordinator.thesis.proposal"  bundle="APPLICATION_RESOURCES"/></h2>

<h3>
    <bean:message key="title.coordinator.selectPerson.select"  bundle="APPLICATION_RESOURCES"/> 
    <bean:message key="<%= "title.coordinator.selectPerson.select." + target %>"  bundle="APPLICATION_RESOURCES"/>
</h3>

<logic:messagesPresent message="true" property="info">
    <html:messages id="message" message="true" property="info">
        <div class="warning0">
            <strong><bean:message key="label.attention"  bundle="APPLICATION_RESOURCES"/></strong>:<br/>
            <bean:write name="message" /><br/>
        </div>
    </html:messages>
</logic:messagesPresent>

<div class="dinline forminline">
    <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=selectExternal&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
        <fr:edit id="bean" name="bean" schema="thesis.bean.selectPerson.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 dinline"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="/scientificCouncilManageThesis.do?method=selectExternalInvalid"/>
        </fr:edit>
        <br/>
        
        <html:submit>
            <bean:message key="button.coordinator.thesis.edit.person.choose" bundle="APPLICATION_RESOURCES"/>
        </html:submit>
    </fr:form>
    
    <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=editProposal&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
        <html:submit>
            <bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/>
        </html:submit>
    </fr:form>
</div>
