<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.create"/></h2>

<div class="dinline forminline">
    <fr:form action="<%= String.format("/manageThesis.do?method=createProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
        <fr:edit id="bean" name="bean" schema="thesis.jury.proposal.information.edit">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdtop thlight thright dinline"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
        </fr:edit>
        <br/>
        <html:submit>
            <bean:message key="label.submit"/>
        </html:submit>
    </fr:form>
    
    <fr:form action="<%= String.format("/manageThesis.do?method=listThesis&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
        <html:submit>
            <bean:message key="label.cancel"/>
        </html:submit>
    </fr:form>
</div>