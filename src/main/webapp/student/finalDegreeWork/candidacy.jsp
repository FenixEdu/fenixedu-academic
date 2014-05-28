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
<%@page import="org.joda.time.Interval"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message key="title.finalDegreeWork.candidacy"/></h2>

<div class="inobullet mvert15">
	<!-- Error messages go here -->
	<html:errors />
</div>

<logic:present name="infoExecutionDegrees">
	<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionYear"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentToRemove" property="studentToRemove"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>

		<bean:message key="label.finalDegreeWork.degree"/>:
		<html:select bundle="HTMLALT_RESOURCES" property="executionYearOID" size="1"
					 onchange='this.form.method.value=\'selectExecutionYear\';this.form.page.value=\'0\';this.form.submit();'>
			<html:option value=""><!-- w3c complient--></html:option>
			<html:options property="externalId"
						  labelProperty="nextYearsYearString"
					  	collection="executionYears" />
		</html:select>
		<br />
		<br />

		<logic:empty name="infoExecutionDegrees">
			<p>
				<em>
					<bean:message key="message.no.final.degree.proposals.available"/>
				</em>
			</p>
		</logic:empty>
	</html:form>

	<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
		<bean:define id="executionDegree" name="infoExecutionDegree" property="executionDegree" type="net.sourceforge.fenixedu.domain.ExecutionDegree"/>
		<bean:define id="degree" name="executionDegree" property="degree"/>
		<logic:notPresent name="executionDegree" property="scheduling">
			<h3>
				<bean:write name="degree" property="presentationName"/>
			</h3>
			<p>
				<em>
					<bean:message key="message.scheduling.not.defined"/>
				</em>
			</p>
		</logic:notPresent>
		<logic:present name="executionDegree" property="scheduling">
			<bean:define id="scheduling" name="executionDegree" property="scheduling" type="net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing"/>
			<%
				final Interval proposalPeriodInterval = scheduling.getProposalPeriodInterval();
				final Interval candidacyPeriodInterval = scheduling.getCandidacyPeriodInterval();
			%>
			<h3>
				<bean:write name="degree" property="presentationName"/>
			</h3>
			<ul>
				<li>
					<bean:message key="finalDegreeWorkProposal.setProposalPeriod.header" bundle="APPLICATION_RESOURCES"/>:
					<%= proposalPeriodInterval == null ? "-" : proposalPeriodInterval.getStart().toString("yyyy-MM-dd HH:mm") %>
					-
					<%= proposalPeriodInterval == null ? "-" : proposalPeriodInterval.getEnd().toString("yyyy-MM-dd HH:mm") %>
				</li>
				<li>
					<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header" bundle="APPLICATION_RESOURCES"/>:
					<%= candidacyPeriodInterval == null ? "-" : candidacyPeriodInterval.getStart().toString("yyyy-MM-dd HH:mm") %>
					-
					<%= candidacyPeriodInterval == null ? "-" : candidacyPeriodInterval.getEnd().toString("yyyy-MM-dd HH:mm") %>
				</li>
				<li>
					<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsFirstCycle" bundle="APPLICATION_RESOURCES"/>:
					<%= scheduling.getMinimumCompletedCreditsFirstCycle() == null ? "---" : scheduling.getMinimumCompletedCreditsFirstCycle().toString() %>
				</li>
				<li>
					<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsSecondCycle" bundle="APPLICATION_RESOURCES"/>:
					<%= scheduling.getMinimumCompletedCreditsSecondCycle() == null ? "---" : scheduling.getMinimumCompletedCreditsSecondCycle().toString() %>
				</li>
				<li>
					<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfProposalCandidaciesPerGroup" bundle="APPLICATION_RESOURCES"/>:
					<%= scheduling.getMaximumNumberOfProposalCandidaciesPerGroup() == null ? "---" : scheduling.getMaximumNumberOfProposalCandidaciesPerGroup().toString() %>
				</li>
				<li>
					<bean:message key="finalDegreeWorkCandidacy.requirements.attributionByTeachers" bundle="APPLICATION_RESOURCES"/>:
					<% if (scheduling.getAttributionByTeachers() == null) { %>
						---
					<% } else if (scheduling.getAttributionByTeachers().booleanValue()) { %>
						<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES"/>
					<% } else { %>
						<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES"/>
					<% } %>
				</li>
				<li>
					<bean:message key="finalDegreeWorkCandidacy.requirements.allowSimultaneousCoorientationAndCompanion" bundle="APPLICATION_RESOURCES"/>:
					<% if (scheduling.getAllowSimultaneousCoorientationAndCompanion() == null) { %>
						---
					<% } else if (scheduling.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) { %>
						<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES"/>
					<% } else { %>
						<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES"/>
					<% } %>
				</li>
			</ul>
			<html:link href="<%= request.getContextPath()
					+ "/publico/finalDegreeWorks.do?method=search"
					+ "&amp;executionYearOID=" + executionDegree.getExecutionYear().getExternalId()
					+ "&amp;executionDegreeOID=" + executionDegree.getExternalId() %>"
					target="blank">
				<bean:message key="label.final.degree.work.proposals.list" bundle="APPLICATION_RESOURCES"/>
			</html:link>
			|
			<html:link action="<%= "/finalDegreeWorkCandidacy.do?method=selectExecutionDegree"
					+ "&amp;executionDegreeOID=" + executionDegree.getExternalId() %>">
				<bean:message key="link.finalDegreeWork.selectProposals"/>
			</html:link>
			|
			<html:link action="<%= "/finalDegreeWorkAttribution.do?method=prepareWithArgs"
					+ "&amp;executionYearOID=" + executionDegree.getExecutionYear().getExternalId() %>">
				<bean:message key="link.finalDegreeWork.confirmAttribution"/>
			</html:link>
		</logic:present>
		<br/>
		<br/>
	</logic:iterate>
