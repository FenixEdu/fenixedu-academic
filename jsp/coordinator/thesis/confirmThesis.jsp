<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.confirm"/></h2>

<fr:view name="thesis" schema="thesis.thesis.confirm.details">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.coordinator.thesis.confirm.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.coordinator.thesis.confirm.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" schema="coordinator.thesis.file">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<h3><bean:message key="title.coordinator.thesis.confirm.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <bean:message key="label.coordinator.thesis.confirm.noExtendedAbstract"/>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" schema="coordinator.thesis.file">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<h3><bean:message key="title.coordinator.thesis.confirm.section"/></h3>

<p>
    <bean:message key="label.coordinator.thesis.confirm.message"/>
</p>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<fr:edit name="thesis" schema="coordinator.thesis.evaluate"
         action="<%= String.format("/manageThesis.do?method=evaluateThesis&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    
    <fr:destination name="cancel" path="<%= String.format("/manageThesis.do?method=listApproved&amp;degreeCurricularPlanID=%s", dcpId) %>"/>
</fr:edit>
