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


<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.ScientificCommission"%><html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlan" name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"/>
<bean:define id="executionYear" name="executionYear" type="net.sourceforge.fenixedu.domain.ExecutionYear"/>
<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>
<bean:define id="filter" name="filter"/>

<h2><bean:message key="title.coordinator.thesis.list"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="mail">
    <html:messages id="message" message="true" property="mail">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="nextAction">
    <html:messages id="message" message="true" property="nextAction">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%-- select execution Year --%>
<logic:present name="contextBean">
    <fr:form action="<%= "/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=" + dcpId %>">
	    <fr:edit id="contextBean" name="contextBean" schema="thesis.context.bean">
	        <fr:layout name="tabular">
	            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	            <fr:property name="columnClasses" value=",,tdclear"/>
	        </fr:layout>
	    </fr:edit>
 	</fr:form>
</logic:present>

<logic:empty name="theses">
    <p>
        <em><bean:message key="label.coordinator.thesis.list.empty" bundle="APPLICATION_RESOURCES"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="theses">
	<div class="color888">
	    <p class="mvert0"><bean:message key="ThesisPresentationState.UNEXISTING.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.UNEXISTING.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DRAFT.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DRAFT.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.SUBMITTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.REJECTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.REJECTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.APPROVED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.APPROVED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DOCUMENTS_SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DOCUMENTS_SUBMITTED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisPresentationState.DOCUMENTS_CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DOCUMENTS_CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.EVALUATED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.EVALUATED.label"/></p>
    </div>

	<% if (degreeCurricularPlan.isCurrentUserScientificCommissionMember(executionYear)) { %>

    <fr:view name="theses" schema="coordinator.thesis.table">
        <fr:layout name="tabular-sortable">
            	<fr:property name="classes" value="tstyle1"/>

            <fr:property name="link(create)" value="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId)  %>"/>
            <fr:property name="key(create)" value="link.coordinator.list.create"/>
            <fr:property name="param(create)" value="student.externalId/studentID,enrolmentOID/enrolmentOID"/>
            <fr:property name="order(create)" value="1"/>
            <fr:property name="visibleIf(create)" value="unassigned"/>

            <fr:property name="link(edit)" value="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(edit)" value="link.coordinator.list.edit"/>
            <fr:property name="param(edit)" value="thesisId/thesisID"/>
            <fr:property name="order(edit)" value="2"/>
            <fr:property name="visibleIf(edit)" value="draft"/>

            <fr:property name="link(print)" value="<%= String.format("/manageThesis.do?method=viewSubmitted&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(print)" value="link.coordinator.list.print"/>
            <fr:property name="param(print)" value="thesisId/thesisID"/>
            <fr:property name="order(print)" value="3"/>
            <fr:property name="visibleIf(print)" value="submitted"/>

            <fr:property name="link(approveJuri)" value="<%= String.format("/manageThesis.do?method=viewSubmitted&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(approveJuri)" value="link.coordinator.approve.jury"/>
            <fr:property name="param(approveJuri)" value="thesisId/thesisID"/>
            <fr:property name="order(approveJuri)" value="4"/>
            <fr:property name="visibleIf(approveJuri)" value="submittedAndIsCoordinatorAndNotOrientator"/>

            <fr:property name="link(confirm)" value="<%= String.format("/manageThesis.do?method=viewApproved&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(confirm)" value="link.coordinator.list.confirm"/>
            <fr:property name="param(confirm)" value="thesisId/thesisID"/>
            <fr:property name="order(confirm)" value="5"/>
            <fr:property name="visibleIf(confirm)" value="waitingConfirmation"/>

            <fr:property name="link(revise)" value="<%= String.format("/manageThesis.do?method=viewConfirmed&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(revise)" value="link.coordinator.list.revise"/>
            <fr:property name="param(revise)" value="thesisId/thesisID"/>
            <fr:property name="order(revise)" value="6"/>
            <fr:property name="visibleIf(revise)" value="confirmed"/>

            <fr:property name="link(evaluated)" value="<%= String.format("/manageThesis.do?method=viewEvaluated&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(evaluated)" value="link.coordinator.list.evaluated"/>
            <fr:property name="param(evaluated)" value="thesisId/thesisID"/>
            <fr:property name="order(evaluated)" value="7"/>
            <fr:property name="visibleIf(evaluated)" value="evaluated"/>

            <fr:property name="link(recreate)" value="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(recreate)" value="link.coordinator.list.create"/>
            <fr:property name="param(recreate)" value="student.externalId/studentID,thesisId/thesisID,enrolmentOID/enrolmentOID"/>
            <fr:property name="order(recreate)" value="8"/>
            <fr:property name="visibleIf(recreate)" value="preEvaluated"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYear=%s&amp;filter=%s", dcpId, executionYearId, filter) %>"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>

	<% } else { %>
	    <fr:view name="theses" schema="coordinator.thesis.table">
    	    <fr:layout name="tabular-sortable">
        	    	<fr:property name="classes" value="tstyle1"/>
            	<fr:property name="sortParameter" value="sortBy"/>
            	<fr:property name="sortUrl" value="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYear=%s&amp;filter=%s", dcpId, executionYearId, filter) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        	</fr:layout>
    	</fr:view>
	<% } %>

</logic:notEmpty>
