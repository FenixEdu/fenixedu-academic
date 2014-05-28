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

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="finalDegreeWorkProposal">
	<table>
		<tr>
			<td class="listClasses-supheader">
				<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.number"/>:
			</td>
			<td class="listClasses-supheader" colspan="2">
				<strong>
					<bean:write name="finalDegreeWorkProposal" property="proposalNumber"/>
				</strong>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.title"/>:
			</td>
			<td class="listClasses-supheader" colspan="2">
				<strong>
					<bean:write name="finalDegreeWorkProposal" property="title"/>
				</strong>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader">
				<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.responsable"/></strong>:
			</td>
			<td class="listClasses-supheader" colspan="2">
				<bean:write name="finalDegreeWorkProposal" property="orientator.name"/>
				(<bean:write name="finalDegreeWorkProposal" property="orientator.username"/>)
				&nbsp;
				&nbsp;
				--
				&nbsp;
				&nbsp;
				<bean:write name="finalDegreeWorkProposal" property="orientatorsCreditsPercentage"/>
				<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.credits.short"/>
			</td>
		</tr>
		<logic:present name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td class="listClasses-supheader">
					<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.coResponsable"/></strong>:
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader" colspan="2">
					<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.coResponsable"/></strong>:
					<bean:write name="finalDegreeWorkProposal" property="coorientator.name"/>
					(<bean:write name="finalDegreeWorkProposal" property="coorientator.username"/>)
					&nbsp;
					&nbsp;
					--
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="coorientatorsCreditsPercentage"/>
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.credits.short"/>
				</td>
			</tr>
		</logic:present>
		<logic:notPresent name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td class="listClasses-supheader">
					<strong>
						<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.companion"/>
					<strong>:
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.name"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companionName"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.mail"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companionMail"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.phone"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companionPhone"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.companyName"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companyName"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.companyAdress"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companyAdress"/>
				</td>
			</tr>
		</logic:notPresent>
	</table>

	<br/>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.framing"/></strong>:</p>
	<p width="100%"><bean:write name="finalDegreeWorkProposal" property="framing"/></p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.objectives"/></strong>:</p>
	<p width="100%"><bean:write name="finalDegreeWorkProposal" property="objectives"/></p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.description"/></strong>:</p>
	<p width="100%"><bean:write name="finalDegreeWorkProposal" property="description"/></p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.requirements"/></strong>:</p>
	<p width="100%"><bean:write name="finalDegreeWorkProposal" property="requirements"/></p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.deliverable"/></strong>:</p>
	<p width="100%"><bean:write name="finalDegreeWorkProposal" property="deliverable"/></p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.url"/></strong>:</p>
	<p>
        <bean:write name="finalDegreeWorkProposal" property="url"/>
    </p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.observations"/></strong>:</p>
	<p width="100%"><bean:write name="finalDegreeWorkProposal" property="observations"/></p>

	<br/>
	<p><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.location"/></strong>:</p>
	<p>
        <bean:write name="finalDegreeWorkProposal" property="location"/>
    </p>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposal">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.notPresent"/></span>
</logic:notPresent>