<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<h2><bean:message key="title.scientificCouncil.thesis.list"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="mail">
    <html:messages id="message" message="true" property="mail">
        <p><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%-- select degree and curricular year --%>
<logic:present name="contextBean">
    <fr:form action="<%= "/scientificCouncilManageThesis.do?method=listThesis" %>">
	    <fr:edit id="contextBean" name="contextBean" schema="scientificCouncil.thesis.context.bean">
	        <fr:layout name="tabular">
	            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	            <fr:property name="columnClasses" value=",,tdclear"/>
	        </fr:layout>
	    </fr:edit>
 	</fr:form>
</logic:present>

<logic:empty name="theses">
    <p>
        <em><bean:message key="label.scientificCouncil.thesis.list.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="theses">
    <div class="color888">
        <p class="mvert0"><bean:message key="ThesisState.SUBMITTED.simple"/> - <bean:message key="ThesisState.SUBMITTED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisState.APPROVED.simple"/> - <bean:message key="ThesisState.APPROVED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisState.CONFIRMED.simple"/> - <bean:message key="ThesisState.CONFIRMED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisState.EVALUATED.simple"/> - <bean:message key="ThesisState.EVALUATED.label"/></p>
    </div>

    <fr:view name="theses" schema="scientificCouncil.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle1"/>
        
            <fr:property name="link(approve)" value="<%= String.format("/scientificCouncilManageThesis.do?method=reviewProposal&amp;degreeID=%s&amp;executionYearID=%s", degreeId, executionYearId) %>"/>
            <fr:property name="key(approve)" value="link.scientificCouncil.view.proposal"/>
            <fr:property name="param(approve)" value="idInternal/thesisID"/>
            <fr:property name="order(approve)" value="1"/>
            <fr:property name="visibleIf(approve)" value="submitted"/>

            <fr:property name="link(disapprove)" value="<%= String.format("/scientificCouncilManageThesis.do?method=reviewProposal&amp;degreeID=%s&amp;executionYearID=%s", degreeId, executionYearId) %>"/>
            <fr:property name="key(disapprove)" value="link.scientificCouncil.review.proposal"/>
            <fr:property name="param(disapprove)" value="idInternal/thesisID"/>
            <fr:property name="order(disapprove)" value="2"/>
            <fr:property name="visibleIf(disapprove)" value="approved"/>

            <fr:property name="link(approveDiscussion)" value="<%= String.format("/scientificCouncilManageThesis.do?method=reviewThesis&amp;degreeID=%s&amp;executionYearID=%s", degreeId, executionYearId) %>"/>
            <fr:property name="key(approveDiscussion)" value="link.scientificCouncil.approve.discussion"/>
            <fr:property name="param(approveDiscussion)" value="idInternal/thesisID"/>
            <fr:property name="order(approveDiscussion)" value="3"/>
            <fr:property name="visibleIf(approveDiscussion)" value="confirmed"/>

            <fr:property name="link(view)" value="<%= String.format("/scientificCouncilManageThesis.do?method=viewThesis&amp;degreeID=%s&amp;executionYearID=%s", degreeId, executionYearId) %>"/>
            <fr:property name="key(view)" value="link.scientificCouncil.evaluated.view"/>
            <fr:property name="param(view)" value="idInternal/thesisID"/>
            <fr:property name="order(view)" value="4"/>
            <fr:property name="visibleIf(view)" value="evaluated"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/scientificCouncilManageThesis.do?method=listThesis"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>