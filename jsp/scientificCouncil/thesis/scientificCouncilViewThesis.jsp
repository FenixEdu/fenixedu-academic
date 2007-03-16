<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.scientificCouncil.thesis.proposal"/></h2>

<ul>
    <li>
        <html:link page="/scientificCouncilManageThesis.do?method=listThesis">
            <bean:message key="title.scientificCouncil.thesis.back"/>
        </html:link>
    </li>
    <li>
    	<html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=approveProposal&amp;thesisID=%s", thesisId) %>">
        	<bean:message key="label.scientificCouncil.approve.thesis.proposal"/>
        </html:link>
    </li>
</ul>

<%-- Dissertation --%>
<h3><bean:message key="title.scientificCouncil.thesis"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<%-- Jury --%>
<h3><bean:message key="title.scientificCouncil.thesis.section.jury"/></h3>

<%-- Orientator --%>
<h4><bean:message key="title.scientificCouncil.thesis.section.orientation.orientator"/></h4>

<logic:empty name="thesis" property="orientator">
    <p>
        <bean:message key="title.scientificCouncil.thesis.orientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Coorientator --%>
<h4><bean:message key="title.scientificCouncil.thesis.section.orientation.coorientator"/></h4>

<logic:empty name="thesis" property="coorientator">
    <p>
        <bean:message key="title.scientificCouncil.thesis.coorientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/President --%>
<h4><bean:message key="title.scientificCouncil.thesis.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.scientificCouncil.thesis.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4><bean:message key="title.scientificCouncil.thesis.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.scientificCouncil.thesis.vowels.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person">
            <fr:layout name="tabular">
            </fr:layout>
        </fr:view>
    </logic:iterate>
</logic:notEmpty>
