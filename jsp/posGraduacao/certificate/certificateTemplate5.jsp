<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
do curso de  <bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
		<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> 
			<logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
     			ministrado neste Instituto,obteve aproveitamento nas disciplinas abaixo discriminadas, que fazem parte do curso especializado conducente à obtenção do grau de mestre:
     			<br />
			</logic:equal>
			<logic:equal name="infoStudentCurricularPlan" property="specialization" value="Especialização">
 				ministrado neste instituto,obteve aproveitamento nas disciplinas abaixo discriminada.
			</logic:equal>
			<br />
