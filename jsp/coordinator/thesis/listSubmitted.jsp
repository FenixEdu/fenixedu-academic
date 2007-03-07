<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.list.submitted"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="theses">
    <bean:message key="label.coordinator.list.submitted.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <fr:view name="theses" schema="thesis.submitted.table">
        <fr:layout name="tabular">
            <fr:property name="link(edit)" value="<%= "/manageThesis.do?method=viewProposal&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(edit)" value="label.coordinator.list.submitted.thesis.view"/>
            <fr:property name="param(edit)" value="idInternal/thesisID"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%= "/manageThesis.do?method=generateSheet&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(delete)" value="label.coordinator.list.submitted.thesis.print"/>
            <fr:property name="param(delete)" value="idInternal/thesisID"/>
            <fr:property name="order(delete)" value="1"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>