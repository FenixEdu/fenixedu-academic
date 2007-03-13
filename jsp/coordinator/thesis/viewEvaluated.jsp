<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.view"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listEvaluated&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="label.back"/>
        </html:link>
    </li>
</ul>

<fr:view name="thesis" schema="thesis.thesis.confirmed.details">
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

