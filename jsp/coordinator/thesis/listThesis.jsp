<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="executionYearId" name="executionYearId"/>

<h2><bean:message key="title.coordinator.thesis.list"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
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
        <em><bean:message key="label.coordinator.thesis.list.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="theses">
	<div class="color888">
	    <p class="mvert0"><bean:message key="ThesisPresentationState.UNEXISTING.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.UNEXISTING.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DRAFT.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DRAFT.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.SUBMITTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.REJECTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.REJECTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.APPROVED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.APPROVED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.EVALUATED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.EVALUATED.label"/></p>
    </div>

    <fr:view name="theses" schema="coordinator.thesis.table">
        <fr:layout name="tabular-sortable">
            	<fr:property name="classes" value="tstyle1"/>
            
            <fr:property name="link(create)" value="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId)  %>"/>
            <fr:property name="key(create)" value="link.coordinator.list.create"/>
            <fr:property name="param(create)" value="student.idInternal/studentID"/>
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

            <fr:property name="link(confirm)" value="<%= String.format("/manageThesis.do?method=viewApproved&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(confirm)" value="link.coordinator.list.confirm"/>
            <fr:property name="param(confirm)" value="thesisId/thesisID"/>
            <fr:property name="order(confirm)" value="4"/>
            <fr:property name="visibleIf(confirm)" value="waitingConfirmation"/>

            <fr:property name="link(revise)" value="<%= String.format("/manageThesis.do?method=viewConfirmed&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(revise)" value="link.coordinator.list.revise"/>
            <fr:property name="param(revise)" value="thesisId/thesisID"/>
            <fr:property name="order(revise)" value="5"/>
            <fr:property name="visibleIf(revise)" value="confirmed"/>

            <fr:property name="link(evaluated)" value="<%= String.format("/manageThesis.do?method=viewEvaluated&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(evaluated)" value="link.coordinator.list.evaluated"/>
            <fr:property name="param(evaluated)" value="thesisId/thesisID"/>
            <fr:property name="order(evaluated)" value="6"/>
            <fr:property name="visibleIf(evaluated)" value="evaluated"/>

            <fr:property name="link(recreate)" value="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>"/>
            <fr:property name="key(recreate)" value="link.coordinator.list.create"/>
            <fr:property name="param(recreate)" value="student.idInternal/studentID"/>
            <fr:property name="order(recreate)" value="7"/>
            <fr:property name="visibleIf(recreate)" value="preEvaluated"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYear=%s", dcpId, executionYearId) %>"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
