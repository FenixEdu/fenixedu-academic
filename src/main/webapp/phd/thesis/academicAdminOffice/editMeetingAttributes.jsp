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

<bean:define id="thesisProcessId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.thesis.jury.meeting.scheduling" bundle="PHD_RESOURCES" /></h2>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdThesisProcess.do?method=viewMeetingSchedulingProcess&amp;processId=" + thesisProcessId.toString() %>">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br>

<%-- ### Process Information ### --%>
<br/>

<bean:define id="meetingId" name="meeting" property="externalId" />

<fr:form action="<%= String.format("/phdMeetingSchedulingProcess.do?method=editMeetingAttributes&amp;processId=%s&amp;meetingId=%s", thesisProcessId, meetingId)  %>" >
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-edit" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdEditMeetingBean" bundle="PHD_RESOURCES">
			<fr:slot name="scheduledDate" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.DateTimeValidator" />
			</fr:slot>
			<fr:slot name="scheduledPlace" required="true">
			</fr:slot>
		</fr:schema>

		<fr:destination name="invalid" path="<%= String.format("/phdMeetingSchedulingProcess.do?method=editMeetingAttributes&amp;processId=%s&amp;meetingId=%s", thesisProcessId, meetingId)  %>" />
				
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,error1 clear" />
		</fr:layout>
	</fr:edit>
	
	<p><html:submit><bean:message key="label.edit" bundle="PHD_RESOURCES" /></html:submit></p>
	
</fr:form>


<!-- Operational Area -->

<!-- History of meetings Area -->
