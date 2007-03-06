<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="target" name="bean" property="targetType"/>

<h2><bean:message key="title.coordinator.createThesis"/></h2>

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
        <bean:message key="label.coordinator.createThesis.externalPerson.create"/><br/>
    </div>
</logic:present>

<logic:equal name="bean" property="internal" value="true">
    <div class="dinline forminline">
        <fr:form action="/manageThesis.do?method=selectPerson">
            <fr:edit id="bean" name="bean" schema="thesis.bean.selectPerson.internal">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                </fr:layout>
                
                <fr:destination name="changePersonType" path="/manageThesis.do?method=changePersonType"/>
                <fr:destination name="invalid" path="/manageThesis.do?method=selectPersonInvalid"/>
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
</logic:equal>

<logic:equal name="bean" property="internal" value="false">
    <div class="dinline forminline">
        <fr:form action="/manageThesis.do?method=selectExternalPerson">
            <fr:edit id="bean" name="bean" layout="tabular" schema="thesis.bean.selectPerson.external">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                </fr:layout>
                
                <fr:destination name="changePersonType" path="/manageThesis.do?method=changePersonType"/>
                <fr:destination name="invalid" path="/manageThesis.do?method=selectPersonInvalid"/>
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
        
        <logic:present name="proposeCreation">
            <fr:form action="/manageThesis.do?method=createExternalPerson">
                <fr:edit id="bean-invisible" name="bean" visible="false"/>
                
                <html:submit styleClass="mtop05">
                    <bean:message key="button.coordinator.createThesis.externalPerson.create"/>
                </html:submit>
            </fr:form>
        </logic:present>
    </div>
</logic:equal>
