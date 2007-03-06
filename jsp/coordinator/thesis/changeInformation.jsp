<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.coordinator.createThesis"/></h2>

<h3>
    <bean:message key="title.coordinator.changeInformation"/> 
</h3>

<div class="dinline forminline">
    <fr:form action="/manageThesis.do?method=backToCreation">
        <fr:edit id="bean" name="bean" schema="thesis.jury.proposal.information.edit">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="/manageThesis.do?method=changeInformation"/>
            <fr:destination name="input" path="/manageThesis.do?method=changeInformationAgain"/>
        </fr:edit>
        
        <html:submit>
            <bean:message key="label.submit"/>
        </html:submit>
    </fr:form>
    
    <fr:form action="/manageThesis.do?method=backToCreation">
        <fr:edit id="bean-invisible" name="bean" visible="false"/>
        
        <html:submit>
            <bean:message key="label.cancel"/>
        </html:submit>
    </fr:form>
</div>