<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<p>
	<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
		<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
		<br />
 		<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com 
 		<bean:write name="itr" property="infoEnrolmentEvaluation.grade" />
 		valores
	</logic:iterate>
</p>