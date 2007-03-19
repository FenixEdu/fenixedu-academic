<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="thesisId" name="thesis" property="idInternal"/>

<html:xhtml/>

<h2><bean:message key="title.scientificCouncil.thesis.evaluate"/></h2>

<ul>
    <li>
        <html:link page="/scientificCouncilManageThesis.do?method=listThesis">
            <bean:message key="link.scientificCouncil.thesis.list.back"/>
        </html:link>
    </li>
    <logic:equal name="thesis" property="confirmed" value="true">
        <li>
            <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=confirmApprove&amp;thesisID=%s", thesisId) %>">
                <bean:message key="title.scientificCouncil.thesis.evaluation.approve"/>
            </html:link>
        </li>
    </logic:equal>
    <logic:equal name="thesis" property="evaluated" value="true">
        <li>
            <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=confirmDisapprove&amp;thesisID=%s", thesisId) %>">
                <bean:message key="title.scientificCouncil.thesis.evaluation.disapprove"/>
            </html:link>
        </li>
    </logic:equal>
</ul>

<%-- Approve proposal --%>
<logic:present name="confirmApprove">
    <bean:message key="label.scientificCouncil.thesis.evaluation.approve.confirm"/>
    
    <div class="forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=approveThesis&amp;&amp;thesisID=%s", thesisId) %>">
            <html:submit>
                <bean:message key="button.scientificCouncil.thesis.evaluation.approve"/>
            </html:submit>
        </fr:form>
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=reviewThesis&amp;&amp;thesisID=%s", thesisId) %>">
            <html:cancel>
                <bean:message key="button.cancel"/>
            </html:cancel>
        </fr:form>
    </div>
</logic:present>

<%-- Disapprove proposal --%>
<logic:present name="confirmDisapprove">
    <bean:message key="label.scientificCouncil.thesis.evaluation.disapprove.confirm"/>
    
    <div class="forminline">
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=disapproveThesis&amp;&amp;thesisID=%s", thesisId) %>">
            <html:submit>
                <bean:message key="button.scientificCouncil.thesis.evaluation.disapprove"/>
            </html:submit>
        </fr:form>
        <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=reviewThesis&amp;&amp;thesisID=%s", thesisId) %>">
            <html:cancel>
                <bean:message key="button.cancel"/>
            </html:cancel>
        </fr:form>
    </div>
</logic:present>

<%-- Dissertation --%>
<h3><bean:message key="title.scientificCouncil.thesis.evaluation.details"/></h3>

<fr:view name="thesis" schema="thesis.jury.proposal.information">
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<h3><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <bean:message key="label.thesis.abstract.empty"/>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="thesisAbstract">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>

<h3><bean:message key="label.thesis.abstract"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <bean:message key="label.thesis.keywords.empty"/>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages" value="true">
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="pt"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
    
    <p>
        <fr:view name="thesis" property="keywords">
            <fr:layout>
                <fr:property name="language" value="en"/>
                <fr:property name="showLanguageForced" value="true"/>
            </fr:layout>
        </fr:view>
    </p>
</logic:equal>

<h3><bean:message key="title.scientificCouncil.thesis.evaluation.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <bean:message key="label.scientificCouncil.thesis.evaluation.noExtendedAbstract"/>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="extendedAbstract.size" layout="fileSize"/>)
</logic:notEmpty>

<h3><bean:message key="title.scientificCouncil.thesis.evaluation.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.scientificCouncil.thesis.evaluation.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" layout="values" schema="coordinator.thesis.file"/>
    (<fr:view name="thesis" property="dissertation.size" layout="fileSize"/>)
</logic:notEmpty>

<h3><bean:message key="title.scientificCouncil.thesis.evaluation.gradeAndDate"/></h3>

<fr:view name="thesis" schema="coordinator.thesis.revision.view">
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<%-- Jury --%>
<h3><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%-- Orientator --%>
<h4><bean:message key="title.scientificCouncil.thesis.review.section.orientation.orientator"/></h4>

<logic:empty name="thesis" property="orientator">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.orientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Coorientator --%>
<h4><bean:message key="title.scientificCouncil.thesis.review.section.orientation.coorientator"/></h4>

<logic:empty name="thesis" property="coorientator">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.coorientator.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/President --%>
<h4><bean:message key="title.scientificCouncil.thesis.review.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.president.empty"/>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person">
        <fr:layout name="tabular">
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4><bean:message key="title.scientificCouncil.thesis.review.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <bean:message key="title.scientificCouncil.thesis.review.vowels.empty"/>
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
