<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

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
<%@page import="net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType" %>

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
			<logic:notEmpty name="thesisProcess" property="provisionalThesisDocument">
			<li>
				
				<bean:define id="provisionalThesisDownloadUrl" name="thesisProcess" property="provisionalThesisDocument.downloadUrl" />
				<a href="<%= provisionalThesisDownloadUrl.toString() %>">
					<bean:write name="thesisProcess" property="provisionalThesisDocument.documentType.localizedName"/>
					(<bean:message  key="label.version" bundle="PHD_RESOURCES"/> <bean:write name="thesisProcess" property="provisionalThesisDocument.documentVersion"/>)
				</a>
			</li>
			</logic:notEmpty>
			
			<li>
				<logic:notEmpty name="thesisProcess" property="finalThesisDocument">
				<bean:define id="finalThesisDownloadUrl" name="thesisProcess" property="finalThesisDocument.downloadUrl" />
				<a href="<%= finalThesisDownloadUrl.toString() %>">
					<bean:write name="thesisProcess" property="finalThesisDocument.documentType.localizedName"/> 
					(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="thesisProcess" property="finalThesisDocument.documentVersion"/>)
				</a>
				</logic:notEmpty>
			</li>

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

			<li>
				<html:link action="/phdThesisProcess.do?method=manageStates" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states" />
				</html:link>
			</li>


			<li>
				<html:link action="/phdThesisProcess.do?method=listConclusionProcesses" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="link.phd.conclusionProcess" />
				</html:link>
			</li>

		</ul>
	</td>
  </tr>
 </table>

<ul class="operations">
	<logic:notEqual name="thesisProcess" property="activeState.name" value="NEW">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=manageThesisJuryElements" paramId="processId" paramName="process" paramProperty="thesisProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.jury.elements"/>
			</html:link>
		</li>
	</logic:notEqual>
		
</ul>
</logic:equal>
<br/><br/>
</logic:notEmpty>
