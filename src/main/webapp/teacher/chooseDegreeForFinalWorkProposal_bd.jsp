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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.finalWork"/>
</h2>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:present name="infoScheduleing">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.ProposalPeriod.interval"/>
			<dt:format pattern="dd/MM/yyyy HH:mm">
				<bean:write name="infoScheduleing" property="startOfProposalPeriod.time"/>
			</dt:format>
			-
			<dt:format pattern="dd/MM/yyyy HH:mm">
				<bean:write name="infoScheduleing" property="endOfProposalPeriod.time"/>
			</dt:format>
		</span>
	</p>
</logic:present>


<html:form action="/finalWorkManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareFinalWorkInformation"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.responsibleCreditsPercentage" property="responsibleCreditsPercentage"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.coResponsibleCreditsPercentage" property="coResponsibleCreditsPercentage"/>

	<p class="mtop15 mbottom05"><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.chooseDegreeAndYear"/>:</strong></p>
	
	<table class="tstyle5 mtop05">
		<tr>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="executionYear"
						onchange="this.form.method.value='changeExecutionYear';this.form.submit();"
						>
					<html:options collection="infoExecutionYears" property="externalId" labelProperty="nextExecutionYearYear" />
				</html:select>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="degree"
						onchange="this.form.method.value='chooseDegree';this.form.submit();"
						>
					<html:options collection="executionDegreeList" property="externalId" labelProperty="infoDegreeCurricularPlan.presentationName" />
				</html:select>
			</td>
		</tr>
	</table>
	
	<p class="mbottom05"><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.role" />:</strong></p>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.role" property="role" value="responsable" /><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.responsable"/><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.role" property="role" value="coResponsable" /><bean:message bundle="APPLICATION_RESOURCES" key="label.teacher.finalWork.coResponsable"/><br/>
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="APPLICATION_RESOURCES" key="button.create"/></html:submit></p>
</html:form>

<bean:define id="degree" name="finalWorkInformationForm" property="degree"/>
<bean:define id="executionYear" name="finalWorkInformationForm" property="executionYear"/>
<logic:notEmpty name="degree">
	<logic:notEmpty name="executionYear">
		<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.ListOfProposals"/>
		<html:link page="<%= "/finalWorkManagement.do?method=listProposals&amp;degree=" + degree.toString() + "&amp;executionYear=" + executionYear.toString() %>">
			<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.here"/>
		</html:link>.
	</logic:notEmpty>
</logic:notEmpty>

