<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeeting"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeetingRequest"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeeting"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="individualProcess" name="process" property="individualProgramProcess" />
<bean:define id="meetingProcess" name="process" property="meetingProcess" />
<bean:define id="individualProcessId" name="individualProcess" property="externalId" />
<bean:define id="thesisProcessId" name="process" property="externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.thesis.jury.meeting.scheduling" bundle="PHD_RESOURCES" /></h2>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProcessId.toString() %>">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br>

<%-- ### Process Information ### --%>
<br/>
<fr:view schema="PhdMeetingSchedulingProcess.view.simple" name="process" property="meetingProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
	</fr:layout>
</fr:view>
<br>


<!-- Operational Area -->
<phd:activityAvailable process="<%= meetingProcess  %>"	activity="<%= ScheduleFirstThesisMeetingRequest.class %>">
	<li style="display: inline;">
	<html:link action="/phdMeetingSchedulingProcess.do?method=prepareRequestScheduleFirstThesisMeeting" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.request.schedule.meeting" />
	</html:link>
	</li>
</phd:activityAvailable>

<phd:activityAvailable process="<%= meetingProcess  %>"	activity="<%= ScheduleFirstThesisMeeting.class %>">
	<li style="display: inline;">
	<html:link action="/phdMeetingSchedulingProcess.do?method=prepareScheduleFirstThesisMeeting" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.schedule.thesis.meeting" />
	</html:link>
	</li>
</phd:activityAvailable>

<phd:activityAvailable process="<%= meetingProcess  %>"	activity="<%= ScheduleThesisMeetingRequest.class %>">
	<li style="display: inline;">
	<html:link action="/phdMeetingSchedulingProcess.do?method=prepareRequestScheduleThesisMeeting" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.request.schedule.meeting" />
	</html:link>
	</li>
</phd:activityAvailable>

<phd:activityAvailable process="<%= meetingProcess  %>"	activity="<%= ScheduleThesisMeeting.class %>">
	<li style="display: inline;">
	<html:link action="/phdMeetingSchedulingProcess.do?method=prepareScheduleThesisMeeting" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.schedule.thesis.meeting" />
	</html:link>
	</li>
</phd:activityAvailable>

<!-- History of meetings Area -->

<fr:view name="meetingProcess" property="meetings">

	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdMeeting.class.getName() %>">
		<fr:slot name="meetingPlace" />
		<fr:slot name="meetingDate" layout="year-month"/>
		<fr:slot name="versionOfLatestDocumentVersion" layout="null-as-label"/> 
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		
		<fr:link name="edit" label="link.edit,APPLICATION_RESOURCES" 
			link="<%= "/phdMeetingSchedulingProcess.do?method=prepareEditMeetingAttributes&amp;meetingId=${externalId}&amp;processId=" + thesisProcessId %>" />

		<fr:link name="minutesLink" label="label.view,PHD_RESOURCES" order="1" condition="documentsAvailable"
			link="${latestDocumentVersion.downloadUrl}" module="" contextRelative="false" hasContext="true"/>
	
		<fr:link name="submitMinutes" label="label.phd.meeting.submit.meeting.minutes,PHD_RESOURCES" order="2"
			link="<%= "/phdMeetingSchedulingProcess.do?method=prepareSubmitThesisMeetingMinutes&meetingId=${externalId}&processId=" + thesisProcessId.toString() %>" />
			
	</fr:layout>
</fr:view>

</logic:present>