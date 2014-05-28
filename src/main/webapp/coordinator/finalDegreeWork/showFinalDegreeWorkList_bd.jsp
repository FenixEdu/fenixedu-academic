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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegreeOID" scope="request" />
<bean:define id="confirmDelete">return confirm('<bean:message key="message.confirm.delete"/>')</bean:define>

<h2>
	<bean:message key="title.final.degree.work.administration"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
	<p>
		<strong>
			<bean:message key="message.final.degree.work.other.execution.degrees"/>
		</strong>
	</p>

	<table>
		<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
			<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID.toString() %>">
				<tr>
					<td>
						<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
					</td>
<!--
					<td>
						<html:form action="/manageFinalDegreeWork">
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeExecutionDegree"/>
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>
							<bean:define id="otherExecutionDegreeID" name="currentExecutionDegree" property="externalId"/>
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.otherExecutionDegreeID" property="otherExecutionDegreeID" value="<%= otherExecutionDegreeID.toString() %>"/>
							<html:submit><bean:message key="label.remove"/></html:submit>
						</html:form>
					</td>
-->
				</tr>
			</logic:notEqual>
		</logic:iterate>
<!--
		<html:form action="/manageFinalDegreeWork">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="addExecutionDegree"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

			<bean:define id="executionDegrees" name="executionDegree" property="executionYear.executionDegreesSortedByDegreeName"/>

			<tr>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.otherExecutionDegreeID" property="otherExecutionDegreeID">
						<html:option value=""/>
						<html:options collection="executionDegrees" property="externalId" labelProperty="degreeCurricularPlan.presentationName"/>
					</html:select>
				</td>
				<td>
					<html:submit><bean:message key="label.add"/></html:submit>
				</td>
			</tr>
		</html:form>
-->
	</table>
</logic:notEqual>
</logic:present>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:present name="sucessfulSetOfDegreeProposalPeriod">
	<p>
		<span class="success0"><bean:message key="finalDegreeWorkProposal.setProposalPeriod.sucess"/><br /><br /></span>
	</p>
</logic:present>
<logic:present name="sucessfulSetOfDegreeCandidacyPeriod">
	<p>
		<span class="success0"><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.sucess"/><br /><br /></span>
	</p>
</logic:present>


<html:form action="/manageFinalDegreeWork">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setFinalDegreeProposalPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>


	<p class="mtop2 mbottom05"><strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header"/></strong></p>

	<table class="tstyle4 thlight mtop05">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.date"/>
			</th>
			<th>
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.hour"/>
			</th>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.start"/>
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfProposalPeriodDate" property="startOfProposalPeriodDate" size="14"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfProposalPeriodHour" property="startOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/>
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfProposalPeriodDate" property="endOfProposalPeriodDate" size="14"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfProposalPeriodHour" property="endOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="finalDegreeWorkProposal.setProposalPeriod.button"/>
		</html:submit>
	</p>
</html:form>



<html:form action="/setFinalDegreeWorkCandidacyPeriod">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setFinalDegreeCandidacyPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<p class="mtop2 mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header"/></strong></p>

	<table class="tstyle4 thlight mtop05">
		<tr>
			<th>
			</th>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.date"/>
			</th>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.hour"/>
			</th>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.start"/>
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfCandidacyPeriodDate" property="startOfCandidacyPeriodDate" size="14"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfCandidacyPeriodHour" property="startOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.end"/>
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfCandidacyPeriodDate" property="endOfCandidacyPeriodDate" size="14"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfCandidacyPeriodHour" property="endOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
	</table>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.button"/>
		</html:submit>
	</p>
</html:form>


<html:form action="/setFinalDegreeWorkCandidacyRequirements">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setFinalDegreeCandidacyRequirements"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<html:messages id="msg" message="true">
		<p><span class="success0"><bean:write name="msg"/></span></p>
	</html:messages>

	<p class="mtop2 mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header"/>:</strong></p>

	<table class="tstyle4 thlight thright mtop05">
