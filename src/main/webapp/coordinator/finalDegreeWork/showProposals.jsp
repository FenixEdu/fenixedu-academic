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
<%@ page isELIgnored="true"%>
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
<bean:define id="sortBy" name="sortBy" scope="request" />

<h2>
	<bean:message key="title.finalDegreeWorkProposals"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	

<p><html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="label.return"/></html:link></p>

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
	<div class="infoop2">
		<p>
			<strong><bean:message key="message.final.degree.work.other.execution.degrees"/></strong>
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
	<li><html:link page="<%= "/finalDegreeWorkProposal.do?method=createNewFinalDegreeWorkProposal&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="finalDegreeWorkProposal.label.create"/></html:link></li>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=publishAprovedProposals&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="finalDegreeWorkProposal.publishAproved.button"/></html:link></li>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=proposalsXLS&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="link.export.to.excel" bundle="APPLICATION_RESOURCES"/></html:link></li>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=detailedProposalList&page=0&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID%>"><bean:message key="print"/></html:link></li>
</ul>
<fr:form action="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s",executionDegreeOID, degreeCurricularPlanID) %>">	
<fr:edit id="filterBean" name="filterBean" visible="false"/>		
<p class="mbottom0"><bean:message key="label.visualization.options"/></p>
<table class="tstyle1 tdtop mtop05">
	<tr>
		<td>
			<fr:edit name="filterBean" schema="thesis.proposals.filter.status" >
				<fr:layout>
					<fr:property name="columnClasses" value="dnone,noborder,dnone"></fr:property>
					<fr:property name="classes" value="mvert0 tdnopadding"></fr:property>				
				</fr:layout>
			</fr:edit>
		</td>
		<td class="inomargin">
			<fr:edit name="filterBean" slot="attribution" layout="radio" >
				<fr:layout>
					<fr:property name="classes" value=""/>
				</fr:layout>
			</fr:edit>
		</td>
		<td class="inomargin">
			<fr:edit name="filterBean" slot="withCandidates" layout="radio" />
		</td>
		<td style="vertical-align:middle;">
			<html:submit><bean:message key="button.filter"/></html:submit>
		</td>
	</tr>
</table>
</fr:form>



