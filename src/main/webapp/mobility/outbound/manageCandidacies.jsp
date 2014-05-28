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
<%@ page language="java"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.CandidacyGroupContestState.CandidacyGroupContestStateStage"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.CandidacyGroupContestState"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>
<%@page import="net.sourceforge.fenixedu.domain.Grade"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityAgreement"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest"%>
<%@page import="net.sourceforge.fenixedu.domain.Country"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<html:xhtml />

<bean:define id="outboundMobilityContextBean" name="outboundMobilityContextBean" type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean"/>

<%
	final OutboundMobilityCandidacyContestGroup mobilityGroup = outboundMobilityContextBean.getMobilityGroups().iterator().next();
	final ExecutionYear executionYear = outboundMobilityContextBean.getExecutionYear();
%>

<style>
	.savedGrade { font-size: large; color: green; font-weight: bold; }
<%
	for (final OutboundMobilityCandidacyPeriod candidacyPeriod : outboundMobilityContextBean.getCandidacyPeriods()) {
    	for (final OutboundMobilityCandidacySubmission candidacySubmission : candidacyPeriod.getOutboundMobilityCandidacySubmissionSet()) {
    	    final Registration registration = candidacySubmission.getRegistration();
    	    final Person person = registration.getPerson();
    	    if (mobilityGroup.handles(registration.getDegree())) {
				int candidacyCount = candidacySubmission.getSortedOutboundMobilityCandidacySet().size();
				if (candidacyCount > 5) {
%>
	.<%= "hide" + candidacySubmission.getExternalId() %> { display: none; }
	.<%= "show" + candidacySubmission.getExternalId() %> {  }
<%
				}
    	    }
    	}
	}
%>
.tableStyle {
	text-align: center;
	width: 100%;
}

.stateTableStyle {
	border-collapse: separate;
	border-spacing: 10px;
}

.legendTableStyle {
	border-collapse: separate;
	border-spacing: 10px;
	border-style: dotted;
	border-width: thin;
	background-color: #FEFEFE;
}

.box {
	border-style: solid;
	border-width: thin;
	padding: 5px;
	border-radius: 2em;
	-moz-border-radius: 2em;
	text-align: center;
}

.state {
	width: 120px;
}

.legend {
	width: 12px;
}

.underWay {
	background-color: #F6E3CE;
	border-color: #B45F04;
}

