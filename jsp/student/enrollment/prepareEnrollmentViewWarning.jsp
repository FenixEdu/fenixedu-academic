<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>

<div align="center">

	<h2 class="acenter"><bean:message key="title.student.shift.enrollment" /></h2>

	<div class="inobullet">
		<!-- Error messages go here --><html:errors />
	</div>
	
	<p>
		<em>Reservas de turmas do 1º Ano só a partir de 24 de Setembro de 2007</em>
	</p>

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true">
			<p><span class="error0"><bean:write name="messages" /></span></p>
		</html:messages>
	</logic:messagesPresent>

	<div class="infoop2" style="text-align: left">
	<ul>
		<li><bean:message key="message.warning.student.enrolmentClasses" /> <html:link page="<%= "/studentEnrollmentManagement.do?method=prepare" %>"><bean:message key="message.warning.student.enrolmentClasses.Fenix" /></html:link>.</li>
		<li><bean:message key="message.warning.student.enrolmentClasses.labs" /></li>
		<li>
			<bean:message key="message.warning.student.enrolmentClasses.notEnroll" />
			<ul>
				<li>Alunos Externos</li>
				<li>Melhorias de Nota</li>
				<li>Alunos inscritos em Época Especial 2006/2007</li>
				<li>Alunos com processos de Equivalência em curso</li>
			</ul>
		</li>
		<li><bean:message key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse" /> <html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&amp;selectCourses=true&amp;registrationOID=" + registration.getIdInternal().toString()%>"><bean:message key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse.link" /></html:link></li>
	</ul>
	</div>
	
	<br />
	<html:form action="/studentShiftEnrollmentManager">
		<input alt="input.method" type="hidden" name="method" value="start"/>
		
		<html:hidden property="registrationOID" value="<%=registration.getIdInternal().toString()%>"/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.continue.enrolment"/>
		</html:submit>
	</html:form>

</div>
