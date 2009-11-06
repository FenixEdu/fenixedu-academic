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
				(<bean:write name="finalDegreeWorkProposal" property="orientator.employee.employeeNumber"/>)
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
					(<bean:write name="finalDegreeWorkProposal" property="coorientator.employee.employeeNumber"/>)
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
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.framing"/></strong>:
	<pre><bean:write name="finalDegreeWorkProposal" property="framing"/></pre>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.objectives"/></strong>:
	<pre><bean:write name="finalDegreeWorkProposal" property="objectives"/></pre>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.description"/></strong>:
	<pre><bean:write name="finalDegreeWorkProposal" property="description"/></pre>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.requirements"/></strong>:
	<pre><bean:write name="finalDegreeWorkProposal" property="requirements"/></pre>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.deliverable"/></strong>:
	<pre><bean:write name="finalDegreeWorkProposal" property="deliverable"/></pre>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.url"/></strong>:
	<p>
        <bean:write name="finalDegreeWorkProposal" property="url"/>
    </p>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.observations"/></strong>:
	<pre><bean:write name="finalDegreeWorkProposal" property="observations"/></pre>

	<br/>
	<strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.location"/></strong>:
	<p>
        <bean:write name="finalDegreeWorkProposal" property="location"/>
    </p>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposal">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.notPresent"/></span>
</logic:notPresent>