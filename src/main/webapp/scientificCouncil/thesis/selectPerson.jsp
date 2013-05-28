<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="thesisOid" name="thesis" property="externalId"/>
<bean:define id="thesisId" name="thesis" property="externalId"/>
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml/>

<bean:define id="target" name="bean" property="targetType"/>
<bean:define id="internal" name="bean" property="internal"/>

<h2><bean:message key="title.coordinator.thesis.proposal"  bundle="APPLICATION_RESOURCES"/></h2>

<h3>
    <bean:message key="title.coordinator.selectPerson.select"  bundle="APPLICATION_RESOURCES"/> 
    <bean:message key="<%= "title.coordinator.selectPerson.select." + target %>"  bundle="APPLICATION_RESOURCES"/>
</h3>

<logic:messagesPresent message="true" property="info">
    <html:messages id="message" message="true" property="info">
        <div class="warning0">
            <strong><bean:message key="label.attention"  bundle="APPLICATION_RESOURCES"/></strong>:<br/>
            <bean:write name="message" /><br/>
        </div>
    </html:messages>
</logic:messagesPresent>

<logic:present name="proposeCreation">
    <div class="warning0">
        <strong><bean:message key="label.attention"  bundle="APPLICATION_RESOURCES"/></strong>:<br/>
        <bean:message key="label.coordinator.thesis.edit.externalPerson.create"  bundle="APPLICATION_RESOURCES"/><br/>
    </div>
</logic:present>

<logic:equal name="bean" property="internal" value="true">
    <div class="dinline forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=selectPerson&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <fr:edit id="bean" name="bean" schema="<%= "thesis.bean.selectPerson.internal." + target %>">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 dinline"/>
                    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                </fr:layout>
                
                <fr:destination name="changePersonType" path="/scientificCouncilManageThesis.do?method=changePersonType"/>
                <fr:destination name="invalid" path="/scientificCouncilManageThesis.do?method=selectPersonInvalid"/>
            </fr:edit>
            <br/>
            
            <html:submit>
                <bean:message key="button.coordinator.thesis.edit.person.choose" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
        </fr:form>
        
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=editProposal&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <html:submit>
                <bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
        </fr:form>
    </div>
</logic:equal>

<logic:equal name="bean" property="internal" value="false">
    <div class="dinline forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=selectExternalPerson&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <fr:edit id="bean" name="bean" layout="tabular" schema="thesis.bean.selectPerson.external">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05 dinline"/>
                    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
                </fr:layout>
                
                <fr:destination name="changePersonType" path="/scientificCouncilManageThesis.do?method=changePersonType"/>
                <fr:destination name="invalid" path="/scientificCouncilManageThesis.do.do?method=selectPersonInvalid"/>
            </fr:edit>
	        <br/>
    
            <html:submit>
                <bean:message key="button.coordinator.thesis.edit.person.choose" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
    
            <logic:present name="proposeCreation">
                <html:submit property="create">
                    <bean:message key="button.coordinator.thesis.edit.externalPerson.create" bundle="APPLICATION_RESOURCES"/>
                </html:submit>
            </logic:present>    
        </fr:form>
    
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=editProposal&amp;degreeId=%s&amp;executionYearId=%s&amp;thesisID=%s", degreeId, executionYearId, thesisId) %>">
            <html:submit>
                <bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/>
            </html:submit>
        </fr:form>
    </div>
</logic:equal>