<!--
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumNumberOfCompletedCourses"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumNumberOfCompletedCourses" property="minimumNumberOfCompletedCourses" size="2"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumCurricularYearToCountCompletedCourses"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCurricularYearToCountCompletedCourses" property="maximumCurricularYearToCountCompletedCourses" size="2"/>
			</td>
		</tr>
-->
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsFirstCycle"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCompletedCreditsFirstCycle" property="minimumCompletedCreditsFirstCycle" size="2"/>
			</td>
		</tr>

		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsSecondCycle"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCompletedCreditsSecondCycle" property="minimumCompletedCreditsSecondCycle" size="2"/>
			</td>
		</tr>
<!--
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCurricularYear"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCompletedCurricularYear" property="minimumCompletedCurricularYear" size="2"/>
			</td>
		</tr>
-->
<!--
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumNumberOfStudents"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumNumberOfStudents" property="minimumNumberOfStudents" size="2"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfStudents"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumNumberOfStudents" property="maximumNumberOfStudents" size="2"/>
			</td>
		</tr>
-->
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfProposalCandidaciesPerGroup"/>:
			</th>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumNumberOfProposalCandidaciesPerGroup" property="maximumNumberOfProposalCandidaciesPerGroup" size="2"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.attributionByTeachers"/>:
			</th>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.attributionByTeachers" property="attributionByTeachers"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="finalDegreeWorkCandidacy.requirements.allowSimultaneousCoorientationAndCompanion"/>:
			</th>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.allowSimultaneousCoorientationAndCompanion" property="allowSimultaneousCoorientationAndCompanion"/>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.button"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form>

<p>
	<html:link page="<%= "/finalDegreeWorkProposal.do?method=proposalsXLS&amp;page=0" + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" paramId="executionDegreeOID" paramName="executionDegreeOID">
		Listagem em excel
	</html:link>
</p>

<p>
	<html:link page="<%= "/finalDegreeWorkProposal.do?method=detailedProposalList&amp;page=0"  + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>" paramId="executionDegreeOID" paramName="executionDegreeOID">
		Listagem para imprimir
	</html:link>
</p>

<html:form action="/finalDegreeWorkProposal">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createNewFinalDegreeWorkProposal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>
	<p>
		<html:submit>
			<bean:message key="finalDegreeWorkProposal.create.new.button"/>
		</html:submit>
	</p>
</html:form>

<html:form action="/manageFinalDegreeWork">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="publishAprovedProposals"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>
	<p>
		<html:submit>
			<bean:message key="finalDegreeWorkProposal.publishAproved.button"/>
		</html:submit>
	</p>
</html:form>

<logic:present name="collectionPager">
	<logic:notEqual name="numberOfPages" value="1">
		<p>
			<bean:message key="label.pages" bundle="APPLICATION_RESOURCES"/>:
			<bean:define id="pageUrl" >/coordinator/manageFinalDegreeWork.do?method=prepare&degreeCurricularPlanID=<%= degreeCurricularPlanID %>&amp;executionDegreeOID=<%= executionDegreeOID.toString() %></bean:define>
			<cp:collectionPages url="<%= pageUrl %>" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
		</p>
	</logic:notEqual>
</logic:present>

