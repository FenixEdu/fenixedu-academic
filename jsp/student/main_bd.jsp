<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p><img src="<%= request.getContextPath() %>/images/portalEst-id.gif" alt="Portal Estudante" /></p>
	<div class="photo" align="center">
		<img src="<%= request.getContextPath() %>/images/student_photo1.jpg" width="150px" height="100px" alt="" />
		<img src="<%= request.getContextPath() %>/images/student_photo2.jpg" width="150px" height="100px" alt="" />
		<img src="<%= request.getContextPath() %>/images/student_photo3.jpg" width="150px" height="100px" alt="" />
	</div>
<p><bean:message key="message.info.student" /></p>
<br/>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th>
			<bean:message key="group.enrolment"/>
		</th>
	</tr>
	<%--<tr>
		<td>
			<html:link page="/curricularCourseEnrolmentManager.do?method=start">
				<bean:message key="link.curricular.course.enrolment"/>
			</html:link>
		</td> 
	</tr>--%>
	<tr>
		<td>
			<html:link page="/studentShiftEnrolmentManager.do?method=enrollCourses&amp;firstTime=yes">
				<bean:message key="link.shift.enrolment"/>
			</html:link>
		</td>
	</tr>
	<tr>
		<td>
                    <html:link page="/listAllSeminaries.do"> <bean:message key="link.seminaries.enrolment"/> </html:link> 
					<a href='<bean:message key="link.seminaries.rules" />' target="_blank"> <bean:message key="label.seminairies.seeRules"/> </a>
                </td>
            </tr>
		<tr>
		<td><br/>
			<html:link page="/examEnrollmentManager.do?method=viewExamsToEnroll" >
				<bean:message key="link.exams.enrolment"/>
			</html:link>
		</td>
	</tr>
 </table>
<br />
<br />


<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th>
			<html:link page="/studentTimeTable.do" target="_blank" >
				O Meu Horário
			</html:link>
		</th>
	</tr>
</table>
<br />
<br />


<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th>
			<html:link page="/viewCurriculum.do?method=getStudentCP" >
				<bean:message key="link.student.curriculum"/>
			</html:link>
		</th>
	</tr>
</table>
<br />
<br />

<%--
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th>
			<html:link page="/studentExecutionCourse.do?method=viewStudentExecutionCourses" >
				<bean:message key="link.myExecutionCourses"/>
			</html:link>
		</th>
	</tr>
</table>
--%>

<br />
<br />
<%--
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<th>
			
		<html:link page="/viewEnroledExecutionCourses.do" >
				<bean:message key="link.groups"/>
			</html:link>
		</th>
	</tr>
</table>--%>