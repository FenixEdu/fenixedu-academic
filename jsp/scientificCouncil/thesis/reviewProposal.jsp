<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="idInternal" />
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml />

<h2><bean:message key="title.scientificCouncil.thesis.proposal.approve" /></h2>

<ul>
    <li>
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=listThesis&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
            <bean:message key="link.scientificCouncil.thesis.list.back" />
        </html:link>
    </li>
    <logic:equal name="thesis" property="submitted" value="true">
        <li>
            <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=approveProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <bean:message key="link.scientificCouncil.thesis.proposal.approve" />
            </html:link>
        </li>
        <li>
            <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=confirmRejectProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <bean:message key="link.scientificCouncil.thesis.proposal.reject" />
            </html:link>
        </li>
    </logic:equal>
    <logic:equal name="thesis" property="approved" value="true">
        <li>
            <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=confirmRejectProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
                <bean:message key="link.scientificCouncil.thesis.proposal.disapprove" />
            </html:link>
        </li>
    </logic:equal>
</ul>

<logic:present name="confirmReject">
    <bean:message key="label.scientificCouncil.thesis.proposal.reject.confirm"/>
    
    <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=listThesis&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
        <fr:edit id="thesisRejection" name="thesis" schema="thesis.rejection.comment">
            <fr:layout name="tabular">
            </fr:layout>
            
            <fr:destination name="cancel" path="<%= String.format("/scientificCouncilManageThesis.do?method=reviewProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
            <fr:destination name="invalid" path="<%= String.format("/scientificCouncilManageThesis.do?method=confirmRejectProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
        </fr:edit>
    
        <html:submit>
            <bean:message key="button.submit"/>
        </html:submit>
        <html:cancel>
            <bean:message key="button.cancel"/>
        </html:cancel>
    </fr:form>
</logic:present>

<%-- Student information--%>
<h3><bean:message key="title.scientificCouncil.thesis.review.section.dissertation" /></h3>

<fr:view name="thesis" layout="tabular" schema="scientificCouncil.thesis.approve.dissertation">
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