<fr:form action="<%= String.format("/manageFinalDegreeWork.do?executionDegreeOID=%s&degreeCurricularPlanID=%s",executionDegreeOID, degreeCurricularPlanID) %>">
<input type="hidden" name="method"/>
<logic:present name="filterBean">
	<bean:define id="filterParameters" name="filterBean" property="proposalsFilterAsParameters"/>
	<li>
		<bean:message key="label.order.by.proposal.number" bundle="APPLICATION_RESOURCES"/>
		(<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalNumber|ascending" + filterParameters, executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.ascendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>, 
		<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalNumber|descending" + filterParameters, executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.descendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>)
	</li>
	<li>
		<bean:message key="label.order.by.proposal.status" bundle="APPLICATION_RESOURCES"/>
		(<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalStatus|ascending" + filterParameters, executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.ascendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>, 
		<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalStatus|descending" + filterParameters, executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.descendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>)
	</li>
	<li>
		<bean:message key="label.order.by.number.of.candidates" bundle="APPLICATION_RESOURCES"/>
		(<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=numberOfCandidates|ascending" + filterParameters, executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.ascendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>, 
		<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=numberOfCandidates|descending" + filterParameters, executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.descendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>)
	</li>
</logic:present>
<logic:notPresent name="filterBean">
	<li>
		<bean:message key="label.order.by.proposal.number" bundle="APPLICATION_RESOURCES"/>
		(<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalNumber|ascending", executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.ascendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>, 
		<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalNumber|descending", executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.descendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>)
	</li>
	<li>
		<bean:message key="label.order.by.proposal.status" bundle="APPLICATION_RESOURCES"/>
		(<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalStatus|ascending", executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.ascendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>, 
		<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=proposalStatus|descending", executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.descendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>)
	</li>
	<li>
		<bean:message key="label.order.by.number.of.candidates" bundle="APPLICATION_RESOURCES"/>
		(<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=numberOfCandidates|ascending", executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.ascendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>, 
		<html:link
			page="<%= String.format("/manageFinalDegreeWork.do?method=showProposals&executionDegreeOID=%s&degreeCurricularPlanID=%s&sortBy=numberOfCandidates|descending", executionDegreeOID, degreeCurricularPlanID) %>">
				<bean:message key="label.descendant" bundle="APPLICATION_RESOURCES"/>
		</html:link>)
	</li>
</logic:notPresent>
<table class="mtop1 mbottom05">
	<tr>
		<td><b><bean:write name="countProposals"/></b> <bean:message key="label.finalDegreeWorkProposal.results"/> <bean:write name="filterBean"/></td>
		<td class="aright">
		<logic:present name="filterBean">
			<bean:define id="filter" name="filterBean" property="status.status"/>
			<bean:define id="filterParameters" name="filterBean" property="proposalsFilterAsParameters"/>
			<cp:collectionPages url="<%= "/coordinator/manageFinalDegreeWork.do?method=showProposals&filter=" +  filter + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID + "&sortBy=" + sortBy + filterParameters %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/></td>
		</logic:present>
		
	</tr>
	<tr>
		<td colspan="2">
				<fr:view name="proposals" schema="thesis.proposal">
					<fr:layout name="tabular">
						<fr:property name="checkable" value="true"/>
						<fr:property name="checkboxName" value="selectedProposals"/>
						<fr:property name="checkboxValue" value="externalId"/>
						<fr:property name="classes" value="tstyle1 thlight mtop05"/>
						<fr:property name="columnClasses" value=",,,inobullet inomargin,acenter,acenter,"/>
						<fr:property name="labelTerminator" value=""></fr:property>
						<fr:property name="linkGroupSeparator" value=" | "></fr:property>
						
						<fr:property name="link(approve)" value="<%= String.format("/manageFinalDegreeWork.do?method=approveProposal&executionDegreeOID=%s&degreeCurricularPlanID=%s", executionDegreeOID, degreeCurricularPlanID) %>"/>
				        <fr:property name="key(approve)" value="finalDegreeWorkProposal.approve"/>
				        <fr:property name="param(approve)" value="externalId/proposalOID"/>
				        <fr:property name="order(approve)" value="1"/>
						<fr:property name="visibleIf(approve)" value="forApproval"/>
				        
						<fr:property name="link(publish)" value="<%= String.format("/manageFinalDegreeWork.do?method=publishProposal&executionDegreeOID=%s&degreeCurricularPlanID=%s",executionDegreeOID, degreeCurricularPlanID) %>"/>
				        <fr:property name="key(publish)" value="finalDegreeWorkProposal.publish"/>
				        <fr:property name="param(publish)" value="externalId/proposalOID"/>
				        <fr:property name="order(publish)" value="2"/>
				        <fr:property name="visibleIf(publish)" value="forPublish"/>
				        
				        <fr:property name="link(delete)" value="<%= String.format("/manageFinalDegreeWork.do?method=deleteProposal&executionDegreeOID=%s&degreeCurricularPlanID=%s",executionDegreeOID, degreeCurricularPlanID) %>"/>
				        <fr:property name="key(delete)" value="finalDegreeWorkProposal.delete"/>
				        <fr:property name="param(delete)" value="externalId/proposalOID"/>
				        <fr:property name="order(delete)" value="3"/>
				        <fr:property name="confirmationKey(delete)" value="message.confirm.delete"/>
					</fr:layout>
					<fr:destination name="showProposal" path="<%= String.format("/manageFinalDegreeWork.do?method=showProposal&proposalOID=${externalId}&degreeCurricularPlanID=%s&executionDegreeOID=%s",degreeCurricularPlanID,executionDegreeOID) %>"/>
				</fr:view>
		</td>
	</tr>
</table>

<p class="mtop05">
	<html:submit onclick="this.form.method.value='aproveSelectedProposals'"><bean:message key="finalDegreeWorkProposal.aproveSelectedProposals.button"/></html:submit>
	<html:submit onclick="this.form.method.value='publishSelectedProposals'"><bean:message key="finalDegreeWorkProposal.publishSelectedProposals.button"/></html:submit>
</p>
</fr:form>
			
<p class="mtop2 mbottom05"><em><bean:message key="label.legend"/></em></p>
<ul class="list7 italic mtop05">
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR.description"/>
	</li>
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.ATTRIBUTED" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.ATTRIBUTED.description"/>
	</li>
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED.description"/>
	</li>
	<li>
		<span class="active">
			<bean:message key="CandidacyAttributionType.NOT_ATTRIBUTED" bundle="ENUMERATION_RESOURCES"/>
		</span>
		- <bean:message key="CandidacyAttributionType.NOT_ATTRIBUTED.description"/>
	</li>

</ul>