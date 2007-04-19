<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
<bean:define id="conclusionDate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="date" name="<%= SessionConstants.DATE %>" />
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<table class="apura-final" width="90%" cellspacing="0">
	<tr>
		<td colspan="2" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;">Atribuição de média</td>
		</td>
	</tr>
	<tr> 
		<td width="50%" style="padding: 5px;">média ponderada <bean:write name="infoFinalResult" property="averageWeighted" /></td>
    	<td width="50%" style="padding: 5px; padding-left: 100px;">O coordenador do curso,</td>
    </tr>
    <tr>
    	<td width="50%" style="padding: 5px;">média simples <bean:write name="infoFinalResult" property="averageSimple" /></td>
    	<td width="50%" style="padding: 5px;" align="center" >&nbsp;</td>
    </tr>
    <tr>
    	<td width="50%" style="padding: 5px;">Resultado final <bean:write name="infoFinalResult" property="finalAverage" /> valores</td>
    	<td width="50%" style="padding: 5px; padding-left: 100px;">_____________________________ </td>
    </tr>
 </table>
 <br />  
 <table class="apura-final" width="90%" cellspacing="0">
	<tr>
		<td style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;">Informação</td>
	</tr>
    <tr>
    	<td style="padding: 5px;"><p class="apura-pt9">Concluiu a parte escolar do curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em <bean:write name="conclusionDate"/> com a média final de 
    		<bean:write name="infoFinalResult" property="finalAverage" /> valores.</p>
    		<p class="apura-pt9">Secretaria dos Serviços académicos, em <bean:write name="date"/></p><br /><br />
    	</td>
    </tr>
    <tr>
    	<td>
    		<div class="homologo">Homologo</div>
    		<div class="consid">À consideração de V.Exª.</div>
    	</td>	
    </tr>
    	<td style="padding: 5px;"><br /><br /><p class="apura-pt9">O Aluno nº <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/></p></td>
    </tr>
 </table>