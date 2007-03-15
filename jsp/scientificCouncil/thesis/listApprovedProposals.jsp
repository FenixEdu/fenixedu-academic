<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.scientificCouncil.list.approved"/></h2>

<logic:empty name="theses">
    <bean:message key="label.scientificCouncil.list.approved.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <fr:view name="theses" schema="thesis.table.approved.proposals">
        <fr:layout name="tabular">
            <fr:property name="link(show)" value="/scientificCouncilManageThesis.do?method=reviewApprovedProposal"/>
            <fr:property name="key(show)" value="label.scientificCouncil.review.proposal"/>
            <fr:property name="param(show)" value="idInternal/thesisId"/>
            <fr:property name="order(show)" value="0"/>

            <fr:property name="link(reject)" value="/scientificCouncilManageThesis.do?method=justifyApprovedProposalRejection"/>
            <fr:property name="key(reject)" value="label.scientificCouncil.reject.proposal"/>
            <fr:property name="param(reject)" value="idInternal/thesisId"/>
            <fr:property name="order(reject)" value="1"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>