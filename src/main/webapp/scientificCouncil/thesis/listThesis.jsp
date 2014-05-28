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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeId" name="degreeId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<em><bean:message key="scientificCouncil"/></em>
<h2><bean:message key="title.scientificCouncil.thesis.list"/></h2>

<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="mail">
    <html:messages id="message" message="true" property="mail">
        <p><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%-- select degree and curricular year --%>
<logic:present name="contextBean">
    <fr:form action="<%= "/scientificCouncilManageThesis.do?method=listThesis" %>">
	    <fr:edit id="contextBean" name="contextBean" schema="scientificCouncil.thesis.context.bean">
	        <fr:layout name="tabular">
	            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	            <fr:property name="columnClasses" value=",,tdclear"/>
	        </fr:layout>
	    </fr:edit>
 	</fr:form>
</logic:present>

<p>
	<bean:define id="linkUrl">/scientificCouncilManageThesis.do?method=downloadDissertationsList&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
	<html:link action="<%= linkUrl %>"><bean:message key="link.download.dissertation.list"/></html:link>
</p>

<logic:empty name="theses">
    <p>
        <em><bean:message key="label.scientificCouncil.thesis.list.empty"/></em>
    </p>
</logic:empty>

<logic:present name="contextBean" property="executionYear">
	<logic:present name="contextBean" property="degree">
		<bean:define id="url">/scientificCouncilManageThesis.do?method=listScientificComission&amp;degreeId=<bean:write name="degreeId"/>&amp;executionYearId=<bean:write name="executionYearId"/></bean:define>
		<html:link page="<%= url %>">
			<bean:message key="link.list.scientific.comission"/>
		</html:link>
		<br/>
		<br/>
	</logic:present>
</logic:present>

<logic:notEmpty name="theses">
    <div class="color888">
        <p class="mvert0"><bean:message key="ThesisState.SUBMITTED.simple"/> - <bean:message key="ThesisState.SUBMITTED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisState.APPROVED.simple"/> - <bean:message key="ThesisState.APPROVED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisState.CONFIRMED.simple"/> - <bean:message key="ThesisState.CONFIRMED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisState.EVALUATED.simple"/> - <bean:message key="ThesisState.EVALUATED.label"/></p>
    </div>

    <fr:view name="theses" schema="scientificCouncil.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle1"/>

            <fr:property name="link(approve)" value="/scientificCouncilManageThesis.do?method=reviewProposal"/>
            <fr:property name="key(approve)" value="link.scientificCouncil.view.proposal"/>
            <fr:property name="param(approve)" value="externalId/thesisID"/>
            <fr:property name="order(approve)" value="1"/>
            <fr:property name="visibleIf(approve)" value="submitted"/>

            <fr:property name="link(disapprove)" value="/scientificCouncilManageThesis.do?method=reviewProposal"/>
            <fr:property name="key(disapprove)" value="link.scientificCouncil.review.proposal"/>
            <fr:property name="param(disapprove)" value="externalId/thesisID"/>
            <fr:property name="order(disapprove)" value="2"/>
            <fr:property name="visibleIf(disapprove)" value="approved"/>

            <fr:property name="link(approveDiscussion)" value="/scientificCouncilManageThesis.do?method=reviewThesis"/>
            <fr:property name="key(approveDiscussion)" value="link.scientificCouncil.approve.discussion"/>
            <fr:property name="param(approveDiscussion)" value="externalId/thesisID"/>
            <fr:property name="order(approveDiscussion)" value="3"/>
            <fr:property name="visibleIf(approveDiscussion)" value="confirmed"/>

            <fr:property name="link(view)" value="/scientificCouncilManageThesis.do?method=viewThesis"/>
            <fr:property name="key(view)" value="link.scientificCouncil.evaluated.view"/>
            <fr:property name="param(view)" value="externalId/thesisID"/>
            <fr:property name="order(view)" value="4"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="<%= String.format("/scientificCouncilManageThesis.do?method=listThesis&amp;degreeID=%s&amp;executionYearID=%s", degreeId, executionYearId) %>"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
