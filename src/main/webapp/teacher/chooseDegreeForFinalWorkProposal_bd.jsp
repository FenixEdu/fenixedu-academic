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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
		<p><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.ListOfProposals"/>
		<html:link page="<%= "/finalWorkManagement.do?method=listProposals&amp;degree=" + degree.toString() + "&amp;executionYear=" + executionYear.toString() %>">
			<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.here"/>
		</html:link>.</p>
	</logic:notEmpty>
</logic:notEmpty>

<bean:define id="degree" name="finalWorkInformationForm" property="degree"/>
<logic:present name="finalDegreeWorkProposalHeaders">
	<logic:greaterEqual name="finalDegreeWorkProposalHeaders" value="1">
		<c:forEach var="proposal" items="${finalDegreeWorkProposalHeaders}">
			<c:if test="${proposal.executionDegreeOID == degree}">
				<%-- Pass from Infos to Domain --%>
				<c:set var="proposal" value="${proposal.proposal}"/>
				<div class="panel panel-default" id="proposal${proposal.externalId}">
					<div class="panel-heading clearfix">
						<h4 class="col-sm-8">
							${proposal.title} (${proposal.proposalNumber})
						</h4>
						<span class="col-sm-4 text-right">
							<html:link target="_blank" action="/finalWorkManagement.do?method=print&finalDegreeWorkProposalOID=${proposal.externalId}" styleClass="btn btn-default">${fr:message('resources.ApplicationResources', 'print')}</html:link>
							<html:link target="_self" action="finalWorkManagement.do?method=prepareToTransposeFinalDegreeWorkProposal&finalDegreeWorkProposalOID=${proposal.externalId}" styleClass="btn btn-default">${fr:message('resources.ApplicationResources', 'finalDegreeWorkProposalHeader.transpose')}</html:link>
						</span>
					</div>
					<div class="panel-body">
						<h5><b><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.orientatorName"/></b>: ${proposal.orientator.name}</h5>
						<c:if test="${not empty proposal.coorientator}">
							<h5><b><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.coorientatorName"/></b>: ${proposal.coorientator.name}</h5>
						</c:if>
						<c:if test="${not empty proposal.companionName}">
							<h5><b><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.companyLink"/></b>: ${proposal.companionName}</h5>
						</c:if>

						<br>

						<logic:empty name="proposal" property="groupProposals">
									<p><b><bean:message bundle="APPLICATION_RESOURCES" key="message.finalDegreeWorkProposal.no.candidates"/></b></p>
						</logic:empty>
						<logic:notEmpty name="proposal" property="groupProposals">

									<h5><b><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidates"/></b></h5>
									<table class="table">
										<thead>
											<tr>
												<th><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.preference"/></th>
												<th><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.number"/></th>
												<th><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.name"/></th>
												<th><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.email"/></th>
												<th><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.candidacies.student.phone"/></th>
												<th><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeader.teacher.attributed"/></th>
												<th></th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${proposal.groupProposals}" var="groupProposal">
												<logic:notEmpty name="groupProposal" property="finalDegreeDegreeWorkGroup">
													<c:forEach items="${groupProposal.finalDegreeDegreeWorkGroup.groupStudents}" var="groupStudent">
														<bean:define id="student" name="groupStudent" property="student"/>
														<bean:define id="registrationOID" name="student" property="externalId"/>
														<bean:define id="curriculumLink">
														/viewStudentCurriculum.do?method=prepare&amp;registrationOID=<%= registrationOID.toString() %>&amp;executionDegreeId=<%= degree.toString() %>
														</bean:define>
														<tr>
															<td><bean:write name="groupProposal" property="orderOfPreference"/></td>
															<td><html:link page='<%= curriculumLink.toString() %>'>${groupStudent.student.person.username}</html:link></td>
															<td>${groupStudent.student.person.name}</td>
															<td>${groupStudent.student.person.profile.email}</td>
															<td>
																${groupStudent.student.person.defaultPhoneNumber}
															</td>
															
															<c:if test="${groupProposal.finalDegreeDegreeWorkGroup == proposal.groupAttributedByTeacher}">
																<td>
																	<span class="glyphicon glyphicon-ok"></span>
																	<c:if test="${groupStudent.finalDegreeWorkProposalConfirmation == proposal}">
																		<bean:message bundle="APPLICATION_RESOURCES" key="label.attribution.confirmed"/>
																	</c:if>
																</td>
																<td>
																	<a href="${pageContext.request.contextPath}/teacher/finalWorkManagement.do?method=attributeFinalDegreeWork&selectedGroupProposal=${groupProposal.externalId}&executionYear=${executionYear}&degree=${degree}" class="btn btn-danger btn-xs"><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.delete.attribution"/></a>
																</td>
															</c:if>
															<c:if test="${groupProposal.finalDegreeDegreeWorkGroup != proposal.groupAttributedByTeacher}">
																<td><span class="glyphicon glyphicon-remove"></span></td>
																<td><a href="${pageContext.request.contextPath}/teacher/finalWorkManagement.do?method=attributeFinalDegreeWork&selectedGroupProposal=${groupProposal.externalId}&executionYear=${executionYear}&degree=${degree}" class="btn btn-success btn-xs"><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.attribution"/></button></td>
															</c:if>
														</tr>
													</c:forEach>
												</logic:notEmpty>
											</c:forEach>
										</tbody>
									</table>
						</logic:notEmpty>
					</div>
				</div>
			</c:if>
		</c:forEach>
	</logic:greaterEqual>
	<logic:lessThan name="finalDegreeWorkProposalHeaders" value="1">
		<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeaders.notPresent"/></span>
	</logic:lessThan>
</logic:present>

<logic:notPresent name="finalDegreeWorkProposalHeaders">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposalHeaders.notPresent"/></span>
</logic:notPresent>
