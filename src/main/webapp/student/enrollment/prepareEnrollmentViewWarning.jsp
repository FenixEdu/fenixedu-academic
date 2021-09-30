<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml/>

<bean:define id="registration" name="registration" type="org.fenixedu.academic.domain.student.Registration"/>
<bean:define id="executionSemesterID" name="executionSemesterID" type="java.lang.String"/>

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
		<%--
		<li>
			<bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll" />
			<ul>
				<li>Alunos Externos</li>
				<li>Melhorias de Nota</li>
				<li>Alunos com processos de Equivalência em curso</li>
			</ul>
		</li>
		<li><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse" /> <html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&amp;selectCourses=true&amp;registrationOID=" + registration.getExternalId().toString() + "&executionSemesterID=" + executionSemesterID %>"><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse.link" /></html:link></li>
		--%>
        <li><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.first.year.first.semester" /></li>
    
    </ul>
	</div>
	
	<br />
	<html:form action="/studentShiftEnrollmentManager">
		<input alt="input.method" type="hidden" name="method" value="start"/>
		
		<html:hidden property="registrationOID" value="<%=registration.getExternalId().toString()%>"/>
		<html:hidden property="executionSemesterID" value="<%= executionSemesterID %>"/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="STUDENT_RESOURCES" key="button.continue.enrolment"/>
		</html:submit>
	</html:form>

</div>
