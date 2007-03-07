<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<html:xhtml/>

<logic:empty name="bean" property="thesis">
    <h2><bean:message key="title.coordinator.createThesis"/></h2>
</logic:empty>
<logic:notEmpty name="bean" property="thesis">
    <h2><bean:message key="title.coordinator.updateThesis"/></h2>
</logic:notEmpty>

<%-- Student information--%>
<h3><bean:message key="title.coordinator.createThesis.section.student"/></h3>

<fr:view name="bean" property="student" layout="tabular" schema="thesis.jury.proposal.student">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:view>

<%-- Orientator --%>
<h3><bean:message key="title.coordinator.createThesis.section.orientator"/></h3>

<logic:empty name="bean" property="orientator">
    <p>
        <bean:message key="title.coordinator.createThesis.orientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="orientator">
    <logic:empty name="bean" property="orientator.externalPerson">
        <fr:view name="bean" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:empty>
    <logic:notEmpty name="bean" property="orientator.externalPerson">
        <fr:view name="bean" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>
    
<fr:form action="<%= "/manageThesis.do?method=changePerson&amp;target=orientator&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="bean" name="bean" visible="false"/>

    <html:submit>
        <bean:message key="button.coordinator.createThesis.changePerson"/>
    </html:submit>
    <logic:notEmpty name="bean" property="orientator">
        <html:submit property="remove">
            <bean:message key="button.coordinator.createThesis.removePerson"/>
        </html:submit>
    </logic:notEmpty>
</fr:form>

<%-- Coorientator --%>
<h3><bean:message key="title.coordinator.createThesis.section.coorientator"/></h3>

<logic:empty name="bean" property="coorientator">
    <p>
        <bean:message key="title.coordinator.createThesis.coorientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="coorientator">
    <logic:empty name="bean" property="coorientator.externalPerson">
        <fr:view name="bean" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:empty>
    <logic:notEmpty name="bean" property="coorientator.externalPerson">
        <fr:view name="bean" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>

<fr:form action="<%= "/manageThesis.do?method=changePerson&amp;target=coorientator&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="bean" name="bean" visible="false"/>
    
    <html:submit>
        <bean:message key="button.coordinator.createThesis.changePerson"/>
    </html:submit>
    <logic:notEmpty name="bean" property="coorientator">
        <html:submit property="remove">
            <bean:message key="button.coordinator.createThesis.removePerson"/>
        </html:submit>
    </logic:notEmpty>
</fr:form>

<%-- Dissertation --%>
<h3><bean:message key="title.coordinator.createThesis.section.dissertation"/></h3>

<logic:empty name="bean" property="title">
    <p>
        <bean:message key="label.coordinator.createThesis.information.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="title">
    <fr:view name="bean" schema="thesis.jury.proposal.information">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight mtop05"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<fr:form action="<%= "/manageThesis.do?method=changeInformation&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="bean" name="bean" visible="false"/>
    
    <html:submit>
        <bean:message key="button.coordinator.createThesis.changeInformation"/>
    </html:submit>
</fr:form>

<%-- Jury --%>
<h3><bean:message key="title.coordinator.createThesis.section.jury"/></h3>

<%-- Jury/President --%>
<h4><bean:message key="title.coordinator.createThesis.section.jury.president"/></h4>

<logic:empty name="bean" property="president">
    <p>
        <bean:message key="title.coordinator.createThesis.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="president">
    <logic:empty name="bean" property="president.externalPerson">
        <fr:view name="bean" property="president" layout="tabular" schema="thesis.jury.proposal.person">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:empty>
    <logic:notEmpty name="bean" property="president.externalPerson">
        <fr:view name="bean" property="president" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>
    
<fr:form action="<%= "/manageThesis.do?method=changePerson&amp;target=president&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="bean" name="bean" visible="false"/>
    
    <html:submit>
        <bean:message key="button.coordinator.createThesis.changePerson"/>
    </html:submit>
    <logic:notEmpty name="bean" property="president">
        <html:submit property="remove">
            <bean:message key="button.coordinator.createThesis.removePerson"/>
        </html:submit>
    </logic:notEmpty>
</fr:form>

<%-- Jury/"Vowels" --%>
<h4><bean:message key="title.coordinator.createThesis.section.vowels"/></h4>

<logic:empty name="bean" property="vowels">
    <p>
        <bean:message key="title.coordinator.createThesis.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="vowels">
    <logic:iterate id="vowel" name="bean" property="vowels">
        <logic:empty name="vowel" property="externalPerson">
            <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="vowel" property="externalPerson">
            <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.external">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
        
        <bean:define id="vowelId" name="vowel" property="idInternal"/>
        <fr:form action="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;degreeCurricularPlanID=%s", vowelId, dcpId) %>">
            <fr:edit id="bean" name="bean" visible="false"/>
            
            <html:submit>
                <bean:message key="button.coordinator.createThesis.changePerson"/>
            </html:submit>
            <html:submit property="remove">
                <bean:message key="button.coordinator.createThesis.removePerson"/>
            </html:submit>
        </fr:form>
    </logic:iterate>
</logic:notEmpty>

<bean:size id="vowelsSize" name="bean" property="vowels"/>
<logic:lessThan name="vowelsSize" value="3">
    <fr:form action="<%= "/manageThesis.do?method=changePerson&amp;target=vowel&amp;degreeCurricularPlanID=" + dcpId %>">
        <fr:edit id="bean" name="bean" visible="false"/>
        <html:submit>
            <bean:message key="button.coordinator.createThesis.addVowel"/>
        </html:submit>
    </fr:form>
</logic:lessThan>

<%-- Submit --%>
<fr:form action="<%= "/manageThesis.do?method=updateProposal&amp;degreeCurricularPlanID=" + dcpId %>">
    <fr:edit id="bean" name="bean" visible="false"/>
    
    <html:submit styleClass="mtop15">
        <logic:empty name="bean" property="thesis">
            <bean:message key="button.coordinator.createThesis.create"/>
        </logic:empty>
        <logic:notEmpty name="bean" property="thesis">
            <bean:message key="button.coordinator.updateThesis.update"/>
        </logic:notEmpty>
    </html:submit>
</fr:form>
