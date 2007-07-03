<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%><html:xhtml/>
<em><bean:message key="title.student.portalTitle"/></em><h2><bean:message key="title.student.enrollment.simple"/></h2><div class="infoop3 mtop15">
		<h3 style="padding-left: 1em;">Atenção:</h3>		<ul>		<li><bean:message key="message.students.with.tutor"/></li>		<li><bean:message key="message.LEEC.students.tutor"/></li>		<li><bean:message key="message.LMAC.students"/></li>		<li><bean:message key="message.LEFT.students"/></li>		</ul>
		<ul>
			<li><a href="@enrollment.faq.url@" target="_blank"><bean:message key="message.enrollment.instructions"/></a>	</li>		</ul></div>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<p class="mtop15">	<html:form action="/curricularCoursesEnrollment.do">		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="start"/>		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationId" property="registrationId"/>		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">			<bean:message key="button.continue.enrolment"/>		</html:submit>	</html:form></p>