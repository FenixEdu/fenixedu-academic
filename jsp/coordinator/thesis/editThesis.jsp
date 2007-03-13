<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.coordinator.thesis.edit"/></h2>

<%--  general problems about the proposal --%>
<logic:notEmpty name="generalConditions">
    <logic:iterate id="condition" name="generalConditions">
        <p>
            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
            <bean:message key="<%= key %>"/>
        </p>
    </logic:iterate>
</logic:notEmpty>

<%-- Student information--%>
<h3><bean:message key="title.coordinator.thesis.edit.section.student"/></h3>

<fr:view name="thesis" property="student" layout="tabular" schema="thesis.jury.proposal.student">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:view>

<%-- Orientation --%>
<h3><bean:message key="title.coordinator.thesis.edit.section.orientation"/></h3>

<%--  problems about the orientation --%>
<logic:notEmpty name="orientationConditions">
    <logic:iterate id="condition" name="orientationConditions">
        <p>
            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
            <bean:message key="<%= key %>"/>
        </p>
    </logic:iterate>
</logic:notEmpty>

<%-- Orientator --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.orientation.orientator"/></h4>

<logic:empty name="thesis" property="orientator">
    <p>
        <bean:message key="title.coordinator.thesis.edit.orientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <logic:empty name="thesis" property="orientator.externalPerson">
        <logic:empty name="thesis" property="orientator.teacher">
            <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="thesis" property="orientator.teacher">
            <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="thesis" property="orientator.externalPerson">
        <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>
  
<html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changePerson"/>
</html:link>
<logic:notEmpty name="thesis" property="orientator">
    , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=orientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson"/>
    </html:link>
</logic:notEmpty>

<%-- Coorientator --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.orientation.coorientator"/></h4>

<logic:empty name="thesis" property="coorientator">
    <p>
        <bean:message key="title.coordinator.thesis.edit.coorientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
    <logic:empty name="thesis" property="coorientator.externalPerson">
        <logic:empty name="thesis" property="coorientator.teacher">
            <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="thesis" property="coorientator.teacher">
            <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="thesis" property="coorientator.externalPerson">
        <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>

<html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changePerson"/>
</html:link>
<logic:notEmpty name="thesis" property="coorientator">
    , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=coorientator&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson"/>
    </html:link>
</logic:notEmpty>

<%-- Dissertation --%>
<h3><bean:message key="title.coordinator.thesis.edit.section.dissertation"/></h3>

<logic:empty name="thesis" property="title">
    <p>
        <bean:message key="label.coordinator.thesis.edit.information.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="title">
    <fr:view name="thesis" schema="thesis.jury.proposal.information">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight mtop05"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<html:link page="<%= String.format("/manageThesis.do?method=changeInformation&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changeInformation"/>
</html:link>

<%-- Jury --%>
<h3><bean:message key="title.coordinator.thesis.edit.section.jury"/></h3>

<%-- Jury/President --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.jury.president"/></h4>

<%--  problems about the president --%>
<logic:notEmpty name="presidentConditions">
    <logic:iterate id="condition" name="presidentConditions">
        <p>
            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
            <bean:message key="<%= key %>"/>
        </p>
    </logic:iterate>
</logic:notEmpty>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.coordinator.thesis.edit.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <logic:empty name="thesis" property="president.externalPerson">
        <logic:empty name="thesis" property="president.teacher">
            <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:empty>
        <logic:notEmpty name="thesis" property="president.teacher">
            <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                </fr:layout>
            </fr:view>
        </logic:notEmpty>
    </logic:empty>
    <logic:notEmpty name="thesis" property="president.externalPerson">
        <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.external">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 thlight mtop05"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:notEmpty>

<html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
    <bean:message key="link.coordinator.thesis.edit.changePerson"/>
</html:link>
<logic:notEmpty name="thesis" property="president">
    , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=president&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.removePerson"/>
    </html:link>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4><bean:message key="title.coordinator.thesis.edit.section.vowels"/></h4>

<%--  problems about the president --%>
<logic:notEmpty name="vowelsConditions">
    <logic:iterate id="condition" name="vowelsConditions">
        <p>
            <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
            <bean:message key="<%= key %>"/>
        </p>
    </logic:iterate>
</logic:notEmpty>


<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.coordinator.thesis.edit.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <logic:empty name="vowel" property="externalPerson">
            <logic:empty name="vowel" property="teacher">
                <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                    </fr:layout>
                </fr:view>
            </logic:empty>
            <logic:notEmpty name="vowel" property="teacher">
                <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.teacher">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
                    </fr:layout>
                </fr:view>
            </logic:notEmpty>
        </logic:empty>
    
        <bean:define id="vowelId" name="vowel" property="idInternal"/>
        
        <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.changePerson"/>
        </html:link>
        , <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;vowelID=%s&amp;remove=true&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", vowelId, dcpId, thesisId) %>">
            <bean:message key="link.coordinator.thesis.edit.removePerson"/>
        </html:link>
    </logic:iterate>
</logic:notEmpty>

<bean:size id="vowelsSize" name="thesis" property="vowels"/>
<logic:lessThan name="vowelsSize" value="3">
    <html:link page="<%= String.format("/manageThesis.do?method=changePerson&amp;target=vowel&amp;degreeCurricularPlanID=%s&amp;thesisID=%s", dcpId, thesisId) %>">
        <bean:message key="link.coordinator.thesis.edit.addVowel"/>
    </html:link>
</logic:lessThan>
