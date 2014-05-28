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

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="target" name="bean" property="targetType"/>
<bean:define id="internal" name="bean" property="internal"/>

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

<logic:present name="proposeCreation">
    <div class="warning0">
        <strong><bean:message key="label.attention"  bundle="APPLICATION_RESOURCES"/></strong>:<br/>
        <bean:message key="label.coordinator.thesis.edit.externalPerson.create"  bundle="APPLICATION_RESOURCES"/><br/>
    </div>
</logic:present>

<logic:equal name="bean" property="internal" value="true">
    <div class="dinline forminline">
        <fr:form action="<%= String.format("/manageThesis.do?method=selectPerson&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <fr:edit id="bean" name="bean" schema="<%= "thesis.bean.selectPerson.internal." + target %>">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 dinline"/>
                    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                </fr:layout>
                
                <fr:destination name="changePersonType" path="/manageThesis.do?method=changePersonType"/>
                <fr:destination name="invalid" path="/manageThesis.do?method=selectPersonInvalid"/>
            </fr:edit>
            <br/>
            
            <html:submit>
                <bean:message key="button.coordinator.thesis.edit.person.choose" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
        </fr:form>
        
        <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <html:submit>
                <bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
        </fr:form>
    </div>
</logic:equal>

<logic:equal name="bean" property="internal" value="false">
    <div class="dinline forminline">
        <fr:form action="<%= String.format("/manageThesis.do?method=selectExternalPerson&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <fr:edit id="bean" name="bean" layout="tabular" schema="thesis.bean.selectPerson.external">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 dinline"/>
                    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                </fr:layout>
                
                <fr:destination name="changePersonType" path="/manageThesis.do?method=changePersonType"/>
                <fr:destination name="invalid" path="/manageThesis.do?method=selectPersonInvalid"/>
            </fr:edit>
	        <br/>
    
            <html:submit>
                <bean:message key="button.coordinator.thesis.edit.person.choose" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
    
            <logic:present name="proposeCreation">
                <html:submit property="create">
                    <bean:message key="button.coordinator.thesis.edit.externalPerson.create" bundle="APPLICATION_RESOURCES"/>
                </html:submit>
            </logic:present>    
        </fr:form>
    
        <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
            <html:submit>
                <bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
        </fr:form>
    </div>
</logic:equal>
