<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoEnrolmentEvaluation" %>
<tr>
<td>

</td>
</tr>
<logic:present name="<%= SessionConstants.ENROLMENT_LIST%>">
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
</logic:present>
<logic:present name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>">
<bean:define id="infoEnrolmentExtraCurricularPlan" name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>" />
</logic:present>


<logic:present name="<%= SessionConstants.ENROLMENT%>">
<logic:present name="<%= SessionConstants.ENROLMENT_LIST%>">
	<tr>
	<td>
	nas seguintes disciplinas:
	</td>
    </tr>
<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
<tr>
<td>
<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
</td>
</tr>
</logic:iterate>
</logic:present>

<logic:present name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>">
	<tr>
	<td>
	nas seguintes disciplinas Extra-Curriculares:
	</td>
    </tr>
<logic:iterate id="itr" name="infoEnrolmentExtraCurricularPlan">
<tr>
<td>
<bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
</td>
</tr>
</logic:iterate>
</logic:present>

</logic:present>

<logic:present name="<%= SessionConstants.APROVMENT%>">
<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">

<tr>
<td>
 <bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
 <bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 com 
 <logic:iterate id="itr1" name="itr" property="infoEvaluations">
 <bean:write name="itr1" property="grade" />
 </logic:iterate>(nota) valores
</td>
</tr>

</logic:iterate>
</logic:present>


<logic:present name="<%= SessionConstants.EXTRA_CURRICULAR_APROVMENT%>">
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.EXTRA_ENROLMENT_LIST%>" />
<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">

<tr>
<td>
 <bean:write name="itr" property="infoCurricularCourseScope.infoCurricularCourse.name" />
 <bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 com 
 <logic:iterate id="itr1" name="itr" property="infoEvaluations">
 <bean:write name="itr1" property="grade" />
 </logic:iterate>(nota) valores
</td>
</tr>

</logic:iterate>
</logic:present>