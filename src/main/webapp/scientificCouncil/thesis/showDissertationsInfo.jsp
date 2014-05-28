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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<em><bean:message key="title.scientific.council.portalTitle"/></em>
<h2><bean:message key="title.finalDegreeWork.dissertations" bundle="STUDENT_RESOURCES"/></h2>

<div class="inobullet mvert15">
	<!-- Error messages go here -->
	<html:errors />
</div>

	<logic:present name="dissertationsContextBean">
    <fr:form action="<%= "/scientificCouncilManageThesis.do?method=dissertations" %>">
	    <fr:edit id="dissertationsContextBean" name="dissertationsContextBean" schema="scientificCouncil.thesis.for.execution.year.bean">
	        <fr:layout name="tabular">
	            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
	            <fr:property name="columnClasses" value=",,tdclear"/>
	        </fr:layout>
	    </fr:edit>
 	</fr:form>
	</logic:present>
		<logic:present name="executionDegree">
		
			<p class="mtop25 mbottom05"><strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header" bundle="APPLICATION_RESOURCES"/></strong></p>
			<logic:present name="scheduling">
				<logic:present name="scheduling" property="startOfProposalPeriod">
					<logic:present name="scheduling" property="endOfProposalPeriod">
						<table class="tstyle2 thlight thleft tdcenter mtop05">
							<tr>
				   				<th>
				   					<bean:message key="finalDegreeWorkProposal.setProposalPeriod.start" bundle="APPLICATION_RESOURCES"/>:
				   				</th>
				   				<td>
				   					<bean:write name="scheduling" property="startOfProposalPeriodDateYearMonthDay"/>,
				   					<bean:write name="scheduling" property="startOfProposalPeriodTimeHourMinuteSecond"/>
				   				</td>
				   			</tr>
				   			<tr>
								<th>
									<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end" bundle="APPLICATION_RESOURCES"/>:
								</th>
								<td>
									<bean:write name="scheduling" property="endOfProposalPeriodDateYearMonthDay"/>,
									<bean:write name="scheduling" property="endOfProposalPeriodTimeHourMinuteSecond"/>
								</td>
							</tr>
				   		</table>
			   		</logic:present>
		   		</logic:present>
	   		</logic:present>
	   		<logic:notPresent name="scheduling" property="startOfProposalPeriod">
				<logic:notPresent name="scheduling" property="endOfProposalPeriod">
					<p><em><bean:message key="error.message.ProposalPeriodNotDefined" bundle="STUDENT_RESOURCES"/></em></p>
		   		</logic:notPresent>
	   		</logic:notPresent>

   			<p class="mtop15 mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header" bundle="APPLICATION_RESOURCES"/></strong></p>
			<logic:present name="scheduling">
				<logic:present name="scheduling" property="startOfCandidacyPeriod">
					<logic:present name="scheduling" property="endOfCandidacyPeriod">
						<table class="tstyle2 thlight thleft tdcenter mtop05">
		    				<tr>
		    					<th>
		    						<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.start" bundle="APPLICATION_RESOURCES"/>:
		    					</th>
		    					<td>
		    						<bean:write name="scheduling" property="startOfCandidacyPeriodDateYearMonthDay"/>,
		    						<bean:write name="scheduling" property="startOfCandidacyPeriodTimeHourMinuteSecond"/>
		    					</td>
		    				</tr>
							<tr>
		    					<th>
		    						<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.end" bundle="APPLICATION_RESOURCES"/>:
		    					</th>
		    					<td>
		    						<bean:write name="scheduling" property="endOfCandidacyPeriodDateYearMonthDay"/>,
		    						<bean:write name="scheduling" property="endOfCandidacyPeriodTimeHourMinuteSecond"/>
		    					</td>
		    				</tr>
		    			</table>
		   			</logic:present>
		   		</logic:present>
	   		</logic:present>
	   		<logic:notPresent name="scheduling" property="startOfCandidacyPeriod">
		   		<logic:notPresent name="scheduling" property="endOfCandidacyPeriod">
		   			<p><em><bean:message key="error.message.CandidacyPeriodNotDefinedException" bundle="STUDENT_RESOURCES"/></em></p>
		   		</logic:notPresent>
		   	</logic:notPresent>

	   		<p class="mtop15 mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header" bundle="APPLICATION_RESOURCES"/></strong></p>
			<logic:present name="scheduling">
				<table class="tstyle2 thlight thleft tdcenter mtop05">
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsFirstCycle" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:present name="scheduling" property="minimumCompletedCreditsFirstCycle">
								<bean:write name="scheduling" property="minimumCompletedCreditsFirstCycle"/>
							</logic:present>
							<logic:notPresent name="scheduling" property="minimumCompletedCreditsFirstCycle">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsSecondCycle" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
						    <logic:present name="scheduling" property="minimumCompletedCreditsSecondCycle">
								<bean:write name="scheduling" property="minimumCompletedCreditsSecondCycle"/>
							</logic:present>
							<logic:notPresent name="scheduling" property="minimumCompletedCreditsSecondCycle">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfProposalCandidaciesPerGroup" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:present name="scheduling" property="maximumNumberOfProposalCandidaciesPerGroup">
								<bean:write name="scheduling" property="maximumNumberOfProposalCandidaciesPerGroup"/>
							</logic:present>
							<logic:notPresent name="scheduling" property="maximumNumberOfProposalCandidaciesPerGroup">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.attributionByTeachers" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:equal name="scheduling" property="attributionByTeachers" value="true">
								<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:equal name="scheduling" property="attributionByTeachers" value="false">
								<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:notPresent name="scheduling" property="attributionByTeachers">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.allowSimultaneousCoorientationAndCompanion" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:equal name="scheduling" property="allowSimultaneousCoorientationAndCompanion" value="true">
								<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:equal name="scheduling" property="allowSimultaneousCoorientationAndCompanion" value="false">
								<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:notPresent name="scheduling" property="allowSimultaneousCoorientationAndCompanion">
								-
							</logic:notPresent>
						</td>
					</tr>
				</table>
			</logic:present>
			<logic:empty name="scheduling">
				<p><em><bean:message key="message.candidacy.requirements.not.available" bundle="STUDENT_RESOURCES"/></em></p>
			</logic:empty>
		</logic:present>