<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
	<html:form action="/manageFinalDegreeWork">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="aproveSelectedProposalsStatus"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

		<table class="tstyle4 thlight">
			<tr>
				<th rowspan="2">
				</th>
				<th rowspan="2">
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
					<bean:message key="finalDegreeWorkProposal.status"/>
				</th>
				<th rowspan="2">
				</th>
				<th rowspan="2">
				</th>
			</tr>
			<tr>
		        <th>
		        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </th>
			</tr>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="resultPage">
				<tr>
					<th rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.proposal"/>
					</th>
					<td rowspan="2">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedProposals" property="selectedProposals">
							<bean:write name="finalDegreeWorkProposalHeader" property="externalId"/>
						</html:multibox>
					</td>
					<td rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/>
						<bean:define id="deleteProposalLink">
								/manageFinalDegreeWork.do?method=deleteProposal&amp;page=0&amp;finalDegreeWorkProposalOID=<%= ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString()
										%>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID
										%>&amp;executionDegreeOID=<%= executionDegreeOID.toString() %>
						</bean:define>
						<html:link page='<%= deleteProposalLink.toString() %>' onclick="<%= confirmDelete.toString() %>">
							(<bean:message key="finalDegreeWorkProposal.delete"/>)
						</html:link>
					</td>
					<td rowspan="2">
			        	<html:link page="<%= "/finalDegreeWorkProposal.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID 
			        			+ "&amp;executionDegreeOID="
								+ executionDegreeOID.toString()
			        	%>">
							<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
				        </html:link>
					</td>
					<td>
						<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
					</td>
					<td rowspan="2">
						<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
					</td>
					<td rowspan="2">
						<logic:present name="finalDegreeWorkProposalHeader" property="status">
							<bean:write name="finalDegreeWorkProposalHeader" property="status.key"/> 
						</logic:present>
					</td>
					<td rowspan="2">
					</td>
					<td rowspan="2">
					</td>
				</tr>
				<tr>
					<td>
						<bean:write name="finalDegreeWorkProposalHeader" property="coorientatorName"/> 
					</td>
				</tr>

			<logic:equal name="finalDegreeWorkProposalHeader" property="groupProposals.empty" value="false">
			<bean:size id="numberOfGroups" name="finalDegreeWorkProposalHeader" property="groupProposals"/>
			<% int total = 1; %>
			<logic:iterate id="groupProposal" name="finalDegreeWorkProposalHeader" property="groupProposals">
				<logic:notEmpty name="groupProposal" property="infoGroup">
					<bean:size id="numberOfStudents" name="groupProposal" property="infoGroup.groupStudents"/>
					<% total += numberOfStudents.intValue(); %>
				</logic:notEmpty>
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
			<logic:iterate id="groupProposal" name="finalDegreeWorkProposalHeader" property="groupProposals" type="net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal">
			<% isOdd = !isOdd; %>
			<% if (isOdd) { %>
				<% bgColor = "#d3cec8"; %>
			<% } else { %>
				<% bgColor = "#eae7e4"; %>
			<% } 

			Integer numberOfStudents = groupProposal.getInfoGroup() == null ? Integer.valueOf(1) : Integer.valueOf(groupProposal.getInfoGroup().getGroupStudents().size());
			
			java.lang.String emails = ""; %>
			
			<logic:notEmpty name="groupProposal" property="infoGroup">
			<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
				<logic:present name="groupStudent" property="student">
					<bean:define id="student" name="groupStudent" property="student"/>
					<logic:present name="student" property="infoPerson.email">
						<bean:define id="email" name="student" property="infoPerson.email"/>
						<% emails += email; %>
					</logic:present>
				</logic:present>
			</logic:iterate>
			<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" offset="1">
				<logic:present name="groupStudent" property="student">
					<bean:define id="student" name="groupStudent" property="student"/>
					<logic:present name="student" property="infoPerson.email">
						<bean:define id="student" name="groupStudent" property="student"/>
						<bean:define id="email" name="student" property="infoPerson.email"/>
						<% emails += "," + email; %>
					</logic:present>
				</logic:present>
			</logic:iterate>
				<tr>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<bean:define id="onChange">
							this.form.method.value='attributeGroupProposal';this.form.selectedGroupProposal.value='<bean:write name="groupProposal" property="externalId"/>';this.form.submit();
						</bean:define>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.attributions" property="attributions" onchange='<%= onChange.toString() %>'>
							<bean:write name="finalDegreeWorkProposalHeader" property="externalId"/><bean:write name="groupProposal" property="infoGroup.externalId"/>
						</html:multibox>
					</td>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<a href="mailto:<%= emails %>"><bean:write name="groupProposal" property="orderOfPreference"/></a>
					</td>
					<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
						<logic:present name="groupStudent" property="student">
							<bean:define id="student" name="groupStudent" property="student"/>
							<td bgcolor="<%= bgColor %>" align="center">
								<logic:present name="student" property="number">
									<bean:define id="studentNumber" name="student" property="number"/>
									<bean:define id="curriculumLink">
										/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID.toString() %>
									</bean:define>
									<html:link page='<%= curriculumLink.toString() %>'>
										<bean:write name="student" property="infoPerson.username"/>
									</html:link>
									<bean:define id="groupProposalId" name="groupProposal" property="externalId"/>
									<bean:define id="deleteCandidacyLink">
										/manageFinalDegreeWork.do?method=deleteCandidacy&amp;page=0&amp;groupProposal=<%= groupProposalId.toString()
												%>&amp;finalDegreeWorkProposalOID=<%= ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString()
												%>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID
												%>&amp;executionDegreeOID=<%= executionDegreeOID.toString() %>
									</bean:define>
									<html:link page='<%= deleteCandidacyLink.toString() %>' onclick="<%= confirmDelete.toString() %>">
										(<bean:message key="finalDegreeWorkProposal.delete.candidacy"/>)
									</html:link>
									<bean:define id="deleteAttributionLink">
										/manageFinalDegreeWork.do?method=deleteAttributions&amp;page=0&amp;groupProposal=<%= groupProposalId.toString()
												%>&amp;finalDegreeWorkProposalOID=<%= ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString()
												%>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID
												%>&amp;executionDegreeOID=<%= executionDegreeOID.toString() %>
									</bean:define>
									<html:link page='<%= deleteAttributionLink.toString() %>' onclick="<%= confirmDelete.toString() %>">
										(<bean:message key="finalDegreeWorkProposal.delete.attribution"/>)
									</html:link>
								</logic:present>
							</td>
							<td bgcolor="<%= bgColor %>" align="center">
								<logic:present name="student" property="number">
									<bean:define id="studentNumber" name="student" property="number"/>
									<bean:define id="curriculumLink">
										/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID.toString() %>
									</bean:define>
									<html:link page='<%= curriculumLink.toString() %>'>
										<bean:write name="student" property="infoPerson.nome"/>
									</html:link>
								</logic:present>
							</td>
							<td bgcolor="<%= bgColor %>" align="center">
								<logic:present name="student" property="infoPerson.email">
									<bean:define id="email" name="student" property="infoPerson.email"/>
									<a href="mailto:<%= email %>"><bean:write name="email"/></a>
								</logic:present>
							</td>
							<td bgcolor="<%= bgColor %>" align="center">
								<bean:write name="student" property="infoPerson.telefone"/>
							</td>
						</logic:present>
					</logic:iterate>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.attributedByTeacher" property="attributedByTeacher" disabled="true">
								<bean:write name="finalDegreeWorkProposalHeader" property="externalId"/><bean:write name="groupProposal" property="infoGroup.externalId"/>
							</html:multibox>
					</td>
					<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
						<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents">
							<logic:present name="groupStudent" property="finalDegreeWorkProposalConfirmation">
								<bean:define id="proposalID" name="finalDegreeWorkProposalHeader" property="externalId"/>
								<logic:equal name="groupStudent" property="finalDegreeWorkProposalConfirmation.externalId" value="<%= proposalID.toString() %>">
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
								/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="curriculumLink">
								/manageFinalDegreeWork.do?method=getStudentCP&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>&amp;degreeCurricularPlanID=<%= degreeCurricularPlanID.toString() %>
							</bean:define>
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.nome"/>
							</html:link>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<logic:present name="student" property="infoPerson.email">
								<bean:define id="email" name="student" property="infoPerson.email"/>
								<a href="mailto:<%= email %>"><bean:write name="email"/></a>
							</logic:present>
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:write name="student" property="infoPerson.telefone"/>
						</td>
					</tr>
				</logic:iterate>					
			</logic:notEmpty>
			</logic:iterate>
			</logic:equal>

			</logic:iterate>
		</table>

		<p>
			<html:submit>
				<bean:message key="finalDegreeWorkProposal.aproveSelectedProposals.button"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='this.form.method.value=\'publishSelectedProposals\';this.form.submit();'>
				<bean:message key="finalDegreeWorkProposal.publishSelectedProposals.button"/>
			</html:submit>
		</p>
	</html:form>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<p>
			<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
		</p>
	</logic:lessThan>
</logic:present>

<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<p>
		<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</p>
</logic:notPresent>