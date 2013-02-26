<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="net.sourceforge.fenixedu.domain.Country"%>
<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest"%>
<%@page import="net.sourceforge.fenixedu.domain.period.CandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:xhtml/>

<style>
  .sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
  .sortable li { margin: 0 5px 5px 5px; padding: 5px; font-size: 1.2em; }
  #sortable { list-style-type: none; margin: 0; padding: 0; width: 60%; }
  #sortable li { margin: 0 5px 5px 5px; padding: 5px; font-size: 1.2em; }
  html>body #sortable li { line-height: 1.2em; }
  .ui-state-highlight { height: 1.5em; line-height: 1.2em; }
</style>
<script>
	$(function() {
		$( "#sortable" ).sortable({
			placeholder: "ui-state-highlight",
			stop: function(event, ui) {
				var item = $(ui).attr('item');
				var itemId = item.attr('id');
				var itemParent = item.parent();
				var itemParentId = itemParent.attr('id');
				var index = $("#" + itemParentId + ">li").index(item);
				var checksum = $(("#orderForm > input[name='_request_checksum_']")).attr("value")
				$.post($("#orderForm").attr("action"), {
					method: "reorder",
					_request_checksum_: checksum,
					contentContextPath_PATH: "/estudante/estudante",
					candidacyOid: itemId,
					index: index
					}, function(data) {
						 $("#sucessfulSave").fadeIn();
						 $("#sucessfulSave").delay(2000).fadeOut();
					});
			}
		});
	});
</script>

<h2><bean:message key="link.title.student.mobility.processes"/></h2>

<bean:define id="student" name="student" type="net.sourceforge.fenixedu.domain.student.Student"/>

	<h2 class="separator2">
		<bean:write name="student" property="person.name"/>
		<span class="color777" style="font-weight:normal;">(
		<bean:write name="student" property="person.username"/>
		)</span>
	</h2>
	<logic:iterate id="registration" name="student" property="registrations" type="net.sourceforge.fenixedu.domain.student.Registration">
		<% if (registration.hasAnyOutboundMobilityCandidacySubmission()) { %>
		
				<%
					for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
					    final OutboundMobilityCandidacyPeriod candidacyPeriod = submission.getOutboundMobilityCandidacyPeriod();
					    final int candidacyCount = submission.getOutboundMobilityCandidacyCount();
					    final int spanner = candidacyCount + 1;
				%>
						<form id="orderForm" action="<%= request.getContextPath() + "/student/erasmusOutboundManagement.do?method=reorder" %>">
						</form>
						<h3>
							<%= registration.getDegree().getPresentationName() %>
							-
							<%= candidacyPeriod.getExecutionInterval().getName() %>
						</h3>
						<table style="width: 100%;"><tr>
						<td>
						<ul>
							<li><bean:message key="label.candidacy.period"/>: <strong><%= candidacyPeriod.getIntervalAsString() %></strong></li>
							<li>
								<bean:message key="label.submitted.candidacies"/>:
								<div style="margin-top: 10px; margin-left: 15px;">
									<ul
										<% if (candidacyPeriod.isOpen()) { %>
											id="sortable"
										<% } %>
										style="width: 100%;" class="sortable">
										<%
											for (final OutboundMobilityCandidacy candidacy : submission.getSortedOutboundMobilityCandidacySet() ) {
											    final Unit unit = candidacy.getOutboundMobilityCandidacyContest().getMobilityAgreement().getUniversityUnit();
											    final Country country = unit.getCountry();
										%>
												<li class="ui-state-default" id="<%= candidacy.getExternalId() %>">
														<% final String name = unit.getName(); %>
														<strong><%= name %></strong>
														<%= name.length() >= 70 ? "<br/>" : "" %>&nbsp;-&nbsp;
														<%= candidacy.getOutboundMobilityCandidacyContest().getMobilityAgreement().getMobilityProgram().getRegistrationAgreement().getDescription() %>
														<%= country == null ? "" : "(" + country.getName() + ")" %>
														<% if (candidacyPeriod.isOpen()) { %>
															<div style="float: right;">
																<html:link action="<%= "/erasmusOutboundManagement.do?method=removeCandidacy&amp;candidacyOid=" + candidacy.getExternalId() %>"
																		style="border-bottom: 0px;"><img src="../images/iconRemoveOff.png" alt="remove"></html:link>
															</div>
														<% } %>
												</li>
										<%  } %>
									</ul>
									<% if (candidacyPeriod.isOpen()) { %>
										<div style="margin-top: 10px; margin-left: 15px; width: 100%; color: graytext;">
											<bean:message key="label.info.sort.candidacies"/>
										</div>
									<% } %>
								</div>
							</li>
						</ul>
						</td>
						<td style="color: green; font-weight: bolder; text-align: center; font-size: medium; width: 30%">
							<div id="sucessfulSave" style="padding: 10px; display: none;">
								<bean:message key="label.saved.order.complete"/>
							</div>
						</td>
						</tr></table>
				<% } %>
		<% } else { %>
			<h3>
				<%= registration.getDegree().getPresentationName() %>
			</h3>
			<p>
				<bean:message key="message.outboundMobilityCandidacySubmission.none"/>
			</p>
		<% } %>
	</logic:iterate>

