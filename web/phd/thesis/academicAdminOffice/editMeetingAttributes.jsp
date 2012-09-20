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

<bean:define id="thesisProcessId" name="process" property="externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
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


</logic:present>