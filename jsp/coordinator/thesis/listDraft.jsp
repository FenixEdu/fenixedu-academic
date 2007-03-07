<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.list.draft"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:empty name="theses">
    <bean:message key="label.coordinator.list.draft.empty"/>
</logic:empty>

<logic:notEmpty name="theses">
    <fr:view name="theses" schema="thesis.draft.table">
        <fr:layout name="tabular">
            <fr:property name="link(edit)" value="<%= "/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(edit)" value="label.coordinator.list.draft.thesis.edit"/>
            <fr:property name="param(edit)" value="idInternal/thesisID"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%= "/manageThesis.do?method=confirmDeleteProposal&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(delete)" value="label.coordinator.list.draft.thesis.delete"/>
            <fr:property name="param(delete)" value="idInternal/thesisID"/>
            <fr:property name="order(delete)" value="1"/>

            <fr:property name="link(submit)" value="<%= "/manageThesis.do?method=confirmSubmitProposal&amp;degreeCurricularPlanID=" + dcpId %>"/>
            <fr:property name="key(submit)" value="label.coordinator.list.draft.thesis.submit"/>
            <fr:property name="param(submit)" value="idInternal/thesisID"/>
            <fr:property name="order(submit)" value="2"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>