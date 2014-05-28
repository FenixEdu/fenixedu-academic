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
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup"%>
<%@page import="java.util.SortedSet"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmissionGrade"%>
<%@page import="net.sourceforge.fenixedu.domain.Country"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<bean:define id="outboundMobilityContextBean" name="outboundMobilityContextBean" type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean"/>

<%
	final Person person = outboundMobilityContextBean.getPerson();
	request.setAttribute("person", person);
%>

<style>
  .sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
  .sortable li { margin: 0 5px 5px 5px; padding: 5px; font-size: 1.2em; }
  #sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
  #sortable li { margin: 0 5px 5px 5px; padding: 5px; font-size: 1.2em; }
  html>body #sortable li { line-height: 1.2em; }
  .ui-state-highlight { height: 1.5em; line-height: 1.2em; }
  .ui-state-default-selected { border: 1px; border-color: green; border-style: solid; background: #CCFFCC url(images/ui-bg_glass_75_e6e6e6_1x400.png) 50% 50% repeat-x; font-weight: normal; color: #555555; }

	.savedGrade { font-size: large; color: green; font-weight: bold; }

<%	for (final Registration registration : person.getStudent().getRegistrations()) {
		if (registration.hasAnyOutboundMobilityCandidacySubmission()) {
			for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
%>
	.<%= "hide" + submission.getExternalId() %> { display: none; }
	.<%= "show" + submission.getExternalId() %> {  }
<%
    	    }
    	}
	}
%>
</style>
<script type="text/javascript">
	function EscapeKeyAbort (event, toggle1, toggle2) {
		if (event.keyCode == 27) {
			ToggleGradeInput(toggle1, toggle2);
		}
	};

	function ToggleGradeInput (toggle1, toggle2) {
		var t1 = '#' + toggle1;
		$(t1).toggle();
		var t2 = '#' + toggle2;
		$(t2).toggle();
	};

	function SaveGrade (candidacySubmissionOid, mobilityGroupOid, inputBox, gradeText, toggle1, toggle2) {
		document.getElementById('mobilityGradeForm').candidacySubmissionOid.value = candidacySubmissionOid;		
		document.getElementById('mobilityGradeForm').mobilityGroupOid.value = mobilityGroupOid;		

		var gt = '#' + gradeText;
		var ib = '#' + inputBox;
		var grade = $(ib).val();
		var checksum = $(("#mobilityGradeForm > input[name='_request_checksum_']")).attr("value");
		var contextPath = $(("#mobilityGradeForm > input[name='contentContextPath_PATH']")).attr("value");
		$.post($("#mobilityGradeForm").attr("action"), {
			method: "editGrade",
			_request_checksum_: checksum,
			contentContextPath_PATH: contextPath,
			candidacySubmissionOid: candidacySubmissionOid,
			mobilityGroupOid: mobilityGroupOid,
			grade: grade
			}, function(data) {
				$(gt).empty().append( grade );
				$(gt).addClass('savedGrade');

				var t1 = '#' + toggle1;
				$(t1).toggle();

				var t2 = '#' + toggle2;
				$(t2).fadeIn(1000, function() {
	                $(gt).removeClass("savedGrade").fadeIn(550);             
	            });
			});
	};
</script>



<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/></h2>

<h2 class="separator2">
	<%= person.getName() %>
	<span class="color777" style="font-weight:normal;">(
	<%= person.getUsername() %>
	)</span>
</h2>

<table>
	<tr>
		<td>
			<br/>
			<div style="border: 1px solid #ddd; padding: 8px; margin: 0 20px 20px 0;">
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<%= person.getUsername() %></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</div> 
		</td>
		<td>
			<table><tr>
				<td><h4><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.information"/></h4></td>
				<td>
					&nbsp;&nbsp;&nbsp;
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.email"/>
					<bean:write name="person" property="emailForSendingEmails"/>
					&nbsp;&nbsp;&nbsp;
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.phone"/>
					<bean:write name="person" property="defaultPhoneNumber"/>
					&nbsp;&nbsp;&nbsp;
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobile"/>
					<bean:write name="person" property="defaultMobilePhoneNumber"/>
				</td>
			</tr></table>
			<logic:notPresent name="person" property="student">
				<br/> <br/>
				<em style="color: red;">
					<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.person.not.a.student"/>
				</em>
				<br/> <br/> <br/> <br/>
			</logic:notPresent>
			<logic:present name="person" property="student">
				<div style="font-weight: bold;">
					<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registrations"/>
				</div>
				<table class="tstyle1 thlight mtop025">
					<tr>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.startDate"/>
						</th>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.number"/>
						</th>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.degree"/>
						</th>
						<th>
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.registrationAgreement"/>
						</th>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.state"/>
						</th>
						<th>
						</th>
					</tr>
					<logic:iterate id="registration" name="person" property="student.registrations" type="net.sourceforge.fenixedu.domain.student.Registration">
						<tr>
							<td>
								<bean:write name="registration" property="startDate"/>
							</td>
							<td>
								<bean:write name="registration" property="number"/>
							</td>
							<td>
								<bean:write name="registration" property="degreeNameWithDescription"/>
							</td>
							<td>
								<bean:write name="registration" property="registrationAgreement.description"/>
							</td>
							<td>
								<bean:write name="registration" property="activeStateType.description"/>
							</td>
							<td>
								<html:link href="<%= request.getContextPath() + "/academicAdministration/viewStudentCurriculum.do?method=prepare&registrationOID=" + registration.getExternalId() %>">
									<bean:message key="label.view"/>
								</html:link>
							</td>
						</tr>
					</logic:iterate>
				</table>
			</logic:present>
		</td>
	</tr>
</table>


