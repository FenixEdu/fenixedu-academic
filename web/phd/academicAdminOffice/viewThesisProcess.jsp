<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadProvisionalThesisDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadFinalThesisDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadThesisRequirement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryElements"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryReviews"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.RemindJuryReviewToReporters"%>
<%@page import="net.sourceforge.fenixedu.applicationTier.Servico.thesis.SubmitThesis"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.ScheduleThesisDiscussion"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.RatifyFinalThesis"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.SetFinalGrade"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElementsDocuments"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackUpload" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.ConcludePhdProcess" %>
<%@page import="net.sourceforge.fenixedu.domain.accessControl.PermissionType" %>

<logic:notEmpty name="process" property="thesisProcess">
<logic:equal name="process" property="activeState.active" value="true">

<bean:define id="thesisProcess" name="process" property="thesisProcess" />

<br/>
<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
	<fr:view schema="PhdThesisProcess.view" name="process" property="thesisProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
		</fr:layout>
	</fr:view>
	</td>
	<td>
		<ul class="operations" >
			<li>
				<html:link action="/phdThesisProcess.do?method=manageThesisDocuments" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manageThesisDocuments"/>
				</html:link>
			</li>
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadProvisionalThesisDocument.class %>">
			<li>
				<bean:define id="provisionalThesisDownloadUrl" name="thesisProcess" property="provisionalThesisDocument.downloadUrl" />
				<a href="<%= provisionalThesisDownloadUrl.toString() %>">
					<bean:write name="thesisProcess" property="provisionalThesisDocument.documentType.localizedName"/>
					(<bean:message  key="label.version" bundle="PHD_RESOURCES"/> <bean:write name="thesisProcess" property="provisionalThesisDocument.documentVersion"/>)
				</a>
			</li>
			</phd:activityAvailable>
			
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadFinalThesisDocument.class %>">
			<li>
				<bean:define id="finalThesisDownloadUrl" name="thesisProcess" property="finalThesisDocument.downloadUrl" />
				<a href="<%= finalThesisDownloadUrl.toString() %>">
					<bean:write name="thesisProcess" property="finalThesisDocument.documentType.localizedName"/> 
					(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="thesisProcess" property="finalThesisDocument.documentVersion"/>)
				</a>
			</li>
			</phd:activityAvailable>
			<logic:notEmpty name="thesisProcess" property="meetingProcess">
				<li>
					<html:link action="/phdThesisProcess.do?method=viewMeetingSchedulingProcess" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
						<bean:message key="label.phd.thesis.meeting.scheduling" bundle="PHD_RESOURCES" />
					</html:link>
					<logic:equal value="true" name="thesisProcess" property="meetingProcess.anyMinutesDocumentMissing">
							(<bean:message key="label.phd.meeting.missing.meeting.minutes" bundle="PHD_RESOURCES" />)
					</logic:equal>
				</li>
			</logic:notEmpty>
			<phd:permissionFor permission="<%= PermissionType.MANAGE_PHD_PROCESS_STATES %>">
			<li>
				<html:link action="/phdThesisProcess.do?method=manageStates" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states" />
				</html:link>
			</li>
			</phd:permissionFor>
			<li>
				<html:link action="/phdThesisProcess.do?method=prepareEditPhdThesisProcessInformation" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.editPhdThesisProcessInformation"/>
				</html:link>
			</li>
			<phd:activityAvailable process="<%= thesisProcess %>" activity="<%= ConcludePhdProcess.class %>">
			<li>
				<html:link action="/phdThesisProcess.do?method=listConclusionProcesses" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="link.phd.conclusionProcess" />
				</html:link>
			</li>
			</phd:activityAvailable>
		</ul>
	</td>
  </tr>
 </table>

<ul class="operations">
	<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= RequestJuryElements.class %>">
	<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareRequestJuryElements" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.request.jury.elements"/>
			</html:link>
	</li>
	</phd:activityAvailable>
	
	<logic:notEqual name="thesisProcess" property="activeState.name" value="NEW">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareSubmitJuryElementsDocument" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.jury.elements.document"/>
			</html:link>
		</li>
		
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=manageThesisJuryElements" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.jury.elements"/>
			</html:link>
		</li>
		<phd:activityAvailable process="<%= thesisProcess %>" activity="<%= RejectJuryElementsDocuments.class %>">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareRejectJuryElementsDocuments" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId" >
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.reject.jury.elements.documents" />
			</html:link>
		</li>
		</phd:activityAvailable>
	</logic:notEqual>
	
	<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= RequestJuryReviews.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareRequestJuryReviews" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.request.jury.reviews"/>
		</html:link>
	</li>
	</phd:activityAvailable>
	
	<phd:activityAvailable process="<%= thesisProcess %>" activity="<%= RemindJuryReviewToReporters.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareRemindJuryReviews" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.remind.jury.reviews"/>
		</html:link>
	</li>
	</phd:activityAvailable>
	
	<phd:activityAvailable process="<%= thesisProcess %>" activity="<%= ScheduleThesisDiscussion.class %>">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareScheduleThesisDiscussion" paramId="processId" paramName="thesisProcess" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.schedule.thesis.discussion"/>
			</html:link>
		</li>
	</phd:activityAvailable>

	<%-- This activity is not used yet, maybe when some modifications are necessary --%>
	<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= SubmitThesis.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareSubmitThesis" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.submit.thesis"/>
		</html:link>
	</li>
	</phd:activityAvailable>
	
	<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= RatifyFinalThesis.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareRatifyFinalThesis" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.ratify.final.thesis"/>
		</html:link>
	</li>
	</phd:activityAvailable>
	
	<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= SetFinalGrade.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareSetFinalGrade" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.set.final.grade"/>
		</html:link>
	</li>
	</phd:activityAvailable>
	
	<phd:activityAvailable process="<%= thesisProcess %>" activity="<%=  JuryReporterFeedbackUpload.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=prepareJuryReportFeedbackUpload" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.report.feedback.upload"/>
		</html:link>
	</li>
	</phd:activityAvailable>
	
</ul>
</logic:equal>
<br/><br/>
</logic:notEmpty>
