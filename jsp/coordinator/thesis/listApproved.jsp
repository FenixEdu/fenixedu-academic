<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.list.approved"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="theses">
    <bean:message key="label.coordinator.list.approved.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <fr:view name="theses" schema="thesis.approved.table">
        <fr:layout name="tabular">
            <fr:property name="link(edit)" value="<%= "/manageThesis.do?method=confirmThesis&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(edit)" value="label.coordinator.list.approved.thesis.confirm"/>
            <fr:property name="param(edit)" value="idInternal/thesisID"/>
            <fr:property name="order(edit)" value="0"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>