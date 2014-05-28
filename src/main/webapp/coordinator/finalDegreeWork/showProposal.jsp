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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>
<bean:define id="proposalOID" name="proposal" property="externalId" type="String"/>

<h2>
	<bean:message key="title.final.degree.work.administration"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>

<p><html:link page="<%= "/manageFinalDegreeWork.do?method=showProposals&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>">« <bean:message key="title.finalDegreeWorkProposals"/></html:link></p>

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
<div class="infoop2">
	<p>
		<strong>
			<bean:message key="message.final.degree.work.other.execution.degrees"/>
		</strong>
	</p>
		<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
			<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID %>">
				<p class="mvvert05">
					<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
				</p>
			</logic:notEqual>
		</logic:iterate>
</div>
</logic:notEqual>
</logic:present>

<ul class="mvert15">
	<li><html:link page="<%= "/finalDegreeWorkProposal.do?method=viewFinalDegreeWorkProposal&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID + "&proposalOID=" + proposalOID %>">Editar proposta</html:link></li>
</ul>


<div style="border: 1px solid #eee; padding: 0 1em; background: #fafafa; margin: 1em 0;">
	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.title"/></strong>

	<p class="mtop05"><span class="highlight1"><bean:write name="proposal" property="title"/></span></p>
	
	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.proposal.number"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="proposalNumber"/></p>
	
	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.responsable"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="orientator.name"/></p>

	<logic:present name="proposal" property="coorientator">
		<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.coadvisor"/></strong></p>

		<p class="mtop05">
			<bean:write name="proposal" property="coorientator.name"/>
		</p>
	</logic:present>

	<logic:present name="proposal" property="companionName">
		<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.companion"/></strong></p>

		<p class="mtop05">
			<bean:write name="proposal" property="companionName"/>
			<br/>
			<font color="grey">
				<bean:message key="label.teacher.finalWork.mail"/>:
			</font>
			 <bean:write name="proposal" property="companionMail"/>
			<br/>
			<font color="grey">
				<bean:message key="label.teacher.finalWork.phone"/>: 
			</font>
			<bean:write name="proposal" property="companionPhone"/>
			<br/>
			<font color="grey">
				<bean:message key="label.teacher.finalWork.companyName"/>: 
			</font>
			<bean:write name="proposal" property="companyName"/>
			<br/>
			<font color="grey">
				<bean:message key="label.teacher.finalWork.companyAdress"/>: 
			</font>
			<bean:write name="proposal" property="companyAdress"/>
		</p>
	</logic:present>
	
	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.credits"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="orientatorsCreditsPercentage"/> / <bean:write name="proposal" property="coorientatorsCreditsPercentage"/></p>
	
	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.framing.short"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="framing"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.objectives"/></strong></p>

	<p class="mtop05"><bean:write name="proposal" property="objectives"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.description"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="description"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.requirements"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="requirements"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.deliverable"/></strong></p>

	<p class="mtop05"><bean:write name="proposal" property="deliverable"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.url"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="url"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.observations"/></strong></p>
	<p class="mtop05"><bean:write name="proposal" property="observations"/></p>

	<p class="mbottom05"><strong><bean:message key="label.teacher.finalWork.location"/></strong></p>
 
	<p class="mtop05"><bean:write name="proposal" property="location"/></p>
</div>

<h3 class="mbottom05">
	<bean:message key="label.numberOfCandidates"/>
</h3>

<logic:empty name="candidacies">
	<p class="mtop05 mbottom15">
		<em><bean:message key="message.finalDegreeWorkProposal.no.candidates"/></em>
	</p>
</logic:empty>
<logic:notEmpty name="candidacies">
	<jsp:include page="showCandidates_table.jsp"></jsp:include>
</logic:notEmpty>
