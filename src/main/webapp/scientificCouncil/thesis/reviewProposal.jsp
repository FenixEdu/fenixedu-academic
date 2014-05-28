<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="externalId" />
<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<html:xhtml />

<em><bean:message key="scientificCouncil.thesis.process" /></em>
<h2><bean:message key="title.scientificCouncil.thesis.proposal.approve" /></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message" filter="false"/></span></p>
    </html:messages>
</logic:messagesPresent>

<ul>
    <logic:notEmpty name="degreeId"><logic:notEmpty name="executionYearId">
    <li>
		<bean:define id="url">/scientificCouncilManageThesis.do?method=listScientificComission&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
		<html:link page="<%= url %>">
			<bean:message key="link.list.scientific.comission"/>
		</html:link>
    </li>
	</logic:notEmpty></logic:notEmpty>
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
        <p class="mtop0 mbottom1">
            <strong><bean:message key="label.attention" bundle="APPLICATION_RESOURCES"/>:</strong><br/>
            <bean:message key="label.scientificCouncil.thesis.proposal.reject.confirm"/>
        </p>
    
	    <fr:form action="<%= String.format("/scientificCouncilManageThesis.do?method=listThesis&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>">
	        <fr:edit id="thesisRejection" name="thesis" schema="thesis.rejection.comment">
	            <fr:layout name="tabular">
		           <fr:property name="classes" value="thtop thlight mbottom0"/>
		           <fr:property name="columnClasses" value="width125px,,tdclear tderror1"/>
	            </fr:layout>
	            <fr:destination name="cancel" path="<%= String.format("/scientificCouncilManageThesis.do?method=reviewProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
	            <fr:destination name="invalid" path="<%= String.format("/scientificCouncilManageThesis.do?method=confirmRejectProposal&amp;thesisID=%s&amp;degreeID=%s&amp;executionYearID=%s", thesisId, degreeId, executionYearId) %>"/>
	        </fr:edit>
	        
	        <table class="mtop0 tgluetop">
	        <tr>
		        <td class="width125px">
		        </td>
		        <td>
		            <html:submit>
		                <bean:message key="button.submit"/>
		            </html:submit>
		            <html:cancel>
		                <bean:message key="button.cancel"/>
		            </html:cancel>
		        </td>
	        </tr>
	        </table>
	    </fr:form>
    </div>
</logic:present>

<%-- general process message, under a notPresent to avoid having two message boxes next to each other --%>
<logic:equal name="thesis" property="submitted" value="true">
	<logic:notPresent name="confirmReject">
		<div class="infoop2">
			<strong><bean:message key="label.attention"/>:</strong><br/>
			<p class="mvert0"><bean:message key="message.scientificCouncil.thesis.proposal.process"/></p>
		</div>
	</logic:notPresent>
</logic:equal>

<logic:equal name="thesis" property="approved" value="true">
	<logic:notPresent name="confirmReject">
		<div class="infoop2">
			<p class="mvert0"><bean:message key="message.scientificCouncil.thesis.approved.proposal.process"/></p>
		</div>
	</logic:notPresent>
</logic:equal>

<%-- Student information--%>
<h3 class="mtop15 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.dissertation" /></h3>

<fr:view name="thesis" layout="tabular" schema="scientificCouncil.thesis.approve.dissertation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
	</fr:layout>
</fr:view>


<%-- Orientation --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.orientation"/></h3>

<logic:empty name="thesis" property="orientator">
    <logic:empty name="thesis" property="coorientator">
        <p>
            <em><bean:message key="title.scientificCouncil.thesis.review.orientation.empty"/></em>
        </p>
    </logic:empty>
</logic:empty>

<logic:notEmpty name="thesis" property="orientator">
	<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation.orientator"/></h4>
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
	<h4 class="mtop2 mbottom05"><bean:message key="title.scientificCouncil.thesis.review.section.orientation.coorientator"/></h4>
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

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

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
