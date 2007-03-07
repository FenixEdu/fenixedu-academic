<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<h2><bean:message key="title.coordinator.thesis.confirm.delete"/></h2>

<p>
    <bean:message key="label.coordinator.thesis.confirm.delete.message"/>
</p>

<fr:view name="thesis" schema="thesis.proposal.delete.details">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:view>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<div class="dinline forminline">
    <fr:form action="<%= String.format("/manageThesis.do?method=deleteProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <html:submit>
            <bean:message key="button.coordinator.thesis.delete"/>
        </html:submit>  
    </fr:form>

    <fr:form action="<%= String.format("/manageThesis.do?method=listDraft&amp;degreeCurricularPlanID=%s", dcpId) %>">
        <html:submit>
            <bean:message key="button.cancel"/>
        </html:submit>  
    </fr:form>
</div>
