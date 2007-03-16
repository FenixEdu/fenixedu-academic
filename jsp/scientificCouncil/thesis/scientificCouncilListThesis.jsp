<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.scientificCouncil.thesis.list"/></h2>

<!--
<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>
-->

<logic:empty name="theses">
    <bean:message key="label.scientificCouncil.thesis.list.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <p><bean:message key="ScientificCouncilThesisState.SUBMITTED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisState.SUBMITTED.label"/></p>
    <p><p><bean:message key="ScientificCouncilThesisState.APPROVED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisState.APPROVED.label"/></p>
    <p><bean:message key="ScientificCouncilThesisState.CONFIRMED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisState.CONFIRMED.label"/></p>
    <p><bean:message key="ScientificCouncilThesisState.EVALUATED" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisState.EVALUATED.label"/></p>

    <fr:view name="theses" schema="scientificCouncil.thesis.table">
        <fr:layout name="tabular">
            <fr:property name="link(review)" value="/scientificCouncilManageThesis.do?method=viewThesis"/>
            <fr:property name="key(review)" value="link.scientificCouncil.review.thesis"/>
            <fr:property name="param(review)" value="thesisId/idInternal"/>
            <fr:property name="order(review)" value="1"/>

        </fr:layout>
    </fr:view>
</logic:notEmpty>