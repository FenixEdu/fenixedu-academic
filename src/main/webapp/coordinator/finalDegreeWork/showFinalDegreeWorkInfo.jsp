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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" type="String"/>

<jsp:include page="/coordinator/context.jsp" />

<h2>
	<bean:message key="title.final.degree.work.administration"/>
</h2>	

<h3>
	<bean:message key="message.final.degree.work.administration"/>
	<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
</h3>	

<ul>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=showProposals&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>">Propostas</html:link></li>
	<li><html:link page="<%= "/manageFinalDegreeWork.do?method=showCandidates&degreeCurricularPlanID=" + degreeCurricularPlanID + "&executionDegreeOID=" + executionDegreeOID %>">Candidatos</html:link></li>
</ul>

<logic:present name="executionDegree" property="scheduling">
	<logic:notEqual name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName" value="1">
		<div class="infoop2">
			<p>
				<strong><bean:message key="message.final.degree.work.other.execution.degrees"/></strong>
			</p>
				<logic:iterate id="currentExecutionDegree" name="executionDegree" property="scheduling.executionDegreesSortedByDegreeName">
					<logic:notEqual name="currentExecutionDegree" property="externalId" value="<%= executionDegreeOID %>">
						<p class="mvvert05">
							<bean:write name="currentExecutionDegree" property="degreeCurricularPlan.presentationName"/>
						</p>
					</logic:notEqual>
				</logic:iterate>
		</div>
	</logic:notEqual>
</logic:present>




			
			<table class="tdtop mtop0">
				<tr>
					<td style="padding-right: 20px;">
						<p class="mbottom05 mtop05"><strong><bean:message key="title.proposals"/></strong></p>
						<fr:view name="summary" property="proposalsSummary" schema="thesis.bean.proposals.summary">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05"/>
								<fr:property name="labelTerminator" value=""/>
							</fr:layout>
						</fr:view>
					</td>
					<td>
						<p class="mbottom05 mtop05"><strong><bean:message key="title.candidacy"/></strong></p>
						<fr:view name="summary" property="candidaciesSummary" schema="thesis.bean.candidacies.summary">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05"/>
								<fr:property name="labelTerminator" value=""/>
							</fr:layout>
						</fr:view>
					</td>

				</tr>			
			</table>
			
			<bean:define id="count" name="summary" property="candidaciesSummary.candidatesWithoutDissertationEnrolment"/>
                        <logic:greaterThan name="count" value="0">
                          <p class="mtop05">
                            <html:link page="<%= "/manageFinalDegreeWork.do?method=showCandidatesWithoutDissertationEnrolments&executionDegreeOID=" +  executionDegreeOID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
                              <bean:write name="count"/> <bean:message key="message.candidatesWithoutDissertationEnrolment"/>
                            </html:link>
                          </p>
			</logic:greaterThan>
                        <logic:equal name="count" value="0">
                          <bean:write name="count"/> <bean:message key="message.candidatesWithoutDissertationEnrolment"/>
			</logic:equal>
                        <p class="mbottom05 mtop05"><strong><bean:message key="title.dissertations"/></strong></p>
			
			<table class="tstyle1 thlight thleft tdright mtop05">
				<logic:iterate id="stateCount" name="summary" property="thesisSummary.thesisCount">
					<bean:define id="key" name="stateCount" property="key"/>
					<tr>
						<th>
							<bean:write name="key" property="label"/>
						</th>
						<td>
							<logic:notEqual name="stateCount" property="value" value="0">
                                                          <logic:present name="executionYearId">
                                                                <bean:define id="executionYearId" name="executionYearId"/>
								<html:link page="<%= "/manageThesis.do?method=listThesis&executionYearId=" + executionYearId + "&degreeCurricularPlanID=" + degreeCurricularPlanID + "&filter=" + key %>">
									<bean:write name="stateCount" property="value"/>
								</html:link>
                                                          </logic:present>
                                                        </logic:notEqual>
							<logic:equal name="stateCount" property="value" value="0">0</logic:equal>
                                                        <logic:notEqual name="stateCount" property="value" value="0">
                                                          <logic:notPresent name="executionYearId">
								<bean:write name="stateCount" property="value"/>
                                                          </logic:notPresent>
                                                        </logic:notEqual>
						</td>
					</tr>
				</logic:iterate>
			</table>
			
			
			<p class="mbottom05"><strong><bean:message key="title.periods"/></strong></p>
			<fr:view name="summary" schema="thesis.bean.periods.summary" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05 mbottom05"/>
					<fr:property name="labelTerminator" value=""/>
				</fr:layout>
			</fr:view>

			<ul class="list5 mtop05 mbottom15"><li><html:link page="<%= "/manageFinalDegreeWork.do?method=editFinalDegreePeriods&executionDegreeOID=" +  executionDegreeOID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>"><bean:message key="label.edit.periods"/></html:link></li></ul>
		

			<p class="mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header"/></strong></p>
			<logic:present name="executionDegree" property="scheduling.allowCandaciesOnlyForStudentsWithADissertationEnrolment">
				<logic:equal name="executionDegree" property="scheduling.allowCandaciesOnlyForStudentsWithADissertationEnrolment" value="true">
					<p class="error0">
						<bean:message key="label.scheduling.allowCandaciesOnlyForStudentsWithADissertationEnrolment.warning"/>
					</p>
				</logic:equal>
			</logic:present>
			<fr:view name="executionDegree" property="scheduling" schema="thesis.requirements">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thleft tdright mtop05 mbottom05"/>
					<fr:property name="labelTerminator" value=""/>
				</fr:layout>
			</fr:view>
			<ul class="list5 mtop05"><li><html:link page="<%= "/manageFinalDegreeWork.do?method=editFinalDegreeRequirements&executionDegreeOID=" +  executionDegreeOID + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>"><bean:message key="label.edit.requirements"/></html:link></li></ul>

		</div>
		<!-- End Wrap -->
