<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
	<logic:present name="<%= SessionConstants.DISCRIMINATED_WITH_AVERAGE%>">
		<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
	</logic:present>
	do curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
    	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> ministrado neste Instituto, 
    	obteve aproveitamento nas disciplinas abaixo discriminadas, com as quais concluiu o curso especializado conducente à obtenção do grau de mestre 
    	em <bean:write name="conclusiondate" /> 
    		<logic:present name="<%= SessionConstants.DISCRIMINATED_WITH_AVERAGE%>">
     			com a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores
    		</logic:present>.<br />