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

<h2><bean:message key="title.finalDegreeWorkProposal"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="finalDegreeWorkProposal">
	<table>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="finalDegreeWorkProposalHeader.year"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="executionDegree.infoExecutionYear.nextExecutionYearYear"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="finalDegreeWorkProposalHeader.degree"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="executionDegree.infoDegreeCurricularPlan.infoDegree.nome"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="finalDegreeWorkProposalHeader.number"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="proposalNumber"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.title"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="title"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.responsable"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#c2cec8" align="center">
				<bean:message key="label.teacher.finalWork.number" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>
			</td>
			<td bgcolor="#c2cec8" align="center">
				<bean:message key="label.teacher.finalWork.name"/>
			</td>
			<td bgcolor="#c2cec8" align="center">
				<bean:message key="label.teacher.finalWork.credits.short"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center">
				<bean:write name="finalDegreeWorkProposal" property="orientator.person.username"/>
			</td>
			<td bgcolor="#eae7e4" align="center">
				<bean:write name="finalDegreeWorkProposal" property="orientator.person.name"/>
			</td>
			<td bgcolor="#eae7e4" align="center">
				<bean:write name="finalDegreeWorkProposal" property="orientatorsCreditsPercentage"/>
			</td>
		</tr>
		<logic:present name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td bgcolor="#a2aebc" align="center" colspan="3">
					<bean:message key="label.teacher.finalWork.coResponsable"/>
				</td>
			</tr>
			<tr>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.number" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>
				</td>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.name"/>
				</td>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.credits.short"/>
				</td>
			</tr>
			<tr>
				<td bgcolor="#eae7e4" align="center">
					<bean:write name="finalDegreeWorkProposal" property="coorientator.person.username"/>
				</td>
				<td bgcolor="#eae7e4" align="center">
					<bean:write name="finalDegreeWorkProposal" property="coorientator.person.name"/>
				</td>
				<td bgcolor="#eae7e4" align="center">
					<bean:write name="finalDegreeWorkProposal" property="coorientatorsCreditsPercentage"/>
				</td>
			</tr>
		</logic:present>
		<logic:notPresent name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td bgcolor="#a2aebc" align="center" colspan="3">
					<bean:message key="label.teacher.finalWork.companion"/>
				</td>
			</tr>
			<tr>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.name"/>
				</td>
				<td bgcolor="#eae7e4" align="center" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companionName"/>
				</td>
			</tr>			
			<tr>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.mail"/>
				</td>
				<td bgcolor="#eae7e4" align="center" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companionMail"/>
				</td>
			</tr>			
			<tr>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.phone"/>
				</td>
				<td bgcolor="#eae7e4" align="center" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companionPhone"/>
				</td>
			</tr>			
			<tr>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.companyName"/>
				</td>
				<td bgcolor="#eae7e4" align="center" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companyName"/>
				</td>
			</tr>			
			<tr>
				<td bgcolor="#c2cec8" align="center">
					<bean:message key="label.teacher.finalWork.companyAdress"/>
				</td>
				<td bgcolor="#eae7e4" align="center" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companyAdress"/>
				</td>
			</tr>
		</logic:notPresent>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.framing"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="framing"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.objectives"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="objectives"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.description"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="description"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.requirements"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="requirements"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.deliverable"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="deliverable"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.url"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="url"/>
			</td>
		</tr>
<!--
		<logic:present name="finalDegreeWorkProposal" property="branches">
			<tr>
				<td bgcolor="#a2aebc" align="center" colspan="3">
					<bean:message key="label.teacher.finalWork.priority.info"/>
				</td>
			</tr>
			<logic:iterate id="branch" name="finalDegreeWorkProposal" property="branches">
				<tr>
					<td bgcolor="#eae7e4" align="center" colspan="3">
						<bean:write name="branch" property="name"/>
					</td>
				</tr>
			</logic:iterate>		
		</logic:present>
-->
<!--
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.numberOfGroupElements"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#c2cec8" align="center">
				<bean:message key="label.teacher.finalWork.minimumNumberGroupElements"/>
			</td>
			<td bgcolor="#eae7e4" align="center" colspan="2">
				<bean:write name="finalDegreeWorkProposal" property="minimumNumberOfGroupElements"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#c2cec8" align="center">
				<bean:message key="label.teacher.finalWork.maximumNumberGroupElements"/>
			</td>
			<td bgcolor="#eae7e4" align="center" colspan="2">
				<bean:write name="finalDegreeWorkProposal" property="maximumNumberOfGroupElements"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.degreeType"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="degreeType"/>
			</td>
		</tr>
-->
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.observations"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="observations"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.location"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="location"/>
			</td>
		</tr>
	</table>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposal">
	<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposal.notPresent"/></span>
</logic:notPresent>