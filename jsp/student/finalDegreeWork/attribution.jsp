<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.attribution"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/finalDegreeWorkAttribution">
	<html:hidden property="method" value="confirmAttribution"/>
	<html:hidden property="selectedGroupProposal"/>
	<html:hidden property="page" value="1"/>

	<logic:present name="infoGroup">
		<bean:message key="label.finalDegreeWork.group"/>:
		<br />
		<logic:present name="infoGroup" property="groupStudents">
			<table>
				<tr>
					<td class="listClasses-header">
						<bean:message key="label.finalDegreeWork.group.username"/>
					</td>
					<td class="listClasses-header">
						<bean:message key="label.finalDegreeWork.group.name"/>
					</td>
				</tr>
			<logic:iterate id="groupStudent" name="infoGroup" property="groupStudents">
				<bean:define id="student" name="groupStudent" property="student"/>
				<tr>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.username"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="infoPerson.nome"/>
					</td>
				</tr>
			</logic:iterate>
			</table>
		</logic:present>
		<br />
		<bean:message key="label.finalDegreeWork.groupProposals"/>:
		<br />
		<logic:present name="infoGroup" property="groupProposals">
			<table>
				<tr>
					<td class="listClasses-header" rowspan="2">
						<bean:message key="label.finalDegreeWork.proposal.orderOfPreference"/>
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
						<bean:message key="finalDegreeWorkProposalHeader.attribution.byTeacher"/>
					</td>
					<logic:present name="infoGroup" property="groupStudents">
						<logic:iterate id="groupStudent" name="infoGroup" property="groupStudents">
							<td class="listClasses-header" rowspan="2">
								<bean:message key="finalDegreeWorkProposalHeader.student.confirmation"/>
								<bean:write name="groupStudent" property="student.infoPerson.username"/>
							</td>							
						</logic:iterate>
					</logic:present>
				</tr>
				<tr>
					<td class="listClasses-header">
						<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
					</td>
				</tr>
				<logic:iterate id="groupProposal" name="infoGroup" property="groupProposals">

					<tr>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="orderOfPreference"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.proposalNumber"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:define id="proposalID" name="groupProposal" property="finalDegreeWorkProposal.idInternal"/>
							<html:link target="_blank" href="<%= request.getContextPath() + "/publico/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + proposalID.toString() %>">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.title"/>
					        </html:link>
						</td>
						<td class="listClasses">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.orientator.infoPerson.nome"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.companionName"/>
						</td>
						<td class="listClasses" rowspan="2">
							<html:multibox property="attributedByTeacher" disabled="true">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.idInternal"/>
							</html:multibox>
						</td>
						<logic:present name="infoGroup" property="groupStudents">
							<logic:iterate id="groupStudent" name="infoGroup" property="groupStudents">
								<td class="listClasses" rowspan="2">
									<bean:define id="username"><%= ServidorApresentacao.Action.sop.utils.SessionUtils.getUserView(request).getUtilizador() %></bean:define>
									<logic:equal name="groupStudent" property="student.infoPerson.username" value="<%= username %>">
										<bean:define id="onChange">
											this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';this.form.submit();
										</bean:define>
										<html:multibox property="confirmAttributions" onchange='<%= onChange.toString() %>'>
											<bean:write name="groupProposal" property="finalDegreeWorkProposal.idInternal"/><bean:write name="groupStudent" property="student.idInternal"/>
										</html:multibox>
									</logic:equal>
									<logic:notEqual name="groupStudent" property="student.infoPerson.username" value="<%= username %>">
										<html:multibox property="confirmAttributions" disabled="true">
											<bean:write name="groupProposal" property="finalDegreeWorkProposal.idInternal"/><bean:write name="groupStudent" property="student.idInternal"/>
										</html:multibox>
									</logic:notEqual>
								</td>							
							</logic:iterate>
						</logic:present>
					</tr>
					<tr>
						<td class="listClasses">
							<logic:present name="groupProposal" property="finalDegreeWorkProposal.coorientator">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.coorientator.infoPerson.nome"/>
							</logic:present>
						</td>
					</tr>

				</logic:iterate>
			</table>
		</logic:present>
		<br />
	</logic:present>

	<logic:notPresent name="infoGroup">
		<span class="error">
			<bean:message key="finalDegreeWork.candidacies.not.found"/>
		</span>
	</logic:notPresent>

</html:form>
<br />