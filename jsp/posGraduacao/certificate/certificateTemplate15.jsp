<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
<tr>
<td align="center">
	<h2 class='diploma_posGrad'>DIPLOMA</h2>
	<h3 class='diploma_posGrad'>O Instituto Superior T�cnico certifica que</h3>
	<p class="diplomado">
		<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
	</p>
	<h3 class='diploma_posGrad'>concluiu a parte curricular do Programa de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/></h3>
	<h3>(MBA em <bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/>)</h3>
    	<h3 class='diploma_posGrad'>com a m�dia de <bean:write name="infoFinalResult" property="finalAverage"/> valores.</h3>
<div class="dipl_signature">    	
    	<h4  class='diploma_posGrad'><bean:write name="<%= SessionConstants.DATE %>" /></h4>
    	<h4 class='diploma_posGrad'>O Presidente do Instituto Superior T�cnico</h4>
</div>