<br/>

<h2 class="separator2">
	<bean:message key="label.available.candidacies"/>
</h2>

<%
	final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	boolean hasSomeCandidacyPeriod = false;
	for (final CandidacyPeriod candidacyPeriod : executionYear.getCandidacyPeriodsSet()) {
    	if (candidacyPeriod instanceof OutboundMobilityCandidacyPeriod) {
        	final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod =
            	    (OutboundMobilityCandidacyPeriod) candidacyPeriod;
        	if (outboundMobilityCandidacyPeriod.hasAnyOutboundMobilityCandidacyContest()) {
	        	hasSomeCandidacyPeriod = true;
%>
				<h3>
					<bean:message key="label.execution.year"/>: <%= candidacyPeriod.getExecutionInterval().getName() %>
				</h3>
				<ul>
					<li><bean:message key="label.candidacy.period"/>: <%= outboundMobilityCandidacyPeriod.getIntervalAsString() %></li>
					<li>
						<bean:message key="label.available.candidacies"/>:
						<div style="margin-top: 10px; margin-left: 15px; width: 1050px;">
							<table class="tstyle1 mtop05">
								<tr>
									<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.country"/></th>
									<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.university"/></th>
									<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobilityProgram"/></th>
									<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.vacancies"/></th>
									<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.count"/></th>
									<th></th>
								</tr>
								<%
									for (final OutboundMobilityCandidacyContest contest : outboundMobilityCandidacyPeriod.getSortedOutboundMobilityCandidacyContest()) {
									    if (contest.findBestRegistration(student) != null) {
								%>
									<tr>
										<td>
											<%
												final Country country = contest.getMobilityAgreement().getUniversityUnit().getCountry();
												if (country != null) {
											%>
													<%= country.getName() %>
											<%  } %>
										</td>
										<td><%= contest.getMobilityAgreement().getUniversityUnit().getPresentationName() %></td>
										<td><%= contest.getMobilityAgreement().getMobilityProgram().getRegistrationAgreement().getDescription() %></td>
										<td><%= contest.getVacancies() == null ? "" : contest.getVacancies().toString() %></td>
										<td><%= contest.getOutboundMobilityCandidacyCount() %></td>
										<td>
											<%
												if (contest.isAcceptingCandidacies(student) && !contest.hasCandidacy(student)) {
											%>
													<html:link action="<%= "/erasmusOutboundManagement.do?method=apply&amp;contestOid=" + contest.getExternalId() %>">
														<bean:message key="label.apply"/>
													</html:link>
											<% } %>
										</td>
									</tr>
								<%
									    }
									}
								%>
							</table>
						</div>
					</li>
				</ul>

<%
        	}
    	}
	}
	if (!hasSomeCandidacyPeriod) {
%>
		No periods found
<%  } %>
