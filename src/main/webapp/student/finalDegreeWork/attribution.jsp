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
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.attribution"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/finalDegreeWorkAttribution">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirmAttribution"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:select bundle="HTMLALT_RESOURCES" property="executionYearOID" size="1"
				 onchange='this.form.method.value=\'prepare\';this.form.page.value=\'0\';this.form.submit();'>
		<html:option value=""><!-- w3c complient--></html:option>
		<html:options property="externalId"
					  labelProperty="nextYearsYearString"
					  collection="executionYears" />
	</html:select>
	<br/>
	<br/>

	<logic:present name="infoGroup">
		<p class="mtop15 mbottom05">
			<bean:message key="label.finalDegreeWork.group"/>
		</p>
		<logic:present name="infoGroup" property="groupStudents">
			<table class="tstyle4 thlight mtop05">
				<tr>
					<th>
						<bean:message key="label.finalDegreeWork.group.username"/>
					</th>
					<th>
						<bean:message key="label.finalDegreeWork.group.name"/>
					</th>
				</tr>
			<logic:iterate id="groupStudent" name="infoGroup" property="groupStudents">
				<bean:define id="student" name="groupStudent" property="student"/>
				<tr>
					<td>
						<bean:write name="student" property="infoPerson.username"/>
					</td>
					<td>
						<bean:write name="student" property="infoPerson.nome"/>
					</td>
				</tr>
			</logic:iterate>
			</table>
		</logic:present>

		<p class="mtop15 mbottom05">
			<bean:message key="label.finalDegreeWork.groupProposals"/>:
		</p>
		<logic:present name="infoGroup" property="groupProposals">
			<table class="tstyle4 thlight mtop05">
				<tr>
					<th rowspan="2">
						<bean:message key="label.finalDegreeWork.proposal.orderOfPreference"/>
					</th>
					<th rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.number"/>
					</th>
					<th rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.title"/>
					</th>
					<th>
						<bean:message key="finalDegreeWorkProposalHeader.orientatorName"/>
					</th>
					<th rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.companyLink"/>
					</th>
					<th rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.attribution.byTeacher"/>
					</th>
					<logic:present name="infoGroup" property="groupStudents">
						<logic:iterate id="groupStudent" name="infoGroup" property="groupStudents">
							<th rowspan="2">
								<bean:message key="finalDegreeWorkProposalHeader.student.confirmation"/>
								<bean:write name="groupStudent" property="student.infoPerson.username"/>
							</th>							
						</logic:iterate>
					</logic:present>
				</tr>
				<tr>
					<th>
						<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
					</th>
				</tr>
				<logic:iterate id="groupProposal" name="infoGroup" property="groupProposals">

					<tr>
						<td rowspan="2">
							<bean:write name="groupProposal" property="orderOfPreference"/>
						</td>
						<td rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.proposalNumber"/>
						</td>
						<td rowspan="2">
							<bean:define id="proposalID" name="groupProposal" property="finalDegreeWorkProposal.externalId"/>
							<html:link target="_blank" href="<%= request.getContextPath() + "/publico/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + proposalID.toString() %>">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.title"/>
					        </html:link>
						</td>
						<td>
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.orientator.person.name"/>
						</td>
						<td rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.companionName"/>
						</td>
						<td rowspan="2">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.attributedByTeacher" property="attributedByTeacher" disabled="true">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.externalId"/>
							</html:multibox>
						</td>
						<logic:present name="infoGroup" property="groupStudents">
							<logic:iterate id="groupStudent" indexId="i" name="infoGroup" property="groupStudents">
								<td rowspan="2">
										<% org.fenixedu.bennu.core.domain.User userView = org.fenixedu.bennu.core.security.Authenticate.getUser(); %>
									<bean:define id="username"><%= userView.getUsername() %></bean:define>
									<logic:equal name="groupStudent" property="student.infoPerson.username" value="<%= username %>">
										<bean:define id="onChange">
											this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="externalId"/>';this.form.submit();
										</bean:define>
										<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.confirmAttributions" property="confirmAttributions" onchange='<%= onChange.toString() %>'>
											<bean:write name="groupProposal" property="finalDegreeWorkProposal.externalId"/><bean:write name="groupStudent" property="student.externalId"/>
										</html:multibox>
<!--
										<html:submit styleId="<%= "javascriptButtonID" + i %>" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
											<bean:message key="button.submit"/>
										</html:submit>
-->
									</logic:equal>
									<logic:notEqual name="groupStudent" property="student.infoPerson.username" value="<%= username %>">
										<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.confirmAttributions" property="confirmAttributions" disabled="true">
											<bean:write name="groupProposal" property="finalDegreeWorkProposal.externalId"/><bean:write name="groupStudent" property="student.externalId"/>
										</html:multibox>
									</logic:notEqual>
								</td>							
							</logic:iterate>
						</logic:present>
					</tr>
					<tr>
						<td>
							<logic:present name="groupProposal" property="finalDegreeWorkProposal.coorientator">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.coorientator.person.name"/>
							</logic:present>
						</td>
					</tr>

				</logic:iterate>
			</table>
		</logic:present>
		<br />
	</logic:present>

	<logic:notPresent name="infoGroup">
		<span class="error"><!-- Error messages go here -->
			<bean:message key="finalDegreeWork.candidacies.not.found"/>
		</span>
	</logic:notPresent>

</html:form>
<br />