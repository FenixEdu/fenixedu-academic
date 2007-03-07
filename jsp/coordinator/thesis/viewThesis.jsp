<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.proposal.view"/></h2>

<ul>
    <li>
        <html:link page="<%= "/manageThesis.do?method=listSubmitted&amp;degreeCurricularPlanID=" + dcpId %>">
            <bean:message key="label.back"/>
        </html:link>
    </li>
    <li>
        <bean:define id="thesisId" name="bean" property="thesis.idInternal"/>
        <html:link page="<%= String.format("/manageThesis.do?method=generateSheet&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
            <bean:message key="label.coordinator.list.submitted.thesis.print"/>
        </html:link>
    </li>
</ul>

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
    </logic:iterate>
</logic:notEmpty>

