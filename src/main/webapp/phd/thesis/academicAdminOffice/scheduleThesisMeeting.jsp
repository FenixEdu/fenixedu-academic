<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.thesis.schedule.thesis.meeting" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdMeetingSchedulingProcess.do?method=viewMeetingSchedulingProcess&amp;processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<%--  ### End Of Context Information  ### --%>


<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<jsp:include page="/phd/thesis/common/scheduleThesisEvent.jsp">
	<jsp:param name="notifyElements" value="true" />
	<jsp:param name="submitMethod" value="scheduleThesisMeeting" />
	<jsp:param name="invalidMethod" value="scheduleThesisMeetingInvalid" />
	<jsp:param name="postBackMethod" value="scheduleThesisMeetingPostback" />
	<jsp:param name="processName" value="phdMeetingSchedulingProcess" />
</jsp:include>

<%--  ### End of Operation Area  ### --%>
