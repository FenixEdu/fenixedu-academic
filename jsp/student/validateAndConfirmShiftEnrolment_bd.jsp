<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<h3>
<html:link page="/studentShiftEnrolmentManager.do?method=initializeShiftEnrolment">Ver Horário</html:link>
</h3>		
   <logic:present name="infoStudentShiftEnrolment">
	   <logic:present name="infoStudentShiftEnrolment" property="currentEnrolment">
			Estes são os turnos onde estavas inscrito até agora:<br>
<table>

  
   	 		<logic:iterate name="infoStudentShiftEnrolment" id="shift" property="currentEnrolment">
<tr>
   	 <td class="listClasses">			Turno:<bean:write name="shift" property="nome"/> </td>
</tr>
	   	 	</logic:iterate>
</table>
	   	 </logic:present>
    <logic:present name="infoStudentShiftEnrolment" property="errors">
   	 	<!-- show errors here -->
   	 </logic:present>
     <logic:present name="infoStudentShiftEnrolment" property="newShifts">
	 	Turnos em que deixaste de estar inscrito:<br>
<table>
	     <logic:iterate name="infoStudentShiftEnrolment" id="shiftPairs" property="newShifts">
		     <logic:iterate name="shiftPairs" id="shift"offset="0" length="1">
		     	<logic:present name="shift">
		<tr><td class="listClasses">		 	Nome: <bean:write name="shift" property="nome"/></td></tr>
		<tr><td class="listClasses">			Tipo: <bean:write name="shift" property="tipo.siglaTipoAula"/></td></tr>
				</logic:present>
		     </logic:iterate>
    	 </logic:iterate>
</table>
		<br>Turnos em que te inscreveste:<br>
<table>
    	 <logic:iterate name="infoStudentShiftEnrolment" id="shiftPairs" property="newShifts">
	    	 <logic:iterate name="shiftPairs" id="shift"offset="1" length="1">
	     		<logic:present name="shift">
				<tr><td class="listClasses">		 	Nome: <bean:write name="shift" property="nome"/></td></tr>
				<tr><td class="listClasses">			Tipo: <bean:write name="shift" property="tipo.siglaTipoAula"/></td></tr>
				</logic:present>
		     </logic:iterate>
    	 </logic:iterate>
</table>
   	 </logic:present>
   	 <br><br>
   	 <logic:present name="infoStudentShiftEnrolment" property="errors">
   	 	Foram encontrados os seguintes problemas:<br>
<table>
   	 	<logic:iterate name="infoStudentShiftEnrolment" id="error" property="errors">
   	 	<tr><td class="listClasses">	Turno:<bean:write name="error" property="shift.nome"/></td></tr>
   	 	<tr><td class="listClasses">	Problema:<bean:write name="error" property="msg"/></td></tr>
   	 	</logic:iterate>
</table>
   	 </logic:present>
    </logic:present>
 