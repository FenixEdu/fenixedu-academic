<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadProvisionalThesisDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadFinalThesisDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.DownloadThesisRequirement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.JuryReporterFeedbackUpload"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.ScheduleThesisMeeting"%>

<logic:notEmpty name="process" property="thesisProcess">
<logic:equal name="process" property="activeState.active" value="true">

<bean:define id="thesisProcess" name="process" property="thesisProcess" />

<br/>
<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
	<fr:view schema="PhdThesisProcess.view.simple" name="process" property="thesisProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
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
					<fr:view name="thesisProcess" property="provisionalThesisDocument" layout="link" />
				</li>
			</phd:activityAvailable>
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadFinalThesisDocument.class %>">
				<li>
					<fr:view name="thesisProcess" property="finalThesisDocument" layout="link" />
				</li>
			</phd:activityAvailable>
			<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= DownloadThesisRequirement.class %>">
				<li><fr:view name="thesisProcess" property="thesisRequirementDocument" layout="link" /></li>
			</phd:activityAvailable>
			<logic:notEmpty name="thesisProcess" property="juryPresidentDocument">
				<li>
					<fr:view name="thesisProcess" property="juryPresidentDocument" layout="link" />
				</li>
			</logic:notEmpty>
		</ul>
	</td>
  </tr>
 </table>

<%-- ### Operations ### --%>

<ul class="operations">
	<phd:activityAvailable process="<%= thesisProcess  %>" activity="<%= JuryReporterFeedbackUpload.class %>">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareJuryReportFeedbackUpload" paramId="processId" paramName="thesisProcess" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.jury.feedback.upload.document"/>
			</html:link>
		</li>
	</phd:activityAvailable>
<%--  	
	<phd:activityAvailable process="<%= thesisProcess %>" activity="<%= ScheduleThesisMeeting.class %>">
		<li style="display: inline;">
			<html:link action="/phdThesisProcess.do?method=prepareScheduleThesisMeeting" paramId="processId" paramName="thesisProcess" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.schedule.thesis.meeting"/>
			</html:link>
		</li>
	</phd:activityAvailable>
--%>	
</ul>
</logic:equal>
<br/>
</logic:notEmpty>
