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

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingBean"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%>

<html:xhtml/>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />
<bean:define id="meeting" name="meetingBean" property="meeting" />


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.thesis.submit.thesis.meeting.minutes" bundle="PHD_RESOURCES" /></h2>
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
<fr:view schema="PhdMeeting.view" name="meeting">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>


<logic:notEmpty name="meetingBean" property="meeting.latestDocumentVersion">
	<br/><br/>
	<strong><bean:message key="label.phd.thesis.schedule.meeting.minutes.previous.document" bundle="PHD_RESOURCES" /></strong>
	<fr:view name="meetingBean" property="meeting.latestDocumentVersion" layout="link" />
	<br/>
</logic:notEmpty>

<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area ### --%>

<fr:form action="<%= "/phdMeetingSchedulingProcess.do?processId=" + processId.toString() %>" encoding="multipart/form-data">
	<input type="hidden" name="method" />
	<fr:edit id="meetingBean" name="meetingBean" visible="false" />
	
	
	<strong><bean:message key="label.phd.thesis.submit.thesis.meeting.minutes" bundle="PHD_RESOURCES" /></strong>
	<fr:edit id="meetingBean.notify" name="meetingBean">

		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdMeetingBean.class.getName() %>">
			<fr:slot name="toNotify" key="net.sourceforge.fenixedu.domain.phd.thesis.meeting.toNotify" bundle="PHD_RESOURCES" layout="radio">
				<fr:property name="classes" value="liinline nobullet"/>
			</fr:slot>
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	
	<fr:edit id="meetingBean.edit.document" name="meetingBean" property="document">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramDocumentUploadBean.class.getName() %>">
			<fr:slot name="type" readOnly="true" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.type" layout="phd-enum-renderer" />
			<fr:slot name="file" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.file">
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
	</fr:edit>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='submitThesisMeetingMinutes';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewMeetingSchedulingProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	
</fr:form>


<br/><br/>

<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
