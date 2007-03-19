<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.scientificCouncil.thesis.list"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="theses">
    <bean:message key="label.scientificCouncil.thesis.list.empty"/>
</logic:empty>

<logic:notEmpty name="theses">

    <fr:view name="theses" schema="scientificCouncil.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="link(approve)" value="<%= "/scientificCouncilManageThesis.do?method=reviewProposal" %>"/>
            <fr:property name="key(approve)" value="link.scientificCouncil.view.proposal"/>
            <fr:property name="param(approve)" value="idInternal/thesisID"/>
            <fr:property name="order(approve)" value="1"/>
            <fr:property name="visibleIf(approve)" value="submitted"/>

            <fr:property name="link(disapprove)" value="<%= "/scientificCouncilManageThesis.do?method=reviewProposal" %>"/>
            <fr:property name="key(disapprove)" value="link.scientificCouncil.review.proposal"/>
            <fr:property name="param(disapprove)" value="idInternal/thesisID"/>
            <fr:property name="order(disapprove)" value="2"/>
            <fr:property name="visibleIf(disapprove)" value="approved"/>

            <fr:property name="link(approveDiscussion)" value="<%= "/scientificCouncilManageThesis.do?method=reviewThesis" %>"/>
            <fr:property name="key(approveDiscussion)" value="link.scientificCouncil.approve.discussion"/>
            <fr:property name="param(approveDiscussion)" value="idInternal/thesisID"/>
            <fr:property name="order(approveDiscussion)" value="3"/>
            <fr:property name="visibleIf(approveDiscussion)" value="confirmed"/>

            <fr:property name="link(disapproveDiscussion)" value="<%= "/scientificCouncilManageThesis.do?method=reviewThesis" %>"/>
            <fr:property name="key(disapproveDiscussion)" value="link.scientificCouncil.disapprove.discussion"/>
            <fr:property name="param(disapproveDiscussion)" value="idInternal/thesisID"/>
            <fr:property name="order(disapproveDiscussion)" value="4"/>
            <fr:property name="visibleIf(disapproveDiscussion)" value="evaluated"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/scientificCouncilManageThesis.do?method=listThesis"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>