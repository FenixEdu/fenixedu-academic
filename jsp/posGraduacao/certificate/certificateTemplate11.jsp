<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />

<tr>
	<td align="center">
	<H2>DIPLOMA</H2>
	</td>
</tr>
<tr>
	<td align="center">
	O Instituto Superior Técnico certifica que
	</td>
</tr>
<tr>
	<td align="center">
		<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
	</td>
</tr>
<tr>
	<td align="center">
	concluiu a parte curricular do Curso de 
	</td>
</tr>
<tr>
	<td align="center">
	<bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/>
	</td>
</tr>
<tr>
	<td align="center">
    	com a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores.
	</td>
</tr>
<tr>
	<td align="center">
    	<bean:write name="<%= SessionConstants.DATE %>" />
	</td>
</tr>
<tr>
	<td align="center">
    	O Presidente do Instituto Superior Técnico
	</td>
</tr>

