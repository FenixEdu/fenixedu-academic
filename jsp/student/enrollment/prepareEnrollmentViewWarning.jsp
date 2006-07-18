<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<div  align="center"  >
	<span class="error"><html:errors/></span>
	<br />
	<h2 style="text-align:center"><bean:message key="title.student.shift.enrollment" /></h2>

<div class="infoselected" style="text-align: left">
<ul>
	<li><bean:message key="message.warning.student.enrolmentClasses" /> <html:link page="<%= "/warningFirst.do" %>"><bean:message key="message.warning.student.enrolmentClasses.Fenix" /></html:link>.</li>
	<li><bean:message key="message.warning.student.enrolmentClasses.labs" /></li>
	<li><bean:message key="message.warning.student.enrolmentClasses.notEnroll" /></li>
	<ul>
		<li>Alunos Externos</li>
		<li>Melhorias de Nota</li>
		<li>Alunos inscritos em �poca Especial 2003/2004</li>
		<li>Alunos cuja inscri��o � efectuada pelo Coordenador de Licenciatura/Tutor</li>
		<li>Alunos com processos de Equival�ncia em curso</li>
	</ul>
	<li><bean:message key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse" /> <html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&selectCourses=true" %>"><bean:message key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse.link" /></html:link></li>
</ul>
</div>

<br />
<html:form action="/studentShiftEnrollmentManager">
	<input alt="input.method" type="hidden" name="method" value="start">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.continue.enrolment"/>
	</html:submit>
</html:form>

</div>