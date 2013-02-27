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
<%@ page language="java"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
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
</script>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/></h2>

<h2 class="separator2">
	<% for (final ExecutionDegree executionDegree : mobilityGroup.getSortedExecutionDegrees()) { %>
			<%= executionDegree.getDegree().getSigla() %>
	<% } %>
</h2>

<ul>
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
</ul>

<h3>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.candidacies"/>:
</h3>

<form id="mobilityGradeForm" action="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=editGrade&mobilityGroupOid=" + mobilityGroup.getExternalId() %>">

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
    	for (final OutboundMobilityCandidacySubmission candidacySubmission : candidacyPeriod.getOutboundMobilityCandidacySubmissionSet()) {
    	    final Registration registration = candidacySubmission.getRegistration();
    	    final Person person = registration.getPerson();
    	    if (mobilityGroup.handles(registration.getDegree())) {
    	        final BigDecimal grade = candidacySubmission.getGrade(mobilityGroup);
				final String hideGradeID = "hideGrade" + candidacySubmission.getExternalId();
				final String showGradeID = "showGrade" + candidacySubmission.getExternalId();
				final String inputGradeID = "inputGrade" + candidacySubmission.getExternalId();
				final String gradeText = "grade" + candidacySubmission.getExternalId();
%>
				<tr>
					<td>
						<div><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + person.getExternalId() %>"  style="padding: 1em 0;" /></div>
					</td>
					<td><%= person.getUsername() %></td>
					<td><%= person.getName() %></td>
					<td><%= registration.getDegree().getSigla() %></td>
					<td style="text-align: center;">
						<span id="<%= showGradeID %>">
							<em id="<%= gradeText %>"><%= grade == null ? "" : grade.toString() %></em>
							&nbsp;
							<a href="#" onclick="<%= "ToggleGradeInput('" + showGradeID + "', '" + hideGradeID + "'); $('#" + inputGradeID + "').focus()" %>"
									style="border-bottom: 0px; float: right;">
								<img src="<%= request.getContextPath() +"/images/iconEditOn.png" %>" />
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
							    	final String styleString = contest.getOutboundMobilityCandidacyContestGroup() == mobilityGroup ?
							    	        "" : "color: grey;";
							%>
								<li <% if (candidacyCount++ > 4) { %> class="<%= "hide" + candidacySubmission.getExternalId() %>" <% } %>>
									<div style="<%= styleString %>">
										<%= unit.getPresentationName() %> -
										<span style="color: gray;"><%= country.getName() %></span>
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

</form>
