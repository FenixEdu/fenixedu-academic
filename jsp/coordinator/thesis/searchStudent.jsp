<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.coordinator.selectStudent"/></h2>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<logic:present name="proposeStartProcess">
    <bean:message key="label.coordinator.thesis.propose.shortcut"/>
    
    <fr:form action="<%= "/manageThesis.do?method=collectBasicInformation&amp;degreeCurricularPlanID=" + dcpId %>">
        <fr:edit id="bean-invisible" name="bean" visible="false"/>
        
        <html:submit>
            <bean:message key="button.coordinator.thesis.proposal.create"/>
        </html:submit>
    </fr:form>
</logic:present>

<fr:form action="<%= "/manageThesis.do?method=selectStudent&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="student" name="bean" schema="thesis.bean.student">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>

    <html:submit styleClass="mtop05">
        <bean:message key="label.coordinator.searchStudent"/>
    </html:submit>
</fr:form>
