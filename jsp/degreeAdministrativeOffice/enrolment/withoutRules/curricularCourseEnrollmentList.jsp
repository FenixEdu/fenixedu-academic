<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrolment.without.rules" /></h2>

<span class="error"><html:errors/></span>


<logic:present name="infoStudentEnrolmentContext">

<bean:define id="infoEnrollmentsWithStateEnrolled" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" />
<bean:size id="enrollmentsSize" name="infoEnrollmentsWithStateEnrolled" />
<bean:write name="enrollmentsSize" />

</logic:present>