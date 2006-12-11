<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.candidacy"/></h2>

<div class="mvert15">
<!-- Error messages go here -->
<html:errors />
</div>

<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="somemethod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentToRemove" property="studentToRemove"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>

	<bean:message key="label.finalDegreeWork.degree"/>:
	<br />
	<html:select bundle="HTMLALT_RESOURCES" property="executionDegreeOID" size="1"
				 onchange='this.form.method.value=\'selectExecutionDegree\';this.form.page.value=\'0\';this.form.submit();'>
		<html:option value=""><!-- w3c complient--></html:option>
		<html:options property="idInternal"
					  labelProperty="infoDegreeCurricularPlan.infoDegree.nome"
					  collection="infoExecutionDegrees" />
	</html:select>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
	<br />
	<br />
	<logic:present name="infoGroup">
		<bean:message key="label.finalDegreeWork.group"/>:
		<br />
		<logic:present name="infoGroup" property="groupStudents">
			<table>
				<tr>
					<th class="listClasses-header">
						<bean:message key="label.finalDegreeWork.group.username"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="label.finalDegreeWork.group.name"/>
					</th>
					<th class="listClasses-header">
					</th>
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
					<td class="listClasses">
						<bean:define id="onClick">
							this.form.method.value='removeStudent';this.form.studentToRemove.value='<bean:write name="student" property="idInternal"/>';
						</bean:define>
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= onClick.toString() %>'>
							<bean:message key="label.finalDegreeWork.group.remove"/>
						</html:submit>
					</td>
				</tr>
			</logic:iterate>
				<tr>
					<td class="listClasses">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentUsernameToAdd" property="studentUsernameToAdd" size="6"/>
					</td>
					<td class="listClasses">
					</td>
					<td class="listClasses">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='this.form.method.value=\'addStudent\';'>
							<bean:message key="label.finalDegreeWork.group.add"/>
						</html:submit>
					</td>
				</tr>
			</table>
		</logic:present>
		<br />
		<bean:message key="label.finalDegreeWork.groupProposals"/>:
		<br />
		<logic:present name="infoGroup" property="groupProposals">
			<table>
				<tr>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="label.finalDegreeWork.proposal.orderOfPreference"/>
					</th>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.number"/>
					</th>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.title"/>
					</th>
					<th class="listClasses-header">
						<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
					</th>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
					</th>
					<th class="listClasses-header" rowspan="2">
					</th>
				</tr>
				<tr>
					<th class="listClasses-header">
						<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
					</th>
				</tr>
				<logic:iterate id="groupProposal" name="infoGroup" property="groupProposals">

					<tr>
						<td class="listClasses" rowspan="2">
							<bean:define id="groupProposalOrderOfPreference" name="groupProposal" property="orderOfPreference"/>
							<bean:define id="onChange">
								this.form.method.value='changePreferenceOrder';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';this.form.submit();
							</bean:define>
							<bean:define id="propertyName">orderOfProposalPreference<bean:write name="groupProposal" property="idInternal"/></bean:define>
							<html:text alt='<%= propertyName %>' property='<%= propertyName %>' size="2"
									   value='<%= groupProposalOrderOfPreference.toString() %>'
									   onchange='<%= onChange.toString() %>'
								/>
							<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
								<bean:message key="button.submit"/>
							</html:submit>
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
							<bean:define id="onClick">
								this.form.method.value='removeProposal';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="idInternal"/>';
							</bean:define>
							<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= onClick.toString() %>'>
								<bean:message key="label.finalDegreeWork.group.remove"/>
							</html:submit>
						</td>
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
		<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='this.form.method.value=\'selectProposals\';'>
			<bean:message key="link.finalDegreeWork.selectProposals"/>
		</html:submit>			
	</logic:present>
</html:form>
<br />