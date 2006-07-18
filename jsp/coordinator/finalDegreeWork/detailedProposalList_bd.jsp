<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:iterate id="finalDegreeWorkProposal" name="proposals">
	<h2><bean:message key="title.finalDegreeWorkProposal"/></h2>
	<br />

	<table>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="finalDegreeWorkProposalHeader.year"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				<bean:message key="finalDegreeWorkProposalHeader.degree"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<logic:iterate id="executionDegreeIter" name="finalDegreeWorkProposal" property="scheduleing.executionDegrees">
					<bean:write name="executionDegreeIter" property="degreeCurricularPlan.degree.nome"/>
					<br/>
				</logic:iterate>
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
				<bean:message key="finalDegreeWorkProposal.status"/>
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<logic:present name="finalDegreeWorkProposal" property="status">
					<bean:write name="finalDegreeWorkProposal" property="status.key"/>
				</logic:present>
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
				<bean:write name="finalDegreeWorkProposal" property="orientator.teacherNumber"/>
			</td>
			<td bgcolor="#eae7e4" align="center">
				<bean:write name="finalDegreeWorkProposal" property="orientator.person.nome"/>
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
					<bean:write name="finalDegreeWorkProposal" property="coorientator.teacherNumber"/>
				</td>
				<td bgcolor="#eae7e4" align="center">
					<bean:write name="finalDegreeWorkProposal" property="coorientator.person.nome"/>
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
				<logic:present name="finalDegreeWorkProposal" property="degreeType">
					<bean:message bundle="ENUMERATION_RESOURCES" name="finalDegreeWorkProposal" property="degreeType.name"/>
				</logic:present>
				<logic:notPresent name="finalDegreeWorkProposal" property="degreeType">
					<bean:message bundle="ENUMERATION_RESOURCES" key="DEGREE"/>
					,
					<bean:message bundle="ENUMERATION_RESOURCES" key="MASTER_DEGREE"/>
				</logic:notPresent>
			</td>
		</tr>
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
		<tr>
			<td bgcolor="#a2aebc" align="center" colspan="3">
				Trabalho attribuido a
			</td>
		</tr>
		<tr>
			<td bgcolor="#eae7e4" align="center" colspan="3">
				<logic:present name="finalDegreeWorkProposal" property="groupAttributed">
					<logic:iterate id="groupStudent" name="finalDegreeWorkProposal" property="groupAttributed.groupStudentsSet">
						<bean:write name="groupStudent" property="student.number"/>
					</logic:iterate>
				</logic:present>
				<logic:notPresent name="finalDegreeWorkProposal" property="groupAttributed">
					<logic:present name="finalDegreeWorkProposal" property="groupAttributedByTeacher">
						<logic:iterate id="groupStudent" name="finalDegreeWorkProposal" property="groupAttributedByTeacher.groupStudentsSet">
							<bean:write name="groupStudent" property="student.number"/>
						</logic:iterate>
					</logic:present>
				</logic:notPresent>
			</td>
		</tr>
	</table>
</logic:iterate>
