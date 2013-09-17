<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<html:xhtml/>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegreeOID" scope="request" />
<bean:define id="confirmDelete">return confirm('<bean:message key="message.confirm.delete"/>')</bean:define>

<strong>
	<bean:message key="message.final.degree.work.administration"/>
</strong>
<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>

<br />
<br />

<logic:present name="executionDegree" property="scheduling">
<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
	<strong>
		<bean:message key="message.final.degree.work.other.execution.degrees"/>
	</strong>
	<br/>
	<table>
		<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
			<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID.toString() %>">
				<tr>
					<td class="listClasses">
						<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
					</td>
<!--
					<td class="listClasses">
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
				<td class="listClasses">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.otherExecutionDegreeID" property="otherExecutionDegreeID">
						<html:option value=""/>
						<html:options collection="executionDegrees" property="externalId" labelProperty="degreeCurricularPlan.presentationName"/>
					</html:select>
				</td>
				<td class="listClasses">
					<html:submit><bean:message key="label.add"/></html:submit>
				</td>
			</tr>
		</html:form>
-->
	</table>
	<br />
	<br />
</logic:notEqual>
</logic:present>

<span class="error"><!-- Error messages go here --><html:errors /><br /><br /></span>
<logic:present name="sucessfulSetOfDegreeProposalPeriod">
	<span class="success0"><bean:message key="finalDegreeWorkProposal.setProposalPeriod.sucess"/><br /><br /></span>
</logic:present>
<logic:present name="sucessfulSetOfDegreeCandidacyPeriod">
	<span class="success0"><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.sucess"/><br /><br /></span>
</logic:present>

<table width="90%">
<tr>
<td align="center">
<html:form action="/manageFinalDegreeWork">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setFinalDegreeProposalPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header"/></strong>
	<br />
	<table>
		<tr>
			<th class="listClasses-header">
			</th>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.date"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.ProposalPeriod.hour"/>
			</th>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.start"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfProposalPeriodDate" property="startOfProposalPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfProposalPeriodHour" property="startOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfProposalPeriodDate" property="endOfProposalPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfProposalPeriodHour" property="endOfProposalPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="finalDegreeWorkProposal.setProposalPeriod.button"/>
	</html:submit>
</html:form>
</td>
<td align="center">
<html:form action="/setFinalDegreeWorkCandidacyPeriod">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setFinalDegreeCandidacyPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header"/></strong>
	<br />
	<table>
		<tr>
			<th class="listClasses-header">
			</th>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.date"/>
			</th>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.CandidacyPeriod.hour"/>
			</th>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.start"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfCandidacyPeriodDate" property="startOfCandidacyPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startOfCandidacyPeriodHour" property="startOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.end"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfCandidacyPeriodDate" property="endOfCandidacyPeriodDate" size="14"/>
			</td>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endOfCandidacyPeriodHour" property="endOfCandidacyPeriodHour" size="10"/>
			</td>
		</tr>
	</table>
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.button"/>
	</html:submit>
</html:form>
</td>
</tr>
</table>

<br />
<html:form action="/setFinalDegreeWorkCandidacyRequirements">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setFinalDegreeCandidacyRequirements"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<html:messages id="msg" message="true">
		<span class="success0"><bean:write name="msg"/></span><br/>
	</html:messages>

	<strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header"/>:</strong>
	<br />
	<table>
<!--
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumNumberOfCompletedCourses"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumNumberOfCompletedCourses" property="minimumNumberOfCompletedCourses" size="2"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumCurricularYearToCountCompletedCourses"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumCurricularYearToCountCompletedCourses" property="maximumCurricularYearToCountCompletedCourses" size="2"/>
			</td>
		</tr>
-->
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsFirstCycle"/>:
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCompletedCreditsFirstCycle" property="minimumCompletedCreditsFirstCycle" size="2"/>
			</td>
		</tr>

		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsSecondCycle"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCompletedCreditsSecondCycle" property="minimumCompletedCreditsSecondCycle" size="2"/>
			</td>
		</tr>
<!--
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCurricularYear"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumCompletedCurricularYear" property="minimumCompletedCurricularYear" size="2"/>
			</td>
		</tr>
-->
<!--
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.minimumNumberOfStudents"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimumNumberOfStudents" property="minimumNumberOfStudents" size="2"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfStudents"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumNumberOfStudents" property="maximumNumberOfStudents" size="2"/>
			</td>
		</tr>
