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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeeting"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeetingRequest"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleThesisMeeting"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeeting"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>

<bean:define id="individualProcess" name="process" property="individualProgramProcess" />
<bean:define id="meetingProcess" name="process" property="meetingProcess" />
<bean:define id="individualProcessId" name="individualProcess" property="externalId" />
<bean:define id="thesisProcessId" name="process" property="externalId" />

<%-- ### Title #### --%>
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
