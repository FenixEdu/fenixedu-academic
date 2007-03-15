<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.proposal"/></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s", dcpId) %>">
            <bean:message key="title.coordinator.thesis.back"/>
        </html:link>
    </li>
    <li>
        <logic:equal name="thesis" property="valid" value="true">
            <html:link page="<%= String.format("/manageThesis.do?method=printApprovalDocument&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
                <bean:message key="label.coordinator.list.submitted.thesis.print"/>
            </html:link>
        </logic:equal>
    </li>
</ul>

<h3><bean:message key="title.coordinator.thesis.edit.proposal"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.state.submitted">
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<%-- Dissertation --%>
<h3><bean:message key="title.coordinator.thesis.edit.dissertation"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>
