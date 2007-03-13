<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.list.confirmed"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="theses">
    <bean:message key="label.coordinator.list.confirmed.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <fr:view name="theses" schema="thesis.confirmed.table">
        <fr:layout name="tabular">
            <fr:property name="link(view)" value="<%= "/manageThesis.do?method=viewConfirmed&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(view)" value="label.coordinator.list.confirmed.thesis.view"/>
            <fr:property name="param(view)" value="idInternal/thesisID"/>
            <fr:property name="order(view)" value="0"/>
            
            <fr:property name="link(revision)" value="<%= "/manageThesis.do?method=confirmRevision&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(revision)" value="label.coordinator.list.confirmed.thesis.revision"/>
            <fr:property name="param(revision)" value="idInternal/thesisID"/>
            <fr:property name="order(revision)" value="1"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>