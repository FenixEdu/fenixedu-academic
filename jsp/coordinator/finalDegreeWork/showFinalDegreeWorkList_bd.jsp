<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<br />
<span class="error"><html:errors/><br /><br /></span>
<logic:present name="sucessfulSetOfDegreeProposalPeriod">
	<span class="sucessfulOperarion"><bean:message key="finalDegreeWorkProposal.setProposalPeriod.sucess"/><br /><br /></span>
</logic:present>
<logic:present name="sucessfulSetOfDegreeCandidacyPeriod">
	<span class="sucessfulOperarion"><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.sucess"/><br /><br /></span>
</logic:present>

<table width="90%">
<tr>
<td align="center">
<html:form action="/manageFinalDegreeWork">
	<html:hidden property="method" value="setFinalDegreeProposalPeriod"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden property="page" value="1"/>

	<strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header"/></strong>
	<br />
	<table>
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.date"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.hour"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.start"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfProposalPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfProposalPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="finalDegreeWorkProposal.setProposalPeriod.button"/>
	</html:submit>
</html:form>
</td>
<td align="center">
<html:form action="/setFinalDegreeWorkCandidacyPeriod">
	<html:hidden property="method" value="setFinalDegreeCandidacyPeriod"/>
	<html:hidden property="page" value="1"/>

	<strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header"/></strong>
	<br />
	<table>
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.date"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.hour"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.start"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfCandidacyPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="startOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.end"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfCandidacyPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text property="endOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<br />
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:submit styleClass="inputbutton">
		<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.button"/>
	</html:submit>
</html:form>
</td>
</tr>
</table>

<br />
<html:form action="/setFinalDegreeWorkCandidacyRequirements">
	<html:hidden property="method" value="setFinalDegreeCandidacyRequirements"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>

	<html:messages id="msg" message="true">
		<span class="sucessfulOperarion"><bean:write name="msg"/></span><br>
	</html:messages>

	<strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header"/>:</strong>
	<br />
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumNumberOfCompletedCourses"/>
			</td>
			<td class="listClasses">
				<html:text property="minimumNumberOfCompletedCourses" size="2"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumNumberOfStudents"/>
			</td>
			<td class="listClasses">
				<html:text property="minimumNumberOfStudents" size="2"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfStudents"/>
			</td>
			<td class="listClasses">
				<html:text property="maximumNumberOfStudents" size="2"/>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfProposalCandidaciesPerGroup"/>
			</td>
			<td class="listClasses">
				<html:text property="maximumNumberOfProposalCandidaciesPerGroup" size="2"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<html:submit styleClass="inputbutton">
					<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.button"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form>

<br />
<html:form action="/finalDegreeWorkProposal">
	<html:hidden property="method" value="createNewFinalDegreeWorkProposal"/>
	<html:hidden property="page" value="0"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:submit>
		<bean:message key="finalDegreeWorkProposal.create.new.button"/>
	</html:submit>
</html:form>
<html:form action="/manageFinalDegreeWork">
	<html:hidden property="method" value="publishAprovedProposals"/>
	<html:hidden property="page" value="0"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:submit>
		<bean:message key="finalDegreeWorkProposal.publishAproved.button"/>
	</html:submit>
