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

<logic:present role="MANAGER">

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

<!-- History of meetings Area -->

<fr:view name="meetingProcess" property="meetings">

	<fr:schema bundle="PHD_RESOURCES" type="<%= PhdMeeting.class.getName() %>">
		<fr:slot name="meetingPlace" />
		<fr:slot name="meetingDate" layout="year-month"/>
		<fr:slot name="versionOfLatestDocumentVersion" layout="null-as-label"/> 
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		
		<fr:link name="minutesLink" label="label.view,PHD_RESOURCES" order="1" condition="documentsAvailable"
			link="${latestDocumentVersion.downloadUrl}" module="" contextRelative="false" hasContext="true"/>
	
	</fr:layout>
</fr:view>

</logic:present>