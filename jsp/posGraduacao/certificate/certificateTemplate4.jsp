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
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />


<logic:present name="<%= SessionConstants.ENROLMENT%>">
<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
<tr>
<td>
<bean:write name="itr" property="infoCurricularCourse.name" />
</td>
</tr>
</logic:iterate>
</logic:present>

<logic:present name="<%= SessionConstants.APROVMENT%>">

<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">

<tr>
<td>
 <bean:write name="itr" property="infoCurricularCourse.name" />
 <bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 com 
 <logic:iterate id="itr1" name="itr" property="infoEvaluations">
 <bean:write name="itr1" property="grade" />
 </logic:iterate>(nota) valores
</td>
</tr>

</logic:iterate>

</logic:present>