<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
		<p>
            <img src="<%= request.getContextPath() %>/images/portalEstudante.gif" width="289" height="45" alt="">
        </p>
		<br/>
        <table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<th>
					<bean:message key="group.enrolment"/>
				</th>
			</tr>
            <tr>
                <td>
                    <html:link page="/viewEnrolment.do"> <bean:message key="link.shift.enrolment"/> </html:link>
                </td>
            </tr>
            <tr>
                <td>
                    <html:link page="/curricularCourseEnrolmentManager.do?method=start"> <bean:message key="link.curricular.course.enrolment"/> </html:link>
                </td>
            </tr>
             <tr>
                <td>
                    <html:link page="/examEnrollmentManager.do?method=viewExamsToEnroll" > <bean:message key="link.exams.enrolment"/> </html:link>
                </td>
            </tr>
        </table>