</logic:present>

<logic:present name="infoGroup">
	<bean:define id="group" name="infoGroup" property="group" type="net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup"/>
	<br/>
	<h3>
		<%= group.getExecutionDegree().getDegree().getPresentationName() %>
		-
		<%= group.getExecutionDegree().getExecutionYear().getNextYearsYearString() %>
	</h3>
	
	<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionYear"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentToRemove" property="studentToRemove"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>

<!--
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
							this.form.method.value='removeStudent';this.form.studentToRemove.value='<bean:write name="student" property="externalId"/>';
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
-->
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
								this.form.method.value='changePreferenceOrder';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="externalId"/>';this.form.submit();
							</bean:define>
							<bean:define id="propertyName">orderOfProposalPreference<bean:write name="groupProposal" property="externalId"/></bean:define>
							<html:text alt='<%= propertyName %>' property='<%= propertyName %>' size="2"
									   value='<%= groupProposalOrderOfPreference.toString() %>'
									   onchange='<%= onChange.toString() %>'
								/>
<!--
							<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
								<bean:message key="button.submit"/>
							</html:submit>
-->
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.proposalNumber"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:define id="proposalID" name="groupProposal" property="finalDegreeWorkProposal.externalId"/>
							<html:link target="_blank" href="<%= request.getContextPath() + "/publico/finalDegreeWorks.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + proposalID.toString() %>">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.title"/>
					        </html:link>
						</td>
						<td class="listClasses">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.orientator.person.name"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="groupProposal" property="finalDegreeWorkProposal.companionName"/>
						</td>
						<td class="listClasses" rowspan="2">
							<bean:define id="onClick">
								this.form.method.value='removeProposal';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="externalId"/>';
							</bean:define>
							<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= onClick.toString() %>'>
								<bean:message key="label.finalDegreeWork.group.remove"/>
							</html:submit>
						</td>
					</tr>
					<tr>
						<td class="listClasses">
							<logic:present name="groupProposal" property="finalDegreeWorkProposal.coorientator">
								<bean:write name="groupProposal" property="finalDegreeWorkProposal.coorientator.person.name"/>
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
	</html:form>			
</logic:present>

