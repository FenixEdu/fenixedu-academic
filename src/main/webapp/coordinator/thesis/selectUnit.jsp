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

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="target" name="bean" property="targetType"/>
<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<h2><bean:message key="title.coordinator.thesis.proposal"/></h2>

<h3>
    <bean:message key="title.coordinator.selectPerson.select"/> 
    <bean:message key="<%= "title.coordinator.selectPerson.select." + target %>"/>
</h3>

<logic:messagesPresent message="true" property="info">
    <html:messages id="message" message="true" property="info">
        <p><span class="info0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:present name="proposeCreation">
    <div class="warning0">
        <strong><bean:message key="label.attention"/></strong>:<br/>
        <bean:message key="label.coordinator.thesis.edit.externalUnit.create"/><br/>
    </div>
</logic:present>

<div class="dinline forminline">
    <fr:form action="<%= String.format("/manageThesis.do?method=selectExternalUnit&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
        <fr:edit id="bean" name="bean" layout="tabular" schema="thesis.bean.selectUnit.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="changeUnitType" path="/manageThesis.do?method=changeUnitType"/>
            <fr:destination name="invalid" path="/manageThesis.do?method=selectUnitInvalid"/>
        </fr:edit>
        <br/>
        
        <html:submit>
            <bean:message key="button.coordinator.thesis.edit.unit.choose"/>
        </html:submit>
        
        <logic:present name="proposeCreation">
            <html:submit property="create">
                <bean:message key="button.coordinator.thesis.edit.externalUnit.create"/>
            </html:submit>
        </logic:present>
    </fr:form>

    <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
        <fr:edit id="bean-invisible" name="bean" visible="false"/>
    
        <html:submit>
            <bean:message key="button.cancel"/>
        </html:submit>
    </fr:form>
</div>

