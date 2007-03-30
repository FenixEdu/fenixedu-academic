<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="idInternal" />
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml />

<em><bean:message key="scientificCouncil.thesis.process" /></em>
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

<%-- rejection comment --%>
<logic:present name="confirmReject">
    <div class="warning0" style="padding: 1em">
        <p class="mvert0">
            <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
            <bean:message key="label.scientificCouncil.thesis.proposal.reject.confirm"/>
        </p>
    </div>
    <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=listThesis&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
        <fr:edit id="thesisRejection" name="thesis" schema="thesis.rejection.comment">
            <fr:layout name="tabular">
	           <fr:property name="classes" value="tstyle5 thtop thlight"/>
            </fr:layout>
            
            <fr:destination name="cancel" path="<%= String.format("/scientificCouncilManageThesis.do?method=reviewProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
            <fr:destination name="invalid" path="<%= String.format("/scientificCouncilManageThesis.do?method=confirmRejectProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
        </fr:edit>
        <p class="mbottom15">
            <html:submit>
                <bean:message key="button.submit"/>
            </html:submit>
            <html:cancel>
                <bean:message key="button.cancel"/>
            </html:cancel>
        </p>
    </fr:form>
</logic:present>

<%-- Student information--%>
<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.dissertation" /></h3>

<fr:view name="thesis" layout="tabular" schema="scientificCouncil.thesis.approve.dissertation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
	</fr:layout>
</fr:view>

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%-- Orientation --%>
<h4 class="mtop25 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation"/></h4>

<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.scientificCouncil.thesis.review.orientation.empty"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
    <fr:view name="thesis" property="orientator" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="orientatorCreditsDistributionNeeded" value="true">
        <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
            <tr>
                <th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits"/>:</th>
                <td class="width35em">
                    <logic:empty name="thesis" property="orientatorCreditsDistribution">-</logic:empty>
                    <logic:notEmpty name="thesis" property="orientatorCreditsDistribution">
                        <fr:view name="thesis" property="orientatorCreditsDistribution"/> %
                    </logic:notEmpty>
                </td>
            </tr>
        </table>
    </logic:equal>
</logic:notEmpty>
  
<logic:notEmpty name="thesis" property="coorientator">
    <fr:view name="thesis" property="coorientator" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom0"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>
        </fr:layout>
    </fr:view>
    <logic:equal name="thesis" property="coorientatorCreditsDistributionNeeded" value="true">
        <table class="tstyle2 thlight thright mtop0 mbottom05 tgluetop">
            <tr>
                <th class="width12em"><bean:message key="label.scientificCouncil.thesis.edit.teacher.credits"/>:</th>
                <td class="width35em">
                    <logic:empty name="thesis" property="coorientatorCreditsDistribution">-</logic:empty>
                    <logic:notEmpty name="thesis" property="coorientatorCreditsDistribution">
                        <fr:view name="thesis" property="coorientatorCreditsDistribution"/> %
                    </logic:notEmpty>
                </td>
            </tr>
        </table>
    </logic:equal>
</logic:notEmpty>

<%-- Jury/President --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.jury.president"/></h4>

<logic:empty name="thesis" property="president">
    <p>
        <em><bean:message key="title.scientificCouncil.thesis.review.president.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="president">
    <fr:view name="thesis" property="president" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            <fr:property name="columnClasses" value="width12em,width35em,"/>        
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<%-- Jury/"Vowels" --%>
<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.vowels"/></h4>

<logic:empty name="thesis" property="vowels">
    <p>
        <em><bean:message key="title.scientificCouncil.thesis.review.vowels.empty"/></em>
    </p>
</logic:empty>

<logic:notEmpty name="thesis" property="vowels">
    <logic:iterate id="vowel" name="thesis" property="vowels">
        <fr:view name="vowel" layout="tabular" schema="thesis.jury.proposal.person.loginInfo">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,width35em,"/>
            </fr:layout>
        </fr:view>
    </logic:iterate>
</logic:notEmpty>
