<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="finalDegreeWorkProposal">
	<table>
		<tr>
			<td class="listClasses-supheader">
				<bean:message key="finalDegreeWorkProposalHeader.number"/>:
			</td>
			<td class="listClasses-supheader" colspan="2">
				<strong>
					<bean:write name="finalDegreeWorkProposal" property="proposalNumber"/>
				</strong>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader">
				<bean:message key="label.teacher.finalWork.title"/>:
			</td>
			<td class="listClasses-supheader" colspan="2">
				<strong>
					<bean:write name="finalDegreeWorkProposal" property="title"/>
				</strong>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader">
				<strong><bean:message key="label.teacher.finalWork.responsable"/></strong>:
			</td>
			<td class="listClasses-supheader" colspan="2">
				<bean:write name="finalDegreeWorkProposal" property="orientator.name"/>
				(<bean:write name="finalDegreeWorkProposal" property="orientator.teacher.teacherNumber"/>)
				&nbsp;
				&nbsp;
				--
				&nbsp;
				&nbsp;
				<bean:write name="finalDegreeWorkProposal" property="orientatorsCreditsPercentage"/>
				<bean:message key="label.teacher.finalWork.credits.short"/>
			</td>
		</tr>
		<logic:present name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td class="listClasses-supheader">
					<strong><bean:message key="label.teacher.finalWork.coResponsable"/></strong>:
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader" colspan="2">
					<strong><bean:message key="label.teacher.finalWork.coResponsable"/></strong>:
					<bean:write name="finalDegreeWorkProposal" property="coorientator.name"/>
					(<bean:write name="finalDegreeWorkProposal" property="coorientator.teacher.teacherNumber"/>)
					&nbsp;
					&nbsp;
					--
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="coorientatorsCreditsPercentage"/>
					<bean:message key="label.teacher.finalWork.credits.short"/>
				</td>
			</tr>
		</logic:present>
		<logic:notPresent name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td class="listClasses-supheader">
					<strong>
						<bean:message key="label.teacher.finalWork.companion"/>
					<strong>:
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message key="label.teacher.finalWork.name"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companionName"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message key="label.teacher.finalWork.mail"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companionMail"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message key="label.teacher.finalWork.phone"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companionPhone"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message key="label.teacher.finalWork.companyName"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companyName"/>
				</td>
			</tr>
			<tr>
				<td class="listClasses-supheader">
				</td>
				<td class="listClasses-supheader" colspan="2">
					<bean:message key="label.teacher.finalWork.companyAdress"/>:
					&nbsp;
					&nbsp;
					<bean:write name="finalDegreeWorkProposal" property="companyAdress"/>
				</td>
			</tr>
		</logic:notPresent>
	</table>

	<br/>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.framing"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="framing"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.objectives"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="objectives"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.description"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="description"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.requirements"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="requirements"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.deliverable"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="deliverable"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.url"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="url"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.observations"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="observations"/>
	</p>

	<br/>
	<strong><bean:message key="label.teacher.finalWork.location"/></strong>:
	<p>
		<bean:write name="finalDegreeWorkProposal" property="location"/>
	</p>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposal">
	<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposal.notPresent"/></span>
</logic:notPresent>