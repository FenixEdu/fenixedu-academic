<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.sendEmail" bundle="APPLICATION_RESOURCES"/></h2>

<em>
	<logic:notPresent name="receiversBean">
		<bean:message key="error.tutor.noActiveTutorships" />
	</logic:notPresent>
</em>

<logic:messagesPresent property="error" message="true">
	<html:messages id="message" property="error" message="true" bundle="MESSAGING_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="problem" message="true">
    <p>
        <span class="error0">
            <bean:message key="messaging.mail.address.problem.text" bundle="MESSAGING_RESOURCES"/>:
            <html:messages id="message" property="problem" message="true" bundle="MESSAGING_RESOURCES">
                <p class="mtop0 mbottom0"><bean:write name="message"/></p>
            </html:messages>
        </span>
    </p>
</logic:messagesPresent>

<logic:messagesPresent property="warning" message="true">
    <html:messages id="message" property="warning" message="true" bundle="MESSAGING_RESOURCES">
        <p class="mtop1"><span class="warning0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:messagesPresent property="confirmation" message="true">
    <html:messages id="message" property="confirmation" message="true" bundle="MESSAGING_RESOURCES">
        <p><span class="success0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<logic:present name="receiversBean">
	<b><bean:message key="label.teacher.tutor.sendMail.chooseReceivers" bundle="APPLICATION_RESOURCES" /></b>
	<br />
	<p class="color888 mvert05"><bean:message key="label.teacher.tutor.sendMail.chooseReceivers.help" bundle="APPLICATION_RESOURCES" /></p>

	<fr:form action="/sendMailToTutoredStudents.do?method=prepareCreateMail">
		<fr:edit id="receivers" name="receiversBean" schema="teacher.tutor.chooseEmailReceivers">
		    <fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
		    <fr:destination name="invalid" path="/sendMailToTutoredStudents.do?method=prepare"/>
		</fr:edit>
		<html:submit ><bean:message key="button.teacher.tutor.select" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="selectAll"><bean:message key="button.teacher.tutor.selectAll" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:submit property="reset"><bean:message key="button.teacher.tutor.clear" bundle="APPLICATION_RESOURCES" /></html:submit>
	</fr:form>
</logic:present>