<h3 class="separator2">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies"/>
</h3>

<form id="mobilityGradeForm" action="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do" %>"
		method="post">
	<input type="hidden" name="method" value="editGrade"/>
	<input type="hidden" name="candidacySubmissionOid" value=""/>
	<input type="hidden" name="mobilityGroupOid" value=""/>

	<logic:iterate id="registration" name="person" property="student.registrations" type="net.sourceforge.fenixedu.domain.student.Registration">
		<% if (registration.hasAnyOutboundMobilityCandidacySubmission()) { %>
		
				<%
					for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
					    final OutboundMobilityCandidacyPeriod candidacyPeriod = submission.getOutboundMobilityCandidacyPeriod();
					    final int candidacyCount = submission.getOutboundMobilityCandidacyContestGroupSet().size();
					    final int spanner = candidacyCount + 1;
				%>
						<h3>
							<%= registration.getDegree().getPresentationName() %>
							-
							<%= candidacyPeriod.getExecutionInterval().getName() %>
						</h3>
						<table style="width: 95%;"><tr>
						<td>
						<ul>
							<li><bean:message key="label.candidacy.period" bundle="STUDENT_RESOURCES"/>: <strong><%= candidacyPeriod.getIntervalAsString() %></strong></li>
							<li>
								<bean:message key="label.classification" bundle="ACADEMIC_OFFICE_RESOURCES"/>:
									<%	final SortedSet<OutboundMobilityCandidacyContestGroup> groups = submission.getOutboundMobilityCandidacyContestGroupSet();
										if (groups.size() > 1) { %>
											<ul>
									<%	}
										for (final OutboundMobilityCandidacyContestGroup group : groups) {
											final String hideGradeID = "hideGrade" + submission.getExternalId();
											final String showGradeID = "showGrade" + submission.getExternalId();
											final String inputGradeID = "inputGrade" + submission.getExternalId();
											final String gradeText = "grade" + submission.getExternalId();

										    final BigDecimal grade = submission.getGrade(group);
											if (groups.size() > 1) { %>
												<li><%= group.getDescription() %>:
									<%      } %>

											&nbsp;&nbsp;
											<span id="<%= showGradeID %>">
												<em id="<%= gradeText %>"><%= grade == null ? "" : grade.toString() %></em>
												&nbsp;
												<a href="#" onclick="<%= "ToggleGradeInput('" + showGradeID + "', '" + hideGradeID + "'); $('#" + inputGradeID + "').focus()" %>"
														style="border-bottom: 0px;">
													&nbsp;&nbsp;
													<img src="<%= request.getContextPath() +"/images/iconEditOn.png" %>" />
												</a>
											</span>
											<span id="<%= hideGradeID %>" style="display: none;">
												<input id="<%= inputGradeID %>" name="grade" value="<%= grade == null ? "" : grade.toString() %>"
													onchange="<%= "SaveGrade('" + submission.getExternalId()+ "', '" + group.getExternalId() + "', '" + inputGradeID + "', '" + gradeText + "', '" + hideGradeID + "', '" + showGradeID + "');" %>"
													onkeydown="<%= "EscapeKeyAbort(event, '" + hideGradeID + "', '" + showGradeID + "');" %>"
													size="5"/>
											</span>
									<%		if (groups.size() > 1) { %>
												</li>
									<%		}
										}
										if (groups.size() > 1) { %>
											</ul>
									<%	} %>
							</li>
							<li>
								<bean:message key="label.submitted.candidacies" bundle="STUDENT_RESOURCES"/>:
								<div style="margin-top: 10px; margin-left: 15px;">
									<ul style="width: 100%;" class="sortable">
										<%
											int i = 0;
											for (final OutboundMobilityCandidacy candidacy : submission.getSortedOutboundMobilityCandidacySet() ) {
											    final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
											    final Unit unit = contest.getMobilityAgreement().getUniversityUnit();
											    final Country country = unit.getCountry();
											    final String selectionClass = candidacy.getSubmissionFromSelectedCandidacy() == null ? "ui-state-default" : "ui-state-default-selected";
										%>
												<li class="<%= selectionClass %>" id="<%= candidacy.getExternalId() %>">
														<% final String name = unit.getName(); %>
														<strong><%= name %></strong>
														<%= name.length() >= 70 ? "<br/>" : "" %>&nbsp;-&nbsp;
														<%= contest.getMobilityAgreement().getMobilityProgram().getRegistrationAgreement().getDescription() %>
														<%= country == null ? "" : "(" + country.getName() + ")" %>
														<span style="float: right;">
															<% if (submission.getSelectedCandidacy() == null) { %>
																	<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=selectCandite&candidacyOid=" + candidacy.getExternalId() %>">
																		<bean:message key="label.select"/>
																	</html:link>
															<% } %>
															&nbsp;&nbsp;
															<%= contest.getOutboundMobilityCandidacyContestGroup().getDescription() %>
															<% if (candidacy.getSubmissionFromSelectedCandidacy() != null) { %>
																	<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=unselectCandite&candidacyOid=" + candidacy.getExternalId() %>"
																		style="border-bottom: 0px;"><img src="../images/iconRemoveOff.png" alt="remove"></html:link>
															<% } %>
														</span>
												</li>
										<%  } %>
									</ul>
								</div>
							</li>
						</ul>
						</td>
						</tr></table>
				<% } %>
		<% } else { %>
			<h3>
				<%= registration.getDegree().getPresentationName() %>
			</h3>
			<p>
				<bean:message key="message.outboundMobilityCandidacySubmission.none" bundle="STUDENT_RESOURCES"/>
			</p>
		<% } %>
	</logic:iterate>
	<input name="xpto" style="display: none;">
</form>
