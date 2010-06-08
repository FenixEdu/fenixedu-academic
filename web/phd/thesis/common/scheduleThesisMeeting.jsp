<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>

<html:xhtml/>

<%
	final String notifyElements = request.getParameter("notifyElements");
	if (notifyElements != null) {
		request.setAttribute("notifyElements", notifyElements);
	} else {
	    request.setAttribute("notifyElements", Boolean.FALSE);
	}
%>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%=String.format("/phdThesisProcess.do?processId=%s", processId) %>">

	<input type="hidden" name="method" value="" />
	<fr:edit id="thesisProcessBean" name="thesisProcessBean" visible="false" />

	<br/><br/>
	<strong><bean:message key="label.phd.thesis.schedule.thesis.meeting" bundle="PHD_RESOURCES" /></strong>
	<fr:edit id="thesisProcessBean-info" name="thesisProcessBean">

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=scheduleThesisMeetingInvalid&processId=" + processId %>" />
		<fr:destination name="postBack" path="<%= "/phdThesisProcess.do?method=scheduleThesisMeetingPostback&processId=" + processId %>"/>

		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">

			<logic:equal name="notifyElements" value="true">
				<fr:slot name="toNotify" layout="radio-postback">
					<fr:property name="classes" value="liinline nobullet"/>
				</fr:slot>
			</logic:equal>

			<fr:slot name="meetingDate" required="true" />
			<fr:slot name="meetingPlace" required="true">
				<fr:property name="size" value="40" />
			</fr:slot>
		</fr:schema>

	</fr:edit>
	
	<logic:equal name="thesisProcessBean" property="toNotify" value="true">
		<br/><br/>
		<strong><bean:message key="label.phd.email.to.send" bundle="PHD_RESOURCES" />:</strong>
		<fr:edit id="thesisProcessBean-mail-information" name="thesisProcessBean">
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
	
			<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=scheduleThesisMeetingInvalid&processId=" + processId %>" />
			<fr:destination name="postBack" path="<%= "/phdThesisProcess.do?method=scheduleThesisMeetingPostback&processId=" + processId %>"/>
	
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
				<fr:slot name="mailSubject" required="true">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="mailBody" layout="longText" required="true">
					<fr:property name="rows" value="15" />
					<fr:property name="columns" value="100" />
				</fr:slot>
			</fr:schema>
	
		</fr:edit>
	
		<p>
			<strong>Notas - </strong> O sistema irá anexar automaticamente no final da mensagem de mail a seguinte informação:
		</p>
		<ul>
			<li>Data, hora e local da reunião de júri introduzidos</li>
			<li>Informação de acesso aos pareceres para os elementos do júri</li>
		</ul>
	</logic:equal>	
	
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='scheduleThesisMeeting';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>	

</fr:form>
