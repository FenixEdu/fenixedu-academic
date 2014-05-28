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


<%@page import="java.util.TreeSet"%>
<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%><html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="dcpId" name="degreeCurricularPlan" property="externalId"/>
<bean:define id="executionYearId" name="executionYearId"/>

<h2><bean:message key="title.coordinator.viewStudent"/></h2>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%--
<p class="mtop15 mbottom025"><strong><bean:message key="title.coordinator.viewStudent.subTitle"/>:</strong></p>
--%>

<fr:form action="<%= String.format("/manageThesis.do?method=selectStudent&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
    <fr:edit id="student" name="bean" schema="thesis.bean.student">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle mtop15 mbottom05"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
	<p class="mtop05">
	    <html:submit>
	        <bean:message key="button.view"/>
	    </html:submit>
    </p>
</fr:form>

<logic:present name="hasAssignment">
	<fr:view name="proposal" schema="thesis.proposal.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,width35em,"/>
		</fr:layout>
	</fr:view>

	<logic:notPresent name="proposalEnrolment">
		<em><bean:message key="label.student.thesis.notFound"/></em>
	</logic:notPresent>
	<logic:present name="proposalEnrolment">
		<div class="warning0" style="padding: 1em;">
			<p class="mtop0 mbottom1">
				<bean:message key="label.coordinator.proposal.assigned"/>
		    </p>

		    <bean:define id="studentId" name="bean" property="student.externalId"/>
		    <bean:define id="enrolmentId" name="proposalEnrolment" property="externalId"/>
		    <bean:define id="executionYear" name="proposalEnrolment" property="executionYear" type="net.sourceforge.fenixedu.domain.ExecutionYear"/>
			<bean:define id="degreeCurricularPlan" name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"/>

	<% if (degreeCurricularPlan.isCurrentUserScientificCommissionMember(executionYear)) { %>

		    <fr:form action="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;studentID=%s&amp;enrolmentOID=%s", dcpId, executionYearId, studentId, enrolmentId) %>">
	    	    <html:submit>
	        	    <bean:message key="button.coordinator.thesis.proposal.create"/>
	        	</html:submit>
		    </fr:form>
	<% } %>
    	</div>
    </logic:present>
</logic:present>

<logic:present name="proposeStartProcess">
	<div class="warning0" style="padding: 1em;">
		<p class="mtop0 mbottom1">
			<strong><bean:message key="label.attention"/>:</strong><br/>
		    <bean:message key="label.coordinator.thesis.propose.shortcut"/>
	    </p>
	</div>

		<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="bean" property="student"/>
		<% 
			final TreeSet<Enrolment> enrolments = student.getDissertationEnrolments(null);
			request.setAttribute("enrolments", enrolments);
		%>

<logic:empty name="enrolments">
	<em><bean:message key="label.student.thesis.notFound"/></em>
</logic:empty>
<logic:notEmpty name="enrolments">
<table class="tstyle2 thlight thcenter tdcenter mtop050">
	<tr>
		<th>
			<bean:message key="label.executionYear"/>
		</th>
		<th>
			<bean:message key="label.semester" bundle="STUDENT_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.degree.name"/>
		</th>
		<th>
			<bean:message key="label.state" bundle="STUDENT_RESOURCES"/>
		</th>
		<th>
		</th>
	</tr>
	<logic:iterate id="enrolment" type="net.sourceforge.fenixedu.domain.Enrolment" name="enrolments">
		<tr>
			<td>
				<bean:write name="enrolment" property="executionYear.year"/>
			</td>
			<td>
				<bean:write name="enrolment" property="executionPeriod.semester"/>
			</td>
			<td>
				<bean:write name="enrolment" property="curricularCourse.degreeCurricularPlan.degree.presentationName"/>
			</td>
				<%
					final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(enrolment.getThesis());
				%>
			<td>
				<%
					final String key = ThesisPresentationState.class.getSimpleName() + "." + thesisPresentationState.name();
				%>
				<bean:message bundle="ENUMERATION_RESOURCES" key="<%= key %>"/>
			</td>
			<td>
				<% if (thesisPresentationState == ThesisPresentationState.UNEXISTING) { %>
	    			<bean:define id="url">/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=<bean:write name="enrolment" property="degreeCurricularPlanOfStudent.externalId"/>&amp;executionYearId=<bean:write name="enrolment" property="executionYear.externalId"/>&amp;studentID=<bean:write name="enrolment" property="student.externalId"/></bean:define>
					<html:link page="<%= url %>" titleKey="title.student.thesis.submission"
							paramId="enrolmentOID" paramName="enrolment" paramProperty="externalId">
						<bean:message key="button.coordinator.thesis.proposal.create"/>
					</html:link>
				<% } %>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<br/>
<div class="color888" style="text-indent: 25px">
<%
	for (final ThesisPresentationState thesisPresentationState : ThesisPresentationState.values()) {
		final String key = ThesisPresentationState.class.getSimpleName() + "." + thesisPresentationState.name();
		final String keySimple = key + ".simple";
%>
	<p class="mvert0">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= key %>"/>
	</p>
<%
	}
%>
</div>

</logic:present>

<logic:present name="hasThesis">
    <div class="warning0" style="padding: 1em;">
        <p class="mtop0 mbottom0">
            <bean:message key="label.coordinator.thesis.existing"/>

			<bean:define id="degreeCurricularPlan" name="thesis"
					property="enrolment.degreeCurricularPlanOfDegreeModule"
					type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"/>
			<bean:define id="executionYear" name="thesis"
					property="enrolment.executionYear"
					type="net.sourceforge.fenixedu.domain.ExecutionYear"/>
			<% if (degreeCurricularPlan.isCurrentUserScientificCommissionMember(executionYear)) { %>
            		<bean:define id="thesisId" name="thesis" property="externalId"/>
            		<html:link page="<%= String.format("/manageThesis.do?method=viewThesis&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                		<bean:message key="label.coordinator.thesis.state.view"/>
            		</html:link>
            <% } %>
        </p>
    </div>
</logic:present>
