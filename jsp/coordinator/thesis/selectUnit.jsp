<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="target" name="bean" property="targetType"/>
<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<h2><bean:message key="title.coordinator.thesis.proposal"/></h2>

<h3>
    <bean:message key="title.coordinator.selectPerson.select"/> 
    <bean:message key="<%= "title.coordinator.selectPerson.select." + target %>"/>
</h3>

<logic:messagesPresent message="true" property="info">
    <html:messages id="message" message="true" property="info">
        <p><span class="info0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:present name="proposeCreation">
    <div class="warning0">
        <strong><bean:message key="label.attention"/></strong>:<br/>
        <bean:message key="label.coordinator.thesis.edit.externalUnit.create"/><br/>
    </div>
</logic:present>

<div class="dinline forminline">
    <fr:form action="<%= String.format("/manageThesis.do?method=selectExternalUnit&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <fr:edit id="bean" name="bean" layout="tabular" schema="thesis.bean.selectUnit.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05 dinline"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="changeUnitType" path="/manageThesis.do?method=changeUnitType"/>
            <fr:destination name="invalid" path="/manageThesis.do?method=selectUnitInvalid"/>
        </fr:edit>
        <br/>
        
        <html:submit>
            <bean:message key="button.coordinator.thesis.edit.unit.choose"/>
        </html:submit>
        
        <logic:present name="proposeCreation">
            <html:submit property="create">
                <bean:message key="button.coordinator.thesis.edit.externalUnit.create"/>
            </html:submit>
        </logic:present>
    </fr:form>

    <fr:form action="<%= String.format("/manageThesis.do?method=editProposal&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <fr:edit id="bean-invisible" name="bean" visible="false"/>
    
        <html:submit>
            <bean:message key="button.cancel"/>
        </html:submit>
    </fr:form>
<div>

