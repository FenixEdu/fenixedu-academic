<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.coordinator.createThesis"/></h2>

<h3><bean:message key="title.coordinator.createThesis.section.student"/></h3>

<fr:view name="bean" property="student" layout="tabular" schema="thesis.jury.proposal.student"/>

<h3><bean:message key="title.coordinator.createThesis.section.orientator"/></h3>

<logic:empty name="bean" property="orientator">
    <p>
        <bean:message key="title.coordinator.createThesis.orientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="orientator">
    <logic:empty name="bean" property="orientator.externalPerson">
        <fr:view name="bean" property="orientator" schema="thesis.jury.proposal.person"/>
    </logic:empty>
    <logic:notEmpty name="bean" property="orientator.externalPerson">
        <fr:view name="bean" property="orientator" schema="thesis.jury.proposal.person.external"/>
    </logic:notEmpty>
</logic:notEmpty>
    
<fr:form action="/manageThesis.do?method=changePerson&amp;target=orientator">
    <fr:edit id="bean" name="bean" visible="false"/>

    <html:submit>
        <bean:message key="button.coordinator.createThesis.changePerson"/>
    </html:submit>
    <html:submit property="remove">
        <bean:message key="button.coordinator.createThesis.removePerson"/>
    </html:submit>
</fr:form>

<h3><bean:message key="title.coordinator.createThesis.section.coorientator"/></h3>

<logic:empty name="bean" property="coorientator">
    <p>
        <bean:message key="title.coordinator.createThesis.coorientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="coorientator">
    <logic:empty name="bean" property="coorientator.externalPerson">
        <fr:view name="bean" property="coorientator" schema="thesis.jury.proposal.person"/>
    </logic:empty>
    <logic:notEmpty name="bean" property="coorientator.externalPerson">
        <fr:view name="bean" property="coorientator" schema="thesis.jury.proposal.person.external"/>
    </logic:notEmpty>
</logic:notEmpty>
    
<fr:form action="/manageThesis.do?method=changePerson&amp;target=coorientator">
    <fr:edit id="bean" name="bean" visible="false"/>
    
    <html:submit>
        <bean:message key="button.coordinator.createThesis.changePerson"/>
    </html:submit>
    <html:submit property="remove">
        <bean:message key="button.coordinator.createThesis.removePerson"/>
    </html:submit>
</fr:form>

<h3><bean:message key="title.coordinator.createThesis.section.dissertation"/></h3>

<h3><bean:message key="title.coordinator.createThesis.section.jury"/></h3>

<h4><bean:message key="title.coordinator.createThesis.section.jury.president"/></h4>

<logic:empty name="bean" property="president">
    <p>
        <bean:message key="title.coordinator.createThesis.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="president">
    <logic:empty name="bean" property="president.externalPerson">
        <fr:view name="bean" property="president" schema="thesis.jury.proposal.person"/>
    </logic:empty>
    <logic:notEmpty name="bean" property="president.externalPerson">
        <fr:view name="bean" property="president" schema="thesis.jury.proposal.person.external"/>
    </logic:notEmpty>
</logic:notEmpty>
    
<fr:form action="/manageThesis.do?method=changePerson&amp;target=president">
    <fr:edit id="bean" name="bean" visible="false"/>
    
    <html:submit>
        <bean:message key="button.coordinator.createThesis.changePerson"/>
    </html:submit>
    <html:submit property="remove">
        <bean:message key="button.coordinator.createThesis.removePerson"/>
    </html:submit>
</fr:form>

<h4><bean:message key="title.coordinator.createThesis.section.vowels"/></h4>

<logic:empty name="bean" property="vowels">
    <p>
        <bean:message key="title.coordinator.createThesis.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="bean" property="vowels">
    <logic:iterate id="vowel" name="bean" property="vowels">
        <logic:empty name="vowel" property="externalPerson">
            <fr:view name="vowel" schema="thesis.jury.proposal.person"/>
        </logic:empty>
        <logic:notEmpty name="vowel" property="externalPerson">
            <fr:view name="vowel" schema="thesis.jury.proposal.person.external"/>
        </logic:notEmpty>
        
        <bean:define id="vowelId" name="vowel" property="idInternal"/>
        <fr:form action="<%= "/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=" + vowelId %>">
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
    <fr:form action="/manageThesis.do?method=changePerson&amp;target=vowel">
        <fr:edit id="bean" name="bean" visible="false"/>
        <html:submit>
            <bean:message key="button.coordinator.createThesis.addVowel"/>
        </html:submit>
    </fr:form>
</logic:lessThan>

<html:submit>
    <bean:message key="button.coordinator.createThesis.create"/>
</html:submit>