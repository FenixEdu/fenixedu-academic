<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoEnrolmentEvaluation" %>
<p>
	<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
		<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
 		<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com 
 		<logic:iterate id="itr1" name="itr" property="infoEvaluations">
 		<bean:write name="itr1" property="grade" />
 		</logic:iterate>(nota) valores
	</logic:iterate>
</p>