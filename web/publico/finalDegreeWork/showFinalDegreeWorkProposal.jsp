<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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
				<bean:write name="finalDegreeWorkProposal" property="executionDegree.infoExecutionYear.nextInfoExecutionYear.year"/>
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
				<bean:message key="label.teacher.finalWork.number"/>
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
				<bean:write name="finalDegreeWorkProposal" property="orientator.person.employee.employeeNumber"/>
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
					<bean:message key="label.teacher.finalWork.number"/>
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
					<bean:write name="finalDegreeWorkProposal" property="coorientator.person.employee.employeeNumber"/>
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