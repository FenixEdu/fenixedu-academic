<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionPeriod">
	<bean:write name="executionPeriod" property="name"/> -
	<bean:write name="executionPeriod" property="infoExecutionYear.year"/>
</logic:present>
<br />
<logic:present name="executionDegree">
	<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/> de 
	<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
</logic:present>
<br />
<logic:present name="curricularYear">
	<bean:write name="curricularYear" property="year"/>º Ano
</logic:present>