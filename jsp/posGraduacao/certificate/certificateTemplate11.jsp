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
	<H3 class='diploma_posGrad'>INSTITUTO SUPERIOR TÉCNICO</H3>
	</td>
</tr>
<tr>
	<td align="center">
	<H2 class='diploma_posGrad'>DIPLOMA</H2>
	</td>
</tr>
<tr>
	<td align="center">
		&nbsp;
	</td>
</tr>

<tr>
	<td align="center">
	<H4 class='diploma_posGrad'>O Instituto Superior Técnico certifica que</H4>
	</td>
</tr>
<tr>
	<td align="center">
		<H4 class='diploma_posGrad'><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></H4>
	</td>
</tr>
<tr>
	<td align="center">
	<H4 class='diploma_posGrad'>concluiu a parte curricular do Curso de </H4>
	</td>
</tr>
<tr>
	<td align="center">
	<H4 class='diploma_posGrad'><bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/></H4>
	</td>
</tr>
<tr>
	<td align="center">
    	<H4 class='diploma_posGrad'>com a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores.</H4>
	</td>
</tr>
<tr>
	<td align="center">
    	<H4 class='diploma_posGrad'><bean:write name="<%= SessionConstants.DATE %>" /></H4>
	</td>
</tr>
<tr>
	<td align="center">
    	<H4 class='diploma_posGrad'>O(A) Presidente do Instituto Superior Técnico</H4>
	</td>
</tr>
