<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:xhtml/>

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>

<div align="center">

	<h2 class="acenter"><bean:message bundle="STUDENT_RESOURCES" key="title.student.shift.enrollment" /></h2>

	<div class="inobullet">
		<!-- Error messages go here --><html:errors />
	</div>
	
	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true">
			<p><span class="error0"><bean:write name="messages" filter="false" /></span></p>
		</html:messages>
	</logic:messagesPresent>

	<div class="infoop2" style="text-align: left">
	<ul>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses" /> <html:link page="<%= "/studentEnrollmentManagement.do?method=prepare" %>"><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.Fenix" /></html:link>.</li>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.labs" /></li>
		<li>
			<bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll" />
			<ul>
				<li>Alunos Externos</li>
				<li>Melhorias de Nota</li>
				<li>Alunos com processos de Equivalência em curso</li>
			</ul>
		</li>
        
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse" /> <html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&amp;selectCourses=true&amp;registrationOID=" + registration.getExternalId().toString()%>"><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse.link" /></html:link></li>		

        <li><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.first.year.first.semester" /></li>
    
    </ul>
	</div>
	
	<br />
	<html:form action="/studentShiftEnrollmentManager">
		<input alt="input.method" type="hidden" name="method" value="start"/>
		
		<html:hidden property="registrationOID" value="<%=registration.getExternalId().toString()%>"/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="STUDENT_RESOURCES" key="button.continue.enrolment"/>
		</html:submit>
	</html:form>

</div>