</html:form>
<br />
<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
	<html:form action="/manageFinalDegreeWork">
		<html:hidden property="method" value="aproveSelectedProposalsStatus"/>
		<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
		<html:hidden property="selectedGroupProposal"/>
		<html:hidden property="page" value="0"/>

		<table>
			<tr>
				<td class="listClasses-header" rowspan="2">
				</td>
				<td class="listClasses-header" rowspan="2">
				</td>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposalHeader.number"/>
				</td>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposalHeader.title"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
				</td>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
				</td>
				<td class="listClasses-header" rowspan="2">
					<bean:message key="finalDegreeWorkProposal.status"/>
				</td>
				<td class="listClasses-header" rowspan="2">
				</td>
				<td class="listClasses-header" rowspan="2">
				</td>
			</tr>
			<tr>
		        <td class="listClasses-header">
		        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </td>
			</tr>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
				<tr>
					<td class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.proposal"/>
					</td>
					<td class="listClasses" rowspan="2">
						<html:multibox property="selectedProposals">
							<bean:write name="finalDegreeWorkProposalHeader" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/>
					</td>
					<td class="listClasses" rowspan="2">
			        	<html:link page="<%= "/finalDegreeWorkProposal.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((DataBeans.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getIdInternal().toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
							<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
				        </html:link>
					</td>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
					</td>
					<td class="listClasses" rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
					</td>
					<td class="listClasses" rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="status">
							<bean:write name="finalDegreeWorkProposalHeader" property="status.key"/> 
						</logic:present>
					</td>
					<td class="listClasses" rowspan="2">
					</td>
					<td class="listClasses" rowspan="2">
					</td>
				</tr>
				<tr>
					<td class="listClasses">
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>

			<logic:present name="finalDegreeWorkProposalHeader" property="groupProposals">
			<bean:size id="numberOfGroups" name="finalDegreeWorkProposalHeader" property="groupProposals"/>
			<% int total = 1; %>
			<logic:iterate id="groupProposal" name="finalDegreeWorkProposalHeader" property="groupProposals">
				<bean:size id="numberOfStudents" name="groupProposal" property="infoGroup.groupStudents"/>
				<% total += numberOfStudents.intValue(); %>
			</logic:iterate>
				<tr>
					<td bgcolor="#a2aebc" align="center" rowspan="<%= total %>">
						<bean:message key="finalDegreeWorkProposalHeader.candidates"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposal.attribution"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.preference"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.number"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.name"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.email"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposalHeader.candidacies.student.phone"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposal.attribution.byTeacher"/>
					</td>
					<td bgcolor="#a2aebc" align="center">
						<bean:message key="finalDegreeWorkProposal.attribution.confirmation.byStudents"/>
					</td>
				</tr>
			<% boolean isOdd = true; %>
			<% java.lang.String bgColor = null; %>
			<logic:iterate id="groupProposal" name="finalDegreeWorkProposalHeader" property="groupProposals">
			<% isOdd = !isOdd; %>
			<% if (isOdd) { %>
				<% bgColor = "#d3cec8"; %>
			<% } else { %>
				<% bgColor = "#eae7e4"; %>
			<% } %>
			<bean:size id="numberOfStudents" name="groupProposal" property="infoGroup.groupStudents"/>
			<% java.lang.String emails = ""; %>
			<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
				<bean:define id="student" name="groupStudent" property="student"/>
				<bean:define id="email" name="student" property="infoPerson.email"/>
				<% emails += email; %>
			</logic:iterate>
			<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" offset="1">
				<bean:define id="student" name="groupStudent" property="student"/>
				<bean:define id="email" name="student" property="infoPerson.email"/>
				<% emails += "," + email; %>
			</logic:iterate>
				<tr>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<bean:define id="onChange">
							this.form.method.value='attributeGroupProposal';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';this.form.submit();
						</bean:define>
						<html:multibox property="attributions" onchange='<%= onChange.toString() %>'>
							<bean:write name="finalDegreeWorkProposalHeader" property="idInternal"/><bean:write name="groupProposal" property="infoGroup.idInternal"/>
						</html:multibox>
					</td>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<a href="mailto:<%= emails %>"><bean:write name="groupProposal" property="orderOfPreference"/></a>
					</td>
					<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
						<bean:define id="student" name="groupStudent" property="student"/>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="curriculumLink">
								/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="curriculumLink">
								/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.nome"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="email" name="student" property="infoPerson.email"/>
							<a href="mailto:<%= email %>"><bean:write name="email"/></a>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:write name="student" property="infoPerson.telefone"/>
						</td>
					</logic:iterate>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
							<html:multibox property="attributedByTeacher" disabled="true">
								<bean:write name="finalDegreeWorkProposalHeader" property="idInternal"/><bean:write name="groupProposal" property="infoGroup.idInternal"/>
							</html:multibox>
					</td>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents">
							<logic:present name="groupStudent" property="finalDegreeWorkProposalConfirmation">
								<bean:define id="proposalID" name="finalDegreeWorkProposalHeader" property="idInternal"/>
								<logic:equal name="groupStudent" property="finalDegreeWorkProposalConfirmation.idInternal" value="<%= proposalID.toString() %>">
									<bean:define id="student" name="groupStudent" property="student"/>
									<bean:write name="student" property="infoPerson.username"/>
								</logic:equal>
							</logic:present>
						</logic:iterate>
					</td>
				</tr>
				<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" offset="1">
					<bean:define id="student" name="groupStudent" property="student"/>
					<tr>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="curriculumLink">
								/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="curriculumLink">
								/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.nome"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="email" name="student" property="infoPerson.email"/>
							<a href="mailto:<%= email %>"><bean:write name="email"/></a>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:write name="student" property="infoPerson.telefone"/>
						</td>
					</tr>
				</logic:iterate>					
			</logic:iterate>
			</logic:present>

			</logic:iterate>
		</table>

	<html:submit>
		<bean:message key="finalDegreeWorkProposal.aproveSelectedProposals.button"/>
	</html:submit>
	<html:submit onclick='this.form.method.value=\'publishSelectedProposals\';this.form.submit();'>
		<bean:message key="finalDegreeWorkProposal.publishSelectedProposals.button"/>
	</html:submit>
	</html:form>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<span class="error"><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>