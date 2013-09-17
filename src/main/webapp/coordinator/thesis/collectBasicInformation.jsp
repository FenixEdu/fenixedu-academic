<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.create"/></h2>

<div class="infoop2">
	<p class="mvert0"><bean:message key="message.coordinator.thesis.collectBasicInformation"/></p>
</div>

<div class="dinline forminline">
    <fr:form action="<%= String.format("/manageThesis.do?method=createProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
        <fr:edit id="bean" name="bean" schema="thesis.jury.proposal.information.edit.without.subtitle">
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