-->
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfProposalCandidaciesPerGroup"/>
			</th>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.maximumNumberOfProposalCandidaciesPerGroup" property="maximumNumberOfProposalCandidaciesPerGroup" size="2"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.attributionByTeachers"/>
			</th>
			<td class="listClasses">
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.attributionByTeachers" property="attributionByTeachers"/>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
				<bean:message key="finalDegreeWorkCandidacy.requirements.allowSimultaneousCoorientationAndCompanion"/>
			</th>
			<td class="listClasses">
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

<br />
<br />

<html:link page="/finalDegreeWorkProposal.do?method=proposalsXLS&amp;page=0" paramId="executionDegreeOID" paramName="executionDegreeOID">
	Listagem em excel
</html:link>
<br />
<html:link page="/finalDegreeWorkProposal.do?method=detailedProposalList&amp;page=0" paramId="executionDegreeOID" paramName="executionDegreeOID">
	Listagem para imprimir
</html:link>

<br />
<br />
<html:form action="/finalDegreeWorkProposal">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createNewFinalDegreeWorkProposal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<html:submit>
		<bean:message key="finalDegreeWorkProposal.create.new.button"/>
	</html:submit>
</html:form>
<html:form action="/manageFinalDegreeWork">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="publishAprovedProposals"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeOID" property="executionDegreeOID" value="<%= executionDegreeOID.toString() %>"/>

	<html:submit>
		<bean:message key="finalDegreeWorkProposal.publishAproved.button"/>
	</html:submit>
</html:form>
<br />

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

		<table>
			<tr>
				<th class="listClasses-header" rowspan="2">
				</th>
				<th class="listClasses-header" rowspan="2">
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
					<bean:message key="finalDegreeWorkProposal.status"/>
				</th>
				<th class="listClasses-header" rowspan="2">
				</th>
				<th class="listClasses-header" rowspan="2">
				</th>
			</tr>
			<tr>
		        <th class="listClasses-header">
		        	<bean:message key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </th>
			</tr>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="resultPage">
				<tr>
					<th class="listClasses-header" rowspan="2">
						<bean:message key="finalDegreeWorkProposalHeader.proposal"/>
					</th>
					<td class="listClasses" rowspan="2">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedProposals" property="selectedProposals">
							<bean:write name="finalDegreeWorkProposalHeader" property="externalId"/>
						</html:multibox>
					</td>
					<td class="listClasses" rowspan="2">
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
					<td class="listClasses" rowspan="2">
			        	<html:link page="<%= "/finalDegreeWorkProposal.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString() + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID 
			        			+ "&amp;executionDegreeOID="
								+ executionDegreeOID.toString()
			        	%>">
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
<%-- 
						<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
							<bean:message key="button.submit"/>
						</html:submit>
--%>
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
										/manageFinalDegreeWork.do?method=prepare&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
									</bean:define>
		<%-- 
									<html:link page='<%= curriculumLink.toString() %>'>
										<bean:write name="student" property="infoPerson.username"/>
									</html:link>
		--%>
									<bean:write name="student" property="infoPerson.username"/>
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
										/manageFinalDegreeWork.do?method=prepare&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
									</bean:define>
		<%-- 
									<html:link page='<%= curriculumLink.toString() %>'>
										<bean:write name="student" property="infoPerson.nome"/>
									</html:link>
		--%>
									<bean:write name="student" property="infoPerson.nome"/>							
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
								/manageFinalDegreeWork.do?method=prepare&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
							</bean:define>
<%-- 
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.username"/>
							</html:link>
--%>
							<bean:write name="student" property="infoPerson.username"/>							
						</td>
						<td bgcolor="<%= bgColor %>" align="center">
							<bean:define id="studentNumber" name="student" property="number"/>
							<bean:define id="curriculumLink">
								/manageFinalDegreeWork.do?method=prepare&amp;page=0&amp;studentNumber=<%= studentNumber.toString() %>
							</bean:define>
<%-- 
							<html:link page='<%= curriculumLink.toString() %>'>
								<bean:write name="student" property="infoPerson.nome"/>
							</html:link>
--%>
							<bean:write name="student" property="infoPerson.nome"/>
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

	<html:submit>
		<bean:message key="finalDegreeWorkProposal.aproveSelectedProposals.button"/>
	</html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='this.form.method.value=\'publishSelectedProposals\';this.form.submit();'>
		<bean:message key="finalDegreeWorkProposal.publishSelectedProposals.button"/>
	</html:submit>
	</html:form>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>
<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<span class="error"><!-- Error messages go here --><bean:message key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>