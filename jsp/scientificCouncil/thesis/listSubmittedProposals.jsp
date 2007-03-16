<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.scientificCouncil.list.submitted"/></h2>

<logic:empty name="theses">
    <bean:message key="label.scientificCouncil.list.submitted.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <fr:view name="theses" schema="thesis.table.submitted.proposals">
    	<fr:layout name="tabular">
    		<fr:property name="link(show)" value="/scientificCouncilManageThesis.do?method=reviewSubmittedProposal"/>
    		<fr:property name="key(show)" value="label.scientificCouncil.review.proposal"/>
            <fr:property name="param(show)" value="idInternal/thesisId"/>
            <fr:property name="order(show)" value="0"/>
            
            <fr:property name="link(approve)" value="/scientificCouncilManageThesis.do?method=approveProposal"/>
    		<fr:property name="key(approve)" value="label.scientificCouncil.approve.proposal"/>
            <fr:property name="param(approve)" value="idInternal/thesisId"/>
            <fr:property name="order(approve)" value="1"/>
            
            <fr:property name="link(reject)" value="/scientificCouncilManageThesis.do?method=rejectSubmittedProposal"/>
    		<fr:property name="key(reject)" value="label.scientificCouncil.reject.proposal"/>
            <fr:property name="param(reject)" value="idInternal/thesisId"/>
            <fr:property name="order(reject)" value="2"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>