.complete {
	background-color: #CEF6CE;
	border-color: #04B404;
}
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

	function SaveGrade (candidacySubmissionOid, inputBox, gradeText, toggle1, toggle2) {
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

	function ViewCandidate (personOid) {
		document.getElementById('mobilityGradeForm').method.value = 'viewCandidate';
		document.getElementById('mobilityGradeForm').personOid.value = personOid;		
		document.getElementById('mobilityGradeForm').submit();
	};
</script>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/></h2>

<h2 class="separator2">
	<% for (final ExecutionDegree executionDegree : mobilityGroup.getSortedExecutionDegrees()) { %>
			<%= executionDegree.getDegree().getSigla() %>
	<% } %>
</h2>

<table><tr>
<td style="vertical-align: top;"><ul>
	<li><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.executionYear"/>: <%= executionYear.getName() %></li>
	<li><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.period"/>: <%= outboundMobilityContextBean.getCandidacyPeriods().first().getIntervalAsString() %></li>
	<li>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group"/>:
		<% if (mobilityGroup.getMobilityCoordinatorSet().isEmpty()) {  %>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group.empty"/>
		<% } else { %>
			<a id="coordinatorGroupDisplay" href="#" onclick="$('#coordinatorGroupDisplay').toggle(); $('#coordinatorGroupHide').toggle(); $('#coordinatorGroupTable').toggle();">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group.display"/> &gt;&gt;
			</a>
			<a id="coordinatorGroupHide" href="#" onclick="$('#coordinatorGroupDisplay').toggle(); $('#coordinatorGroupHide').toggle(); $('#coordinatorGroupTable').toggle();" style="display: none;">
				&lt;&lt; <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group.hide"/>
			</a>
			<table id="coordinatorGroupTable" class="tstyle1 mtop05" style="margin-left: 30px; display: none;">
				<tr>
					<th></th>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.username"/></th>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.name"/></th>
				</tr>
				<% for (final Person person : mobilityGroup.getMobilityCoordinatorSet()) { %>
						<tr>
							<td>
								<div><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + person.getExternalId() %>"  style="padding: 1em 0;" /></div>
							</td>
							<td><%= person.getUsername() %></td>
							<td><%= person.getName() %></td>
						</tr>
				<% } %>
			</table>
		<% } %>
	</li>
</ul></td>
<td style="vertical-align: top;"><ul>
	<li>
		<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=sendEmailToCandidates&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.send.email.to.candidates"/>
		</html:link>
	</li>
	<li>
		<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=downloadCandidatesInformation&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.download.candidates.information"/>
		</html:link>
	</li>
	<% if (!mobilityGroup.isCandidacySelectionConcluded(outboundMobilityContextBean.getCandidacyPeriods().first())) { %>
		<li>
			<a href="#" onclick="$('#UploadClassificationBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.upload.candidate.classification"/>
			</a>
		</li>
	<% } %>
	<% if (!mobilityGroup.isCandidacySelectionConcluded(outboundMobilityContextBean.getCandidacyPeriods().first())
	        && mobilityGroup.areAllStudentsGraded(outboundMobilityContextBean.getCandidacyPeriods().first())) { %>
		<li>
			<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=selectCandidates&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>"
					onclick="<%= "return confirm('" + BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice",
	                        "label.mobility.candidates.select.candidates.warning") + "')" %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.select.candidates"/>
			</html:link>
		</li>
	<% } %>
	<% if (!mobilityGroup.isCandidacySelectionConcluded(outboundMobilityContextBean.getCandidacyPeriods().first())
	        && mobilityGroup.areAllStudentsGraded(outboundMobilityContextBean.getCandidacyPeriods().first())) { %>
		<li>
			<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=concludeCandidateSelection&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.conclude.candidate.selection"/>
			</html:link>
		</li>
	<% } %>
	<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
		<% if (mobilityGroup.isCandidacySelectionConcluded(outboundMobilityContextBean.getCandidacyPeriods().first())
		        && !mobilityGroup.isCandidateNotificationConcluded(outboundMobilityContextBean.getCandidacyPeriods().first())) { %>
			<li>
				<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=revertConcludeCandidateSelection&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.conclude.candidate.selection.revert"/>
				</html:link>
			</li>
			<li>
				<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=concludeCandidateNotification&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.conclude.candidate.notification"/>
				</html:link>
			</li>
		<% } %>
		<% if (mobilityGroup.isCandidateNotificationConcluded(outboundMobilityContextBean.getCandidacyPeriods().first())) { %>
				<li>
					<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=revertConcludeCandidateNotification&mobilityGroupOid=" + mobilityGroup.getExternalId() + "&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.conclude.candidate.notification.revert"/>
					</html:link>
				</li>
		<% } %>
	</academic:allowed>
</ul></td>
</tr></table>

<logic:messagesPresent message="true" property="success">
	<div class="success0">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="success">
			<span><bean:write name="messages" /></span>
		</html:messages>
	</div>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="error">
	<div class="error0">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
			<span><bean:write name="messages" /></span>
		</html:messages>
	</div>
</logic:messagesPresent>
<div id="UploadClassificationBlock" style="display: none;">
	<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.upload.candidate.classification"/></h3>
	<div class="warning1">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.upload.candidate.classification.instructions"/>
	</div>
	<fr:edit id="uploadClassification" name="outboundMobilityContextBean" action="/outboundMobilityCandidacy.do?method=uploadClassifications">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="stream" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.stream"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
    			<fr:property name="fileNameSlot" value="fileName"/>
    			<fr:property name="fileSizeSlot" value="fileSize"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
</div>

<div class="section1">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.group.instructions"/>
</div>

<%
	for (final OutboundMobilityCandidacyPeriod candidacyPeriod : outboundMobilityContextBean.getCandidacyPeriods()) {
%>
<table class="tableStyle">
	<tr><td align="center"><table class="stateTableStyle"><tr>
		<% for (final CandidacyGroupContestState state : CandidacyGroupContestState.values()) {
		    	final CandidacyGroupContestStateStage stage = state.getStage(mobilityGroup, candidacyPeriod);
		    	final String stageClass = stage == CandidacyGroupContestStateStage.UNDER_WAY ? "underWay" : stage == CandidacyGroupContestStateStage.COMPLETED ? "complete" : "";
		%>
				<td class="box state <%= stageClass %>"><%=state.getLocalizedName()%></td>
		<% } %>
	</tr></table></td></tr>
	<tr><td align="center"><table class="legendTableStyle"><tr>
		<td align="center"><strong> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.legend"/> </strong></td>
		<td class="box legend"></td>
		<td><%= CandidacyGroupContestStateStage.NOT_STARTED.getLocalizedName() %></td>
		<td class="box legend underWay"></td>
		<td><%= CandidacyGroupContestStateStage.UNDER_WAY.getLocalizedName() %></td>
		<td class="box legend complete"></td>
		<td><%= CandidacyGroupContestStateStage.COMPLETED.getLocalizedName() %></td>
	</tr></table></td></tr>
</table>
<% } %>




<form id="mobilityGradeForm" action="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?mobilityGroupOid=" + mobilityGroup.getExternalId() %>"
		method="post">
	<input type="hidden" name="method" value="editGrade"/>
	<input type="hidden" name="personOid" value=""/>
	<input type="hidden" name="executionYearOid" value="<%= outboundMobilityContextBean.getExecutionYear().getExternalId() %>"/>

<h3>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies.admitted"/>:
</h3>

<%
	for (final OutboundMobilityCandidacyPeriod candidacyPeriod : outboundMobilityContextBean.getCandidacyPeriods()) {
%>

		<table class="tstyle1 mtop05">
			<tr>
				<th></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.username"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.name"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degree"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.classification"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contests"/></th>
			</tr>
<%
    	for (final OutboundMobilityCandidacySubmission candidacySubmission : candidacyPeriod.getSortedSubmissionSet(mobilityGroup)) {
    	    final BigDecimal grade = candidacySubmission.getGrade(mobilityGroup);
    	    if (grade != null && grade.signum() == 1) {
    	    	final Registration registration = candidacySubmission.getRegistration();
    	    	final Person person = registration.getPerson();
				final String hideGradeID = "hideGrade" + candidacySubmission.getExternalId();
				final String showGradeID = "showGrade" + candidacySubmission.getExternalId();
				final String inputGradeID = "inputGrade" + candidacySubmission.getExternalId();
				final String gradeText = "grade" + candidacySubmission.getExternalId();
%>
				<tr>
					<td>
						<div><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + person.getExternalId() %>"  style="padding: 1em 0;" /></div>
					</td>
					<td>
						<a href="#" onclick="<%= "ViewCandidate('" + person.getExternalId() + "');" %>">
							<%= person.getUsername() %>
						</a>
					</td>
					<td><%= person.getName() %></td>
					<td><%= registration.getDegree().getSigla() %></td>
					<td style="text-align: center;">
						<span id="<%= showGradeID %>">
							<em id="<%= gradeText %>"><%= grade == null ? "" : grade.toString() %></em>
							&nbsp;
							<a href="#" onclick="<%= "ToggleGradeInput('" + showGradeID + "', '" + hideGradeID + "'); $('#" + inputGradeID + "').focus()" %>"
									style="border-bottom: 0px; float: right;">
								<img src="<%= request.getContextPath() +"/images/iconEditOn.png" %>" />
								&nbsp;&nbsp;
							</a>
						</span>
						<span id="<%= hideGradeID %>" style="display: none;">
							<input id="<%= inputGradeID %>" name="grade" value="<%= grade == null ? "" : grade.toString() %>"
								onchange="<%= "SaveGrade('" + candidacySubmission.getExternalId() + "', '" + inputGradeID + "', '" + gradeText + "', '" + hideGradeID + "', '" + showGradeID + "');" %>"
								onkeydown="<%= "EscapeKeyAbort(event, '" + hideGradeID + "', '" + showGradeID + "');" %>"
								size="5"/>
						</span>
					</td>
					<td>
						<ul>
							<%
								int candidacyCount = 0;
								for (final OutboundMobilityCandidacy candidacy : candidacySubmission.getSortedOutboundMobilityCandidacySet()) {
							    	final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
							    	final MobilityAgreement mobilityAgreement = contest.getMobilityAgreement();
									final Unit unit = mobilityAgreement.getUniversityUnit();
							    	final Country country = unit.getCountry();
							    	String styleString = "";
							    	if (contest.getOutboundMobilityCandidacyContestGroup() != mobilityGroup) {
							    	    if (candidacy.getSubmissionFromSelectedCandidacy() != null) {
								    	    styleString = "color: lime; font-weight: bold; font-size: 150%";
								    	} else {
								    	    styleString = "color: grey;";
								    	}
							    	} else if (candidacy.getSubmissionFromSelectedCandidacy() != null) {
							    	    styleString = "color: green; font-weight: bold; font-size: 120%";							    	    
							    	}
							    	
							%>
								<li <% if (candidacyCount++ > 4) { %> class="<%= "hide" + candidacySubmission.getExternalId() %>" <% } %>>
									<div style="<%= styleString %>">
										<%= unit.getPresentationName() %> -
										<span style="color: gray; font-size: 110%"><%= country.getName() %></span>
										<%= mobilityAgreement.getMobilityProgram().getRegistrationAgreement().getDescription() %>
									</div>
								</li>
    	    				<%
    	    					}
							%>
						</ul>
							<%
    	    					if (candidacyCount > 5) {
    	    				%>
    	    						<span style="margin-left: 30px;">
									<a href="#" onclick="<%= "$('.hide" + candidacySubmission.getExternalId() + "').toggle(); $('.show" + candidacySubmission.getExternalId() + "').toggle();" %>" class="<%= "show" + candidacySubmission.getExternalId() %>">
										<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies.show.more.results"/> (<%= candidacyCount %>) &gt;&gt;
									</a>
									<a href="#" onclick="<%= "$('.hide" + candidacySubmission.getExternalId() + "').toggle(); $('.show" + candidacySubmission.getExternalId() + "').toggle();" %>" class="<%= "hide" + candidacySubmission.getExternalId() %>">
										&lt;&lt; <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies.hide.more.results"/>
									</a>
    	    						</span>
    	    				<%
    	    					}
							%>
					</td>
				</tr>
<%
    	    }
	    }
%>
		</table>
<%
	}
%>

<h3>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies.not.admitted"/>:
</h3>

<%
	for (final OutboundMobilityCandidacyPeriod candidacyPeriod : outboundMobilityContextBean.getCandidacyPeriods()) {
%>

		<table class="tstyle1 mtop05">
			<tr>
				<th></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.username"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.name"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degree"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.classification"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contests"/></th>
			</tr>
<%
    	for (final OutboundMobilityCandidacySubmission candidacySubmission : candidacyPeriod.getSortedSubmissionSet(mobilityGroup)) {
			final BigDecimal grade = candidacySubmission.getGrade(mobilityGroup);
    	    if (grade == null || grade.signum() != 1) {
    	    	final Registration registration = candidacySubmission.getRegistration();
    	    	final Person person = registration.getPerson();
				final String hideGradeID = "hideGrade" + candidacySubmission.getExternalId();
				final String showGradeID = "showGrade" + candidacySubmission.getExternalId();
				final String inputGradeID = "inputGrade" + candidacySubmission.getExternalId();
				final String gradeText = "grade" + candidacySubmission.getExternalId();
%>
				<tr>
					<td>
						<div><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + person.getExternalId() %>"  style="padding: 1em 0;" /></div>
					</td>
					<td>
						<a href="#" onclick="<%= "ViewCandidate('" + person.getExternalId() + "');" %>">
							<%= person.getUsername() %>
						</a>
					</td>
					<td><%= person.getName() %></td>
					<td><%= registration.getDegree().getSigla() %></td>
					<td style="text-align: center;">
						<span id="<%= showGradeID %>">
							<em id="<%= gradeText %>"><%= grade == null ? "" : grade.toString() %></em>
							&nbsp;
							<a href="#" onclick="<%= "ToggleGradeInput('" + showGradeID + "', '" + hideGradeID + "'); $('#" + inputGradeID + "').focus()" %>"
									style="border-bottom: 0px; float: right;">
								<img src="<%= request.getContextPath() +"/images/iconEditOn.png" %>" />
								&nbsp;&nbsp;
							</a>
						</span>
						<span id="<%= hideGradeID %>" style="display: none;">
							<input id="<%= inputGradeID %>" name="grade" value="<%= grade == null ? "" : grade.toString() %>"
								onchange="<%= "SaveGrade('" + candidacySubmission.getExternalId() + "', '" + inputGradeID + "', '" + gradeText + "', '" + hideGradeID + "', '" + showGradeID + "');" %>"
								onkeydown="<%= "EscapeKeyAbort(event, '" + hideGradeID + "', '" + showGradeID + "');" %>"
								size="5"/>
						</span>
					</td>
					<td>
						<ul>
							<%
								int candidacyCount = 0;
								for (final OutboundMobilityCandidacy candidacy : candidacySubmission.getSortedOutboundMobilityCandidacySet()) {
							    	final OutboundMobilityCandidacyContest contest = candidacy.getOutboundMobilityCandidacyContest();
							    	final MobilityAgreement mobilityAgreement = contest.getMobilityAgreement();
									final Unit unit = mobilityAgreement.getUniversityUnit();
							    	final Country country = unit.getCountry();
							    	String styleString = "";
							    	if (contest.getOutboundMobilityCandidacyContestGroup() != mobilityGroup) {
							    	    if (candidacy.getSubmissionFromSelectedCandidacy() != null) {
								    	    styleString = "color: lime; font-weight: bold; font-size: 150%";
								    	} else {
								    	    styleString = "color: grey;";
								    	}
							    	} else if (candidacy.getSubmissionFromSelectedCandidacy() != null) {
							    	    styleString = "color: green; font-weight: bold; font-size: 120%";							    	    
							    	}
							    	
							%>
								<li <% if (candidacyCount++ > 4) { %> class="<%= "hide" + candidacySubmission.getExternalId() %>" <% } %>>
									<div style="<%= styleString %>">
										<%= unit.getPresentationName() %> -
										<span style="color: gray; font-size: 110%"><%= country.getName() %></span>
										<%= mobilityAgreement.getMobilityProgram().getRegistrationAgreement().getDescription() %>
									</div>
								</li>
    	    				<%
    	    					}
							%>
						</ul>
							<%
    	    					if (candidacyCount > 5) {
    	    				%>
    	    						<span style="margin-left: 30px;">
									<a href="#" onclick="<%= "$('.hide" + candidacySubmission.getExternalId() + "').toggle(); $('.show" + candidacySubmission.getExternalId() + "').toggle();" %>" class="<%= "show" + candidacySubmission.getExternalId() %>">
										<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies.show.more.results"/> (<%= candidacyCount %>) &gt;&gt;
									</a>
									<a href="#" onclick="<%= "$('.hide" + candidacySubmission.getExternalId() + "').toggle(); $('.show" + candidacySubmission.getExternalId() + "').toggle();" %>" class="<%= "hide" + candidacySubmission.getExternalId() %>">
										&lt;&lt; <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies.hide.more.results"/>
									</a>
    	    						</span>
    	    				<%
    	    					}
							%>
					</td>
				</tr>
<%
    	    }
	    }
%>
		</table>
<%
	}
%>

	<input name="xpto" style="display: none;">
</form>
