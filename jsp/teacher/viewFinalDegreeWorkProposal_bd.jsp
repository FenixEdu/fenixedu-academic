<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<br />
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="finalDegreeWorkProposal">
	<table>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.title"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="title"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.responsable"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.teacher.finalWork.number"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.teacher.finalWork.name"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="label.teacher.finalWork.credits.short"/>
			</th>
		</tr>
		<tr>
			<td class="listClasses">
				<bean:write name="finalDegreeWorkProposal" property="orientator.teacherNumber"/>
			</td>
			<td class="listClasses">
				<bean:write name="finalDegreeWorkProposal" property="orientator.infoPerson.nome"/>
			</td>
			<td class="listClasses">
				<bean:write name="finalDegreeWorkProposal" property="orientatorsCreditsPercentage"/>
			</td>
		</tr>
		<logic:present name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td class="listClasses-supheader" colspan="3">
					<bean:message key="label.teacher.finalWork.coResponsable"/>
				</td>
			</tr>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.number"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.credits.short"/>
				</th>
			</tr>
			<tr>
				<td class="listClasses">
					<bean:write name="finalDegreeWorkProposal" property="coorientator.teacherNumber"/>
				</td>
				<td class="listClasses">
					<bean:write name="finalDegreeWorkProposal" property="coorientator.infoPerson.nome"/>
				</td>
				<td class="listClasses">
					<bean:write name="finalDegreeWorkProposal" property="coorientatorsCreditsPercentage"/>
				</td>
			</tr>
		</logic:present>
		<logic:notPresent name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<td class="listClasses-supheader" colspan="3">
					<bean:message key="label.teacher.finalWork.companion"/>
				</td>
			</tr>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.name"/>
				</th>
				<td class="listClasses" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companionName"/>
				</td>
			</tr>			
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.mail"/>
				</th>
				<td class="listClasses" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companionMail"/>
				</td>
			</tr>			
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.phone"/>
				</th>
				<td class="listClasses" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companionPhone"/>
				</td>
			</tr>			
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.companyName"/>
				</th>
				<td class="listClasses" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companyName"/>
				</td>
			</tr>			
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.teacher.finalWork.companyAdress"/>
				</th>
				<td class="listClasses" colspan="2">
					<bean:write name="finalDegreeWorkProposal" property="companyAdress"/>
				</td>
			</tr>
		</logic:notPresent>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.framing"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="framing"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.objectives"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="objectives"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.description"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="description"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.requirements"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="requirements"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" align="center" colspan="3">
				<bean:message key="label.teacher.finalWork.deliverable"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" align="center" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="deliverable"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.url"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="url"/>
			</td>
		</tr>
		<logic:present name="finalDegreeWorkProposal" property="branches">
			<tr>
				<td class="listClasses-supheader" colspan="3">
					<bean:message key="label.teacher.finalWork.priority.info"/>
				</td>
			</tr>
			<logic:iterate id="branch" name="finalDegreeWorkProposal" property="branches">
				<tr>
					<td class="listClasses" colspan="3">
						<bean:write name="branch" property="name"/>
					</td>
				</tr>
			</logic:iterate>		
		</logic:present>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.numberOfGroupElements"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.teacher.finalWork.minimumNumberGroupElements"/>
			</th>
			<td class="listClasses" colspan="2">
				<bean:write name="finalDegreeWorkProposal" property="minimumNumberOfGroupElements"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="label.teacher.finalWork.maximumNumberGroupElements"/>
			</th>
			<td class="listClasses" colspan="2">
				<bean:write name="finalDegreeWorkProposal" property="maximumNumberOfGroupElements"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.degreeType"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="degreeType"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.observations"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="observations"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-supheader" colspan="3">
				<bean:message key="label.teacher.finalWork.location"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses" colspan="3">
				<bean:write name="finalDegreeWorkProposal" property="location"/>
			</td>
		</tr>
	</table>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposal">
	<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposal.notPresent"/></span>
</logic:notPresent>