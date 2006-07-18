<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:iterate id="finalDegreeWorkProposal" name="proposals">
	<h2><bean:message key="title.finalDegreeWorkProposal"/></h2>

	<table class="tstyle3 taright th14 breakafter showborder">
		<tr>
			<th>
				<bean:message key="finalDegreeWorkProposalHeader.year"/>:
			</th>
			<td>
				<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkProposalHeader.degree"/>:
			</th>
			<td>
				<logic:iterate id="executionDegreeIter" name="finalDegreeWorkProposal" property="scheduleing.executionDegrees">
					<bean:write name="executionDegreeIter" property="degreeCurricularPlan.degree.nome"/>
					<br/>
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkProposalHeader.number"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="proposalNumber"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkProposal.status"/>:
			</th>
			<td>
				<logic:present name="finalDegreeWorkProposal" property="status">
					<bean:write name="finalDegreeWorkProposal" property="status.key"/>
				</logic:present>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.title"/>:
			</th>
			<td>
				<span class="attention printbold"><bean:write name="finalDegreeWorkProposal" property="title"/></span>
			</td>
		</tr>
		
		
<!-- Orientador	-->

		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.responsable"/>:
			</th>
			<td>
				<em><bean:message key="label.teacher.finalWork.number"/>:</em> <bean:write name="finalDegreeWorkProposal" property="orientator.teacherNumber"/><br/>
				<em><bean:message key="label.teacher.finalWork.name"/>:</em> <bean:write name="finalDegreeWorkProposal" property="orientator.person.nome"/><br/>
				<em><bean:message key="label.teacher.finalWork.credits.short"/>:</em> <bean:write name="finalDegreeWorkProposal" property="orientatorsCreditsPercentage"/><br/>
			</td>
		</tr>
	



<!-- Co-Orientador	-->

		<logic:present name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<th>
					<bean:message key="label.teacher.finalWork.coResponsable"/>:
				</th>
				<td>
					<em><bean:message key="label.teacher.finalWork.number"/>:</em> <bean:write name="finalDegreeWorkProposal" property="coorientator.teacherNumber"/><br/>
					<em><bean:message key="label.teacher.finalWork.name"/>:</em> <bean:write name="finalDegreeWorkProposal" property="coorientator.person.nome"/><br/>
					<em><bean:message key="label.teacher.finalWork.credits.short"/>:</em> <bean:write name="finalDegreeWorkProposal" property="coorientatorsCreditsPercentage"/><br/>
				</td>			
			</tr>
		</logic:present>
	
		<logic:notPresent name="finalDegreeWorkProposal" property="coorientator">
			<tr>
				<th>
					<bean:message key="label.teacher.finalWork.companion"/>:
				</th>
				<td>
					<em><bean:message key="label.teacher.finalWork.name"/>:</em> <bean:write name="finalDegreeWorkProposal" property="companionName"/><br/>
					<em><bean:message key="label.teacher.finalWork.mail"/>:</em> <bean:write name="finalDegreeWorkProposal" property="companionMail"/><br/>
					<em><bean:message key="label.teacher.finalWork.phone"/>:</em> <bean:write name="finalDegreeWorkProposal" property="companionPhone"/><br/>
					<em><bean:message key="label.teacher.finalWork.companyName"/>:</em> <bean:write name="finalDegreeWorkProposal" property="companyName"/><br/>
					<em><bean:message key="label.teacher.finalWork.companyAdress"/>:</em> <bean:write name="finalDegreeWorkProposal" property="companyAdress"/><br/>
				</td>
			</tr>
		</logic:notPresent>
		

		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.framing"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="framing"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.objectives"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="objectives"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.description"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="description"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.requirements"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="requirements"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.deliverable"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="deliverable"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.url"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="url"/>
			</td>
		</tr>
		
		<logic:present name="finalDegreeWorkProposal" property="branches">
			<tr>
				<th>
					<bean:message key="label.teacher.finalWork.priority.info"/>:
				</th>
			<td>
			<logic:iterate id="branch" name="finalDegreeWorkProposal" property="branches">
				<bean:write name="branch" property="name"/><br/>
			</logic:iterate>		
			</td>
			</tr>
		</logic:present>
		
		
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.numberOfGroupElements"/>:
			</th>
			<td>
				<em><bean:message key="label.teacher.finalWork.minimumNumberGroupElements"/>:</em> <bean:write name="finalDegreeWorkProposal" property="minimumNumberOfGroupElements"/><br/>
				<em><bean:message key="label.teacher.finalWork.maximumNumberGroupElements"/>:</em> <bean:write name="finalDegreeWorkProposal" property="maximumNumberOfGroupElements"/><br/>
			</td>
		</td>
		
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.degreeType"/>:
			</th>
			<td>
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
			<th>
				<bean:message key="label.teacher.finalWork.observations"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="observations"/>
			</td>
		</tr>
		
		<tr>
			<th>
				<bean:message key="label.teacher.finalWork.location"/>:
			</th>
			<td>
				<bean:write name="finalDegreeWorkProposal" property="location"/>
			</td>
		</tr>
		
		<tr>
			<th>
				Trabalho attribu�do a:
			</th>
			<td>
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
