<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%><html:xhtml/><div align="center">	<br/>	<h2 style="text-align:center">		<bean:message key="title.student.enrollment.simple"/>	</h2>			</div><div class="infoselected">
		<h2>Atenção:</h2>		<ul>		<li><bean:message key="message.students.with.tutor"/></li>		<li><bean:message key="message.LEEC.students.tutor"/></li>		<li><bean:message key="message.LMAC.students"/></li>		<li><bean:message key="message.LEFT.students"/></li>		</ul>
		<ul>
		<li><a href="@enrollment.faq.url@" target="_blank"><bean:message key="message.enrollment.instructions"/></a>	</li>		</ul></div>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<div><br/><br/>	<html:form action="/curricularCoursesEnrollment.do">		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="start"/>		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationId" property="registrationId"/>		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">			<bean:message key="button.continue.enrolment"/>		</html:submit>	</html:form></div>