<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
		<table class="mtop15">
			<tr>
				<th class="listClasses-header" rowspan="2" colspan="2">
				</th>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.number"/>
				</th>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.title"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.orientatorName"/>
				</th>
				<th class="listClasses-header" rowspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.companyLink"/>
				</th>
				<th class="listClasses-header" rowspan="2">
				</th>
				<th class="listClasses-header" rowspan="2">
				</th>
			</tr>
			<tr>
		        <th class="listClasses-header">
		        	<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.coorientatorName"/>
	    	    </th>
			</tr>
			<% java.util.Set processedExecutionDegreeIds = new java.util.HashSet(); %>
			<bean:define id="degree" name="finalWorkInformationForm" property="degree"/>
			<logic:iterate id="finalDegreeWorkProposalHeader" name="finalDegreeWorkProposalHeaders">
				<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
				<logic:equal name="executionDegreeId" value="<%= degree.toString() %>">
					<tr>
						<th class="listClasses-header" rowspan="2" colspan="2">
							<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.proposal"/>
						</th>				
						<td class="listClasses" rowspan="2">
							<bean:write name="finalDegreeWorkProposalHeader" property="proposalNumber"/> 
						</td>
						<td class="listClasses" rowspan="2">
							<logic:equal name="finalDegreeWorkProposalHeader" property="editable" value="true">
					        	<html:link page="<%= "/finalWorkManagement.do?method=editFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString() %>">
									<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
						        </html:link>
					        </logic:equal>
							<logic:notEqual name="finalDegreeWorkProposalHeader" property="editable" value="true">
					        	<html:link page="<%= "/finalWorkManagement.do?method=viewFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId().toString() %>">
									<bean:write name="finalDegreeWorkProposalHeader" property="title"/>
						        </html:link>
					        </logic:notEqual>
						</td>
						<td class="listClasses">
							<bean:write name="finalDegreeWorkProposalHeader" property="orientatorName"/> 
						</td>
						<td class="listClasses" rowspan="2">
							<bean:write name="finalDegreeWorkProposalHeader" property="companyLink"/>
						</td>
						<td class="listClasses" rowspan="2">
						</td>
						<td class="listClasses" rowspan="2">
				        	<html:link target="_blank" page="<%= "/finalWorkManagement.do?method=print&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId() %>">
								<bean:message bundle="APPLICATION_RESOURCES" key="print"/>
					        </html:link>
					        <br />
				        	<html:link target="_self" page="<%= "/finalWorkManagement.do?method=prepareToTransposeFinalDegreeWorkProposal&amp;finalDegreeWorkProposalOID=" + ((net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader) finalDegreeWorkProposalHeader).getExternalId() %>">
								<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.transpose"/>
					        </html:link>
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
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidates"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.teacher.attribute"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.preference"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.number"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.name"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.email"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
									<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.phone"/>
								</td>
								<td bgcolor="#a2aebc" align="center">
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
								<html:form action="/finalWorkManagement">
								<bean:define id="proposalId" name="groupProposal" property="externalId"/>
								<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="attributeFinalDegreeWork"/>
								<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal" value="<%= String.valueOf(proposalId) %>"/>
								<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear"/>
								<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree"/>
								<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedGroupProposals" property="selectedGroupProposals" onclick="this.form.submit();"><bean:write name="groupProposal" property="externalId"/></html:multibox>
									</html:form>									

										</td>
										<td bgcolor="<%= bgColor %>" align="center" rowspan="<%= numberOfStudents.toString() %>">
											<a href="mailto:<%= emails %>"><bean:write name="groupProposal" property="orderOfPreference"/></a>
										</td>
										<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" length="1">
											<bean:define id="student" name="groupStudent" property="student"/>
											<td bgcolor="<%= bgColor %>" align="center">
												<bean:define id="registrationOID" name="student" property="externalId"/>
												<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
												<bean:define id="curriculumLink">
													/viewStudentCurriculum.do?method=prepare&amp;registrationOID=<%= registrationOID.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
												</bean:define>
												<html:link page='<%= curriculumLink.toString() %>'>
													<bean:write name="student" property="infoPerson.username"/>
												</html:link>
											</td>
											<td bgcolor="<%= bgColor %>" align="center">
												<bean:define id="registrationOID" name="student" property="externalId"/>
												<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
												<bean:define id="curriculumLink">
													/viewStudentCurriculum.do?method=prepare&amp;registrationOID=<%= registrationOID.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
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
											<td bgcolor="<%= bgColor %>" align="center">
												<logic:notEmpty name="groupStudent" property="finalDegreeWorkProposalConfirmation">
													<bean:define id="proposalID" name="finalDegreeWorkProposalHeader" property="externalId"/>
													<logic:equal name="groupStudent" property="finalDegreeWorkProposalConfirmation.externalId" value="<%= proposalID.toString() %>">
														<bean:message bundle="APPLICATION_RESOURCES" key="label.attribution.confirmed"/>
													</logic:equal>
												</logic:notEmpty>
											</td>											
										</logic:iterate>
									</tr>
									<logic:iterate id="groupStudent" name="groupProposal" property="infoGroup.groupStudents" offset="1">
										<bean:define id="student" name="groupStudent" property="student"/>
										<tr>
											<td bgcolor="<%= bgColor %>" align="center">
												<bean:define id="registrationOID" name="student" property="externalId"/>
												<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
												<bean:define id="curriculumLink">
													/viewStudentCurriculum.do?method=prepare&amp;registrationOID=<%= registrationOID.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
												</bean:define>
												<html:link page='<%= curriculumLink.toString() %>'>
													<bean:write name="student" property="infoPerson.username"/> 
												</html:link>
											</td>
											<td bgcolor="<%= bgColor %>" align="center">
												<bean:define id="registrationOID" name="student" property="externalId"/>
												<bean:define id="executionDegreeId" name="finalDegreeWorkProposalHeader" property="executionDegreeOID"/>
												<bean:define id="curriculumLink">
													/viewStudentCurriculum.do?method=prepare&amp;registrationOID=<%= registrationOID.toString() %>&amp;executionDegreeId=<%= executionDegreeId.toString() %>
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
											<td bgcolor="<%= bgColor %>" align="center">
												<logic:notEmpty name="groupStudent" property="finalDegreeWorkProposalConfirmation">
													<bean:define id="proposalID" name="finalDegreeWorkProposalHeader" property="externalId"/>
													<logic:equal name="groupStudent" property="finalDegreeWorkProposalConfirmation.externalId" value="<%= proposalID.toString() %>">
														<bean:message bundle="APPLICATION_RESOURCES" key="label.attribution.confirmed"/>
													</logic:equal>
												</logic:notEmpty>
											</td>
										</tr>
									</logic:iterate>					
								</logic:notEmpty>
							</logic:iterate>

					</logic:equal>
				</logic:equal>
			</logic:iterate>
		</table>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>

<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>
